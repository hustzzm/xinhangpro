import React, { PureComponent } from 'react';
import { connect } from 'dva';
// import { VerticalAlignBottomOutlined, VerticalAlignTopOutlined } from '@ant-design/icons';
import { Form } from '@ant-design/compatible';
import router from "umi/router";
import '@ant-design/compatible/assets/index.css';
import {
    Button,
    Col,
    Input,
    Row,
    DatePicker,
    Modal,
    Select,
    Checkbox,
    Table,
    Tag,
    Upload,
    Space,
    Progress,
    message, Tooltip
} from 'antd';
import {
    PlusOutlined,
    SyncOutlined,
    DeleteOutlined,
    FilterOutlined,
    UploadOutlined,
    FileAddOutlined,
    EditOutlined,
    RollbackOutlined
} from "@ant-design/icons";
import {
    ORDERINFO_LIST,  
    ORDERINFO_REMOVE,
    ORDERINFO_EXPORT
} from '@/actions/gt/order';
import { ROOMTYPE_DICT} from "../../actions/dictionary";

import CommonTable from "@/pages/gt/CommonTable/index";

import styles from '../../utils/utils.less';
import Panel from "../../components/Panel";

import styleTester from "../Wes/index.less";
import { getCurrentUser } from '../../utils/authority';

import moment from "moment";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';


@connect(({ order, dictionary, loading }) => ({
    order,
    dictionary,
    loading: loading.models.order,
}))
@Form.create()
class orderhistory extends PureComponent {

    state = {        
          
        confirmLoading: false,
        addvisible: false,//新增模块，显示隐藏
        selectedRow: { account: '', id: -1 },//选中行
        selectedRowKeys: [],  //选中行主键
        params: {},             //查询条件参数       
      
        // selectedRowKeys: [],  //选中行主键
        onReset: () => {
        },
       
    };

    componentWillMount() {
      
        const { dispatch,detail } = this.props;
        // 查询directory数据集，包括样本状态、样品进度、检测类型
        let params = { code: 'roomtype' }
        dispatch(ROOMTYPE_DICT(params));
        debugger
        let orderparams = {};
        if(detail && detail.openid){
            orderparams.openId = detail.openid;
        }else{
            orderparams.openId = 'XXXXX';            
        }
        this.getList(orderparams);
       
    }

    getList(orderparams){
        const { dispatch } = this.props;       
        dispatch(ORDERINFO_LIST(orderparams));
    }


   
    selectRow = (selectedRowKeys, selectedRows) => {
        this.setState({
            selectedRows: selectedRows,
            selectedRowKeys: selectedRowKeys
        })
    }

    // 清空table选中状态
    clearChildSelect = (ref) => {
        this.childTable = ref
    }

    

    render() {
        const code = 'order';
        const {               
        } = this.state;
        const that = this;
        const {
            form,
            loading,
            dictionary: { roomtypeDicts },
            order: { data },
        } = this.props;
       

        const currentUser = getCurrentUser();
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 12 },
                md: { span: 16 },
            },
        };

        const columns = [
        {
                title: '序号',
                align: 'center',
                width: 70,
                dataIndex: 'index',
                render: (text, record, index) => `${index + 1}`,
            },
          {
            title: '订单号',
            dataIndex: 'orderNo',  
            width:140,              
            },
          {
            title: '姓名',
            dataIndex: 'name',  
            flex:0.1,
          },
          {
            title: '下单时间',
            dataIndex: 'createTime',  
            flex:0.1,
          },
          {
            title: '商品',
            dataIndex: 'userLevel',  
            flex:0.1,
            render: (text, record, index) => {
               let val = '普通会员';
               if(text == '2'){
                   val = '钻石会员'
               }
               val = val + ' ' + moment(record.orderStart).format('YYYY-MM-DD') + '至' + moment(record.orderEnd).format('YYYY-MM-DD')

               return val;
            }
          },
          {
            title: '金额',
            dataIndex: 'orderPrice',  
            flex:0.1,
          },        
          {
            title: '订单状态',
            dataIndex: 'orderStatus',  
            flex:0.1,
            render: (text, record, index) => {
               let val = '支付成功';
               if(text == '20'){
                   val = '支付失败'
               }               
               return val;
            }
          },
         
        ];
    

        return (
            <Panel>
                <CommonTable
                    // scroll={{ y: 504 }}
                    code={code}
                    form={form}                   
                    // renderLeftButton={this.renderLeftButton}
                    // renderSearchForm={this.renderSearchForm}
                    loading={loading}
                    data={data}
                    columns={columns}
                   
                    noCheck
                    isSerial
                    // selectRow={this.selectRow}
                    clearChildSelect={this.clearChildSelect}
                // para={filterkeys}
                />
        
            </Panel>
        )
    }
}

export default orderhistory;
