import { connect } from "dva";
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Card, Col, DatePicker, Input, Row, Select, message, Divider } from "antd";
import React, { PureComponent } from "react";
import Panel from '../../components/Panel';


const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ gtmember, dictionary, loading }) => ({
  gtmember,
    dictionary,
    submitting: loading.effects['gtmember/submit'],
}))
@Form.create()
class GtMemberAdd extends PureComponent {
    state = {};
    handleSubmit = (value) => {
        const {dispatch} = this.props;
        // // values.chip_id = detail.chip_id;

        // let filter = wessampleType.filter(item => item.dictValue == values.sample_type);
        // if (filter.length) {
        //     values.sample_type = filter[0].dictKey;
        // }
        // dispatch(TEST_ADD(values))
        //     .then(result => {

        //         if (result.success) {
        //             message.success('操作成功！');
        //         } else {
        //             message.error(result.msg);
        //             return false;
        //         }
        //     });
    };

    render() {

        const {
            form: { getFieldDecorator },
            submitting,
            gtmember: { submit },
            isEdit,
            wessampleType, wessampleClass
        } = this.props;
        const formItemLayout = {
            labelCol: {
                xs: { span: 24, },
                sm: { span: 7 },
            },
            wrapperCol: {
                // xs: { span: 24 },
                // sm: { span: 12 },
                // md: { span: 10 },
            },
        };

        const formAllItemLayout = {
            labelCol: {
                span: 3,
                //sm: { span: 2 },
            },
            wrapperCol: {
                span: 21,
            },
        };

        const dateFormat = 'YYYY-MM-DD';
        const locale = {
            "lang": {
                "locale": "zh-CN"
            }
        }
        const action = (
            <Button type="primary" onClick={this.handleSubmit} loading={submitting}>
                提交
            </Button>
        );
      
        return (
            <Panel back="/gtmember/list" action={action}>
                <Form>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>
                        <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="用户名" >
                                    {getFieldDecorator('account', {
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '用户名不能为空，且不能超过30字符',
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="编号" >
                                    {getFieldDecorator('code', {
                                        rules: [
                                            { 
                                                pattern:/^[0-9]*[1-9][0-9]*$/,
                                                max:30,
                                                required: true,
                                                message: '编号不能为空，必须为正整数',
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="Token">
                                    {getFieldDecorator('token', {

                                        rules: [
                                            {
                                                max:300,
                                                required: true,
                                                message: 'Token不能为空，且不能超过300字符',
                                            },
                                        ],
                                    })(
                                        <TextArea rows={5}/>)}
                                </FormItem>
                            </Col>              
                        </Row>
                        <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="昵称" >
                                    {getFieldDecorator('name', {
                                        rules: [
                                            {                                       
                                                required: false,                                               
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="Email" >
                                    {getFieldDecorator('email', {
                                        rules: [
                                            {                                            
                                                required: false,   
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                    </Card>
                </Form>
            </Panel>

        )
    }
}
export default GtMemberAdd;
