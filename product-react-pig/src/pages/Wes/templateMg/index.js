/*
 * @Descripttion: 
 * @version: 
 * @Author: Eugene
 * @Date: 2022-02-23 16:45:07
 * @LastEditors: gn
 * @LastEditTime: 2022-02-25 16:13:35
 */

import React, { Fragment, useState, useEffect } from 'react';
import { Table, Form, Input, Select, Row, Col, Button, message, Modal, Card } from 'antd';
import styles from '../../../utils/utils.less';
import styleTester from "../index.less";
import { getList, deleteTemplateApi, downloadTemplateApi } from './api';
import AddTemplate from './addTemplate.js';
import {
  SyncOutlined,
  FilterOutlined,
  PlusOutlined,
  DownloadOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined
} from "@ant-design/icons";
import moment from 'moment';


const TemplateMg = function (props) {

  const [dataSource, setDataSource] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [selectedRow, setSelectedRow] = useState('');

  const [params, setParmas] = useState({ templateName: '', positive: null });
  const [isShowAddModal, setIsShowAddModal] = useState();

  const [searchForm] = Form.useForm();
  useEffect(() => {
    handleSearch({ templateName: '', positive: null });
  }, []);
  //styles

  const fnButtons = {
    marginRight: '10px'
  };
  const columns = [
    {
      title: '序号',
      render: (text, columnd, index) => {
        return index + 1
      }
    },
    {
      title: '模板名称',
      dataIndex: 'templateName'
    },
    {
      title: '模板文件',
      dataIndex: 'templateFilename'
    },
    {
      title: '模板类型',
      dataIndex: 'positive',
      render: (text, record) => {
        return text === 1 ? '阳性' : '阴性';
      }
    },

    {
      title: '上传时间',
      dataIndex: 'createTime',
      render: (text, record) => {
        return text != null ? moment(Date.parse(text)).format('YYYY-MM-DD HH:mm:ss') : '';
      }
    },
    {
      title: '上传人',
      dataIndex: 'createUserId'
    },
    {
      title: '模板描述',
      dataIndex: 'memo'
    },
  ];
  // 选中
  const rowSelection = {
    selectedRowKeys: selectedRowKeys,
    onChange: (selectedRowKeys, selectedRow) => {
      setSelectedRowKeys(selectedRowKeys);
      setSelectedRow(selectedRow);
    }
  };

  // 查询按钮
  const handleSearch = (e) => {
    let qparams = {};
    qparams.templateName = e.templateName ? e.templateName : '';
    qparams.positive = e.positive ? parseInt(e.positive) : null;
    getTableData(qparams);
    // if (!e.positive && !e.templateName) {
    //   getTableData({});
    // } else {
    //   getTableData(e);
    // }
  };

  // 重置
  const handleFormReset = () => {
    searchForm.resetFields();
    handleSearch({ templateName: '', positive: null });
  };

  // update by zzm 20220414
  // 查询表格
  const getTableData = (searchValue) => {
    setLoading(true);

    setParmas(searchValue);
    getList(searchValue).then(res => {

      if (res.success) {
        setDataSource(res.data);
      }
      setLoading(false);
    });
  };

  // 取消添加
  const handleCancelAdd = (form) => {
    setIsShowAddModal(false);
    form.resetFields();
  };

  // 添加
  const openAddModal = () => {
    setIsShowAddModal(true);
  };

  // 下载
  const downloadFile = () => {
    if (selectedRow.length !== 1) {
      message.warning('请选择一个模板进行操作！');
      return;
    };
    let a = document.createElement('a');
    let link = `/api/rdCnwTemplate/download?id=${selectedRowKeys[0]}`;
    a.href = link;
    document.body.appendChild(a);
    a.style.display = 'none';
    a.click(); //点击下载
    document.body.removeChild(a); //下载完成移除元素
    window.URL.revokeObjectURL(link); //释放掉blob对象
  };

  // 删除模板
  const deleteTemplate = () => {
    if (selectedRow.length === 0) {
      message.warning('请选择模板进行操作！');
      return;
    }
    Modal.confirm({
      title: '删除',
      icon: <ExclamationCircleOutlined />,
      content: `是否确认删除${selectedRowKeys.length}个模板？`,
      okText: '确认',
      cancelText: '取消',
      onCancel: () => {

      },
      onOk: () => {
        let formData = new FormData();
        let ids = selectedRowKeys.join(',')
        formData.append('id', ids);
        deleteTemplateApi(formData).then(res => {
          if (res.success) {
            message.success('删除成功！');
            getTableData(params);
            setSelectedRow([])
            setSelectedRowKeys([])
          };
        });
      }
    });
  };
  return (
    <Card bordered={false}>
      <Fragment>
        <Form onFinish={handleSearch} form={searchForm}>
          <div id="query-table" className={styles.query_item_css}>
            <Row gutter={16}>
              <Col span={10}>
                <Form.Item label="模板名称" name={'templateName'}>
                  <Input className={styleTester.width230} />
                </Form.Item>
              </Col>
              <Col span={10}>
                <Form.Item label="模板类型" name={'positive'} >
                  <Select className={styleTester.width230} allowClear={true} >
                    <Select.Option value="1" key="1">阳性</Select.Option>
                    <Select.Option value="0" key="0">阴性</Select.Option>

                  </Select>
                </Form.Item>
              </Col>
              <Col span={4}>
                <div className={styleTester.btnPosition}>
                  <Button icon={<FilterOutlined />} type="primary" htmlType="submit" >
                    查询
                </Button>
                  <Button icon={<SyncOutlined />} onClick={handleFormReset}>
                    重置
                </Button>
                </div>
              </Col>
            </Row>
          </div>
        </Form>
        <div style={{ margin: '20px 0' }}>
          <Button type='primary' style={fnButtons} icon={<PlusOutlined />} onClick={openAddModal}>新增</Button>
          <Button type='primary' style={fnButtons} icon={<DownloadOutlined />} onClick={downloadFile}>下载</Button>
          <Button type='primary' icon={<DeleteOutlined />} onClick={deleteTemplate}>删除</Button>
        </div>
        <Table
          columns={columns}
          dataSource={dataSource}
          pagination={false}
          bordered
          rowKey={"id"}
          loading={loading}
          rowSelection={rowSelection}></Table>
        {/* 新增模板 */}
        <AddTemplate
          isShowAddModal={isShowAddModal}
          refreshTable={() => { getTableData(params) }}
          handleCancelAdd={handleCancelAdd}
        >
        </AddTemplate>
      </Fragment>
    </Card>
  )
}

export default TemplateMg