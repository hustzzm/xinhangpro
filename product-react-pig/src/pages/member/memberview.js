import { connect } from "dva";
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Card, Col, DatePicker, Input, Row, Select,Upload, message, Divider  } from "antd";
import React, { PureComponent } from "react";
import Panel from '../../components/Panel';
import Axios from "axios";

import { getCurrentUser } from '../../utils/authority';

const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ member, dictionary, loading }) => ({
    member,
    dictionary,
    submitting: loading.effects['member/submit'],
}))
@Form.create()
class memberview extends PureComponent {

    state = {
       
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
        const currentUser = getCurrentUser();
        const {
            fileList
        } = this.state;
     
       
        const formItemLayout = {
            labelCol: {
              
                span: 3
            },
            wrapperCol: {
                span: 21
            },
        };

        const formAllItemLayout = {
            labelCol: {
                span:6,
               
            },
            wrapperCol: {
                span: 18
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
                <Form  {...formAllItemLayout} ref={this.formRef}>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>

                    <Row gutter={32}>
                        <Col span={12}>
                            <FormItem  label="会员姓名" >
                                {getFieldDecorator('name', {
                                        initialValue: detail.name,
                                    rules: [
                                        { 
                                            max:30,
                                            required: true,
                                            message: '商家名称不能为空，且不能超过100字符',
                                        },
                                    ],
                                })(<Input disabled={true} />)}
                            </FormItem>
                        </Col>                                        
                        <Col span={12}>
                            <FormItem  label="会员卡号" >
                                {getFieldDecorator('orderNo', {
                                        initialValue: detail.orderNo,
                                    rules: [
                                        {                                    
                                            required: false,
                                            message: '',
                                        },
                                    ],
                                })(<Input disabled={true} />)}
                            </FormItem>
                        </Col>                                         
                    </Row>                     
                    <Row gutter={32}>
                     <Col span={12}>
                                    <FormItem  label="性别" >
                                    {getFieldDecorator('gender', {
                                        initialValue: detail.gender,
                                        rules: [
                                            {   
                                                required: false,
                                                message: '性别不能为空',
                                            },
                                        ],
                                        })(
                                            <Select placeholder="请选择性别" style={{width:149}} disabled={true} >
                                            <Option value="1">男</Option>
                                            <Option value="2">女</Option>
                                            <Option value="3">未知</Option>
                                            </Select>
                                        )}
                                    </FormItem>
                                </Col>          
                        <Col span={12}>
                            <FormItem  label="年龄" >
                                {getFieldDecorator('age', {
                                        initialValue: detail.age,
                                    rules: [
                                        {                                    
                                            required: false,
                                            message: '',
                                        },
                                    ],
                                })(<Input disabled={true} />)}
                            </FormItem>
                        </Col>                              
                        </Row>  
                        <Row gutter={32}>
                        <Col span={12}>
                            <FormItem  label="入会时间" >
                                {getFieldDecorator('startDate', {
                                        initialValue: detail.startDate,
                                    rules: [
                                        {                                    
                                            required: false,
                                            message: '',
                                        },
                                    ],
                                })(<Input disabled={true} />)}
                            </FormItem>
                        </Col>    
                        <Col span={12}>
                            <FormItem  label="会员到期时间" >
                                {getFieldDecorator('endDate', {
                                        initialValue: detail.endDate,
                                    rules: [
                                        {                                    
                                            required: false,
                                            message: '',
                                        },
                                    ],
                                })(<Input disabled={true} />)}
                            </FormItem>
                        </Col>                    
                          
                        </Row> 
                        <Row gutter={32}>                         
                           
                                <Col span={12}>
                                <FormItem  label="联系电话" >
                                    {getFieldDecorator('mobile', {
                                            initialValue: detail.mobile,
                                        rules: [
                                            {                                    
                                                required: false,
                                                message: '',
                                            },
                                        ],
                                    })(<Input disabled={true} />)}
                                </FormItem>
                            </Col>    
                            <Col span={12}>
                                    <FormItem  label="会员等级" >
                                    {getFieldDecorator('userLevel', {
                                        initialValue: detail.userLevel,
                                        rules: [
                                            {   
                                                required: false,
                                                message: '性别不能为空',
                                            },
                                        ],
                                        })(
                                            <Select placeholder="请选择会员等级" style={{width:149}} disabled={true} >
                                            <Option value="1">普通会员</Option>
                                            <Option value="2">钻石会员</Option>                                           
                                            </Select>
                                        )}
                                    </FormItem>
                                </Col>                                  
                        </Row>                     
                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem  {...formItemLayout}  label="备注" >
                                {getFieldDecorator('remark', {
                                    initialValue: detail.remark,
                                    rules: [
                                        {   
                                            required: false,
                                            message: '备注不能为空',
                                        },
                                    ],
                                    })(
                                        <Input.TextArea rows={5} readOnly={true} disabled={true} ></Input.TextArea>
                                    )}
                                </FormItem>
                            </Col>
                        </Row>                
                        <Divider orientation="left" plain>  </Divider>  
                         <Row gutter={32}>
                            <Col span={12}>
                            <Form.Item name="selfpig" label="本人正脸照片">                                    
                                    <img src= {currentUser.imgPath + detail.selfpig}  width={200} />
                            </Form.Item>                           
                            </Col>
                         </Row>  
                         <Divider orientation="left" plain>  </Divider>  
                         <Row gutter={32} style={{minHeight:200}}>
                            <Col span={12}>
                            <Form.Item
                                name="pigjs1" label="驾驶证正面">
                                   <img src={currentUser.imgPath + detail.pigjs1} width={300} />
                            </Form.Item>
                            </Col>
                            <Col span={12}>
                            <Form.Item
                                name="pigjs2" label="驾驶证反面">
                                   <img src={currentUser.imgPath + detail.pigjs2} width={300} />
                            </Form.Item>
                            </Col>
                         </Row>  
                         <Divider orientation="left" plain>  </Divider>  
                         <Row gutter={32} style={{minHeight:200}}>
                            <Col span={12}>
                            <Form.Item
                                name="pigxs1" label="行驶证正面">
                                   <img src={currentUser.imgPath + detail.pigxs1} width={300} />
                            </Form.Item>
                            </Col>
                            <Col span={12}>
                            <Form.Item
                                name="pigxs2" label="行驶证反面">
                                   <img src={currentUser.imgPath + detail.pigxs2} width={300} />
                            </Form.Item>
                            </Col>
                         </Row>  
                         <Divider orientation="left" plain>  </Divider>    
                         <Row gutter={32} style={{minHeight:200}}>
                            <Col span={12}>
                            <Form.Item
                                name="pigsf1" label="身份证正面">
                                   <img src={currentUser.imgPath + detail.pigsf1} width={300} />
                            </Form.Item>
                            </Col>
                            <Col span={12}>
                            <Form.Item
                                name="pigsf2" label="身份证反面">
                                   <img src={currentUser.imgPath + detail.pigsf2} width={300} />
                            </Form.Item>
                            </Col>
                         </Row>                      
                    </Card>
                </Form>
            </Panel>

        )
    }
}
export default memberview;
