import React, { Fragment } from 'react';
import { CopyrightOutlined } from '@ant-design/icons';
import { Layout } from 'antd';
import GlobalFooter from '@/components/GlobalFooter';

import defaultSettings from '../../src/defaultSettings';

const { title, vision } = defaultSettings;
const now = new Date();
const year = now.getFullYear();
const { Footer } = Layout;
const FooterView = () => (
  <Footer style={{ padding: 0 }}>
    <GlobalFooter
      copyright={
        <Fragment>
          <font style={{color:'#ccc'}}><CopyrightOutlined />Copyright 2020-{year}</font> <span style={{color:'#ccc'}}></span> <font style={{color:'#ccc'}}>{vision}</font>{' '}
        </Fragment>
      }
    />
  </Footer>
);
export default FooterView;
