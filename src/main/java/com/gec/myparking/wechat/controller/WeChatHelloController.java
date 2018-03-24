package com.gec.myparking.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeChatHelloController {

	@Autowired
	WxMpService wxMpService;

	/*@RequestMapping("wechatHello")
	public String hello(Model model) throws WxErrorException {
		model.addAttribute("name", "peter");

		//二维码
		WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature("http://abc.21java.xyz/wechatHello");
		long timestamp = jsapiSignature.getTimestamp();
		String nonceStr = jsapiSignature.getNonceStr();
		String signature = jsapiSignature.getSignature();
		model.addAttribute("timestamp", String.valueOf(timestamp));
		model.addAttribute("nonceStr", nonceStr);
		model.addAttribute("signature", signature);
		System.out.println(wxMpService.getAccessToken());

		System.out.println(signature);

		return "hello";
	}*/

	/**
	 * 构造网页授权url（例如可用a标签）
	 * http://abc.21java.xyz/auth
	 *
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("auth")
	public String auth() throws Exception {

		//设置用户点击授权后跳转的url
		String url = "http://abc.21java.xyz/getUserInfo";
		//构造发送到微信服务器的网页授权url
		String s = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
		return "redirect:" + s;
	}

	@RequestMapping("getUserInfo")
	public String getUserInfo(@RequestParam String code) throws WxErrorException {

		//当用户同意授权后，会回调所设置的url并把authorization code传过来，
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		// 然后用这个code获得access token，其中也包含用户的openid等信息
		WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		// 刷新access token
		//wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
		//	验证access token
		boolean valid = wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
		System.out.println(valid);

		return JSONObject.toJSONString(wxMpUser);

	}


}
