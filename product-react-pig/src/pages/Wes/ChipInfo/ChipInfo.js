import React, {PureComponent} from 'react';
import { connect } from 'dva';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Row,Col, Input, DatePicker ,Upload,Modal,message,Checkbox,Space} from 'antd';
import { CHIPINFO_LIST,CHIPINFO_REMOVE,CHIPINFO_COMMIT,CHIPINFO_REANALYSIS } from '../../../actions/chipinfo';
import ChipGrid from "@/pages/Wes/Analysis/Grid/ChipGrid";
import func from "../../../utils/Func";
import styles from '../../../utils/utils.less';
import Panel from "../../../components/Panel";
import {CHIP_DICT,CHIPCELL_DICT} from "../../../actions/dictionary";
import router from "umi/router";
import {PlusOutlined,DownloadOutlined,SyncOutlined,UploadOutlined,DeleteOutlined,InboxOutlined,DownCircleTwoTone,CheckOutlined,FilterOutlined,RedoOutlined} from "@ant-design/icons";
import {getToken } from '../../../utils/authority';
import CaseInfo from '../CaseInfo/CaseInfo';
import ChipInfoAdd from '../ChipInfo/ChipInfoAdd'
// import $ from 'jquery';

const { Dragger } = Upload;
// const { RangePicker } = DatePicker;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';

@connect(({ chipinfo,dictionary, loading }) => ({
  chipinfo,
  dictionary,
  loading: loading.models.chipinfo,
}))
@Form.create()
class ChipInfo extends PureComponent {

  state = {
    current: 1,
    size: 20,
    visible: false,
    excelVisible: false,  //是否显示样品导入
    addpageVisiable:false,//是否显示文库添加页
    confirmLoading: false,
    selectedRow:{},         //选中混合文库的记录
    selectedRowKeys : [],
    onReset: () => {},
    params:{}
  };

  componentWillMount() {
    const { dispatch } = this.props;
    /**
     * 查询样本状态的字段
     */
    let params = {code:'chipStatus'}
    dispatch(CHIP_DICT(params));
    params = {code:'dpchipCell'}
    dispatch(CHIPCELL_DICT(params));

    dispatch(CHIPINFO_LIST({}));

    this.clearSelectRow();
  }

  // ============ 查询 ===============
  handleSearch = params => {
    
    const { dispatch } = this.props;
    const { chip_id,starttime} = params;
    const that = this;
    const payload = {
      ...params,
      starttime:starttime ? func.format(starttime, 'YYYY-MM-DD') : null,
      // starttime_ge: starttime ? func.format(starttime[0], 'YYYY-MM-DD') +' 00:00:00' : null,
      // starttime_le: starttime ? func.format(starttime[1], 'YYYY-MM-DD') +' 23:59:59' : null,
    };

    that.setState({params:params})
    // delete payload.starttime
    dispatch(CHIPINFO_LIST(payload)).then(result =>{
      that.resetSelectRow();
    });

  };

  handleExcelImport = () =>{
    this.setState({
      excelVisible: false,
    });
  }

  handleExcelCancel = () =>{
    this.setState({
      excelVisible: false,
    });
  }

  /**初始化混合文库选中项 */
  clearSelectRow = () =>{
    const that = this;
    that.setState({selectedRow:{chip_id:'',id:'',casenum:0,ready_state:1}},()=>{

      var params_child = that.$child.state.params
      that.$child.handleSearch(params_child);
    });//初始化选中

  }

  /**
   * 更新选中行，如果过滤中没有，则初始化选中行
   */
  resetSelectRow = () =>{
    const {
      chipinfo: { data },
    } = this.props;
    const {  selectedRow } = this.state;
    const that = this;

    if(selectedRow.chip_id != '' && data.list.length > 0){
      const select_tmp_row = data.list.filter((item => item.chip_id == selectedRow.chip_id))[0];
      if(select_tmp_row){
        this.setState({
          selectedRow:{
            chip_id:      select_tmp_row.chip_id,
            id:           select_tmp_row.id,
            casenum:      select_tmp_row.casenum,
            ready_state:  select_tmp_row.ready_state,
            chip_status:  select_tmp_row.chip_status
          }
        }),()=>{
          var params_child = that.$child.state.params
          that.$child.handleSearch(params_child);
        }
      }else{
        this.clearSelectRow();
      }
    }else{
      this.clearSelectRow();
    }
  }


  handleChipCaseInfoList = (id) => {
    router.push({ pathname:`/manage/caseInfo/chipCaseInfo/${id}`,state:{back :'/manage/chipInfo/list'}});
  }


   // 此事件接收样本信息子对象
   childEvevnt = childObject => {
    this.$child = childObject;
  };

  /**
   * 弹出新增文库页
   */
  handleAddOpen = () => {
    this.setState({
      addpageVisiable: true,
    });
  };

  /**
   * 新增文库报保存操作
   */
  handleaddSave = e =>{
    const that = this;
    //验证表单数据
    that.editForm.props.form.validateFields((err, values) => {
          if (!err) {
              //values 可获取到表单中所有键值数据  将数据进行保存操作
              that.editForm.doSubmit(values)

              setTimeout(() => {
                that.setState({
                  addpageVisiable: false,
                },()=>{
                    that.handleSearch(this.state.params);
                    // 清理表单数据
                    that.editForm.props.form.resetFields();
                });
              }, 1000);

          }
      });
    // this.$child.dosubmit();

  }

  /**
   * 新增文库Model关闭消操作
   */
  handleAddClose = () =>{

    this.setState({
      addpageVisiable: false,
    });
  }

   /**
   * 删除文库操作
   */
  handleRemove = () => {
    const { dispatch } = this.props;
    const { selectedRow,params } = this.state;
    const that = this;

    if(selectedRow.chip_id == ''){
      message.warn("请选择混合文库进行操作！")
    }
    else if(selectedRow.ready_state >= 3){
      message.warn("当前混合文库已提交分析，不允许进行操作！")
    }else {

      const payload = {
        ids:selectedRow.id
      }

      Modal.confirm({
        title: '确认提示',
        content: '删除混合文库会删除该文库相关的样本信息，确认执行操作吗?',
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk(){

          dispatch(CHIPINFO_REMOVE(payload)).then(result =>{
            if(result.success){
             message.success('操作成功！');
             that.clearSelectRow()
             that.handleSearch(that.state.params);
            }
           });
        },
        onCancel() {
          return false;
        },
      });
    }

  };

  /**
   * 新增样品导入操作
   */
  handleImport = () => {
   const {selectedRow} = this.state;
   const that = this;

    if(selectedRow.chip_id ==''){

      message.warn("请选择混合文库进行操作！")

    }else if(selectedRow.ready_state >= 3){
      message.warn("当前混合文库已提交分析，不允许进行操作！")
    }
    else if(selectedRow.casenum && parseInt(selectedRow.casenum) > 0){
      Modal.confirm({
        title: '确认提示',
        content: '新导入的样本信息将覆盖现有的记录，确认执行导入操作吗?',
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk(){
          that.setState({
            excelVisible: true,
          });
        },
        onCancel() {
          return false;
        },
      });
    }else{
      that.setState({
        excelVisible: true,
      });
    }

  };

  handleTemplate = () => {
    window.open(`/api/dpimport/template`);
  };

    /**
     * 重新分析操作
    */
  handleReanlysis = () =>{

    const {selectedRow} = this.state;
    const { dispatch } = this.props;
    const that = this;

     if(selectedRow.chip_id ==''){

       message.warn("请选择混合文库进行操作！")
       return false;

     }
     else if(selectedRow.chip_status != 4 && selectedRow.chip_status != 5){
      message.warn("当前混合文库分析状态不允许进行重新分析操作！【只有分析完成或分析失败的混合文库，才允许重新分析】")
      return false;
     } else{
      Modal.confirm({
        title: '确认提示',
        content: '重新分析后，当前混合文库的分析结果将被清除，确认执行重新分析操作吗?',
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk(){

         const payload = {
           chip_id:selectedRow.chip_id
         }

         dispatch(CHIPINFO_REANALYSIS(payload))
         .then(result =>{
           if(result.success){
             message.success('操作成功！');
             that.handleSearch(that.state.params);
               //选中芯片，调用子页面查询方法
               var params_child = that.$child.state.params
               that.$child.handleSearch(params_child);
           }
         });
        },
        onCancel() {
          return false;
        },
      });
     }
  }


  /**
   * 提交分析操作
   */
  handleCommit = () => {
    const {selectedRow} = this.state;
    const { dispatch } = this.props;
    const that = this;

     if(selectedRow.chip_id ==''){

       message.warn("请选择混合文库进行操作！")
     }
     else if(selectedRow.casenum == 0){

      message.warn("请导入样本信息后再进行操作！")
     }else if(selectedRow.ready_state >= 3){

      message.warn("当前混合文库已提交分析，无需重复操作！")
     }
     else{

       Modal.confirm({
         title: '确认提示',
         content: '提交分析后，当前混合文库与样本信息将不可进行操作，确认执行提交分析操作吗?',
         okText: '确定',
         okType: 'danger',
         cancelText: '取消',
         onOk(){

          const payload = {
            chip_id:selectedRow.chip_id
          }

          dispatch(CHIPINFO_COMMIT(payload))
          .then(result =>{
            if(result.success){
              message.success('操作成功！');
              that.handleSearch(that.state.params);
            }
          });
         },
         onCancel() {
           return false;
         },
       });
     }
   };



  renderLeftButton = () => (
    <div>
      <Button icon={<PlusOutlined />} onClick={this.handleAddOpen} type="primary">
        新增混合文库
      </Button>
      <Button icon={<UploadOutlined />} onClick={this.handleImport}>
        导入样本
      </Button>
      <Button icon={<CheckOutlined />} onClick={this.handleCommit}>
        提交分析
      </Button>
      <Button icon={<DeleteOutlined />} onClick={this.handleRemove}>
        删除
      </Button>
      <Button icon={<RedoOutlined />} type="danger" onClick={this.handleReanlysis}>
        重新分析
      </Button>
      {/* <Button icon={<DownloadOutlined />} onClick={this.handleRemove} style={{float:'right','marginRight':'10px'}}>
      下载样品模板
      </Button> */}
    </div>
  );

  onClickReset = () => {
    const { onReset,params } = this.state;
    // onReset();
    this.handleSearch(params);
    //选中芯片，调用子页面查询方法
    var params_child = this.$child.state.params
    this.$child.handleSearch(params_child);
  };

  onUpload = info => {
    const { status } = info.file;
    if (status === 'done') {
      message.success(`${info.file.name} 数据导入成功！`);
      this.handleExcelCancel()
      this.onClickReset()
    } else if (status === 'error') {
      message.error(`${info.file.response.msg}`);
    }
  };

  // ============ 查询表单 ===============
  renderSearchForm = onReset => {
    const { form } = this.props;
    const { getFieldDecorator } = form;
    return (
      <div className={styles.query_item_css}>
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={15} sm={24}>
            <FormItem>
              {getFieldDecorator('chip_id',{rules: [{ max: 30, message: '最多只能输入30个字符！' }]})(<Input placeholder="混合文库名称" />)}
            </FormItem>
            <FormItem>
              {getFieldDecorator('starttime')(<DatePicker format={dateFormat} placeholder="上机时间"
              />)}
            </FormItem>
          </Col>

          <Col md={9} sm={24}>
            <div style={{ float: 'right','paddingTop':'5px' }}>
              <Button icon={<FilterOutlined />} type="primary" htmlType="submit" style={{marginRight:'8px'}}>
                查询
              </Button>
              <Button icon={<SyncOutlined />} style={{ marginLeft: 8 }} onClick={onReset}>
                重置
              </Button>
            </div>
          </Col>
        </Row>
      </div>
    );
  };

  render() {
    const code = 'chipinfo';
    const {
      form,
      loading,
      chipinfo: { data },
      dictionary:{chipStatus,dpchipCell}
    } = this.props;

    const { excelVisible, addpageVisiable,confirmLoading ,selectedRow,bsampleEdit} = this.state;

    const uploadProps = {
      name: 'file',
      headers: {
        'Berry-Auth': getToken(),
      },
      action: "/api/dpimport/importSampleInfo?chip_id=" + selectedRow.chip_id ,
      accept: '.xls,.xlsx'
    };

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

    const that = this

    const columns = [
      // {
      //   title: '选择',
      //   dataIndex: 'chip_id',
      //   width:50,
      //   align:'center',
      //   render(text, record, index) {
      //     var cb_id = 'cb_' + record.id;
      //     var ischeck = that.state.selectedRow.id != record.id ? false : true;
      //     return <Checkbox id={cb_id} onClick={() => doitemclick(record)} checked = {ischeck}></Checkbox>;
      //   }
      // },
      {
        title: '混合文库名称',
        dataIndex: 'chip_id',
        render(text, record, index) {          
          if(text.length > 12){
            return <div title={text}>{text.substring(0,12) + '...'}</div>
          }else{
            return text;
          }
        }
      },
      {
        title: '上机时间',
        dataIndex: 'starttime',
      },
      {
        title: '测序仪编号',
        dataIndex: 'sequencer_number',
      },
      {
        title: 'cell编号',
        dataIndex: 'cell_number',
        render(text,record,index){

          let filter = dpchipCell.filter(item => item.dictKey==text);
          if(filter.length){
            return filter[0].dictValue
          }else{
            return ''
          }
        }

      },
      {
        title: '样本数量',
        dataIndex: 'casenum',
        align:'center',
      },
       {
        title: '分析状态',
        flex:1,
        dataIndex: 'chip_status',
        render(text,record,index){
          let filter = chipStatus.filter(item => item.dictKey===text);
          if(filter.length){
            return filter[0].dictValue
          }else{
            return ''
          }
        }
      },
      // {
      //   title: '提交状态',
      //   dataIndex: 'ready_state',
      //   render(text,record,index){
      //     return !text || text < 3 ? '未提交':'已提交'
      //   }
      // },
    ];

    return (
     <Panel>
         {/* renderRightButton={this.renderRightButton}  {/* onSelectRow={this.onSelectRow} */}
         <table className={styles.chip_table_css}>
           <tbody>
           <tr>
             <td width="42%">
              <ChipGrid
                  code={code}
                  form={form}
                  isSerial
                  onSearch={this.handleSearch}
                  renderSearchForm={this.renderSearchForm}
                  renderLeftButton={this.renderLeftButton}
                  loading={loading}
                  data={data}
                  columns={columns}
                  rowSelection= {{
                    selectedRowKeys: this.state.selectedRowKeys,

                    onChange: async (selectedRowKeys, selectedRows) => {
                      // checkbox 组件模拟radio组件效果
                      const _selectedRowKeys = [...this.state.selectedRowKeys];
                      const singleClick = selectedRows.length <= 2;
                      const selected = selectedRows.pop();

                      _selectedRowKeys.splice(0, _selectedRowKeys.length);
                      selectedRows.splice(0, selectedRows.length);

                      if (singleClick && selected) {
                        _selectedRowKeys.push(selected.id);
                      }

                      this.setState({selectedRowKeys: _selectedRowKeys, selectedRow: selected?selected:{
                        chip_id:'',id:'',casenum:0,ready_state:1,chip_status:1
                        }},()=>{
                        //选中芯片，调用子页面查询方法
                        var params_child = this.$child.state.params
                        this.$child.handleSearch(params_child);
                      });

                    }
                }}

                onRow = {(record) => ({
                  // 行点击事件
                  onClick: async () => {

                    this.setState({selectedRowKeys: [record.id], selectedRow: record},()=>{
                       //选中芯片，调用子页面查询方法
                      var params_child = this.$child.state.params
                      this.$child.handleSearch(params_child);
                    });
                  }
                })}
                //isSerial
                />
                <Modal
                  title="新增混合文库"
                  width={600}
                  visible={addpageVisiable}
                  confirmLoading={confirmLoading}
                  onOk={this.handleaddSave}
                  onCancel={this.handleAddClose}
                  okText="提交"
                  cancelText="取消"

                >
                <ChipInfoAdd wrappedComponentRef={(inst) => { this.editForm = inst; }} ></ChipInfoAdd>
                </Modal>
                <Modal
                  title="样本数据导入"
                  width={600}
                  visible={excelVisible}
                  confirmLoading={confirmLoading}
                  onOk={this.handleExcelImport}
                  onCancel={this.handleExcelCancel}
                  okText="确认"
                  cancelText="取消"
                >
                  <Form style={{ marginTop: 8 }} hideRequiredMark>
                    <FormItem {...formItemLayout} label="导入样本">
                      <Dragger {...uploadProps} onChange={this.onUpload}>
                        <p className="ant-upload-drag-icon">
                          <InboxOutlined  />
                        </p>
                        <p className="ant-upload-text">将文件拖到此处，或点击上传</p>
                        <p className="ant-upload-hint">请上传 .xls,.xlsx 格式的文件</p>
                      </Dragger>
                    </FormItem>
                    <FormItem {...formItemLayout} label="样本模板下载">
                      <Button type="primary" icon={<DownCircleTwoTone />} size="small" onClick={this.handleTemplate}>
                        点击下载
                      </Button>
                    </FormItem>
                  </Form>
                </Modal>
             </td>
             <td width="58%">
             <CaseInfo ref="caseInfoRef" childEvevnt={this.childEvevnt} doChipQuery={this.handleSearch} chipparams={this.state.params}
             select_chip_id={selectedRow.chip_id}
             bsampleEdit={selectedRow.ready_state <  3? true:false}
             ></CaseInfo>
             </td>
           </tr>
           </tbody>
         </table>

       </Panel>
    )
  }

}
export default ChipInfo
