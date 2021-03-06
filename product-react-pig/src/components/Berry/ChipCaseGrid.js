import React, {Fragment, PureComponent} from 'react';
import {Button, Card, Divider, message, Modal, Upload} from 'antd';
import {formatMessage, FormattedMessage} from 'umi/locale';
import router from 'umi/router';
import Link from 'umi/link';
import {getButton, getToken,setExcelError} from '../../utils/authority';
import styles from './SwordPage.less';
import ToolBar from './ToolBar';
import SearchBox from './SearchBox';
import StandardTable from '../StandardTable';
import {requestApi} from '../../services/api';
import {Form} from "@ant-design/compatible";
import {DownCircleTwoTone, InboxOutlined} from "@ant-design/icons";

const {Dragger} = Upload;
const FormItem = Form.Item;
export default class ChipCaseGrid extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      current: 1,
      size: 10,
      formValues: {},
      selectedRows: [],
      confirmLoading: false,
      excelVisible: false,
      uploadProps: null,
      buttons: getButton(props.code),
      excelError: false
    };
  }

  componentDidMount() {
    this.handleSearch();
  }

  handleSearch = e => {
    if (e) {
      e.preventDefault();
      e.stopPropagation();
    }

    const {form} = this.props;

    form.validateFields(async (err, fieldsValue) => {
      if (err) {
        return;
      }

      const values = {
        ...fieldsValue,
      };

      await this.setState({
        formValues: values,
      });

      this.refreshTable(true);
    });
  };

  handleFormReset = async () => {
    const {form, onReset} = this.props;
    form.resetFields();
    await this.setState({
      current: 1,
      size: 10,
      formValues: {},
      selectedRows: [],
    });
    if (onReset) {
      onReset();
    }
    this.refreshTable();
  };

  handleStandardTableChange = async pagination => {
    await this.setState({
      current: pagination.current,
      size: pagination.pageSize,
    });

    this.refreshTable();
  };

  refreshTable = (firstPage = false) => {
    const {onSearch} = this.props;
    const {current, size, formValues} = this.state;

    const params = {
      current: firstPage ? 1 : current,
      size,
      ...formValues,
    };
    if (onSearch) {
      onSearch(params);
    }
  };

  handleTemplate = () => {
    window.open(`/api/import/template`);
  };


  handleExcelCancel = () => {
    this.setState({
      excelVisible: false,
    });
  }

  onUpload = info => {
    const {status} = info.file;
    if (status === 'done') {
      message.success(`${info.file.name} ??????????????????!`);
      this.handleExcelCancel()
      this.refreshTable(true);
    } else if (status === 'error') {
      const msg = info.file.response.msg;
      setExcelError(msg)
      this.setState({excelError:true});
      message.error("????????????????????????,?????????????????????!");
    }
  };


  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });

    const {onSelectRow} = this.props;
    if (onSelectRow) {
      onSelectRow(rows);
    }
  };

  getSelectKeys = () => {
    const {selectedRows} = this.state;
    const {pkField = 'id', childPkField = 'id'} = this.props;
    return selectedRows.map(row => {
      const selectKey = row[pkField] || row[childPkField];
      if (`${selectKey}`.indexOf(',') > 0) {
        return `${selectKey}`.split(',');
      }
      return selectKey;
    });
  };

  handleToolBarClick = btn => {
    const {selectedRows} = this.state;
    const keys = this.getSelectKeys();
    this.handleClick(btn, keys, selectedRows);
  };

  handleClick = (btn, keys = [], rows) => {
    const {path, alias} = btn;
    const {btnCallBack, code} = this.props;
    const refresh = (temp = true) => this.refreshTable(temp);
    if (alias === 'add') {
      if (keys.length > 1) {
        message.warn('???????????????????????????!');
        return;
      }
      if (keys.length === 1) {
        router.push(`${path}/${keys[0]}`);
        return;
      }
      router.push(path);
      return;
    }
    if (alias === 'addFlowcell') {
      if (keys.length > 1) {
        message.warn('???????????????????????????!');
        return;
      }
      if (keys.length === 1) {
        router.push(`${path}/${keys[0]}`);
        return;
      }
      router.push(path);
      return;
    }
    if (alias === 'addCase') {
      if (keys.length > 1) {
        message.warn('???????????????????????????!');
        return;
      }
      if (keys.length === 1) {
        router.push(`${path}/${rows[0].flowcellid}`);
        return;
      }
      router.push(path);
      return;
    }
    if (alias === 'edit') {
      if (keys.length <= 0) {
        message.warn('????????????????????????!');
        return;
      }
      if (keys.length > 1) {
        message.warn('????????????????????????!');
        return;
      }
      router.push(`${path}/${keys[0]}`);
      return;
    }
    if (alias === 'view') {
      if (keys.length <= 0) {
        message.warn('????????????????????????!');
        return;
      }
      if (keys.length > 1) {
        message.warn('????????????????????????!');
        return;
      }
      router.push(`${path}/${keys[0]}`);
      return;
    }
    if (alias === 'importCase') {
      if (keys.length <= 0) {
        message.warn('????????????????????????!');
        return;
      }
      const uploadProps = {
        name: 'file',
        headers: {
          'Berry-Auth': getToken(),
        },
        action: path,
        data: {
          flowcellid: rows[0].flowcellid
        },
        accept: '.xls,.xlsx'
      };
      this.setState({uploadProps: uploadProps})
      this.setState({excelVisible: true})
      this.setState({excelError: false})
      return;
    }
    if (alias === 'delete') {
      if (keys.length <= 0) {
        message.warn('??????????????????????????????!');
        return;
      }

      Modal.confirm({
        title: '????????????',
        content: '????????????????????????????????????????????????????????????????????????????!',
        okText: '??????',
        okType: 'danger',
        cancelText: '??????',
        async onOk() {
          let response = await requestApi(path, {ids: keys.join(',')});
          if (response.success) {
            message.success(response.msg);
            refresh();
          } else {
            message.error(response.msg || '????????????');
          }
        },
        onCancel() {
        },
      });
      return;
    }
    if (btnCallBack) {
      btnCallBack({btn, keys, rows, refresh});
    }
  };

  render() {
    const {buttons, selectedRows, current, size, uploadProps, excelVisible, confirmLoading, excelError} = this.state;
    const {
      loading = false,
      rowKey,
      pkField,
      childPkField,
      data,
      scroll,
      tblProps,
      cardProps,
      actionColumnWidth,
      renderSearchForm,
      renderLeftButton,
      renderRightButton,
      renderActionButton,
      code,
      isSerial,
      expandable,
      noCheck
    } = this.props;
    let {columns} = this.props;
    // /**
    //  * ????????????????????????
    //  */
    // if (isSerial) {
    //   const serialColumn = {
    //     title: '??????',
    //     width: 50,
    //     render(text, record, index) {
    //       return (
    //         `${(current - 1) * (size) + (index + 1)}`
    //       )
    //     }
    //   }
    //   columns.unshift(serialColumn)
    // }

    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 12},
        md: {span: 16},
      },
    };

    //
    // if(buttons){
    //     this.setState({buttons:getButton(this.props.code)})
    // }
    let tmpButtons = buttons
    if (buttons) {
      tmpButtons = getButton(code)
    }
    let filter = tmpButtons.filter(button => button.code.indexOf("chip")>-1);
    const actionButtons = filter.filter(button => button.action === 2 || button.action === 3);


    if (columns && Array.isArray(columns) && (actionButtons.length > 0 || renderActionButton)) {
      const key = pkField || rowKey || 'id';
      columns = [
        ...columns,
        {
          title: formatMessage({id: 'table.columns.action'}),
          width: actionColumnWidth || 200,
          render: (text, record) => (
            <Fragment>
              <div style={{textAlign: 'center'}}>
                {actionButtons.map((button, index) => (
                  <Fragment key={button.code}>
                    {index > 0 ? <Divider type="vertical"/> : null}
                    <a
                      title={formatMessage({id: `button.${button.alias}.name`})}
                      onClick={() =>
                        this.handleClick(button, [record[childPkField || key]], [record])
                      }
                    >
                      <FormattedMessage id={`button.${button.alias}.name`}/>
                    </a>
                  </Fragment>
                ))}
                {renderActionButton
                  ? renderActionButton([record[childPkField || key]], [record])
                  : null}
              </div>
            </Fragment>
          ),
        },
      ];
    }

    return (
      <Card bordered={false} {...cardProps}>
        <div className={styles.swordPage}>
          <SearchBox onSubmit={this.handleSearch} onReset={this.handleFormReset}>
            {renderSearchForm(this.handleFormReset)}
          </SearchBox>
          <ToolBar
            buttons={tmpButtons}
            renderLeftButton={renderLeftButton}
            renderRightButton={renderRightButton}
            onClick={this.handleToolBarClick}
          />
          <StandardTable
            rowKey={rowKey || 'id'}
            selectedRows={selectedRows}
            loading={loading}
            columns={columns}
            data={data}
            onSelectRow={this.handleSelectRows}
            onChange={this.handleStandardTableChange}
            scroll={scroll}
            tblProps={tblProps}
            size="middle"
            bordered
            expandable={expandable}
            noCheck={noCheck}
          />

          <Modal
            title="??????????????????"
            width={600}
            visible={excelVisible}
            confirmLoading={confirmLoading}
            onOk={this.handleExcelCancel}
            onCancel={this.handleExcelCancel}
            okText="??????"
            cancelText="??????"
          >
            <Form style={{marginTop: 8}} hideRequiredMark>
              <FormItem {...formItemLayout} label="????????????">
                <div key={Math.random()}>
                  <Dragger {...uploadProps} onChange={this.onUpload}>
                    <p className="ant-upload-drag-icon">
                      <InboxOutlined/>
                    </p>
                    <p className="ant-upload-text">???????????????????????????????????????</p>
                    <p className="ant-upload-hint">????????? .xls,.xlsx ???????????????</p>
                  </Dragger>
                </div>
              </FormItem>
              {excelError?(<FormItem {...formItemLayout} label="??????????????????">
                <Link to={{ pathname:'/excelError'}} target="_blank"><span>?????????</span></Link>
                {/*<Link to={{ pathname:`/excelError`,search:`?errHtml=${excelHtml}`}} target="_blank"><span>?????????</span></Link>*/}
                {/*<Link to={`/excelError?errHtml=${excelHtml}`} target="_blank"><span>?????????</span></Link>*/}
                {/*<a onClick={this.detailExcelException}>?????????</a>*/}
              </FormItem>):('')}
              <FormItem {...formItemLayout} label="????????????">
                <Button type="primary" icon={<DownCircleTwoTone/>} size="small" onClick={this.handleTemplate}>
                  ????????????
                </Button>
              </FormItem>
            </Form>
          </Modal>
        </div>
      </Card>
    );
  }
}
