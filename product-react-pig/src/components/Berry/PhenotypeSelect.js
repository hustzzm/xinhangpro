import {Checkbox, Col, Input, Row} from "antd";
import {CloseOutlined, SearchOutlined} from "@ant-design/icons";
import {CLEAR_TYPE_LIST, SEARCH_TYPE_LIST} from "../../actions/interpretation";
import {useEffect, useImperativeHandle, useRef, useState} from "react";

export default function PhenotypeSelect(props, ref){

    const {queryTypeList,hasSelectTypes, onChangeSelect, dispatch} = props;
    const [showTypeList, setShowList] = useState(false);
    const inType = useRef(null);
    const [selectTypes, setSelectTypes] = useState([]); // 表型信息

    useEffect(()=>{
        setSelectTypes(hasSelectTypes);
    }, [hasSelectTypes]);

    useImperativeHandle(ref, ()=>({
        clearInputType: clearInputType,
    }))
    const handleInputChange= e=>{
        const content = e.target.value.trim();
        if (content.length<1){
            // 清空typelist
            setShowList(false);
            dispatch(CLEAR_TYPE_LIST());
            return;
        }
        if (content.length>1) { // 长度大于1才去查询
            dispatch(SEARCH_TYPE_LIST(content));
            setShowList(true);
        }
    }

    const updateSelectType = target=>{
        const newArr = JSON.parse(JSON.stringify(selectTypes))
        if (target.checked){ // 新增
            newArr.push(target.value);
        } else{ //删除
            const pos = newArr.indexOf(target.value);
            newArr.splice(pos, 1);
        }
        // setSelectTypes(newArr);
        onChangeSelect(newArr);
    }

    const renderSelectList = ()=>{
        return (showTypeList?<div style={{ height:'200px', width:'290px', overflow:'auto',
            position:'absolute',top:'32px', background:'white', zIndex: 99}}>
            {queryTypeList && queryTypeList.length>0? <Checkbox.Group>
                {queryTypeList.map((item, index)=>{
                    return <div key={`div-${index}`}>
                        <Checkbox key={`cb-${index}`} value={item.keywordCn} onChange={e=>updateSelectType(e.target)}>{item.keywordCn}</Checkbox>
                    </div>
                })}
            </Checkbox.Group>:(showTypeList && queryTypeList && queryTypeList.length<1?<span>未找到相关表型</span>:'')}
        </div>:'');
    }

    const clearInputType = ()=>{
        inType.current&&inType.current.state?inType.current.state.value='':'';
    }

    const renderTypeInput = ()=>{
        return (<div style={{marginLeft:'10px',paddingLeft:'10px',height:'100px', border: '1px solid rgb(217,217,217)'}}>
            {selectTypes.map((item,index)=>{
                return (<span key={`sp-${index}`} style={{marginRight:'12px'}}>{item}&nbsp;
                    <CloseOutlined onClick={()=>{
                        const pos = selectTypes.indexOf(item);
                        if (pos>-1){
                            const newArr = JSON.parse(JSON.stringify(selectTypes));
                            newArr.splice(pos, 1);
                            // setSelectTypes(newArr);
                            onChangeSelect(newArr);
                        }
                    }}/></span>);
            })}
        </div>);
    }

    return (<>
        <Row style={{background:'rgb(242, 242, 242)', marginTop:'16px', fontWeight:'600', height:'30px',padding:'5px 0'}}>产前标准表型输入</Row>
        <Row style={{marginTop:'3px'}}>
            <Col style={{position:'relative'}} span={6} onMouseLeave={()=>setShowList(false)}>
                <Input onChange={handleInputChange} placeholder={"请输入查询表型"} addonAfter={<SearchOutlined />} ref={inType}></Input>
                {renderSelectList()}
            </Col>
            <Col span={18}>
                {renderTypeInput()}
            </Col>
        </Row>
        </>);
}