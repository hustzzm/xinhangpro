import React, { Fragment } from 'react';
import { formatMessage } from 'umi/locale';
import Link from 'umi/link';
import { Icon } from 'antd';
import GlobalFooter from '@/components/GlobalFooter';
import SelectLang from '@/components/SelectLang';
import styles from './UserLayout.less';
import logo from '../assets/favicon.svg';

// const links = [
//   {
//     key: 'help',
//     title: formatMessage({ id: 'layout.user.link.help' }),
//     href: '',
//   },
//   {
//     key: 'privacy',
//     title: formatMessage({ id: 'layout.user.link.privacy' }),
//     href: '',
//   },
//   {
//     key: 'terms',
//     title: formatMessage({ id: 'layout.user.link.terms' }),
//     href: '',
//   },
// ];

const copyright = (
  <Fragment>
    Copyright <Icon type="copyright" /> 2020 {' '}
  </Fragment>
);
const OtherLayout = ({ children }) => (

  // @TODO <DocumentTitle title={this.getPageTitle()}>
  <div>
    {children}
  </div>
);

export default OtherLayout;
