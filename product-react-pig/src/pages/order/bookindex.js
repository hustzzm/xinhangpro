import React, { PureComponent } from 'react';
import { connect } from 'dva';
// import { VerticalAlignBottomOutlined, VerticalAlignTopOutlined } from '@ant-design/icons';
import { Form } from '@ant-design/compatible';
import router from "umi/router";
import '@ant-design/compatible/assets/index.css';
import {
    Button,
    Col,
    Input,
    Row,
    DatePicker,
    Modal,
    Select,
    Checkbox,
    Table,
    Tag,
    Upload,
    Space,
    Progress,
    message, Tooltip
} from 'antd';
import {
    PlusOutlined,
    SyncOutlined,
    DeleteOutlined,
    FilterOutlined,
    RedoOutlined,
    FileAddOutlined,
    EditOutlined,
    RollbackOutlined
} from "@ant-design/icons";
import {
    BOOKINFO_LIST,  
    BOOKINFO_REMOVE,
    BOOKINFO_UPDATE,
} from '@/actions/gt/book';
import { ROOMTYPE_DICT} from "../../actions/dictionary";

import CommonGrid from "@/pages/gt/Grid/MemberGrid";

import styles from '../../utils/utils.less';
import Panel from "../../components/Panel";

import styleTester from "../Wes/index.less";
import { getCurrentUser } from '../../utils/authority';

import moment from "moment";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';


@connect(({ book, dictionary, loading }) => ({
    book,
    dictionary,
    loading: loading.models.book,
}))
@Form.create()
class bookindex extends PureComponent {

    state = {        
        editvisible: false, //修改弹出窗口是否显示
        detailvisible: false,//是否显示详细页面      
        confirmLoading: false,
        addvisible: false,//新增模块，显示隐藏
        selectedRow: { account: '', id: -1 },//选中行
        selectedRowKeys: [],  //选中行主键
        params: {},             //查询条件参数       
        sampleDetail: {},     
        selectedRows: [],
        // selectedRowKeys: [],  //选中行主键
        onReset: () => {
        },        
       
    };

    componentWillMount() {
      
        const { dispatch } = this.props;
        // 查询directory数据集，包括样本状态、样品进度、检测类型
        let params = { code: 'roomtype' }
        dispatch(ROOMTYPE_DICT(params));
        this.getList();
    }

    getList(){
        const { dispatch } = this.props;       
        dispatch(BOOKINFO_LIST({}));
    }


    /**
     * 更新选中行，如果过滤中没有，则初始化选中行
     */
    resetSelectRow = () => {
        const {
            book: { data },
        } = this.props;
        const { selectedRow } = this.state;

        if (selectedRow.account != '' && data.list.length > 0) {
            const select_tmp_row = data.list.filter((item => item.account == selectedRow.account))[0];

            if (select_tmp_row) {
                this.setState({ selectedRow: select_tmp_row, selectedRowKeys: [select_tmp_row.id] })
            } else {
                this.setState({ selectedRow: { account: '', id: -1 }, selectedRowKeys: [] });//初始化选中
            }
        } else {
            this.setState({ selectedRow: { account: '', id: -1 }, selectedRowKeys: [] });//初始化选中
        }
    }

    // ============ 查询 ===============
    handleSearch = params => {
        // 清空选中状态
        this.childTable.clearSelect()
        const { dispatch } = this.props;
        
        this.setState({ params: params });
        const payload = {
            ...params
            // getblood_date_start: this.state.beginDateTime,
            // getblood_date_end: this.state.endDateTime,
          
        };
        // delete payload.getbloodDate
        // delete payload.startDate
      
        dispatch(BOOKINFO_LIST(payload)).then(() => this.resetSelectRow());

    };

   
    /**
     * 关闭-编辑样品信息页
     */
    handleEditClose = () => {
        // 清理表单数据
        // this.editForm.props.form.resetFields();
        this.setState({
            editvisible: false,
        });
    }
    /**
     * 关闭-样品详细页
     */
    handleDetailClose = () => {

        this.setState({
            detailvisible: false,
        });
    }



    onClickReset = () => {
        const { onReset, params } = this.state;
        // onReset();
        this.handleSearch(params);
    };


    //删除模块
    doremove = (record) => {
        const {
            dispatch,
            dictionary: {  },
        } = this.props;
        const that = this;

        Modal.confirm({
            title: '确认提示',
            content: '确认执行删除操作吗?',
            okText: '确定',
            okType: 'danger',
            cancelText: '取消',
            onOk() {
                
                dispatch(BOOKINFO_REMOVE(record.id)).then(result => {
                  
                    if (result.success) {
                        message.success('操作成功！');
                        that.handleSearch({});                           
                    }
                })
            },
            onCancel() {
                return false;
            },
        });
       
    }

    //删除模块
    dofinish = (record) => {
        const {
            dispatch,
            dictionary: {  },
        } = this.props;
        const that = this;

        Modal.confirm({
            title: '确认提示',
            content: '确认执行删除操作吗?',
            okText: '确定',
            okType: 'danger',
            cancelText: '取消',
            onOk() {
                
                dispatch(BOOKINFO_UPDATE(record.id)).then(result => {
                  
                    if (result.success) {
                        message.success('操作成功！');
                        that.handleSearch({});                           
                    }
                })
            },
            onCancel() {
                return false;
            },
        });
       
    }




     // 检测类型
  renderSearchSelect = () => {
    return (
        <Select style={{ width: '183px' }} allowClear={true}>
          <Option key="1">普通会员</Option>
          <Option key="2">钻石会员</Option>
        </Select>
      )
  }
    renderLeftButton = () => (
        <div>
           
            <Button type="primary" onClick={this.addmodal} icon={<PlusOutlined />}>
                添加
            </Button>               
        </div>
    );

    // ============ 查询表单 ===============
    renderSearchForm = onReset => {
        const { form } = this.props;
        const { getFieldDecorator } = form;

        return (
            <div id="query-table" className={styles.query_item_css}>
                <Row gutter={16}>
                    <Col span={18}>
                        <FormItem label="预约号">
                            {getFieldDecorator('booksNo', { rules: [{ max: 30, message: '最多只能输入30个字符！' }] })(<Input />)}
                        </FormItem>
                        <FormItem label="姓名">
                            {getFieldDecorator('name', { rules: [{ max: 30, message: '最多只能输入30个字符！' }] })(<Input />)}
                        </FormItem>
                        <FormItem label="会员等级">
                        {getFieldDecorator('userLevel')(this.renderSearchSelect())}
                        </FormItem>
                    </Col>

                    <Col span={6}>
                        <div className={styleTester.btnPosition}>
                            <Button icon={<FilterOutlined />} type="primary" htmlType="submit">
                                查询
                            </Button>
                            <Button icon={<SyncOutlined />} onClick={onReset}>
                                重置
                            </Button>                          
                        </div>
                    </Col>
                </Row>
            </div>
        );
    };

  
   
    selectRow = (selectedRowKeys, selectedRows) => {
        this.setState({
            selectedRows: selectedRows,
            selectedRowKeys: selectedRowKeys
        })
    }

    // 清空table选中状态
    clearChildSelect = (ref) => {
        this.childTable = ref
    }

    

    render() {
        const code = 'book';
        const {
            editvisible,         
            confirmLoading,        
            selectedRow,         
            addvisible,         
        } = this.state;
     
        const that = this;
        const {
            form,
            loading,
            dictionary: { roomtypeDicts },
            book: { data },
        } = this.props;

        const currentUser = getCurrentUser();
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 12 },
                md: { span: 16 },
            },
        };

        const columns = [
       
          {
            title: '预约号',
            dataIndex: 'booksNo',  
            flex:0.1,          
            },
          {
            title: '姓名',
            dataIndex: 'name',  
            flex:0.1,
          },
          {
            title: '房间名称',
            dataIndex: 'roomName',  
            flex:0.1,
          },
          {
            title: '预订时间',
            dataIndex: 'bookTimesText',            
            flex:0.2,
            // render: (text, record, index) => {
                     
            //     return record.bookDate + ' ' + text;
            //  }
          },
          {
            title: '会员等级',
            dataIndex: 'userLevel',  
            flex:0.1,
            render: (text, record, index) => {
               let val = '普通会员';
               if(text == '2'){
                   val = '钻石会员'
               }             
               return val;
            }
          },
        
          {
            title: '预订状态',
            dataIndex: 'bookStatus',  
            flex:0.1,
            render: (text, record, index) => {
               let val = '预约成功';
               if(text == '3'){
                   val = '消费完成'
               }else if(text == '5'){
                val = '取消预约'
                }                
               return val;
            }
          },
          {
            title: '操作',
            dataIndex: 'id',
            align:'center',
            width:320,
            render:(text, record, index) => {
    
                if(record.bookStatus == '1'){

                    return <Space>  
                    <a onClick={() => this.dofinish(record)} >完成</a>            
                    <a onClick={() => this.doremove(record)} >删除</a>
                </Space>;
                }else{
                    return <Space>                             
                    <a onClick={() => this.doremove(record)} >删除</a>
                </Space>;
                }
               
            }
          },      
        ];
    

        return (
            <Panel>
                <CommonGrid
                    // scroll={{ y: 504 }}
                    code={code}
                    form={form}
                    onSearch={this.handleSearch}
                    // renderLeftButton={this.renderLeftButton}
                    renderSearchForm={this.renderSearchForm}
                    loading={loading}
                    data={data}
                    columns={columns}
                   
                    // noCheck
                    isSerial //序号
                    selectRow={this.selectRow}
                    clearChildSelect={this.clearChildSelect}
                // para={filterkeys}
                />
        
            </Panel>
        )
    }
}

export default bookindex;
