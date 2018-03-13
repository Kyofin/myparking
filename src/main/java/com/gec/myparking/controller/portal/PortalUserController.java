package com.gec.myparking.controller.portal;

import com.gec.myparking.AStart.SVGAdapter;
import com.gec.myparking.controller.UserController;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.service.ParkingOrderService;
import com.gec.myparking.service.ParkingPortService;
import com.gec.myparking.service.QiuNiuService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.MyparkingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("portal/user")
public class PortalUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PortalUserController.class);

	@Autowired
	private QiuNiuService qiuNiuService;

	@Autowired
	private UserService userService;

	@Autowired
	private ParkingPortService portService;


	@RequestMapping("loginPage")
	public String loginPage() {
		return "portal/login";
	}

	@RequestMapping("indexPage")
	public String indexPage() {
		return "portal/index";
	}

	@RequestMapping("registerPage")
	public String registerPage() {
		return "portal/register";
	}

	/**
	 * 预订车位的页面（显示各种车位状态，不同状态车位触发事件不一样。）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("bookPortPage")
	public String bookPortPage(Model model) {
		//获取初始化svg图像
		String initSVG = portService.showPortInfoSVG();

		//将svg图的结果放入
		model.addAttribute("svg", initSVG);

		//判断车位状态添加CSS
		String[] usedPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_USED);
		String[] emptyPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_EMPTY);
		String[] bookingPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_BOOKING);

		//emptyCssSB
		StringBuilder emptyCssSB = new StringBuilder();
		String emptyCssResult = getCssResult(emptyPortNameList, emptyCssSB, "#00cc00");
		model.addAttribute("emptyCss", emptyCssResult);

		//usedCssSB
		StringBuilder usedCssSB = new StringBuilder();
		String usedCssResult = getCssResult(usedPortNameList, usedCssSB, "#585858");
		model.addAttribute("usedCssSB", usedCssResult);

		//bookingCssSB
		StringBuilder bookingCssSB = new StringBuilder();
		String bookingCssResult = getCssResult(bookingPortNameList, bookingCssSB, "#FAFA01");
		model.addAttribute("bookingCssSB", bookingCssResult);

		//增加js事件 todo

		//转发视图
		return "portal/bookPortPage";
	}


	/**
	 * 根据输入点获取路径
	 *
	 * @param beginPortName
	 * @param endPortName
	 * @param model
	 * @return
	 */
	@RequestMapping("getPath")
	public String getPath(@RequestParam(value = "beginPortName",required = false) String beginPortName,
						  @RequestParam("endPortName") String endPortName,
						  Model model) {


		String path = portService.showPath(beginPortName, endPortName);

		System.out.println(path);

		model.addAttribute("path",path);

		//判断车位状态添加CSS
		String[] usedPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_USED);
		String[] emptyPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_EMPTY);
		String[] bookingPortNameList = portService.getPortNameListByStatus(MyparkingUtil.PORT_STATUS_BOOKING);

		//emptyCssSB
		StringBuilder emptyCssSB = new StringBuilder();
		String emptyCssResult = getCssResult(emptyPortNameList, emptyCssSB, "#00cc00");
		model.addAttribute("emptyCss", emptyCssResult);

		//usedCssSB
		StringBuilder usedCssSB = new StringBuilder();
		String usedCssResult = getCssResult(usedPortNameList, usedCssSB, "#585858");
		model.addAttribute("usedCssSB", usedCssResult);

		//bookingCssSB
		StringBuilder bookingCssSB = new StringBuilder();
		String bookingCssResult = getCssResult(bookingPortNameList, bookingCssSB, "#FAFA01");
		model.addAttribute("bookingCssSB", bookingCssResult);


		//转发视图
		return "portal/getPath";
	}

	private String getCssResult(String[] portNameList, StringBuilder CssSB, String color) {
		for (int i = 0; i < portNameList.length; i++) {
			CssSB.append("#" + portNameList[i]);
			if (i == portNameList.length - 1) {
				CssSB.append("{fill:" + color + ";}");
				continue;
			}
			CssSB.append(",");
		}
		return CssSB.toString();
	}

	/**
	 * 用户在门户注册个人账户
	 *
	 * @param username
	 * @param password
	 * @param email
	 * @param response
	 * @return todo  未完成注册用户
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String reg(@RequestParam("username") String username,
					  @RequestParam("password") String password,
					  @RequestParam("email") String email,
					  @RequestParam("headUrl") String headUrl,
					  HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<>();

			map = userService.register(username, password, email, headUrl);

			if (map.containsKey("ticket")) {
				//将ticket保存到客户端以此做票据
				LoginTicket ticket = (LoginTicket) map.get("ticket");
				Cookie cookie = new Cookie("ticket", ticket.getTicket());
				cookie.setPath("/");  //全网有效
				response.addCookie(cookie);

				return MyparkingUtil.getJsonString(0, map, "注册成功");
			}
			return MyparkingUtil.getJsonString(1, map, "注册失败"); //注册失败，返回错误信息
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return MyparkingUtil.getJsonString(1, "发生异常，注册失败");
		}
	}


	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			//图片保存到服务器
			String fileUrl = qiuNiuService.saveImageToQiuNiu(file);
			//String fileUrl = newsService.saveImage(file);
			if (fileUrl == null) {
				return MyparkingUtil.getJsonString(1, "上传图片失败");
			}
			return MyparkingUtil.getJsonString(0, fileUrl);
		} catch (Exception e) {
			LOGGER.error("上传图片异常");
			return MyparkingUtil.getJsonString(1, "上传图片失败");
		}
	}


}
