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

@connect(({ company, dictionary, loading }) => ({
    company,
    dictionary,
    submitting: loading.effects['company/submit'],
}))
@Form.create()
class Companyedit extends PureComponent {

    state = {
        filename1:'',
        filename2:'',
        filename3:''
    };
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

    handleChange1 = (event) => {
        
        const that = this;
        let fileData = event.target.files[0]
        if(!fileData){
            return false;
        }
        const formdata = new FormData();
        formdata.append('file', fileData)

        Axios.post("/api/bizCompany/base64Upload", formdata, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(res => {   
            
        
            that.setState({filename1:res.data})
        }).catch(res => {
            
            that.setState({filename1:''})
        })
    }

    handleChange2 = (event) => {
        
        const that = this;
        let fileData = event.target.files[0]
        if(!fileData){
            return false;
        }
        const formdata = new FormData();
        formdata.append('file', fileData)

        Axios.post("/api/bizCompany/base64Upload", formdata, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(res => {  
          
            that.setState({filename2:res.data})
        }).catch(res => {
            that.setState({filename2:''})
        })
    }

    handleChange3 = (event) => {
        
        const that = this;
        let fileData = event.target.files[0]
        if(!fileData){
            return false;
        }
        const formdata = new FormData();
        formdata.append('file', fileData)

        Axios.post("/api/bizCompany/base64Upload", formdata, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(res => {  
        
            that.setState({filename3:res.data})
        }).catch(res => {
            that.setState({filename3:''})
        })
    }
    
    render() {

        const {
            form: { getFieldDecorator },
            submitting,
            company: { submit },
            isEdit,
            detail,
            commontypeDicts
        } = this.props;
        const {
            fileList
        } = this.state;
        const formItemLayout = {
            labelCol: {
              
                width:200
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
            <Panel back="/company/list" action={action}>
                <Form ref={this.formRef}>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>

                        <Row gutter={32}>
                            <Col span={12}>
                                <FormItem {...formItemLayout} label="商家名称" >
                                    {getFieldDecorator('compname', {
                                         initialValue: detail.compname,
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
                                <FormItem {...formItemLayout} label="商家类别" >
                                {getFieldDecorator('comptype', {
                                    initialValue: detail.comptype,
                                    rules: [
                                        {   
                                            required: true,
                                            message: '商家类别不能为空',
                                        },
                                    ],
                                    })(
                                        <Select placeholder="请选择商家类别" style={{width:149}}>
                                          {commontypeDicts.map((item, index) => <Option key={`roomType-option-${index}`} 
                                            value={item.dictKey}>{item.dictValue}</Option>)}
                                        </Select>
                                    )}
                                </FormItem>
                            </Col>
                            </Row>

                            <Row gutter={32}>
                            <Col span={12}>
                                <FormItem {...formItemLayout} label="联系电话">
                                    {getFieldDecorator('telphone', {
                                        initialValue: detail.telphone,
                                        rules: [
                                            {   
                                                required: true,
                                                message: '联系电话不能为空',
                                            },
                                        ],
                                    })(
                                        <Input  />                                     
                                    )}
                                </FormItem>
                            </Col>
                            <Col span={12}>
                            <FormItem {...formItemLayout} label="折扣      " >
                                    {getFieldDecorator('shopfee', {
                                        initialValue: detail.shopfee,
                                        rules: [
                                            {   
                                                required: false,
                                                message: '折扣不能为空',
                                            },
                                        ],
                                    })(
                                        <Input  />      
                                    )}
                                </FormItem>
                            </Col>              
                            </Row>                           
                        <Row gutter={32}>
                            <Col span={24}>
                                <FormItem {...formItemLayout} label="商家详情" >
                                    {getFieldDecorator('details', {
                                        initialValue: detail.details,
                                        rules: [
                                            {                                                
                                                required: false,
                                                message: '商家详情不能为空',
                                            },
                                        ],
                                    })( <Input.TextArea rows={8}></Input.TextArea>)}
                                </FormItem>
                            </Col>
                            </Row>         
                     
                         <Row gutter={32}>
                            <Col span={24}>
                            <Form.Item
                                name="toppig1"
                                {...formItemLayout}
                                label="商家logo"
                                extra="文件大小 500k 以内, 仅支持 JPG/PNG"
                                rules={[
                                {
                                    required: true,
                                    message: '商家logo必填！',
                                },
                                ]}
                            >                                
                             <Input type="file"  onChange={this.handleChange1} />
                            </Form.Item>
                            </Col>
                         </Row>  
                         <Row gutter={32}>
                            <Col span={24}>
                            <Form.Item
                                name="subheadpig1"
                                {...formItemLayout}
                                label="商家门店图片"
                                extra="文件大小 500k 以内, 仅支持 JPG/PNG"  >                                
                             <Input type="file" onChange={this.handleChange2} />
                            </Form.Item>
                            </Col>
                         </Row>  
                         <Row gutter={32}>
                            <Col span={24}>
                            <Form.Item
                                name="subpig1"
                                {...formItemLayout}
                                label="商家宣传图片"
                                extra="文件大小 500k 以内, 仅支持 JPG/PNG"
                                rules={[
                                {
                                    required: false,
                                    message: '商家logo必填！',
                                },
                                ]}
                            >
                             <Input type="file" onChange={this.handleChange3} />
                            </Form.Item>
                            </Col>
                         </Row>                         
                    </Card>
                </Form>
            </Panel>

        )
    }
}
export default Companyedit;
