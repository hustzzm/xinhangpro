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
    GTMEMBERINFO_LIST,  
    GTMEMBERINFO_DETAIL,  
    GTMEMBERINFO_REMOVE,
    GTMEMBERINFO_SUBMIT,
    GTMEMBERINFO_UPDATE,
    GTHOMETEST_LIST,GTROLEINFO_UPDATE
} from '@/actions/gt/gtmember';

import MemberGrid from "@/pages/gt/Grid/MemberGrid";
import GtMemberAdd from "@/pages/gt/GtMemberAdd";
import GtMemberView from "@/pages/gt/GtMemberView";

import styles from '../../utils/utils.less';
import Panel from "../../components/Panel";

import styleTester from "../Wes/index.less";

import moment from "moment";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';


@connect(({ gtmember, dictionary, loading }) => ({
  gtmember,
    dictionary,
    loading: loading.models.gtmember,
}))
@Form.create()
class GtMember extends PureComponent {

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
      
        this.getList();
    }

    getList(){
        const { dispatch } = this.props;       
        dispatch(GTMEMBERINFO_LIST({}));
    }

   

    /**
     * 弹出详细页面
     */
    openDetailModal = async (record) => {
        const { dispatch } = this.props
        const res = dispatch(GTMEMBERINFO_DETAIL(record.id)).then(result => {
            if (result.success) {
                this.setState({
                    sampleDetail: result.data,
                    detailvisible: true
                });
            } else {
                message.error(response.msg);
            }
        })
    }


    /**
     * 更新选中行，如果过滤中没有，则初始化选中行
     */
    resetSelectRow = () => {
        const {
            gtmember: { data },
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
      
        dispatch(GTMEMBERINFO_LIST(payload)).then(() => this.resetSelectRow());

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
    /**
     * 编辑执行保存操作
     */
    handleEditSave = e => {
        const { selectedRow } = this.state;
        const {
            dispatch,
            dictionary: {  },
        } = this.props;
        const that = this;
        //验证表单数据
        that.editForm.props.form.validateFields((err, values) => {

            if (!err) {
                //values 可获取到表单中所有键值数据  将数据进行保存操作
                const subparams = values;
                subparams.id = selectedRow.id;
                subparams.account = selectedRow.account;
                // subparams.token = selectedRow.token;
              
                //保存操作
                dispatch(GTMEMBERINFO_UPDATE(subparams)).then(result => {

                    if (result.success) {
                        message.success('操作成功！');
                        setTimeout(() => {
                            that.setState({
                                editvisible: false,
                            }, () => {
                                that.handleSearch(that.state.params);
                                // 清理表单数据
                                // this.editForm.props.form.resetFields();
                                that.setState({ selectedRow: [] })
                            });
                            // 清空选中状态
                            this.childTable.clearSelect()
                        }, 1000);
                    } else {
                        message.error(result.msg);
                        return false;
                    }
                });
            }
        });
    }

  
    addmodal = () => {

        this.setState({
            addvisible: true
        })
    }
    //编辑

    domodify = () => {
        const len = this.state.selectedRowKeys.length;
        const { selectedRows } = this.state
        
        if (len != 1) {
            message.warning("请选中一条记录进行操作");          
        }else {
            this.setState({
                editvisible: true,
                selectedRow: selectedRows[0],
            })
        }
        // this.getlistbyparamsAll();
      
    }
   
    addcancle = () => {
        this.setState({
            addvisible: false
        }, () => {
            this.handleSearch(this.state.params);
        })
    }

    onClickReset = () => {
        const { onReset, params } = this.state;
        // onReset();
        this.handleSearch(params);
    };


    //删除模块
    doremove = () => {
        const { dispatch } = this.props
        const { selectedRowKeys } = this.state
        const len = this.state.selectedRowKeys.length;
        const that = this
        if (len == 0) {
            message.warn("请选择一条记录进行操作");
            // that.handleSearch(that.state.params);
       
        }else{
            
            Modal.confirm({
                title: '确认提示',
                content: '确认执行删除操作吗?',
                okText: '确定',
                okType: 'danger',
                cancelText: '取消',
                onOk() {
                    
                    dispatch(GTMEMBERINFO_REMOVE(selectedRowKeys)).then(result => {
                      
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

       
    }

    
     //加入绑定，解除绑定
     dotestRole = (record,roleid) => {
        const { dispatch } = this.props
        
        const subparams = {};
        const that = this;
        subparams.id = record.id;
        subparams.roleId = roleid;    
         //保存操作
         dispatch(GTROLEINFO_UPDATE(subparams)).then(result => {
            
            if (result.success) {
                message.success('操作成功！');
                that.handleSearch({});      
                
            } else {
                message.error('操作失败！');
                return false;
            }
        });
    }

    /**
     * 进入主列表，Home or Live
     * @param {*} record 
     * @param {*} pagetype 
     */
    dotwdHome = (record,pagetype) => {
        const { dispatch } = this.props
      
        router.push({ 
            pathname:`/tester/homelist`,          
            query:{
                account:record.account,
                tokenval:record.token,
                pagetype:pagetype
            },             
            state:{back :'/tester/list'}
    });
    }


    
    renderLeftButton = () => (
        <div>
           
            <Button type="primary" onClick={this.addmodal} icon={<PlusOutlined />}>
                添加
            </Button>
            <Button type="primary" onClick={this.domodify} icon={<EditOutlined />}>
                修改
            </Button>
            <Button type="primary" onClick={this.doremove} icon={<DeleteOutlined />}>
                删除
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
                        <FormItem label="帐号">
                            {getFieldDecorator('account', { rules: [{ max: 30, message: '最多只能输入30个字符！' }] })(<Input />)}
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

  
    //新增成员
    handleadd = (value) => {
      
        const { dispatch } = this.props;
      
        const that = this;
       
        //验证表单数据
        that.editForm.props.form.validateFields((err, values) => {

            //保存操作
            if (!err) {
                dispatch(GTMEMBERINFO_SUBMIT(values)).then(result => {
                    if (result.success) {
                        message.success('操作成功！');
                        setTimeout(() => {
                            that.setState({
                                addvisible: false,
                            }, () => {
                                that.handleSearch(that.state.params);
                                // 清理表单数据
                                // this.editForm.props.form.resetFields();
                            });
                        }, 1000);
                    } else {
                        // message.error(result.msg);
                        return false;
                    }
                });
            }

        }
        );
    }
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
        const code = 'gtmember';
        const {
            editvisible,         
            confirmLoading,
            sampleDetail,
            selectedRow,         
            addvisible,         
        } = this.state;
        const that = this;
        const {
            form,
            loading,
            dictionary: {  },
            gtmember: { data },
        } = this.props;

       

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
            title: '用户名',
            dataIndex: 'account',  
            flex:0.1,
          },
          {
            title: '编号',
            dataIndex: 'code',  
            flex:0.1,
          },
          {
            title: '昵称',
            dataIndex: 'name',  
            flex:0.1,
          },
          {
            title: 'Email',
            dataIndex: 'email',  
            flex:0.2,
          },
          {
            title: 'token值',
            dataIndex: 'token',
            width:400,
            render(text,record,index){
                
                if(text.length > 50){
                    return <div title={text}>{text.substring(0,50) + '...'}</div>
                  }else{
                    return text;
                  }
            }
            // width:500
          },  
          {
            title: '调度状态',
            dataIndex: 'roleId',  
            flex:0.2,
            render(text,record,index){
             
                return record.roleId == "1" ? '已绑定':'未绑定';
            }
          }, 
          {
            title: '操作',
            dataIndex: 'id',
            align:'center',
            width:320,
            render:(text, record, index) => {
    
          
                let roleval = record.roleId == "1" ? '解除调度':'绑定调度';
                let roleid = record.roleId == "1" ? '0':'1';
                return <Space>
                <a onClick={() => this.dotestRole(record,roleid)} >{roleval}</a>
              
            </Space>;
            }
          },      
        ];
    

        return (
            <Panel>
                <MemberGrid
                    // scroll={{ y: 504 }}
                    code={code}
                    form={form}
                    onSearch={this.handleSearch}
                    renderLeftButton={this.renderLeftButton}
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
                
                <Modal
                    title="帐号信息编辑"
                    width={1200}
                    visible={editvisible}
                    confirmLoading={confirmLoading}
                    bodyStyle={{ 'backgroundColor': '#f0f2f5' }}
                    destroyOnClose={true}
                    onOk={this.handleEditSave}
                    onCancel={() => this.handleEditClose(false)}
                    okText="提交"
                    cancelText="取消"
                    maskClosable={false}
                >
                    <GtMemberView
                        wrappedComponentRef={(inst) => {
                            this.editForm = inst;
                        }}
                        selectedRow={selectedRow}
                        isEdit='0'
                      ></GtMemberView>
                </Modal>
                {/* <Modal
                    title="帐号信息详细"
                    width={1200}
                    visible={detailvisible}
                    confirmLoading={confirmLoading}
                    bodyStyle={{ 'backgroundColor': '#f0f2f5' }}
                    destroyOnClose={true}
                    footer={null}
                    onCancel={() => this.handleDetailClose(false)}
                    maskClosable={false}
                >
                    <TesterDetail data={sampleDetail} wessampleType={wessampleType} wessampleClass={wessampleClass} wessampleStatus={wessampleStatus} />
                </Modal> */}
        
                <Modal
                    title="新增帐号"
                    width={1200}
                    visible={addvisible}
                    confirmLoading={confirmLoading}
                    bodyStyle={{ 'backgroundColor': '#f0f2f5' }}
                    destroyOnClose={true}
                    onOk={this.handleadd}
                    onCancel={() => this.addcancle()}
                    okText="提交"
                    cancelText="取消"
                    maskClosable={false}
                >
                    <GtMemberAdd wrappedComponentRef={(inst) => {
                        this.editForm = inst;
                    }}></GtMemberAdd>

                </Modal>

            </Panel>
        )
    }
}

export default GtMember;
