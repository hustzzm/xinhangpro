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
    USER_LIST,  
    USER_REMOVE,
    USER_RESETPWD,
    USER_SUBMIT,
    USER_UPDATE,
} from '@/actions/user';
import { ROOMTYPE_DICT} from "../../actions/dictionary";

import CommonGrid from "@/pages/gt/Grid/CommonGrid";
import Useradd from "@/pages/hxuser/hxuseradd";
import Useredit from "@/pages/hxuser/hxuseredit";
import styles from '../../utils/utils.less';
import Panel from "../../components/Panel";

import styleTester from "../Wes/index.less";

import moment from "moment";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';


@connect(({ user, dictionary, loading }) => ({
    user,
    dictionary,
    loading: loading.models.user,
}))
@Form.create()
class hxuserindex extends PureComponent {

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
        dispatch(USER_LIST({}));
    }


    /**
     * 更新选中行，如果过滤中没有，则初始化选中行
     */
    resetSelectRow = () => {
        const {
            user: { data },
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
      
        dispatch(USER_LIST(payload)).then(() => this.resetSelectRow());

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
                subparams.realName = values.name
                //保存操作
                dispatch(USER_UPDATE(subparams)).then(result => {

                 
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

    domodify = (record) => {
      
        this.setState({
            editvisible: true,
            selectedRow: record,
        })
      
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
             
                dispatch(USER_REMOVE({ids:record.id})).then(result => {
                    message.success('操作成功！');
                    that.handleSearch({});    
                })
            },
            onCancel() {
                return false;
            },
        });
       
    }

    //密码初始化模块
    dochangepwd = (record) => {
        const {
            dispatch,
            dictionary: {  },
        } = this.props;
        const that = this;

        Modal.confirm({
            title: '确认提示',
            content: '将密码置为初始状态"111111",确定执行操作吗?',
            okText: '确定',
            okType: 'danger',
            cancelText: '取消',
            onOk() {                
             
                dispatch(USER_RESETPWD({userIds:record.id})).then(result => {
                    message.success('操作成功！');
                    that.handleSearch({});    
                })
            },
            onCancel() {
                return false;
            },
        });
       
    }

    
     //加入绑定，解除绑定
     dotestRole = (record,roleid) => {
        // const { dispatch } = this.props
        
        // const subparams = {};
        // const that = this;
        // subparams.id = record.id;
        // subparams.roleId = roleid;    
        //  //保存操作
        //  dispatch(GTROLEINFO_UPDATE(subparams)).then(result => {
            
        //     if (result.success) {
        //         message.success('操作成功！');
        //         that.handleSearch({});      
                
        //     } else {
        //         message.error('操作失败！');
        //         return false;
        //     }
        // });
    }


     // 检测类型
  renderSearchSelect = () => {
    const {
        dictionary: { roomtypeDicts },
      } = this.props;
    
      let children = [];
      for (let i = 0; i < roomtypeDicts.length; i++) {
        children.push(<Option key={roomtypeDicts[i].dictKey}>{roomtypeDicts[i].dictValue}</Option>);
      }
      return (
        <Select        
          style={{ width: '183px' }} allowClear={true}>
          {children}
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
                        <FormItem label="姓名">
                            {getFieldDecorator('name', { rules: [{ max: 30, message: '最多只能输入30个字符！' }] })(<Input />)}
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

            values.realName = values.name
            //保存操作
            if (!err) {
                dispatch(USER_SUBMIT(values)).then(result => {
                    
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
        const code = 'user';
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
            user: { data },
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
                title: '帐号',
                dataIndex: 'account',  
                flex:0.1,
              },
          {
            title: '姓名',
            dataIndex: 'name',  
            flex:0.1,
          },
          {
            title: '角色',
            dataIndex: 'roleId',  
            flex:0.1,
            render: (text, record, index) => {
                let value = '普通用户';
                if(text == '1123598816738675204'){
                    value= '管理员'
                } 
                return value;
            }
          },         
        
          {
            title: '操作',
            dataIndex: 'id',
            align:'center',
            width:320,
            render:(text, record, index) => {
    
                if(record.account == 'sysadmin' || record.account == 'admin'){
                    return <Space>
                        <a onClick={() => this.dochangepwd(record)} >密码初始化</a>                
                    </Space>;
                }else{
                    return <Space>
                        <a onClick={() => this.dochangepwd(record)} >密码初始化</a>
                    <a onClick={() => this.domodify(record)} >编辑</a>
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
                    renderLeftButton={this.renderLeftButton}
                    renderSearchForm={this.renderSearchForm}
                    loading={loading}
                    data={data}
                    columns={columns}                   
                    noCheck
                    isSerial //序号
                    selectRow={this.selectRow}
                    clearChildSelect={this.clearChildSelect}
                // para={filterkeys}
                />
                <Modal
                    title="新增用户信息"
                    width={800}
                    visible={addvisible}
                    confirmLoading={confirmLoading}
                    bodyStyle={{ 'backgroundColor': '#f0f2f5' }}
                    destroyOnClose={true}
                    onOk={this.handleadd}
                    onCancel={() => that.setState({  addvisible: false   })}
                    okText="提交"
                    cancelText="取消"
                    maskClosable={false}
                >
                    <Useradd roomtypeDicts={roomtypeDicts} wrappedComponentRef={(inst) => {
                        this.editForm = inst;
                    }}></Useradd>

                </Modal>

                <Modal
                    title="修改用户信息"
                    width={800}
                    visible={editvisible}
                    confirmLoading={confirmLoading}
                    bodyStyle={{ 'backgroundColor': '#f0f2f5' }}
                    destroyOnClose={true}
                    onOk={this.handleEditSave}
                    onCancel={() => that.setState({  editvisible: false   })}
                    okText="提交"
                    cancelText="取消"
                    maskClosable={false}
                >
                    <Useredit detail={selectedRow} roomtypeDicts={roomtypeDicts} wrappedComponentRef={(inst) => {
                        this.editForm = inst;
                    }}></Useredit>

                </Modal>
            </Panel>
        )
    }
}

export default hxuserindex;
