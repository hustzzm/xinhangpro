import React, { PureComponent } from 'react';
import Sound from 'react-sound';
// import { connect } from 'dva';
import { Icon as LegacyIcon } from '@ant-design/compatible';
import Link from 'umi/link';
import Debounce from 'lodash-decorators/debounce';
import styles from './index.less';
import RightContent from './RightContent';

import {
  ORDERINFO_NEWRECORD,  
  ORDERINFO_UPDATESOUNDSTATE
} from '@/actions/gt/order';

// @Form.create()

export default class GlobalHeader extends PureComponent {

  state = {     
    playstate:'STOPPED',
    position:0
  }

  componentWillMount() {
      
    const { dispatch } = this.props;
    // 查询directory数据集，包括样本状态、样品进度、检测类型
   
    dispatch(ORDERINFO_NEWRECORD({})).then(result =>{
     
      if(result.success && result.data && result.data.orderId){
        
          this.setState({playstate:'PLAYING',position:0},()=> {
         
            
            let subparams = {
              orderId:result.data.orderId
            }
            
            dispatch(ORDERINFO_UPDATESOUNDSTATE(subparams))
  
           });
        
      }else{
     
        this.setState({playstate:'STOPPED',position:0})
      }
    });
   
}


  componentWillUnmount() {
    this.triggerResizeEvent.cancel();
  }
  /* eslint-disable*/
  @Debounce(600)
  triggerResizeEvent() {
    // eslint-disable-line
    const event = document.createEvent('HTMLEvents');
    event.initEvent('resize', true, false);
    window.dispatchEvent(event);
  }
  toggle = () => {
    const { collapsed, onCollapse } = this.props;
    onCollapse(!collapsed);
    this.triggerResizeEvent();
  };

  handleSongLoading = () =>{
   
  }
  handleSongPlaying = () =>{
      
  }
  handleSongFinishedPlaying = () =>{
      
    this.setState({playstate:'STOPPED',position:0})
  }

  // getStatusText= () =>{
  //   switch (this.state.playStatus) {
  //     case Sound.status.PLAYING:
  //       return 'playing';
  //     case Sound.status.PAUSED:
  //       return 'paused';
  //     case Sound.status.STOPPED:
  //       return 'stopped';
  //     default:
  //       return '(unknown)';
  //   }
  // }


  render() {
    const { collapsed, isMobile, logo } = this.props;
    const {playstate } = this.state;
    debugger
    return (
      <div className={styles.header}>
        {
          playstate == 'PLAYING' && (
            <Sound
            url="https://xinhang618.com/static/my/news.mp3"
            playStatus='PLAYING'
            playFromPosition={300 /* in milliseconds */}
            position={this.state.position}
            onLoading={this.handleSongLoading}
            onPlaying={this.handleSongPlaying}
            onFinishedPlaying={this.handleSongFinishedPlaying}
        />
          )
        }
       
     
        {isMobile && (
          <Link to="/" className={styles.logo} key="logo">
            <img src={logo} alt="logo" width="32" />
          </Link>
        )}
        <span className={styles.trigger} onClick={this.toggle}>
          <LegacyIcon type={collapsed ? 'menu-unfold' : 'menu-fold'} />
        </span>
       <span >
       信航业务管理系统
         </span>
        <RightContent {...this.props} />
      </div>
    );
  }
}
