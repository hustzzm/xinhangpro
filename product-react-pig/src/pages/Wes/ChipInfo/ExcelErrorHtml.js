import React, {PureComponent} from "react";
import {getExcelError} from '@/utils/authority';
class ExcelErrorHtml extends PureComponent {
  state = {
    errHtml: null,
  };

  componentDidMount() {
    let excelError = getExcelError();
    let parse = JSON.parse(excelError);
    const html  = parse.reminderHTML
    // this.setState({errHtml:html})
    document.getElementById("errorHtml").innerHTML = html

    const reminderList = parse.reminderList;
    let list = JSON.parse(reminderList);
    let listData = ''
    for (let i =  0; i<list.length; i++) {
      listData = listData + list[i] + '</br>'
    }
    document.getElementById('errorList').innerHTML = listData
  }
  render() {
    const {errHtml} = this.state

    return (
      <div>
        <div id="errorHtml">

        </div>
        <span style={{textAlign:"left",color:"red",paddingLeft:"10px"}}>Tips：红色为错误标记</span>
        <table id="errorList" style={{margin: "10px 20px 20px 20px",fontSize: "12pt"}}></table>
      </div>
    )
  }
}
export default ExcelErrorHtml;
