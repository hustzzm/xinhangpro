import React, { Component } from 'react';
import { formatMessage } from 'umi/locale';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Input, Button, Card, message } from 'antd';
import router from 'umi/router';
import Panel from '../../../components/Panel';
import { updatePassword } from '../../../services/user';

const FormItem = Form.Item;

@Form.create()
class PasswordView extends Component {
  handleSubmit = e => {
    
    e.preventDefault();
    const { form } = this.props;
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        updatePassword(values).then(resp => {
          if (resp.success) {
            message.success(resp.msg);
          }
        });
      }
    });
  };

  /**返回上一操作菜单 */
  handelBack = e => {
    e.preventDefault();
    const { form } = this.props;
    router.goBack();
  }

  render() {
    const {
      form: { getFieldDecorator },
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

    const action = (
      <Button type="primary" onClick={this.handleSubmit}>
        提交
      </Button>
    );

    return (
      <Panel title="密码修改" back="/" action={action}>
        <Form style={{ marginTop: 8 }} hideRequiredMark>
          <Card title="基本信息" bordered={false}>
            <FormItem
              {...formItemLayout}
              label={formatMessage({ id: 'app.settings.password.old' })}
            >
              {getFieldDecorator('oldPassword', {
                rules: [
                  {
                    required: true,
                    message: formatMessage({ id: 'app.settings.password.old-message' }, {}),
                  },
                ],
              })(<Input type="password" />)}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label={formatMessage({ id: 'app.settings.password.new' })}
            >
              {getFieldDecorator('newPassword', {
                rules: [
                  {
                    required: true,
                    message: formatMessage({ id: 'app.settings.password.new-message' }, {}),
                  },
                ],
              })(<Input type="password" />)}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label={formatMessage({ id: 'app.settings.password.new1' })}
            >
              {getFieldDecorator('newPassword1', {
                rules: [
                  {
                    required: true,
                    message: formatMessage({ id: 'app.settings.password.new1-message' }, {}),
                  },
                ],
              })(<Input type="password" />)}
            </FormItem>
            <Form.Item {...formItemLayout} style={{'text-align':'right',marginLeft:'20px'}}>
            <Button type="primary" onClick={this.handleSubmit}>
              提交
            </Button>
            <Button onClick={this.handelBack} style={{marginLeft:'20px'}}>
              返回
            </Button>
        </Form.Item>
          </Card>
        </Form>
      </Panel>
    );
  }
}

export default PasswordView;
