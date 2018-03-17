package com.gec.myparking.controller.portal;

import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.service.ParkingPortService;
import com.gec.myparking.service.QiuNiuService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.MyparkingUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
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
		String[] usedPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_USED);
		String[] emptyPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_EMPTY);
		String[] bookingPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_BOOKING);

		//emptyCssSB
		StringBuilder emptyCssSB = new StringBuilder();
		String emptyCssResult = getCssResult(emptyPortNameArray, emptyCssSB, "#00cc00");
		model.addAttribute("emptyCss", emptyCssResult);

		//usedCssSB
		StringBuilder usedCssSB = new StringBuilder();
		String usedCssResult = getCssResult(usedPortNameArray, usedCssSB, "#585858");
		model.addAttribute("usedCssSB", usedCssResult);

		//bookingCssSB
		StringBuilder bookingCssSB = new StringBuilder();
		String bookingCssResult = getCssResult(bookingPortNameArray, bookingCssSB, "#FAFA01");
		model.addAttribute("bookingCssSB", bookingCssResult);

		//增加js事件
		String jsResult = getJsResult(usedPortNameArray,bookingPortNameArray,emptyPortNameArray);
		model.addAttribute("jsResult",jsResult);

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
		String[] usedPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_USED);
		String[] emptyPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_EMPTY);
		String[] bookingPortNameArray = portService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_BOOKING);

		//emptyCssSB
		StringBuilder emptyCssSB = new StringBuilder();
		String emptyCssResult = getCssResult(emptyPortNameArray, emptyCssSB, "#00cc00");
		model.addAttribute("emptyCss", emptyCssResult);

		//usedCssSB
		StringBuilder usedCssSB = new StringBuilder();
		String usedCssResult = getCssResult(usedPortNameArray, usedCssSB, "#585858");
		model.addAttribute("usedCssSB", usedCssResult);

		//bookingCssSB
		StringBuilder bookingCssSB = new StringBuilder();
		String bookingCssResult = getCssResult(bookingPortNameArray, bookingCssSB, "#FAFA01");
		model.addAttribute("bookingCssSB", bookingCssResult);


		//转发视图
		return "portal/getPath";
	}


	private String getJsResult (String[] usedPortNameArray,String[] bookedPortNameArray,String[] emptyPortNameArray){
		for (int i = 0; i < usedPortNameArray.length; i++) {
			usedPortNameArray[i] = "#"+usedPortNameArray[i];
		}
		for (int i = 0; i < bookedPortNameArray.length; i++) {
			bookedPortNameArray[i] = "#"+bookedPortNameArray[i];
		}
		for (int i = 0; i < emptyPortNameArray.length; i++) {
			emptyPortNameArray[i] = "#"+emptyPortNameArray[i];
		}


		String string = "<script>\n" +
				"    $(function () {\n" +
				"        //定义提示模态框\n" +
				"        var infoModal = $('#info');\n" +
				"        //定义loading模态框\n" +
				"        var loadingModal = $('#my-modal-loading');\n" +
				"\n" +
				"        //绑定被预定车位的模态框事件\n" +
				"        $('"+ StringUtils.join(Arrays.asList(bookedPortNameArray))+"').on('click', function (e) {\n" +
				"            //更改模态框中提示信息\n" +
				"            $(\"#info\").find(\".am-modal-bd\").text(\"该车位已经被预定\");\n" +
				"            infoModal.modal('toggle');\n" +
				"        });\n" +
				"        //绑定被使用车位的模态框事件\n" +
				"        $('"+ StringUtils.join(Arrays.asList(usedPortNameArray))+"').on('click', function (e) {\n" +
				"            //更改模态框中提示信息\n" +
				"            $(\"#info\").find(\".am-modal-bd\").text(\"该车位已经被使用，请选择其他车位\");\n" +
				"            infoModal.modal('toggle');\n" +
				"        });\n" +
				"\n" +
				"        //绑定可使用车位的模态框事件\n" +
				"        $('"+ StringUtils.join(Arrays.asList(emptyPortNameArray))+"').on('click', function () {\n" +
				"            var carPortName = this.id;\n" +
				"\n" +
				"            //更改模态框中提示信息\n" +
				"            $(\"#confirmInfo\").find(\".am-modal-bd\").text(\"你确定要预约\" + carPortName + \"车位吗？\");\n" +
				"\n" +
				"            $('#confirmInfo').modal({\n" +
				"                relatedTarget: this,\n" +
				"                onConfirm: function (options) {\n" +
				"                    //todo 发送预定车位请求\n" +
				"                    $.post(\"/parkingport/book/\" + carPortName, {}, function (res) {\n" +
				"                        var resultObject = JSON.parse(res);\n" +
				"\n" +
				"                        //预定成功跳转页面显示路径\n" +
				"                        if (resultObject.code == 0) {\n" +
				"                            infoModal.find(\".am-modal-bd\").text(resultObject.msg + \"，即将为你显示引导路径！\");\n" +
				"                            infoModal.modal('toggle');\n" +
				"                            $(\"#info\").find(\".am-modal-btn\").on(\"click\", function () {\n" +
				"                                loadingModal.modal(\"open\")\n" +
				"                                setTimeout( function () {\n" +
				"                                    window.location.href = \"/portal/user/getPath?endPortName=\" + carPortName;\n" +
				"\n" +
				"                                },1000)\n" +
				"                            })\n" +
				"                        } else {\n" +
				"                            //弹出失败结果信息\n" +
				"                            $(\"#info\").find(\".am-modal-bd\").text(resultObject.error);\n" +
				"                            infoModal.modal('toggle');\n" +
				"                        }\n" +
				"\n" +
				"                    });\n" +
				"\n" +
				"\n" +
				"                },\n" +
				"                // closeOnConfirm: false,\n" +
				"                closeViaDimmer: false,\n" +
				"                onCancel: function () {\n" +
				"                    //alert('不弄了');\n" +
				"                }\n" +
				"            });\n" +
				"        });\n" +
				"    });\n" +
				"</script>";

		return string;
	}

	private String getCssResult(String[] portNameArray, StringBuilder CssSB, String color) {
		for (int i = 0; i < portNameArray.length; i++) {
			CssSB.append("#" + portNameArray[i]);
			if (i == portNameArray.length - 1) {
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
