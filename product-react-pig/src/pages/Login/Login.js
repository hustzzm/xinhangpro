import React, { Component } from 'react';
import { connect } from 'dva';
import { formatMessage, FormattedMessage } from 'umi/locale';
import { Checkbox, Alert } from 'antd';
import Login from '../../components/Login';
import styles from './Login.less';
import { tenantMode, captchaMode } from '../../defaultSettings';

const { Tab, TenantId, UserName, Password, Captcha, Submit } = Login;

@connect(({ login, loading }) => ({
  login,
  submitting: loading.effects['login/login'],
}))
class LoginPage extends Component {
  state = {
    type: 'account',
    autoLogin: true,
  };

  onTabChange = type => {
    this.setState({ type });
  };

  onGetCaptcha = () =>
    new Promise((resolve, reject) => {
      this.loginForm.validateFields(['mobile'], {}, (err, values) => {
        if (err) {
          reject(err);
        } else {
          const { dispatch } = this.props;
          dispatch({
            type: 'login/getCaptcha',
            payload: values.mobile,
          })
            .then(resolve)
            .catch(reject);
        }
      });
    });

  handleSubmit = (err, values) => {
    const { type } = this.state;
    if (!err) {
      const { dispatch } = this.props;
      dispatch({
        type: 'login/login',
        payload: {
          ...values,
          type,
        },
      });
    }
  };

  changeAutoLogin = e => {
    this.setState({
      autoLogin: e.target.checked,
    });
  };

  renderMessage = content => (
    <Alert style={{ marginBottom: 24 }} message={content} type="error" showIcon />
  );

  render() {
    const { login, submitting } = this.props;
    const { type, autoLogin } = this.state;
    return (
      <div className={styles.main}>
        <Login
          defaultActiveKey={type}
          onTabChange={this.onTabChange}
          onSubmit={this.handleSubmit}
          ref={form => {
            this.loginForm = form;
          }}
        >
          {/* <div className={styles.tabbox}> */}
          {/* <Tab key="account" tab={formatMessage({ id: 'app.login.tab-login-credentials' })} className={styles.colors}> */}
            <p className={styles.colors}>账号密码登录</p>
            <UserName
              name="account"
              placeholder={`${formatMessage({ id: 'app.login.userName' })}: `}
              rules={[
                {
                  required: true,
                  message: formatMessage({ id: 'validation.userName.required' }),
                },
              ]}
            />
            <Password
              name="password"
              placeholder={`${formatMessage({ id: 'app.login.password' })}: `}
              rules={[
                {
                  required: true,
                  message: formatMessage({ id: 'validation.password.required' }),
                },
              ]}
              onPressEnter={() => this.loginForm.validateFields(this.handleSubmit)}
            />
            {captchaMode ? <Captcha name="code" mode="image" /> : null}
          {/* </Tab> */}
          {/* </div> */}
          {/*<div>*/}
          {/*  <Checkbox checked={autoLogin} onChange={this.changeAutoLogin}>*/}
          {/*    <FormattedMessage id="app.login.remember-me" />*/}
          {/*  </Checkbox>*/}
          {/*  <a style={{ float: 'right' }} href="">*/}
          {/*    <FormattedMessage id="app.login.forgot-password" />*/}
          {/*  </a>*/}
          {/*</div>*/}
          <Submit loading={submitting}>
            <FormattedMessage id="app.login.login" />
          </Submit>
        </Login>
      </div>
    );
  }
}

export default LoginPage;
