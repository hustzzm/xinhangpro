import React, { PureComponent, Fragment } from 'react';
import { Table, Alert } from 'antd';
import styles from './index.less';
// import './index.less';

function initTotalList(columns) {
  const totalList = [];
  columns.forEach(column => {
    if (column.needTotal) {
      totalList.push({ ...column, total: 0 });
    }
  });
  return totalList;
}

class CommonTable extends PureComponent {
  constructor(props) {
    super(props);
    const { columns, rowKey = 'id' } = props;
    const needTotalList = initTotalList(columns);

    // 使用外层传入的 selectedRows 初始化选中行，避免使用 Grid 时重新初始化 StandardTable 导致的状态异常
    const selectedRowKeys = props.selectedRows ? props.selectedRows.map(row => row[rowKey]) : [];

    this.state = {
      selectedRowKeys,
      needTotalList,
      expandProps: props.expandProps,
    };
  }

  static getDerivedStateFromProps(nextProps) {
    // clean state
    //update by zzm 20201015
    // 如果未设置checkbox选择，则增加为空的判断条件
    if (!nextProps.selectedRows || nextProps.selectedRows.length === 0) {
      const needTotalList = initTotalList(nextProps.columns);
      return {
        selectedRowKeys: [],
        needTotalList,
      };
    }
    if (nextProps.expandProps) {
      return {
        expandProps: nextProps.expandProps,
      };
    }
    return null;
  }

  handleRowSelectChange = (selectedRowKeys, selectedRows) => {
    let { needTotalList } = this.state;
    needTotalList = needTotalList.map(item => ({
      ...item,
      total: selectedRows.reduce((sum, val) => sum + parseFloat(val[item.dataIndex], 20), 0),
    }));
    const { onSelectRow } = this.props;
    if (onSelectRow) {
      onSelectRow(selectedRows);
    }

    this.setState({ selectedRowKeys, needTotalList });
  };

  handleTableChange = (pagination, filters, sorter) => {
    const { onChange } = this.props;
    if (onChange) {
      onChange(pagination, filters, sorter);
      console.log(sorter);
    }
  };

  cleanSelectedKeys = () => {
    this.handleRowSelectChange([], []);
  };

  render() {
    const { selectedRowKeys, needTotalList, expandProps } = this.state;
    const { data = {},noCheck, rowKey, alert = false, ...rest } = this.props;
    const { list = [], pagination } = data;
    const paginationProps = pagination
      ? {
          showSizeChanger: true,
          showQuickJumper: true,
          ...pagination,
          showTotal: (total) => `共 ${total} 条`,
        }
      : false;

    const rowSelection = {      
      selectedRowKeys,     
      onChange: this.handleRowSelectChange,
      getCheckboxProps: record => ({
        disabled: record.disabled,
      }),
    };

    return (
      <div className={styles.standardTable}>
        {alert ? (
          <div className={styles.tableAlert}>
            <Alert
              message={
                <Fragment>
                  已选择 <a style={{ fontWeight: 600 ,width:50}}>{selectedRowKeys.length}</a> 项&nbsp;&nbsp;
                  {needTotalList.map(item => (
                    <span style={{ marginLeft: 8 }} key={item.dataIndex}>
                      {item.title}
                      总计&nbsp;
                      <span style={{ fontWeight: 600 ,width:50}}>
                        {item.render ? item.render(item.total) : item.total}
                      </span>
                    </span>
                  ))}
                  <a onClick={this.cleanSelectedKeys} style={{ marginLeft: 24 }}>
                    清空
                  </a>
                </Fragment>
              }
              type="info"
              showIcon
            />
          </div>
        ) : null}
        {noCheck ? (
          <Table
          className={styles.TowRowHeaderStyle}
            rowKey={rowKey || 'key'}
            dataSource={list}
            pagination={paginationProps}
            onChange={this.handleTableChange}
            {...rest}
            {...expandProps}
          />
        ) : (
          <Table
          className={styles.TowRowHeaderStyle}
            rowKey={rowKey || 'key'}
            rowSelection={rowSelection}            
            dataSource={list}
            pagination={paginationProps}
            onChange={this.handleTableChange}
            {...rest}
            {...expandProps}
          />
        )
        }
        {/*<Table*/}
        {/*  rowKey={rowKey || 'key'}*/}
        {/*  rowSelection={rowSelection}*/}
        {/*  dataSource={list}*/}
        {/*  pagination={paginationProps}*/}
        {/*  onChange={this.handleTableChange}*/}
        {/*  {...rest}*/}
        {/*  {...expandProps}*/}
        {/*/>*/}
      </div>
    );
  }
}

export default CommonTable;
