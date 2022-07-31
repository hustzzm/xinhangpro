import React, { Fragment, PureComponent } from 'react';
import { Card, Divider, message, Modal } from 'antd';
import { formatMessage, FormattedMessage } from 'umi/locale';
import router from 'umi/router';
import { getButton } from '@/utils/authority';
import styles from '@/components/Berry/SwordPage.less';
import ToolBar from '@/components/Berry/ToolBar';
import SearchBox from '@/components/Berry/SearchBox';
import StandardTable from '../Table/index.js';
import { requestApi } from '@/services/api.js';

export default class MemberGrid extends PureComponent {
    constructor(props) {
        super(props);
        this.state = {
            current: 1,
            size: 20,
            formValues: {},
            buttons: getButton(props.code),
            field: "",
            sort: "",
            selectedRowKeys: []
        };
    }

    componentDidMount() {
        // this.handleSearch();
        this.props.clearChildSelect(this)
    }

    // 清空选中
    clearSelect = () => {
        this.setState({
            selectedRowKeys: []
        })
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
            size: 20,
            formValues: {},
            selectedRows: [],
        });
        if (onReset) {
            onReset();
        }
        this.refreshTable();
    };

    handleStandardTableChange = async (pagination, filters, sorter) => {
        if (sorter.order) {
            sorter.order = sorter.order.substring(0, sorter.order.length - 3)
        }
        await this.setState({
            current: pagination.current,
            size: pagination.pageSize,
            sort: sorter.order,
            field: sorter.field
        });

        this.refreshTable();
    };

    refreshTable = (firstPage = false) => {
        const { onSearch } = this.props;
        const { current, size, formValues, sort, field } = this.state;
        const params = {
            current: firstPage ? 1 : current,
            size,
            sort,
            field,
            ...formValues,

        };
        if (onSearch) {
            onSearch(params);
            this.setState({ selectedRowKeys: [] })
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

    rowSelectChange(selectedRowKeys, selectedRows) {
        this.props.selectRow(selectedRowKeys, selectedRows)
        this.setState({ selectedRowKeys })
    }
    selectRow = (record) => {
        const selectedRowKeys = [...this.state.selectedRowKeys];
        selectedRowKeys.splice(0, selectedRowKeys.length);
        selectedRowKeys.push(record.key);
        this.setState({ selectedRowKeys });
    };
    onSelectedRowKeysChange = (selectedRowKeys) => {
        this.setState({ selectedRowKeys });
    };
    render() {
        const { buttons, selectedRows, current, size } = this.state;
        const { ...other } = this.props;
        const that = this
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
            expandable,
            code,
            isSerial,
            noCheck
            
        } = this.props;
        let { columns, onChange } = this.props;

        const { selectedRowKeys } = this.state;
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectedRowKeysChange,
        };
        
        // const expandProps = {rowSelection}

        /**
         * 增加序号列的判断
         */
        if (isSerial) {
            const serialColumn = {
                title: '序号',
                width: 50,
                align: 'center',
                render(text, record, index) {
                    return (
                        `${(current - 1) * (size) + (index + 1)}`
                    )
                }
            }

            let sno = columns.filter(item => item.title == '序号');
            if (sno.length == 0)
                columns.unshift(serialColumn)
        }

        // if(buttons){
        //     this.setState({buttons:getButton(this.props.code)})
        // }
        let tmpButtons = buttons
        if (buttons) {
            tmpButtons = getButton(code)
        }
        const actionButtons = tmpButtons.filter(button => button.action === 2 || button.action === 3);

        if (columns && Array.isArray(columns) && (actionButtons.length > 0 || renderActionButton)) {
            const key = pkField || rowKey || 'id';
            columns = [
                ...columns
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
                        // onSelectRow={this.handleSelectRows}
                        onChange={this.handleStandardTableChange}
                        scroll={scroll}
                        tblProps={tblProps}
                        size="middle"
                        bordered
                        expandable={expandable}
                        noCheck={noCheck}
                        // expandProps = {rowSelection}
                        {...other}
                     
                        rowSelection={{ width:'50px', onChange: this.rowSelectChange.bind(this), selectedRowKeys: this.state.selectedRowKeys }}
                    />

                </div>
            </Card>
        );
    }
}
