import React, { PureComponent } from 'react';
import { FormattedMessage, formatMessage } from 'umi/locale';
import { LogoutOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';
import { Spin, Tag, Menu, Avatar, Tooltip } from 'antd';
import moment from 'moment';
import groupBy from 'lodash/groupBy';
import NoticeIcon from '../NoticeIcon';
import HeaderSearch from '../HeaderSearch';
import HeaderDropdown from '../HeaderDropdown';
import SelectLang from '../SelectLang';
import styles from './index.less';

export default class GlobalHeaderRight extends PureComponent {
  getNoticeData() {
    const { notices = [] } = this.props;
    if (notices.length === 0) {
      return {};
    }
    const newNotices = notices.map(notice => {
      const newNotice = { ...notice };
      if (newNotice.datetime) {
        newNotice.datetime = moment(notice.datetime).fromNow();
      }
      if (newNotice.id) {
        newNotice.key = newNotice.id;
      }
      if (newNotice.extra && newNotice.status) {
        const color = {
          todo: '',
          processing: 'blue',
          urgent: 'red',
          doing: 'gold',
        }[newNotice.status];
        newNotice.extra = (
          <Tag color={color} style={{ marginRight: 0 }}>
            {newNotice.extra}
          </Tag>
        );
      }
      return newNotice;
    });
    return groupBy(newNotices, 'type');
  }

  getUnreadData = noticeData => {
    const unreadMsg = {};
    Object.entries(noticeData).forEach(([key, value]) => {
      if (!unreadMsg[key]) {
        unreadMsg[key] = 0;
      }
      if (Array.isArray(value)) {
        unreadMsg[key] = value.filter(item => !item.read).length;
      }
    });
    return unreadMsg;
  };

  changeReadState = clickedItem => {
    const { id } = clickedItem;
    const { dispatch } = this.props;
    dispatch({
      type: 'global/changeNoticeReadState',
      payload: id,
    });
  };

  fetchMoreNotices = tabProps => {
    const { list, name } = tabProps;
    const { dispatch, notices = [] } = this.props;
    const lastItemId = notices[notices.length - 1].id;
    dispatch({
      type: 'global/fetchMoreNotices',
      payload: {
        lastItemId,
        type: name,
        offset: list.length,
      },
    });
  };

  render() {
    const {
      currentUser,
      fetchingMoreNotices,
      fetchingNotices,
      loadedAllNotices,
      onNoticeVisibleChange,
      onMenuClick,
      onNoticeClear,
      skeletonCount,
      theme,
    } = this.props;
    const menu = (
      <Menu
        className={styles.menu}
        style={{ marginRight: 1 }}
        selectedKeys={[]}
        onClick={onMenuClick}
      >
      
        {/*      
        隐藏掉个人设置
        modify by zzm 20201026

        <Menu.Item key="userinfo">
          <SettingOutlined />
          <FormattedMessage id="menu.account.settings" defaultMessage="account settings" />
        </Menu.Item> */}
        <Menu.Item key="password">
          <UserOutlined />
          <FormattedMessage id="menu.account.password" defaultMessage="password settings" />
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item key="logout">
          <LogoutOutlined />
          <FormattedMessage id="menu.account.logout" defaultMessage="logout" />
        </Menu.Item>
      </Menu>
    );
    const loadMoreProps = {
      skeletonCount,
      loadedAll: loadedAllNotices,
      loading: fetchingMoreNotices,
    };
    const noticeData = this.getNoticeData();
    const unreadMsg = this.getUnreadData(noticeData);
    let className = styles.right;
    if (theme === 'dark') {
      className = `${styles.right}  ${styles.dark}`;
    }
    return (
      <div className={className} style={{ marginRight: 8 }}>
        {/*<HeaderSearch*/}
        {/*  className={`${styles.action} ${styles.search}`}*/}
        {/*  placeholder={formatMessage({ id: 'component.globalHeader.search' })}*/}
        {/*  dataSource={[*/}
        {/*    formatMessage({ id: 'component.globalHeader.search.example1' }),*/}
        {/*    formatMessage({ id: 'component.globalHeader.search.example2' }),*/}
        {/*    formatMessage({ id: 'component.globalHeader.search.example3' }),*/}
        {/*  ]}*/}
        {/*  onSearch={value => {*/}
        {/*    console.log('input', value); // eslint-disable-line*/}
        {/*  }}*/}
        {/*  onPressEnter={value => {*/}
        {/*    console.log('enter', value); // eslint-disable-line*/}
        {/*  }}*/}
        {/*/>*/}
        {/*<Tooltip title={formatMessage({ id: 'component.globalHeader.help' })}>*/}
        {/*  <a*/}
        {/*    target="_blank"*/}
        {/*    href="https://ant.design/docs/react/introduce-cn"*/}
        {/*    rel="noopener noreferrer"*/}
        {/*    className={styles.action}*/}
        {/*  >*/}
        {/*    <Icon type="question-circle-o" />*/}
        {/*  </a>*/}
        {/*</Tooltip>*/}
        {/*<NoticeIcon*/}
        {/*  className={styles.action}*/}
        {/*  count={currentUser.unreadCount}*/}
        {/*  onItemClick={(item, tabProps) => {*/}
        {/*    console.log(item, tabProps); // eslint-disable-line*/}
        {/*    this.changeReadState(item, tabProps);*/}
        {/*  }}*/}
        {/*  locale={{*/}
        {/*    emptyText: formatMessage({ id: 'component.noticeIcon.empty' }),*/}
        {/*    clear: formatMessage({ id: 'component.noticeIcon.clear' }),*/}
        {/*    loadedAll: formatMessage({ id: 'component.noticeIcon.loaded' }),*/}
        {/*    loadMore: formatMessage({ id: 'component.noticeIcon.loading-more' }),*/}
        {/*  }}*/}
        {/*  onClear={onNoticeClear}*/}
        {/*  onLoadMore={this.fetchMoreNotices}*/}
        {/*  onPopupVisibleChange={onNoticeVisibleChange}*/}
        {/*  loading={fetchingNotices}*/}
        {/*  clearClose*/}
        {/*>*/}
        {/*  <NoticeIcon.Tab*/}
        {/*    count={unreadMsg.notification}*/}
        {/*    list={noticeData.notification}*/}
        {/*    title={formatMessage({ id: 'component.globalHeader.notification' })}*/}
        {/*    name="notification"*/}
        {/*    emptyText={formatMessage({ id: 'component.globalHeader.notification.empty' })}*/}
        {/*    emptyImage="https://gw.alipayobjects.com/zos/rmsportal/wAhyIChODzsoKIOBHcBk.svg"*/}
        {/*    {...loadMoreProps}*/}
        {/*  />*/}
        {/*  <NoticeIcon.Tab*/}
        {/*    count={unreadMsg.message}*/}
        {/*    list={noticeData.message}*/}
        {/*    title={formatMessage({ id: 'component.globalHeader.message' })}*/}
        {/*    name="message"*/}
        {/*    emptyText={formatMessage({ id: 'component.globalHeader.message.empty' })}*/}
        {/*    emptyImage="https://gw.alipayobjects.com/zos/rmsportal/sAuJeJzSKbUmHfBQRzmZ.svg"*/}
        {/*    {...loadMoreProps}*/}
        {/*  />*/}
        {/*  <NoticeIcon.Tab*/}
        {/*    count={unreadMsg.event}*/}
        {/*    list={noticeData.event}*/}
        {/*    title={formatMessage({ id: 'component.globalHeader.event' })}*/}
        {/*    name="event"*/}
        {/*    emptyText={formatMessage({ id: 'component.globalHeader.event.empty' })}*/}
        {/*    emptyImage="https://gw.alipayobjects.com/zos/rmsportal/HsIsxMZiWKrNUavQUXqx.svg"*/}
        {/*    {...loadMoreProps}*/}
        {/*  />*/}
        {/*</NoticeIcon>*/}
        {currentUser.name ? (
          <HeaderDropdown overlay={menu} style={{ marginRight: 8 }}>
            <span className={`${styles.action} ${styles.account}`}>
              <Avatar
                size="small"
                className={styles.avatar}
                src={currentUser.avatar}
                alt="avatar"
              />
              <span className={styles.name}>{currentUser.name}</span>
            </span>
          </HeaderDropdown>
        ) : (
          <Spin size="small" style={{ marginLeft: 8, marginRight: 8 }} />
        )}
        {/*<SelectLang className={styles.action} />*/}
      </div>
    );
  }
}
