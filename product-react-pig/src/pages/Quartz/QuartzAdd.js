import React, { PureComponent } from 'react';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Input, Card, Button, Row, Col } from 'antd';
import { connect } from 'dva';
import Panel from '../../components/Panel';
import styles from '../../layouts/Sword.less';
import { QUARTZ_SUBMIT } from '../../actions/quartz';

const { TextArea } = Input;
const FormItem = Form.Item;
@connect(({ loading }) => ({
  submitting: loading.effects['quartz/submit'],
}))
@Form.create()
class QuartzAdd extends PureComponent {
  handleSubmit = e => {
    e.preventDefault();
    const { dispatch, form } = this.props;
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        dispatch(QUARTZ_SUBMIT(values));
      }
    });
  };

  render() {
    const {
      form: { getFieldDecorator },
      submitting,
    } = this.props;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 },
        md: { span: 10 },
      },
    };
    const formAllItemLayout = {
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 20,
      },
    };

    const action = (
      <Button type="primary" onClick={this.handleSubmit} loading={submitting}>
        提交
      </Button>
    );

    return (
      <Panel title="新增" back="/gotask/list" action={action}>
        <Form hideRequiredMark style={{ marginTop: 8 }}>
          <Card title="基本信息" className={styles.card} bordered={false}>
            <FormItem {...formItemLayout} label="任务名称">
              {getFieldDecorator('jobName', {
                rules: [
                  {
                    required: true,
                    message: '请输入任务名称',
                  },
                ],
              })(<Input placeholder="请输入任务名称" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="工作组">
              {getFieldDecorator('jobGroup', {
                rules: [
                  {
                    required: true,
                    message: '请输入工作组',
                  },
                ],
              })(<Input placeholder="请输入工作组" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="定时任务工作类">
              {getFieldDecorator('className', {
                rules: [
                  {
                    required: true,
                    message: '请输入定时任务工作类',
                  },
                ],
              })(<Input placeholder="请输入定时任务工作类" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="时间表达式">
              {getFieldDecorator('cron', {
                rules: [
                  {
                    required: true,
                    message: '请输入时间表达式',
                  },
                ],
              })(<Input placeholder="请输入时间表达式" />)}
            </FormItem>
          </Card>
          <Card title="其他信息" className={styles.card} bordered={false}>
            <Row gutter={24}>
              <Col span={20}>
                <FormItem {...formAllItemLayout} label="备注">
                  {getFieldDecorator('desc')(<TextArea rows={4} placeholder="请输入备注" />)}
                </FormItem>
              </Col>
            </Row>
            <Row gutter={24}>
              <Col span={20}>
              <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                <Button type="primary" onClick={this.handleSubmit} loading={submitting}>
                  保 存
                </Button>
              </Form.Item>
              
              </Col>
            </Row>
          </Card>
          
        </Form>
      </Panel>
    );
  }
}

export default QuartzAdd;
