package com.pig.modules.gt.service.impl;

import com.pig.basic.util.*;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.dao.RoomManageDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * (BizBooking)表服务实现类
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@Slf4j
@Service("bizBookingService")
public class BizBookingServiceImpl implements BizBookingService {
    @Resource
    private BizBookingDao bookingDao;

    @Resource
    private BizMemberDao memberDao;

    @Resource
    private RoomManageDao roomManageDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat df = new SimpleDateFormat("HH");

    @Override
    public Page<BizBooking> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizBooking> specification = (root, criteriaQuery, criteriaBuilder) -> {
            // 封装查询条件
            List<Predicate> list = new ArrayList<>();

            Join join = root.join("bizMember", JoinType.LEFT);
            if (!StringUtils.isEmpty(commonQuery.get("name"))) {
                Predicate predicateJoin = criteriaBuilder.like(join.get("name"), "%" + commonQuery.get("name") + "%");
                list.add(predicateJoin);
            }
            //增加筛选条件
            // 房间名
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(commonQuery.get("roomName"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("roomName"), "%" + commonQuery.get("roomName") + "%"));
            }
            // 房间号
            if (!StringUtils.isEmpty(commonQuery.get("roomCode"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("roomCode"), "%" + commonQuery.get("roomCode") + "%"));
            }
            // 预定时间
            if (!StringUtils.isEmpty(commonQuery.get("bookTimes"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("bookTimes"), "%" + commonQuery.get("bookTimes") + "%"));
            }
            // openid
            if (!StringUtils.isEmpty(commonQuery.get("openid"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("openid"), "%" + commonQuery.get("openid") + "%"));
            }
            // 预定编号
            if (!StringUtils.isEmpty(commonQuery.get("booksNo"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("booksNo"), "%" + commonQuery.get("booksNo") + "%"));
            }
            // 预约状态
            if (!StringUtils.isEmpty(commonQuery.get("bookStatus"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("bookStatus"), "%" + commonQuery.get("bookStatus") + "%"));
            }
            // 如果是不是微信端接口
            if (StringUtils.isEmpty(commonQuery.get("apiType"))) {
                // 开始时间
                if (!StringUtils.isEmpty(commonQuery.get("startTime"))) {
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookDate").as(String.class),
                            String.valueOf(commonQuery.get("startTime"))));
                }
                // 结束时间
                if (!StringUtils.isEmpty(commonQuery.get("endTime"))) {
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("bookDate").as(String.class),
                            String.valueOf(commonQuery.get("endTime"))));
                }
            } else {
                // 预定日期
                if (!StringUtils.isEmpty(commonQuery.get("bookDate"))) {
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("bookDate").as(String.class),
                            String.valueOf(commonQuery.get("bookDate"))));
                }
            }

            list.add(predicate);

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));

        };
        return bookingDao.findAll(specification, pageable);
    }

    @Override
    public CommonResult make(Map<String, Object> params) {
        // 获取会员信息
        try {
            String openid = StringUtil.getCheckString(params.get("openid"));
            BizMember member = memberDao.findByOpenidAndStatus(openid, "-1");
            if (null == member) {
                return CommonResult.failed("会员不存在或失效！");
            }
            String userLevel = member.getUserLevel();
            if (userLevel.equals("0")) {
                return CommonResult.failed("非会员不可预约！");
            }
            // 获取当前时间
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            // 把日期往后增加一天,整数  往后推,负数往前移动
            calendar.add(calendar.DATE, 4);
            // 这个时间就是日期往后推一天的结果
            date = calendar.getTime();
            // 预定时间
            Date bookDate = sdf.parse(StringUtil.getCheckString(params.get("bookDate")));
            // 判断当前预约的时间是否在未来五天内,且预约的时间必须大于当前时间
            if (date.before(bookDate)) {
                return CommonResult.failed("只能预约未来5天内的房间！");
            }
            int appointmentTimes = userLevel.equals("1") ? 2 : 3; // 预约小时数
            String[] bookTimes = StringUtil.getCheckString(params.get("bookTimes")).split(","); // 预约时间
            String nowTime = df.format(new Date());
            // 判断当前预约的时间是否过时
            for (String value : bookTimes) {
                // 当前时间是否在预定时间之前
                if (Integer.valueOf(value) < Integer.valueOf(nowTime)) {
                    return CommonResult.failed(value + "点不可预约！");
                }
            }
            // 查询预约记录
            List<BizBooking> bizBookingList = bookingDao.findByOpenidAndBookStatusOrderByCreateTimeDesc(openid, "1");
            if (ObjectUtils.isEmpty(bizBookingList)) {
                // 如果没有预约记录，按照会员级别来来控制预约的次数
                if (appointmentTimes < bookTimes.length) {
                    return CommonResult.failed("您！");
                }
            } else {
                // 比较用户已预约的小时数+当前要预约的小时数是否大于当前用户的可预约小时数
                if (appointmentTimes < bizBookingList.size() + bookTimes.length) {
                    return CommonResult.failed("超过当前可预约的小时数！");
                }
            }
            // 根据房间编号获取房间信息
            String roomCode = StringUtil.getCheckString(params.get("roomCode"));
            BizRoomManage roomManage = roomManageDao.findByRoomCode(roomCode);
            // save to db
            BizBooking bizBooking = null;
            List<BizBooking> bookingList = new ArrayList<>();
            for (String value : bookTimes) {
                bizBooking = new BizBooking();
                bizBooking.setBooksNo(CheckCommon.getBookingCode());
                bizBooking.setBookTimes(value);
                bizBooking.setRoomCode(roomCode);
                bizBooking.setBookDate(bookDate);
                bizBooking.setBookStatus("1");
                bizBooking.setRoomType(roomManage.getRoomType());
                bizBooking.setRoomName(roomManage.getName());
                bizBooking.setOpenid(openid);
                bookingList.add(bizBooking);
            }
            bookingDao.saveAll(bookingList);
        } catch (Exception e) {
            log.error("预定异常，", e.getMessage(), e);
            return CommonResult.failed("预定异常，" + e.getMessage());
        }
        return CommonResult.ok("预约成功！");
    }

    @Override
    public List<BizBooking> getAllByBookAfterBookDate(String roomId){

        String bookDate = sdf.format(new Date());
        List<BizBooking> list = bookingDao.querylistallbybookStatus(bookDate,roomId);
        return list;
    }
    /**
     * 判断是否可预约，可则保存预约记录
     * @param params
     * @return
     */
    @Override
    public CommonResult booksave(Map<String, Object> params){

        // 获取会员信息
        try {
            String openid = StringUtil.getCheckString(params.get("openid"));
            BizMember member = memberDao.findByOpenidAndStatus(openid, "-1");
            if (null == member) {
                return CommonResult.failed("会员不存在或失效！");
            }
            String userLevel = member.getUserLevel();
            if (userLevel.equals("0")) {
                return CommonResult.failed("非会员不可预约！");
            }

            // 预定时间
            Date bookDate = sdf.parse(StringUtil.getCheckString(params.get("bookDate")));
            //当前时间
            Date timeNow = new Date();

            int appointmentTimes = userLevel.equals("1") ? 2 : 3; // 预约小时数
            // 预约时间，为连续的时间段
            String[] bookTimes = StringUtil.getCheckString(params.get("bookTimes")).split(",");

            int icompare = bookDate.compareTo(sdf.parse(sdf.format(timeNow)));

            //获取当前小时
            String nowHour = df.format(new Date());

            //如果是当天，则判断是否过时，只判断第一个时间段是否过时
            if(icompare == 0 && Integer.parseInt(bookTimes[0]) < Integer.parseInt(nowHour)){

                return CommonResult.failed(bookTimes[0] + "点已过，不可预约,请重新选择时间段");
            }
            //如果当前房间是否已被预订
            for(String bookTime : bookTimes){

                int iRecord = -1;

                if("9".equals(bookTime)){

                    iRecord = bookingDao.querylistByTime(sdf.format(bookDate),bookTime,"9,");
                    if(iRecord > 0){
                        return CommonResult.failed(bookTime + "点已被预约,请重新选择时间段");
                    }
                }else{
                    iRecord = bookingDao.querylistByNormalTime(sdf.format(bookDate),bookTime);
                    if(iRecord > 0){
                        return CommonResult.failed(bookTime + "点已被预约,请重新选择时间段");
                    }
                }
            }

            // 查询当天已预约、已消费的记录
            List<BizBooking> recordsByDate = bookingDao.querylistbybookDate(sdf.format(bookDate),openid);
            if (!ObjectUtils.isEmpty(recordsByDate) && recordsByDate.size() > 0) {
                String bookTs = "";
                for(BizBooking bizBooking : recordsByDate){
                    bookTs += bizBooking.getBookTimes() + ",";
                }
                bookTs = bookTs.trim();
                if(bookTs.endsWith(",")){
                    bookTs = bookTs.substring(0,bookTs.length() -1);
                }

                //当天准备预约的小时数+当天系统已记录的小时数，不能超过会员次数
                if(bookTs.split(",").length + bookTimes.length > appointmentTimes){
                    return CommonResult.failed("您当前预约已超过预约限制，请消费完成后，再进行预约");
                }
            }

            // 查询已经预约记录
            List<BizBooking> bizBookingList = bookingDao.findByOpenidAndBookStatusOrderByCreateTimeDesc(openid, "1");
            if (!ObjectUtils.isEmpty(bizBookingList) && bizBookingList.size() > 0) {

                String bookTs = "";
                for(BizBooking bizBooking : bizBookingList){
                    bookTs += bizBooking.getBookTimes() + ",";
                }
                bookTs = bookTs.trim();
                if(bookTs.endsWith(",")){
                    bookTs = bookTs.substring(0,bookTs.length() -1);
                }

                //当天准备预约的小时数+系统已记录的小时数，不能超过会员次数
                if(bookTs.split(",").length + bookTimes.length > appointmentTimes){
                    return CommonResult.failed("您当前预约已超过预约限制，请消费完成后，再进行预约");
                }
            }

            // 根据房间编号获取房间信息
            String roomCode = StringUtil.getCheckString(params.get("roomCode"));
            BizRoomManage roomManage = roomManageDao.findByRoomCode(roomCode);
            // save to db
            BizBooking bizBooking = null;
//            List<BizBooking> bookingList = new ArrayList<>();
            bizBooking = new BizBooking();
            bizBooking.setBooksNo(CommonUtil.newRandomSNO("B"));
            bizBooking.setBookTimes(params.get("bookTimes").toString());
            bizBooking.setRoomCode(roomCode);
            bizBooking.setBookDate(bookDate);
            bizBooking.setBookStatus("1");
            bizBooking.setRoomType(roomManage.getRoomType());
            bizBooking.setRoomName(roomManage.getName());
            bizBooking.setOpenid(openid);

            bookingDao.save(bizBooking);
        } catch (Exception e) {
            log.error("预定异常，", e.getMessage(), e);
            return CommonResult.failed("预定异常，" + e.getMessage());
        }
        return CommonResult.ok("预约成功！");
    }
}
