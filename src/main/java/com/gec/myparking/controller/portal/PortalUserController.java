package com.gec.myparking.controller.portal;

import com.gec.myparking.controller.UserController;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.service.QiuNiuService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.MyparkingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("portal/user")
public class PortalUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PortalUserController.class);

	@Autowired
	private QiuNiuService qiuNiuService;

	@Autowired
	private UserService userService;

	@RequestMapping("loginPage")
	public String loginPage()
	{
		return "portal/login";
	}

	@RequestMapping("registerPage")
	public String registerPage()
	{
		return "portal/register";
	}

	/**
	 * 用户在门户注册个人账户
	 * @param username
	 * @param password
	 * @param email
	 * @param response
	 * @return
	 * todo  未完成注册用户
	 */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	@ResponseBody
	public  String reg(@RequestParam("username") String username,
					   @RequestParam("password") String password,
					   @RequestParam("email") String email,
					   @RequestParam("headUrl") String headUrl,
					   HttpServletResponse response)
	{
		try {
			Map<String,Object> map = new HashMap<>();

			map = userService.register(username,password,email,headUrl);

			if (map.containsKey("ticket"))
			{
				//将ticket保存到客户端以此做票据
				LoginTicket ticket = (LoginTicket) map.get("ticket");
				Cookie cookie = new Cookie("ticket", ticket.getTicket());
				cookie.setPath("/");  //全网有效
				response.addCookie(cookie);

				return MyparkingUtil.getJsonString(0,map,"注册成功");
			}
			return  MyparkingUtil.getJsonString(1,map,"注册失败"); //注册失败，返回错误信息
		}catch (Exception e )
		{
			LOGGER.error(e.getMessage());
			return MyparkingUtil.getJsonString(1,"发生异常，注册失败");
		}
	}


	@RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
	@ResponseBody
	public String uploadImage(@RequestParam("file")MultipartFile file)
	{
		try {
			//图片保存到服务器
			String fileUrl = qiuNiuService.saveImageToQiuNiu(file);
			//String fileUrl = newsService.saveImage(file);
			if (fileUrl==null)
			{
				return  MyparkingUtil.getJsonString(1,"上传图片失败");
			}
			return MyparkingUtil.getJsonString(0,fileUrl);
		}catch (Exception e)
		{
			LOGGER.error("上传图片异常");
			return  MyparkingUtil.getJsonString(1,"上传图片失败");
		}
	}



}
