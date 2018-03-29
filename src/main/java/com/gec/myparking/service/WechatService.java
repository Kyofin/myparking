package com.gec.myparking.service;

import com.gec.myparking.util.Constant;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WechatService {

	@Autowired
	WxMpService wxMpService;

	/**
	 * 只能用于微信用户
	 * @param openId
	 * @param portName
	 * @throws WxErrorException
	 */
	public void sendBookPortTemplateMsg(String openId,String portName) throws WxErrorException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
				.toUser(openId)
				.templateId("mXArAbptlXV1MSr0bWo6vKGx80oke9vdsoeBNCrHIKM")
				.url(Constant.CONTEXT_URL+"/portal/user/getPath?endPortName="+portName)
				.build();

		templateMessage
				.addData(new WxMpTemplateData("portname", portName, "#FF7F24"))
				.addData(new WxMpTemplateData("date", dateFormat.format(new Date()), "#FF7F24"));
		String msgId = this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
		System.out.println(msgId);
	}
}
