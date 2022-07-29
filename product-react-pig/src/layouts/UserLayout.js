import React, { Fragment } from 'react';
import Link from 'umi/link';
import { CopyrightOutlined } from '@ant-design/icons';
import { Row, Col, Carousel } from 'antd';
import GlobalFooter from '@/components/GlobalFooter';
import styles from './UserLayout.less';

import defaultSettings from '../../src/defaultSettings';

const { title, vision } = defaultSettings;

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
const now = new Date();
const year = now.getFullYear();

const copyright = (
  <Fragment>
    <font style={{color:'#ccc'}}><CopyrightOutlined />Copyright 2020-{year}</font> <span style={{color:'#ccc'}}></span> <font style={{color:'#ccc'}}>{vision}</font>{' '}
  </Fragment>
);

const UserLayout = ({ children }) => (
  // @TODO <DocumentTitle title={this.getPageTitle()}>
  <div className={styles.loginmain}>
    <div className={styles.container}>
        
        <div className={styles.content}>
          <div className={styles.top}>
            <div className={styles.header}>
              <Link to="/">
                <Row>
             
                  <Col span={24}>
                  <p className={styles.title}>
                  信航业务管理系统
                  </p>
                  </Col>
                  {/*<Col span={24} style={{marginTop:'-10px'}}>*/}
                  {/*<span className={styles.title}>数据分析系统</span>*/}
                  {/*</Col>*/}
                </Row>        
              </Link>     
            </div>
            {/* <div className={styles.desc}>
      
            </div> */}
          </div>
          {children}
        </div>
        <GlobalFooter copyright={copyright} />
      </div>
  </div>
  
);

export default UserLayout;
