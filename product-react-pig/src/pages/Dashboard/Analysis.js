import React, {PureComponent, Suspense} from 'react';
import { connect } from 'dva';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import {CHART_DATA} from "@/actions/charts";
import PageLoading from '@/components/PageLoading';

const IntroduceRow = React.lazy(() => import('./IntroduceRow'));

@connect(({ chart, loading }) => ({
  chart,
  loading: loading.models.chart,
}))
class Analysis extends PureComponent {

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch(CHART_DATA())
  }

  render() {
    const {
      loading,
      chart:{visitData}
    } = this.props;
    return (
      <GridContent>
        <Suspense fallback={<PageLoading />}>
          <IntroduceRow loading={loading} visitData={visitData} />
        </Suspense>
      </GridContent>
    );
  }
}

export default Analysis;
