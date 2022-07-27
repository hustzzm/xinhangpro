import { connect } from "dva";
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Card, Col, DatePicker, Input, Row, Select, message, Divider } from "antd";
import React, { PureComponent } from "react";
import Panel from '../../components/Panel';
import Axios from "axios";


const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ room, dictionary, loading }) => ({
    room,
    dictionary,
    submitting: loading.effects['room/submit'],
}))
@Form.create()
class roomedit extends PureComponent {
    state = {
        filename:'',
      
    };

    componentWillMount() {
        const {
            dispatch,        
        } = this.props;        
    }

    handleSubmit = (value) => {
        const {dispatch} = this.props;
    };

    handleChange = (event) => {
        
        const that = this;
        let fileData = event.target.files[0]
        if(!fileData){
            return false;
        }
        const formdata = new FormData();
        formdata.append('file', fileData)

        Axios.post("/api/bizCompany/base64UploadCircular", formdata, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(res => {   
            
        
            that.setState({filename:res.data})
        }).catch(res => {
            
            that.setState({filename:''})
        })
    }

    render() {

        const {
            form: { getFieldDecorator },
            submitting,
            room: { submit },
            detail,
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
            <Panel back="/wesanalysis/list" action={action}>
                <Form>
                    <Card title="" style={{ marginTop: '16px', padding: '8px', }} bordered={false}>
                        <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="房间名称" >
                                    {getFieldDecorator('name', {
                                         initialValue: detail.name,
                                        rules: [
                                            { 
                                                max:30,
                                                required: true,
                                                message: '房间名称不能为空，且不能超过30字符',
                                            },
                                        ],
                                    })(<Input  disabled={true}/>)}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                                <FormItem {...formItemLayout} label="房间类型">
                                    {getFieldDecorator('roomType', {
                                         initialValue: parseInt(detail.roomType),
                                        rules: [
                                            {   
                                                required: true,
                                                message: '房间类型不能为空',
                                            },
                                        ],
                                    })(
                                        <Select placeholder="请选择房间类型" 
                                             style={{ width: '190px' }}>
                                            {roomtypeDicts.map((item, index) => <Option value={item.dictKey}>{item.dictValue}</Option>)}
                                        </Select>                                          
                                    )}
                                </FormItem>
                            </Col>
                            </Row>
                            <Row gutter={32}>
                            <Col span={16}>
                            <FormItem {...formItemLayout} label="会员权限">
                                    {getFieldDecorator('roleType', {
                                         initialValue: parseInt(detail.roleType),
                                        rules: [
                                            {   
                                                required: true,
                                                message: '会员权限不能为空',
                                            },
                                        ],
                                    })(
                                            <Select placeholder="请选择会员权限" style={{ width: '190px'}}>
                                                <Option value={1}>普通会员</Option>
                                                <Option value={2}>钻石会员</Option>
                                                <Option value={3}>普通+钻石会员</Option>
                                            </Select>
                                    )}
                                </FormItem>
                            </Col>              
                        </Row>    
                        <Row gutter={32}>
                            <Col span={16}>
                            <Form.Item
                                name="roomLogo"
                                {...formItemLayout}
                                label="房间log图片"
                                extra="文件大小 500k 以内, 仅支持 JPG/PNG"  >                               
                             <Input type="file" onChange={this.handleChange} />
                            </Form.Item>
                            </Col>
                         </Row>                      
                    </Card>
                </Form>
            </Panel>

        )
    }
}
export default roomedit;
