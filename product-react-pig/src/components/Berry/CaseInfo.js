import React, {PureComponent} from 'react';
import { connect } from 'dva';
import { VerticalAlignBottomOutlined, VerticalAlignTopOutlined } from '@ant-design/icons';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import { Button, Col, Input, Row, DatePicker, Select, Progress } from 'antd';
import {PlusOutlined,DownloadOutlined,SyncOutlined,UploadOutlined,DeleteOutlined,InboxOutlined,DownCircleTwoTone,FilterOutlined} from "@ant-design/icons";
import { CASEINFO_LIST } from '../../../actions/caseinfo';
import Grid from '../../../components/Berry/CaseInfoGrid';
import func from "../../../utils/Func";
import Panel from "../../../components/Panel";
import router from "umi/router";
import {CASEINFO_DICT} from "../../../actions/dictionary";

const { RangePicker } = DatePicker;
const { Option } = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD';

@connect(({ caseinfo,dictionary, loading }) => ({
  caseinfo,
  dictionary,
  loading: loading.models.caseinfo,
}))
@Form.create()
class CaseInfo extends PureComponent {
  state = {
    visible: false,
    excelVisible: false,
    confirmLoading: false,
    select_chip_id:'',
    selectedRows: [],
    checkedTreeKeys: [],
    params: {},
    onReset: () => {},
  };
  componentDidMount() {
    this.props.childEvevnt(this);
  }
  componentWillMount() {
    const { dispatch } = this.props;
    /**
     * 查询样本状态的字段
     */
    let params = {code:'dpsampleStatus'}
    dispatch(CASEINFO_DICT(params));
  }

dotest = ()=>{
  alert(1111);
}

  // ============ 查询 ===============
  handleSearch = params => {
    
    const { dispatch,select_chip_id } = this.props;
    params.chip_id = select_chip_id;    
    const payload = {
      ...params,     
    };
    // delete payload.bloodCollection
    dispatch(CASEINFO_LIST(payload));
  };

  // renderSearchSelect = () =>{
  //   const {
  //     dictionary: { dpsampleStatus },
  //   } = this.props;
  //   let children = [];
  //   for (let i = 0; i < dpsampleStatus.length; i++) {
  //     children.push(<Option key={dpsampleStatus[i].dictKey}>{dpsampleStatus[i].dictValue}</Option>);
  //   }
  //   return (
  //     <Select
  //       mode="multiple"
  //       style={{ width: '400px' }}
  //       placeholder="请选择样本状态"
  //     >
  //       {children}
  //     </Select>
  //   )
  // }

  renderLeftButton = () => (
    <div style={{float:'right',    'paddingBottom': '10px','marginRight': '-6px'}}>
      <Button icon={<PlusOutlined />} onClick={this.handleImport} type="primary" >
        编辑
      </Button>      
      <Button icon={<DeleteOutlined />} onClick={this.handleImport} >
        删除
      </Button>     
    </div>
  );

  // ============ 查询表单 ===============
  renderSearchForm = onReset => {
    const { form } = this.props;
    const { getFieldDecorator } = form;

    return (
      <div style={{'backgroundColor': 'rgba(0, 51, 204, 0.109803921568627)',    padding: '10px'}}>
      <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
      <Col md={16} sm={24}>
          <FormItem>
            {getFieldDecorator('name')(<Input placeholder="受检者姓名" />)}
          </FormItem>
          <FormItem>
            {getFieldDecorator('sample_id')(<Input placeholder="样本编号" />)}
          </FormItem>
        </Col>      
    
        <Col md={8} sm={24}>
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

  onSelectRow = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  handleClickCaseid = (id) =>{
    router.push(`/manage/caseInfo/view/${id}`);
  }
  handleChipCaseInfoList= (id) =>{
    router.push({ pathname:`/manage/caseInfo/chipCaseInfo/${id}`,state:{back :'/manage/caseInfo/list'}});
  }

  render() {

    const code = 'caseinfo';

    const {
      form,
      loading,
      dictionary:{dpsampleStatus},
      caseinfo: { data },
    } = this.props;
    const that = this
    const columns = [
      {
        title: '样本编号',
        flex:1,
        dataIndex: 'sample_id',
        render(text, record, index) {
          return <a onClick={() =>
            that.handleClickCaseid(record.id)
          }>{text}</a>;
        }
      },     
      {
        title: '姓名',
        flex:1,
        dataIndex: 'name',
      },
      {
        title: '年龄',
        flex:1,
        dataIndex: 'age',
      },     
      {
        title: '采样日期',
        dataIndex: 'getblood_date',
        flex:1,
        render(text){ 
          //采血时间取前10位       
          return text ? text.substr(0,10) : null;          
        }
      },
      {
        title: 'barcode编号',
        dataIndex: 'barcode',
        flex:1,
      },
      {
        title: '样本状态',
        dataIndex: 'sample_status',
        flex:1,
        render(text,record,index){
          let filter = dpsampleStatus.filter(item => item.dictKey===text);
          if(filter.length){
            return filter[0].dictValue
          }else{
            return ''
          }
        }
      },     
     
    ];
// onSelectRow={this.onSelectRow}  {/* rowSelection={rowSelection} */}
   
    return (
      <Panel>
      <Grid
        scroll={{ x: 800 }}
        code={code}
        form={form} 
        onSelectRow={this.onSelectRow} 
        onSearch={this.handleSearch}   
        renderLeftButton={this.renderLeftButton}    
        renderSearchForm={this.renderSearchForm}
        loading={loading}
        data={data}
        columns={columns}
        isSerial
      />
      </Panel>
    )
  }
}

export default CaseInfo;
