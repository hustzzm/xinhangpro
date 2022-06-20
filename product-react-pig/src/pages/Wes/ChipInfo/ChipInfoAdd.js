import React, {PureComponent} from 'react';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Input, Card, Row, Col, Button, DatePicker, Select,message } from 'antd';
import { connect } from 'dva';
import Panel from '../../../components/Panel';
import styles from '../../../layouts/Sword.less';
import {CHIPINFO_SUBMIT} from "../../../actions/chipinfo";
import moment from "moment";
import {CHIPCELL_DICT} from "../../../actions/dictionary";
import request from '../../../utils/request';


const FormItem = Form.Item;
@connect(({ chipinfo,dictionary, loading }) => ({
  chipinfo,
  dictionary,
  submitting: loading.effects['dpchipcase/submit'],
}))
@Form.create()
class ChipInfoAdd extends PureComponent {

  componentWillMount() {
    const { dispatch } = this.props;
    /**
     * 查询样本状态的字段
     */
    let params = {code:'dpchipCell'}
    dispatch(CHIPCELL_DICT(params));

  }

  renderCellSelect = () =>{
    const {
      dictionary: { dpchipCell },
    } = this.props;
    let children = [];
    for (let i = 0; i < dpchipCell.length; i++) {
      children.push(<Option key={dpchipCell[i].dictKey}>{dpchipCell[i].dictValue}</Option>);
    }
    return (
      <Select
        //mode="multiple"
        //style={{ width: '200px' }}
        placeholder="请选择cell编号"
      >
        {children}
      </Select>
    )
  }

  /** 提交事件 */
  doSubmit = (values) => {

    const { dispatch, form } = this.props;
    dispatch(CHIPINFO_SUBMIT(values)).then(result =>{
     if(result.success){
      message.success('操作成功！');
     }
    });
  };

  /** 提交事件 */
  // handleSubmit = e => {
  //   e.preventDefault();
  //   const { dispatch, form } = this.props;
  //   form.validateFieldsAndScroll((err, values) => {
  //     if (!err) {
  //       //values.starttime = moment(values.starttime).format('YYYY-MM-DD HH:mm:ss');
  //       dispatch(CHIPINFO_SUBMIT(values));
  //     }
  //   });
  // };

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

    const dateFormat = 'YYYY-MM-DD';
    return (
      <Panel title="新增" back="/dpchipcase/list">
        <Form style={{ marginTop: 8 }}  >
          <Card className={styles.card} bordered={false}>
            <Row gutter={24}>
              <Col span={24}>
                <FormItem {...formItemLayout} label="混合文库名称">
                  {getFieldDecorator('chip_id', {
                    rules: [
                      {
                        required: true,
                        message: '请输入混合文库名称',
                      },
                      { max: 30, message: '最多只能输入30个字符！' }
                    ],
                  })(<Input placeholder="请输入混合文库名称"/>)}
                </FormItem>
              </Col>
              </Row>
              <Row gutter={24}>
              <Col span={24}>
                <FormItem {...formItemLayout} label="上机时间">
                  {getFieldDecorator('starttime', {
                    rules: [
                      {
                        required: true,
                        message: '请选择上机时间',
                      },
                    ],
                  })(<DatePicker format={dateFormat} placeholder="请选择上机时间"/>)}
                </FormItem>
              </Col>
              </Row>
              <Row gutter={24}>
              <Col span={24}>
                <FormItem {...formItemLayout} label="测序仪编号">
                  {getFieldDecorator('sequencer_number',{
                    rules: [
                      {
                        required: true,
                        message: '请输入测序仪编号',
                      },
                      { max: 30, message: '最多只能输入30个字符！' }
                    ],
                  })(<Input placeholder="请输入测序仪编号" />)}
                </FormItem>
              </Col>
            </Row>
            <Row gutter={24}>
              <Col span={24}>
                <FormItem {...formItemLayout} label="cell编号">
                  {getFieldDecorator('cell_number',{
                    rules: [
                      {
                        required: true,
                        message: '请选择cell编号',
                      },
                    ],
                  })(
                   this.renderCellSelect())
                  }
                </FormItem>
              </Col>
            </Row>

          </Card>
        </Form>
        </Panel>
    )
  }
}
export default ChipInfoAdd;
