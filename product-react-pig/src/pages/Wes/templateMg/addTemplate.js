
import React, { useEffect, useState } from 'react';
import { Form, message, Modal, Input, Select, Upload, Button } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

import { uploadTemplate } from './api';

const AddTemplate = (props) => {
  const { title, refreshTable, isShowAddModal, handleCancelAdd } = props;
  const [AddForm] = Form.useForm();

  const [fileList, setFileList] = useState([]);

  const customRequest = (props) => {
    console.log(props, '==============props');
    if (fileList.length > 1) {
      message.warning('只允许上传一个文件！');
      return false;
    };

    if (props.file.type !== "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {
      message.error('只允许上传.docx文件！');
      return false;
    };
    setFileList([props.file]);
    props.onSuccess(props.file)
    props.file.status = 'down'
  }

  // 删除模板
  const removeFile = () => {
    setFileList([]);
  };

  // 确定
  const handleConfirmAdd = () => {
    AddForm.validateFields().then(async (respone) => {

      const formData = new FormData();
      let addFormData = AddForm.getFieldValue();
      for (const key in addFormData) {
        if (key === 'file') {
          if (addFormData[key].file.type !== "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {
            message.error('请选择报告模板且只允许上传.docx文件！');
            return false;
          };
          formData.append(key, fileList[0]);
        } else {
          formData.append(key, addFormData[key]);
        };
      }
      uploadTemplate(formData).then(res => {
        if (res.success) {
          if (props.positiveDefault) {
            refreshTable(props.positiveDefault);
          } else {
            refreshTable({});
          }
          handleCancelAdd(AddForm);
          setFileList([])
        }
      })
    })
  }
  const handleCancelAdd_ = () => {
    handleCancelAdd(AddForm);
    setFileList([]);
  }
  return (
    <>
      <Modal visible={isShowAddModal} title={'新增模板'} onOk={() => { handleConfirmAdd() }}
        onCancel={() => { handleCancelAdd_() }}>
        <Form form={AddForm}>
          <Form.Item name='templateName' label='模板名称' rules={[{ required: true }]}>
            <Input maxLength={30}></Input>
          </Form.Item>
          <Form.Item name='positive' label='阴性/阳性' rules={[{ required: true }]}>
            <Select>
              <Select.Option value={1} key={1}>阳性</Select.Option>
              <Select.Option value={0} key={0}>阴性</Select.Option>

            </Select>
          </Form.Item>

          <Form.Item name='file' label='报告模板' rules={[{ required: true }]}>
            <Upload
              fileList={fileList}
              onRemove={removeFile}
              onUploadProgress={({ total, loaded }) => {
                onProgress({ percent: Math.round(loaded / total * 100).toFixed(2) }, file);
              }}
              onSuccess={(file) => {
                console.log('onSuccess', file.name);
              }}
              customRequest={customRequest}
              accept=".docx"
            >
              <Button icon={<UploadOutlined />} >选择模板</Button>
            </Upload>
          </Form.Item>
          <Form.Item name='memo' label='模板描述' rules={[{ required: false }]}>
            <Input.TextArea maxLength={200}></Input.TextArea>
          </Form.Item>
        </Form>
      </Modal>
    </>
  )
}

export default AddTemplate