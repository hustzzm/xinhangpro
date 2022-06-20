import React, { PureComponent } from 'react';
import { Form } from '@ant-design/compatible';
import '@ant-design/compatible/assets/index.css';
import {Card, Button, Row, Col} from 'antd';
import router from 'umi/router';
import { connect } from 'dva';
import Panel from '../../../components/Panel';
import { CHIPINFO_DETAIL } from '../../../actions/chipinfo';
import styles from '../../../layouts/Sword.less';

const FormItem = Form.Item;

@connect(({ chipinfo }) => ({
  chipinfo,
}))
@Form.create()
class ChipInfoView extends PureComponent {
  componentWillMount() {
    const {
      dispatch,
      match: {
        params: { id },
      },
    } = this.props;
    dispatch(CHIPINFO_DETAIL(id));
  }

  handleEdit = () => {
    const {
      match: {
        params: { id },
      },
    } = this.props;
    router.push(`/caseInfo/chipInfo/edit/${id}`);
  };

  render() {
    const {
      chipinfo: { detail },
    } = this.props;

    const formItemLayout = {
      labelCol: {
        span: 8,
      },
      wrapperCol: {
        span: 16,
      },
    };

    const action = (
      <Button type="primary" onClick={this.handleEdit}>
        修改
      </Button>
    );

    return (
      <Panel title="查看" back="/manage/chipInfo/list" action={action}>
        <Form hideRequiredMark style={{ marginTop: 8 }}>
          <Card title="芯片基本" className={styles.card} bordered={false}>
            <Row gutter={24}>
              <Col span={8}>
                <FormItem {...formItemLayout} label="芯片编号">
                  <span>{detail.flowcellid}</span>
                </FormItem>
              </Col>
              <Col span={8}>
                <FormItem {...formItemLayout} label="上机浓度">
                  <span>{detail.upoflibrary}</span>
                </FormItem>
              </Col>
              <Col span={8}>
                <FormItem {...formItemLayout} label="混合体积">
                  <span>{detail.mixedvolume}</span>
                </FormItem>
              </Col>
            </Row>
            <Row gutter={24}>
              <Col span={8}>
                <FormItem {...formItemLayout} label="测序仪编号">
                  <span>{detail.sequencerNumber}</span>
                </FormItem>
              </Col>
              <Col span={8}>
                <FormItem {...formItemLayout} label="上机时间">
                  <span>{detail.starttime}</span>
                </FormItem>
              </Col>
              <Col span={8}>
                <FormItem {...formItemLayout} label="负责人">
                  <span>{detail.leadingPerson}</span>
                </FormItem>
              </Col>
            </Row>
          </Card>
        </Form>
      </Panel>
    );
  }
}

export default ChipInfoView;
