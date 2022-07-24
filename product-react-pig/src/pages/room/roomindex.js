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
    ROOMINFO_LIST,  
    ROOMINFO_REMOVE,
    ROOMINFO_SUBMIT,
    ROOMINFO_UPDATE,
} from '@/actions/gt/room';
import { ROOMTYPE_DICT} from "../../actions/dictionary";

import CommonGrid from "@/pages/gt/Grid/MemberGrid";
import Roomadd from "@/pages/room/roomadd";
import Roomedit from "@/pages/room/roomedit";
import styles from '../../utils/utils.less';
import Panel from "../../components/Panel";

import styleTester from "../Wes/index.less";
import { getCurrentUser } from '../../utils/authority';

import moment from "moment";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';


@connect(({ room, dictionary, loading }) => ({
    room,
    dictionary,
    loading: loading.models.room,
}))
@Form.create()
class roomindex extends PureComponent {

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
        dispatch(ROOMINFO_LIST({}));
    }


    /**
     * 更新选中行，如果过滤中没有，则初始化选中行
     */
    resetSelectRow = () => {
        const {
            room: { data },
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
      
        dispatch(ROOMINFO_LIST(payload)).then(() => this.resetSelectRow());

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
                let subparams = values;
                subparams.id = selectedRow.id;                 
                
                let filename = that.editForm.state.filename;
             
                if(filename && filename.length > 0){
                    subparams.roomLogo = filename;
                }
            
                // subparams.token = selectedRow.token;
              
                //保存操作
                dispatch(ROOMINFO_UPDATE(subparams)).then(result => {

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
                
                dispatch(ROOMINFO_REMOVE(record.id)).then(result => {
                  
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
                        <FormItem label="房间名称">
                            {getFieldDecorator('name', { rules: [{ max: 30, message: '最多只能输入30个字符！' }] })(<Input />)}
                        </FormItem>
                        <FormItem label="房间类型">
                        {getFieldDecorator('roomType')(this.renderSearchSelect())}
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
                let filename = that.editForm.state.filename;

                if(filename && filename.length > 0){
                    values.roomLogo = filename;
                }
                dispatch(ROOMINFO_SUBMIT(values)).then(result => {
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
        const code = 'room';
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
            room: { data },
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
                title: '房间logo',
                dataIndex: 'roomLogo',  
               width:140,
                render: (text, record, index) => {
                 
                    return text && text.length > 0 ? <img src={currentUser.imgPath + text} width={80}/> : ''
                }
              },
          {
            title: '房间名称',
            dataIndex: 'name',  
            flex:0.1,
          },
          {
            title: '房间类型',
            dataIndex: 'roomType',  
            flex:0.1,
            render: (text, record, index) => {
                const item = roomtypeDicts.find(item => item.dictKey == record.roomType);
                    return item ? item.dictValue : ""
            }
          },
          {
            title: '会员类型',
            dataIndex: 'roleType',  
            flex:0.2,
            render: (text, record, index) => {
                let value = '普通会员';
                if(text == '2'){
                    value= '钻石会员'
                } else if(text == '3'){
                    value= '普通+钻石会员'
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
    
    
                return <Space>
                <a onClick={() => this.domodify(record)} >编辑</a>
                <a onClick={() => this.doremove(record)} >删除</a>
            </Space>;
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
                   
                    // noCheck
                    isSerial //序号
                    selectRow={this.selectRow}
                    clearChildSelect={this.clearChildSelect}
                // para={filterkeys}
                />
                <Modal
                    title="新增房间信息"
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
                    <Roomadd roomtypeDicts={roomtypeDicts} wrappedComponentRef={(inst) => {
                        this.editForm = inst;
                    }}></Roomadd>

                </Modal>

                <Modal
                    title="修改房间信息"
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
                    <Roomedit detail={selectedRow} roomtypeDicts={roomtypeDicts} wrappedComponentRef={(inst) => {
                        this.editForm = inst;
                    }}></Roomedit>

                </Modal>
            </Panel>
        )
    }
}

export default roomindex;
