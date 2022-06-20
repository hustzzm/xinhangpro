import React, { Component } from 'react';
import {Row, Col, Card, Modal,Button} from 'antd';
import { getProduct } from '@/utils/authority';

class ProductChoose extends Component {
  state = {
    productList:[]
  };

  componentWillMount() {
    this.setState({productList:JSON.parse(getProduct())})
  }

  onclick = (id,url) =>{
    window.open(`${url}`)
  }

  loginout = () =>{
    Modal.confirm({
      title: '退出确认',
      content: '是否确定退出登录？',
      okText: '确定',
      okType: 'danger',
      cancelText: '取消',
      onOk() {
        window.g_app._store.dispatch({
          type: 'login/logout',
        });
      },
      onCancel() {},
    });
  }


  render() {
    const {productList} = this.state
    return (
      <Card title="我的产品" bordered={false} extra={<Button shape="round" onClick={()=>this.loginout()}>退出登录</Button>}>
        <Row>
          {productList.map((product) => (
            <Col span={12} style={{marginBottom:'20px'}}>
              <a onClick={()=>this.onclick(product.id,product.productPath)}><img alt={product.productName} src={require(`../../assets/productImage/${product.productImg}.jpg`)} style={{height: 260}}/></a>
            </Col>
          ))}
        </Row>
      </Card>
    );
  }
}

export default ProductChoose;
