/*
 * Copyright (C) 2018. Huawei Technologies Co., LTD. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of Apache License, Version 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Apache License, Version 2.0 for more details.
 */
package com.smn.example;

import com.smn.client.DefaultSmnClient;
import com.smn.client.SmnClient;
import com.smn.common.SmsCallbackEventType;
import com.smn.request.sms.*;
import com.smn.response.sms.*;

import java.util.ArrayList;
import java.util.List;

/**
 * sms demo
 *
 * @author zhangyx
 * @version 2.0.5
 */
public class SmsDemo {
    public static void main(String[] args) {
        SmnClient smnClient = new DefaultSmnClient(
                "YourAccountUserName",
                "YourAccountDomainName",
                "YourAccountPassword",
                "YourRegionName");

        // use aksk authentication
//        SmnClient smnClient = new AkskSmnClient(
//                "YourAccessKeyId",
//                "YourSecretAccessKey",
//                "YourRegionName"
//        );

        // 发送通知验证码类短信，send sms
        smsPublish(smnClient);

        // 批量发送通知验证类短信
        batchPublishSmsMessagePublish(smnClient);

        // 批量发送不同内容的通知验证码类短信
        smsBatchPublishWithDiffMessage(smnClient);

        // 批量发送推广类短信
        promotionSmsPublish(smnClient);

        // 创建短信模板
        createSmsTemplate(smnClient);

        // 查询签名 list sms signs
        listSmsSigns(smnClient);

        // delete sms sign
        deleteSmsSign(smnClient);

        //list sms msg report
        listSmsMsgReport(smnClient);

        //get sms message content
        getSmsMessage(smnClient);

        //ListSmsEvent
        listSmsEvent(smnClient);

        // update sms event
        updateSmsEvent(smnClient);

        // list sms templates
        listSmsTemplates(smnClient);

        // get sms template detail
        getSmsTemplateDetail(smnClient);

        // delete sms template
        deleteSmsTemplate(smnClient);
    }

    /**
     * 发送通知验证码类短信
     */
    public static void smsPublish(SmnClient smnClient) {

        // 构造请求对象
        SmsPublishRequest smnRequest = new SmsPublishRequest();

        // 设置参数
        // MessageIncludeSignFlag为true时，可以不传SignId, 但是内容中必须包含签名,如【华为云】您的验证码是:1234，请查收。签名以【】括起来放在内容头部或者尾部
        smnRequest.setEndpoint("+86136*****87")
                .setMessage("您的验证码是:1234，请查收")
                .setSignId("6be340e91e5241e4b5d85837e6709104")
                .setMessageIncludeSignFlag(false);

        // 发送短信
        try {
            SmsPublishResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ",message_id:" + res.getMessageId()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 不同内容批量发送通知类验证码类短信demo
     */
    public static void smsBatchPublishWithDiffMessage(SmnClient smnClient) {
        final SmsPublishMessage message1 = new SmsPublishMessage();
        message1.setSignId("6be340e91e5241e4b5d85837e6709104");
        message1.setMessage("您的验证码是:12345，请查收");
        message1.setEndpoint("136*****587");

        final SmsPublishMessage message2 = new SmsPublishMessage();
        message2.setMessage("【华为企业云】您的验证码是:123456，请查收");
        message2.setEndpoint("13****587");
        // MessageIncludeSignFlag为true时，可以不传SignId, 但是内容中必须包含签名,如【华为云】您的验证码是:1234，请查收。签名以【】括起来放在内容头部或者尾部
        message2.setMessageIncludeSignFlag(true);

        List<SmsPublishMessage> list = new ArrayList<SmsPublishMessage>() {{
            add(message1);
            add(message2);
        }};

        SmsBatchPublishWithDiffMessageRequest request = new SmsBatchPublishWithDiffMessageRequest();
        request.setSmsMessages(list);
        try {
            SmsBatchPublishWithDiffMessageResponse response = smnClient.sendRequest(request);
            if (response.isSuccess()) {
                List<BatchPublishSmsMessageResult> results = response.getResult();
                for (BatchPublishSmsMessageResult result : results) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("messageId='").append(result.getMessageId()).append('\'');
                    sb.append(", endpoint='").append(result.getEndpoint()).append('\'');
                    sb.append(", code='").append(result.getCode()).append('\'');
                    sb.append(", message='").append(result.getMessage()).append('\'');
                    sb.append('}');
                    System.out.println(sb);
                }
            } else {
                System.out.println("httpCode:" + response.getHttpCode()
                        + ", request_id:" + response.getRequestId()
                        + ", errormessage:" + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量发送推广类短信
     */
    public static void promotionSmsPublish(SmnClient smnClient) {
        // 构造请求对象
        PromotionSmsPublishRequest smnRequest = new PromotionSmsPublishRequest();

        // 设置参数
        List<String> endpoints = new ArrayList<String>();
        endpoints.add("8613688807587");

        // 设置参数
        // 请填写已经审批通过的推广类签名id
        smnRequest.setSignId("47f86cf7c9a7449d98ee61cf193a1060")
                .setSmsTemplateId("bfda25c6406e42ddabad74b4a20f6d05")
                .setEndpoints(endpoints);

        // 发送短信
        try {
            PromotionSmsPublishResponse res = smnClient.sendRequest(smnRequest);

            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
            if (res.isSuccess()) {
                for (PromotionSmsPublishResult result : res.getResult()) {
                    StringBuilder sb = new StringBuilder("PromotionSmsPublishResult{");
                    sb.append("messageId='").append(result.getMessageId()).append('\'');
                    sb.append(", endpoint='").append(result.getEndpoint()).append('\'');
                    sb.append(", code='").append(result.getCode()).append('\'');
                    sb.append(", message='").append(result.getMessage()).append('\'');
                    sb.append('}');
                    System.out.println(sb.toString());
                }
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 批量发送通知验证类短信
     */
    public static void batchPublishSmsMessagePublish(SmnClient smnClient) {
        // 构造请求对象
        BatchPublishSmsMessageRequest smnRequest = new BatchPublishSmsMessageRequest();

        // 设置参数
        List<String> endpoints = new ArrayList<String>();
        endpoints.add("86136****587");
        endpoints.add("1598****83");

        // MessageIncludeSignFlag为true时，可以不传SignId, 但是内容中必须包含签名,如【华为云】您的验证码是:1234，请查收。签名以【】括起来放在内容头部或者尾部
        smnRequest.setMessage("您的验证码是:1234，请查收")
                .setSignId("6be340e91e5241e4b5d85837e6709104")
                .setEndpoints(endpoints)
                .setMessageIncludeSignFlag(false);

        // 发送短信
        try {
            BatchPublishSmsMessageResponse res = smnClient.sendRequest(smnRequest);

            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
            if (res.isSuccess()) {
                for (BatchPublishSmsMessageResult result : res.getResult()) {
                    StringBuilder sb = new StringBuilder("BatchPublishSmsMessageResult{");
                    sb.append("messageId='").append(result.getMessageId()).append('\'');
                    sb.append(", endpoint='").append(result.getEndpoint()).append('\'');
                    sb.append(", code='").append(result.getCode()).append('\'');
                    sb.append(", message='").append(result.getMessage()).append('\'');
                    sb.append('}');
                    System.out.println(sb.toString());
                }
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 创建短信模板
     */
    public static void createSmsTemplate(SmnClient smnClient) {
        // 构造请求对象
        CreateSmsTemplateRequest smnRequest = new CreateSmsTemplateRequest();

        //设置参数
        smnRequest.setRemark("helloworld");
        smnRequest.setSmsTemplateContent("拒绝套路，全线低价！快戳http://t.cn/RWafjJ，如有疑问详细4000-955-988转1，退订回TD");
        smnRequest.setSmsTemplateName("拒绝套路1234");
        smnRequest.setSmsTemplateType(1);

        // 发送短信
        try {
            CreateSmsTemplateResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", sms_template_id:" + res.getSmsTemplateId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询短信签名
     */
    public static void listSmsSigns(SmnClient smnClient) {

        // 构造请求对象
        ListSmsSignsRequest smnRequest = new ListSmsSignsRequest();

        // 发送短信
        try {
            ListSmsSignsResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ",smsSignCount:" + res.getSmsSignCount() +
                    ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());

            List<SmsSignInfo> infos = res.getSmsSigns();
            for (SmsSignInfo info : infos) {
                System.out.println("SmsSignInfo{" +
                        "signName='" + info.getSignName() + '\'' +
                        ", createTime='" + info.getCreateTime() + '\'' +
                        ", signId='" + info.getSignId() + '\'' +
                        ", reply='" + info.getReply() + '\'' +
                        ", overseasFlag='" + info.getOverseasFlag() + '\'' +
                        ", status=" + info.getStatus() +
                        '}');
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 删除短信签名
     */
    public static void deleteSmsSign(SmnClient smnClient) {
        // 构造请求对象
        DeleteSmsSignRequest smnRequest = new DeleteSmsSignRequest();

        //设置参数
        smnRequest.setSignId("f492dc25626641a6bcc3d0bcb3fde280");

        // 发送短信
        try {
            DeleteSmsSignResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询短信的发送状态
     */
    public static void listSmsMsgReport(SmnClient smnClient) {
        // 构造请求对象
        ListSmsMsgReportRequest smnRequest = new ListSmsMsgReportRequest();

        //设置参数
        smnRequest.setStartTime("1515154713850")
                .setEndTime("1515327513000")
                .setLimit(10)
                .setMobile("13688807597");

        // 发送短信
        try {
            ListSmsMsgReportResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询已发送短信的内容
     *
     * @param smnClient
     */
    public static void getSmsMessage(SmnClient smnClient) {
        // 构造请求对象
        GetSmsMessageRequest smnRequest = new GetSmsMessageRequest();

        //设置参数
        smnRequest.setMessageId("58222f25aa974c1887c1e08c2f412806");

        // 发送短信
        try {
            GetSmsMessageResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", message" + res.getMessage()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询短信回调事件
     */
    public static void listSmsEvent(SmnClient smnClient) {
        // 构造请求对象
        ListSmsEventRequest smnRequest = new ListSmsEventRequest();

        //设置参数
        //smnRequest.setEventType("sms_reply_event");

        // 发送短信
        try {
            ListSmsEventResponse res = smnClient.sendRequest(smnRequest);
            List<SmsCallbackInfo> infos = res.getCallback();
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", message" + res.getMessage()
                    + ", errormessage:" + res.getMessage());

            for (SmsCallbackInfo info : infos) {
                System.out.println("{" +
                        "eventType='" + info.getEventType() + '\'' +
                        ", topicUrn='" + info.getTopicUrn() + '\'' +
                        '}');
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 更新短信回调事件
     */
    public static void updateSmsEvent(SmnClient smnClient) {
        // 构造请求对象
        UpdateSmsEventRequest smnRequest = new UpdateSmsEventRequest();

        //设置参数, 添加回调事件参数
        List<SmsCallback> callbacks = new ArrayList<SmsCallback>();
        SmsCallback smsCallback = new SmsCallback();
        smsCallback.setEventType(SmsCallbackEventType.SMS_CALLBACK_SUCCESS);
        smsCallback.setTopicUrn("urn:smn:cn-north-1:cffe4fc4c9a54219b60dbaf7b586e132:topic_test_v1");

        callbacks.add(smsCallback);
        smnRequest.setCallbacks(callbacks);

        // 发送短信
        try {
            UpdateSmsEventResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询短信模板列表
     */
    public static void listSmsTemplates(SmnClient smnClient) {
        // 构造请求对象
        ListSmsTemplatesRequest smnRequest = new ListSmsTemplatesRequest();

        // 设置参数
        smnRequest.setLimit(20)
                .setOffset(0)
                .setSmsTemplateType(1);

        // 发送短信
        try {
            ListSmsTemplatesResponse res = smnClient.sendRequest(smnRequest);
            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", count:" + res.getCount()
                    + ", errormessage:" + res.getMessage());

            for (SmsTemplate smsTemplate : res.getSmsTemplates()) {
                StringBuilder sb = new StringBuilder("SmsTemplate{");
                sb.append("smsTemplateName='").append(smsTemplate.getSmsTemplateName()).append('\'');
                sb.append(", smsTemplateType=").append(smsTemplate.getSmsTemplateType());
                sb.append(", smsTemplateId='").append(smsTemplate.getSmsTemplateId()).append('\'');
                sb.append(", reply='").append(smsTemplate.getReply()).append('\'');
                sb.append(", status=").append(smsTemplate.getStatus());
                sb.append(", createTime='").append(smsTemplate.getCreateTime()).append('\'');
                sb.append(", validityEndTime='").append(smsTemplate.getValidityEndTime()).append('\'');
                sb.append('}');
                System.out.println(sb.toString());
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 查询短信模板详情
     */
    public static void getSmsTemplateDetail(SmnClient smnClient) {
        // 构造请求对象
        GetSmsTemplateDetailRequest smnRequest = new GetSmsTemplateDetailRequest();

        // 设置参数
        smnRequest.setSmsTemplateId("bfda25c6406e42ddabad74b4a20f6d05");

        // 发送请求
        try {
            GetSmsTemplateDetailResponse res = smnClient.sendRequest(smnRequest);

            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage()
                    + ", smsTemplateName:" + res.getSmsTemplateName()
                    + ", smsTemplateType:" + res.getSmsTemplateType()
                    + ", smsTemplateContent:" + res.getSmsTemplateContent()
                    + ", smsTemplateId:" + res.getSmsTemplateId()
                    + ", reply:" + res.getReply()
                    + ", createTime:" + res.getCreateTime()
                    + ", validityEndTime:" + res.getValidityEndTime());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 删除短信模板
     */
    public static void deleteSmsTemplate(SmnClient smnClient) {
        // 构造请求对象
        DeleteSmsTemplateRequest smnRequest = new DeleteSmsTemplateRequest();

        // 设置参数
        smnRequest.setSmsTemplateId("ff5ef17024dd4ff3a79eaac7adf558a8");

        // 发送请求
        try {
            DeleteSmsTemplateResponse res = smnClient.sendRequest(smnRequest);

            System.out.println("httpCode:" + res.getHttpCode()
                    + ", request_id:" + res.getRequestId()
                    + ", errormessage:" + res.getMessage());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }
}
