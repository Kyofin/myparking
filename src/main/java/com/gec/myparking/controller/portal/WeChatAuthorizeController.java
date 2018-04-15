package com.gec.myparking.controller.portal;

import com.alibaba.fastjson.JSONObject;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.domain.User;
import com.gec.myparking.service.LoginTicketService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.service.WebSocket;
import com.gec.myparking.util.Constant;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("portal/user")
public class WeChatAuthorizeController {

	@Autowired
	WxMpService wxMpService;

	@Autowired
	UserService userService;

	@Autowired
	LoginTicketService loginTicketService;

	@Autowired
	WebSocket webSocket;



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
		String url = "http://abc.21java.xyz/portal/user/getUserInfo";
		//构造发送到微信服务器的网页授权url
		String s = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
		return "redirect:" + s;
	}

	@RequestMapping("getUserInfo")
	public String getUserInfo(@RequestParam String code, HttpServletResponse response) throws WxErrorException {

		try {
			//当用户同意授权后，会回调所设置的url并把authorization code传过来，
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			// 然后用这个code获得access token，其中也包含用户的openid等信息
			WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);


			//获取数据库该用户名是否存在，不存在则注册
			String userName = wxMpUser.getOpenId();
			String headUrl = wxMpUser.getHeadImgUrl();
			String nickName = wxMpUser.getNickname();

			User user = userService.getUserByUserName(userName);
			Integer userId = null;
			if (user ==null){
				userService.addWxUser(userName,nickName,headUrl);
				userId = userService.getUserByUserName(userName).getId();
			}else {
				userId = user.getId();
				//更新wx资料到数据库的用户资料
				user.setHeadUrl(headUrl);
				user.setNickName(nickName);
				user.setUpdateTime(new Date());
				userService.updateUser(user);
			}

			//发放token
			LoginTicket loginTicket = loginTicketService.getLoginTicket(userId);


			//将ticket保存到客户端以此做票据
			Cookie cookie = new Cookie("ticket", loginTicket.getTicket());
			cookie.setMaxAge(3600*24*1); //默认保留一天（单位为s）
			cookie.setPath("/");  //访问门户有效
			response.addCookie(cookie);



			//跳转用户首页
			return "redirect:/portal/user/indexPage";

		}catch (Exception e){
			e.printStackTrace();
			return "redirect:/portal/user/loginPage";
		}


	}





}
