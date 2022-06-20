import { connect } from "dva";
import '@ant-design/compatible/assets/index.css';
import { Form,Button, Card, Col, DatePicker, Input, Row, Select, message, Divider } from "antd";
import React, { PureComponent } from "react";
import { requestApi, requestGetApi, getRestful } from "@/services/api";
import Panel from '../../components/Panel';
import styles from '../../layouts/UserLayout.less';
import {
    GTHTMLINFO_DETAIL,     
} from '@/actions/gt/gtmember';

import {
    PlusOutlined,
    DownloadOutlined,
    SyncOutlined,
    UploadOutlined,
    DeleteOutlined,
    InboxOutlined,
    DownCircleTwoTone,
    CheckOutlined,
    FilterOutlined,
    RedoOutlined,
    FileAddOutlined,
    EditOutlined,
    PlusSquareOutlined
} from "@ant-design/icons";
const FormItem = Form.Item;
const { TextArea } = Input;
const { Option } = Select;

@connect(({ gtmember, dictionary, loading }) => ({
  gtmember,
    dictionary,
    submitting: loading.effects['gttest/gthtml'],
}))
class GtWorking extends PureComponent {
    state = {
        resShowText:'234'
    };

    formRef = React.createRef();
    onFinish =  (values) =>{
    //     const {dispatch} = this.props;
    //     const {resShowText} = this.state;
    //     let otherval = this.formRef.current.getFieldValue('testurl');

    //     const params = {
    //         url: otherval,         
    //       };         
        
    //    dispatch(GTHTMLINFO_DETAIL(params)).then(resp=>{
    //        if(resp.success){
    //         this.setState({resShowText:resp.data});
    //        }else{
    //         this.setState({resShowText:'error'});
    //        }
    //    });     
      
    
    };

    render() {

        const {
            // form: { getFieldDecorator },
            
        } = this.props;
        const {resShowText} = this.state;
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

       

        const dateFormat = 'YYYY-MM-DD';
        const locale = {
            "lang": {
                "locale": "zh-CN"
            }
        }        
      
        return (
            <Panel>
                 <Card style={{ marginTop: '16px', padding: '8px', }} bordered={false}>
                   
                           <div style={{width:'800px',height:'400px'}} className={styles.workingstyle}></div>                 
                    </Card>
            </Panel>

        )
    }
}
export default GtWorking;
