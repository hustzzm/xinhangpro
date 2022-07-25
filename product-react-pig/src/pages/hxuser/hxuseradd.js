import { connect } from "dva";
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Card, Col, DatePicker, Input, Row, Select, message, Divider } from "antd";
import React, { PureComponent } from "react";
import Panel from '../../components/Panel';


const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ user, dictionary, loading }) => ({
    user,
    dictionary,
    submitting: loading.effects['user/submit'],
}))
@Form.create()
class hxuseradd extends PureComponent {
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
            user: { submit },
            isEdit,
            roomtypeDicts
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
            <Panel back="/hxuser/list" action={action}>
                <Form>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>
                    <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="帐号" >
                                    {getFieldDecorator('account', {
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '帐号不能为空，且不能超过30字符',
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                        <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="姓名" >
                                    {getFieldDecorator('name', {
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '姓名不能为空，且不能超过30字符',
                                            },
                                        ],
                                    })(<Input  />)}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="角色">
                                    {getFieldDecorator('roleId', {
                                        rules: [
                                            {   
                                                required: true,
                                                message: '角色不能为空',
                                            },
                                        ],
                                    })(
                                        <Select placeholder="请选择角色" 
                                             style={{ width: '190px' }}>
                                       <Option key="role1"  value="1123598816738675204">管理员</Option>
                                       <Option key="role2"  value="1123598816738675205">普通用户</Option>
                                        </Select>                                          
                                    )}
                                </FormItem>
                            </Col>
                            </Row>                             
                    </Card>
                </Form>
            </Panel>

        )
    }
}
export default hxuseradd;
