import React, {memo} from 'react';
import {InfoCircleOutlined} from '@ant-design/icons';
import {Col, Row, Tooltip} from 'antd';
import {FormattedMessage} from 'umi/locale';
import {ChartCard} from '@/components/Charts';
import numeral from 'numeral';

const topColResponsiveProps = {
  xs: 24,
  sm: 12,
  md: 12,
  lg: 12,
  xl: 6,
  style: { marginBottom: 24 },
};

const IntroduceRow = memo(({ loading, visitData }) => (
  <Row gutter={24}>
    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        title={<FormattedMessage id="app.analysis.flowcellnum" defaultMessage="Flowcell Num" />}
        action={
          <Tooltip
            title={<FormattedMessage id="app.analysis.introduce.flowcell" defaultMessage="Introduce" />}
          >
            <InfoCircleOutlined />
          </Tooltip>
        }
        loading={loading}
        total={numeral(visitData.flowcellNum).format('0,0')}
        contentHeight={46}
      >
      </ChartCard>
    </Col>

    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        loading={loading}
        title={<FormattedMessage id="app.analysis.caseTotalNum" defaultMessage="Case TotalNum" />}
        action={
          <Tooltip
            title={<FormattedMessage id="app.analysis.introduce.caseTotalNum" defaultMessage="Introduce" />}
          >
            <InfoCircleOutlined />
          </Tooltip>
        }
        total={numeral(visitData.caseTotalNum).format('0,0')}
        contentHeight={46}
      >
      </ChartCard>
    </Col>
    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        loading={loading}
        title={<FormattedMessage id="app.analysis.caseAnalysisNum" defaultMessage="Anasisly Case" />}
        action={
          <Tooltip
            title={<FormattedMessage id="app.analysis.introduce.caseAnalysisNum" defaultMessage="Introduce" />}
          >
            <InfoCircleOutlined />
          </Tooltip>
        }
        total={numeral(visitData.caseAnalysisNum).format('0,0')}
        contentHeight={46}
      >
      </ChartCard>
    </Col>
    <Col {...topColResponsiveProps}>
      <ChartCard
        loading={loading}
        bordered={false}
        title={
          <FormattedMessage
            id="app.analysis.caseReportNum"
            defaultMessage="Report Case"
          />
        }
        action={
          <Tooltip
            title={<FormattedMessage id="app.analysis.introduce.caseReportNum" defaultMessage="Introduce" />}
          >
            <InfoCircleOutlined />
          </Tooltip>
        }
        total={numeral(visitData.caseReportNum).format('0,0')}
        contentHeight={46}
      >
      </ChartCard>
    </Col>
  </Row>
));

export default IntroduceRow;
