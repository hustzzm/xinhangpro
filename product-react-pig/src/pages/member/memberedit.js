import { connect } from "dva";
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Card, Col, DatePicker, Input, Row, Select,Upload, message, Divider } from "antd";
import React, { PureComponent } from "react";
import Panel from '../../components/Panel';
import Axios from "axios";

import { UploadOutlined,PlusOutlined  } from '@ant-design/icons';

import { uploadTemplate } from "../../services/gt/api";
import { Label } from "bizcharts";
const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ member, dictionary, loading }) => ({
    member,
    dictionary,
    submitting: loading.effects['member/submit'],
}))
@Form.create()
class memberedit extends PureComponent {

    state = {
        filename1:'',
        filename2:'',
        filename3:''
    };
    handleSubmit = (value) => {
        const {dispatch} = this.props;
    };

    
    render() {

        const {
            form: { getFieldDecorator },
            submitting,
            member: { submit },
            isEdit,
            detail,         
        } = this.props;
        const {
         
        } = this.state;
        
        const formItemLayout = {
            // labelCol: {
              
            //     width:200
            // },
            // wrapperCol: {
            //     xs: { span: 24 },
            //     sm: { span: 12 },
            //     md: { span: 10 },
            // },
        };

        const formAllItemLayout = {
            labelCol: {
                span: 4,
               
            },
            wrapperCol: {
                span: 14
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
            <Panel back="/member/list" action={action}>
                <Form {...formAllItemLayout} ref={this.formRef}>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>

                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem {...formItemLayout} label="会员卡号" >
                                    {getFieldDecorator('orderNo', {
                                         initialValue: detail.orderNo,
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '会员姓名不能为空，且不能超过100字符',
                                            },
                                        ],
                                    })(<Input disabled={true} />)}
                                </FormItem>
                            </Col>                           
                            </Row>      
                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem {...formItemLayout} label="会员姓名" >
                                    {getFieldDecorator('name', {
                                         initialValue: detail.name,
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '会员姓名不能为空，且不能超过100字符',
                                            },
                                        ],
                                    })(<Input />)}
                                </FormItem>
                            </Col>                           
                            </Row>                            
                                   
                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem {...formItemLayout} label="性别" >
                                {getFieldDecorator('gender', {
                                    initialValue: detail.gender,
                                    rules: [
                                        {   
                                            required: false,
                                            message: '性别不能为空',
                                        },
                                    ],
                                    })(
                                        <Select placeholder="请选择性别" style={{width:149}}>
                                         <Option value="1">男</Option>
                                         <Option value="2">女</Option>
                                         <Option value="3">未知</Option>
                                        </Select>
                                    )}
                                </FormItem>
                            </Col>
                        </Row>  
                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem {...formItemLayout} label="备注" >
                                {getFieldDecorator('remark', {
                                    initialValue: detail.remark,
                                    rules: [
                                        {   
                                            required: false,
                                            message: '备注不能为空',
                                        },
                                    ],
                                    })(
                                        <Input.TextArea rows={8}
                                        value={detail.remark}>
                                      </Input.TextArea>
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
export default memberedit;
