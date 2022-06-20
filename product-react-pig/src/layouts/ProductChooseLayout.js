import React, { Fragment } from 'react';
import { CopyrightOutlined } from '@ant-design/icons';
import GlobalFooter from '@/components/GlobalFooter';
import styles from './ProductChooseLayout.less';
import defaultSettings from '../../src/defaultSettings';

const { title, vision } = defaultSettings;
const now = new Date();
const year = now.getFullYear();
const copyright = (
  <Fragment>
    <font style={{color:'#ccc'}}><CopyrightOutlined />Copyright 2020-{year}</font> <span style={{color:'#ccc'}}></span> <font style={{color:'#ccc'}}>{vision}</font>{' '}
  </Fragment>
);

const ProductChooseLayout = ({ children }) => (
  <div>
    <div className={styles.productChoose}>
    {children}
    </div>
    <GlobalFooter copyright={copyright} />
  </div>
);

export default ProductChooseLayout;
