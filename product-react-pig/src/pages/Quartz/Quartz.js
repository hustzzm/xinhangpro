import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Col, Input, message, Modal, Row } from 'antd';
import Panel from '../../components/Panel';
import { QUARTZ_LIST,QUARTZ_REMOVE } from '../../actions/quartz';
import Grid from '../../components/Berry/Grid';
import {requestApi} from "@/services/api";

const FormItem = Form.Item;

@connect(({ quartz, loading }) => ({
  quartz,
  loading: loading.models.param,
}))
@Form.create()
class Quartz extends PureComponent {

  // ============ 查询 ===============
  handleSearch = params => {
    const { dispatch } = this.props;
    dispatch(QUARTZ_LIST(params));
  };

  // ============ 查询表单 ===============
  renderSearchForm = onReset => {
    const { form } = this.props;
    const { getFieldDecorator } = form;

    return (
      <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
        <Col md={6} sm={24}>
          <FormItem label="任务名称">
            {getFieldDecorator('jobName')(<Input placeholder="请输入任务名称" />)}
          </FormItem>
        </Col>
        <Col>
          <div style={{ float: 'right' }}>
            <Button type="primary" htmlType="submit">
              查询
            </Button>
            <Button style={{ marginLeft: 8 }} onClick={onReset}>
              重置
            </Button>
          </div>
        </Col>
      </Row>
    );
  };

  render() {
    const code = 'gotask';

    const {
      form,
      loading,
      quartz: { data },
    } = this.props;


    const columns = [
      {
        title: 'jobName',
        dataIndex: 'jobName'
      },
      // {
      //   title: 'job组',
      //   dataIndex: 'jobGroup'
      // },
      {
        title: '执行类名',
        dataIndex: 'jobClassName'
      },
      {
        title: '下次执行时间',
        dataIndex: 'nextFireTime'
      },
      {
        title: '上次执行时间',
        dataIndex: 'prevFireTime'
      },
      {
        title: '当前状态',
        dataIndex: 'triggerState'
      },
      {
        title: '备注说明',
        dataIndex: 'description',
        width:300,
        render(text, record, index) {          
          if(text.length > 200){
            return <div title={text}>{text.substring(0,200) + '...'}</div>
          }else{
            return text;
          }
        }
      },
      {
        title: '执行cron表达式',
        dataIndex: 'cronExpression'
      }      
    ]
    return (
      <Panel>
        <Grid
          code={code}
          form={form}
          onSearch={this.handleSearch}
          renderSearchForm={this.renderSearchForm}
          loading={loading}
          data={data}
          columns={columns}
        />
      </Panel>
    );
  }
}
export default Quartz;
