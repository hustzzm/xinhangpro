import React, { Fragment, PureComponent } from 'react';
import { Card, Divider, message, Modal } from 'antd';
import { formatMessage, FormattedMessage } from 'umi/locale';
import router from 'umi/router';
import { getButton } from '../../utils/authority';
import styles from './SwordPage.less';
import ToolBar from './ToolBar';
import SearchBox from './SearchBox';
import StandardTable from '../StandardTable';
import { requestApi } from '../../services/api';

export default class CaseInfoGrid extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      current: 1,
      size: 20,
      formValues: {},
      selectedRows: [],
      buttons: getButton(props.code),
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

    const { form } = this.props;

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
    const { form, onReset } = this.props;
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
    const { onSearch } = this.props;
    const { current, size, formValues } = this.state;

    const params = {
      current: firstPage ? 1 : current,
      size,
      ...formValues,
    };
    if (onSearch) {
      onSearch(params);
    }
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });

    const { onSelectRow } = this.props;
    if (onSelectRow) {
      onSelectRow(rows);
    }
  };

  getSelectKeys = () => {
    const { selectedRows } = this.state;
    const { pkField = 'id', childPkField = 'id' } = this.props;
    return selectedRows.map(row => {
      const selectKey = row[pkField] || row[childPkField];
      if (`${selectKey}`.indexOf(',') > 0) {
        return `${selectKey}`.split(',');
      }
      return selectKey;
    });
  };

  handleToolBarClick = btn => {
    const { selectedRows } = this.state;
    const keys = this.getSelectKeys();
    this.handleClick(btn, keys, selectedRows);
  };

  handleClick = (btn, keys = [], rows) => {
    const { path, alias } = btn;
    const { btnCallBack,code } = this.props;
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
    if (alias === 'edit') {
      if (keys.length <= 0) {
        message.warn('????????????????????????!');
        return;
      }
      if (keys.length > 1) {
        message.warn('????????????????????????!');
        return;
      }
      /**
       * ?????????????????????????????????????????????
       */
      if(rows[0].caseStatus===3){
        message.warn('?????????????????????????????????!');
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
    if (alias === 'delete') {
      if (keys.length <= 0) {
        message.warn('??????????????????????????????!');
        return;
      }
      if(rows[0].caseStatus===3){
        message.warn('?????????????????????????????????!');
        return;
      }

      Modal.confirm({
        title: '????????????',
        content: '?????????????????????????',
        okText: '??????',
        okType: 'danger',
        cancelText: '??????',
        async onOk() {
          let response = {}
          if(code==='quartz'){
            response = await requestApi(path, rows[0]);
          }else{
            response = await requestApi(path, { ids: keys.join(',') });
          }
          if (response.success) {
            message.success(response.msg);
            refresh();
          } else {
            message.error(response.msg || '????????????');
          }
        },
        onCancel() {},
      });
      return;
    }
    if (btnCallBack) {
      btnCallBack({ btn, keys, rows, refresh });
    }
  };

  render() {
    const { buttons, selectedRows,current,size } = this.state;
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
      isSerial
    } = this.props;
    let { columns } = this.props;
    /**
     * ????????????????????????
     */
    if(isSerial){
      const serialColumn = {
        title: '??????',
        width:50,
        render(text,record,index){
          return(
            `${(current-1)*(size)+(index+1)}`
          )
        }
      }
      columns.unshift(serialColumn)
    }
    // let tmpButtons = buttons
    // if(buttons){
    //   tmpButtons = getButton(code)
    // }
    const actionButtons = buttons.filter(button => button.action === 2 || button.action === 3);

    if (columns && Array.isArray(columns) && (actionButtons.length > 0 || renderActionButton)) {
      const key = pkField || rowKey || 'id';
      columns = [
        ...columns,
        {
          title: formatMessage({ id: 'table.columns.action' }),
          width: actionColumnWidth || 200,
          fixed: 'right',
          render: (text, record) => (
            <Fragment>
              <div style={{ textAlign: 'center' }}>
                {actionButtons.map((button, index) => (
                  <Fragment key={button.code}>
                    {index > 0 ? <Divider type="vertical" /> : null}
                    <a
                      title={formatMessage({ id: `button.${button.alias}.name` })}
                      onClick={() =>
                        this.handleClick(button, [record[childPkField || key]], [record])
                      }
                    >
                      <FormattedMessage id={`button.${button.alias}.name`} />
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
            buttons={buttons}
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
          />
        </div>
      </Card>
    );
  }
}
