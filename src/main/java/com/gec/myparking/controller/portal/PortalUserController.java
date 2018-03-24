package com.gec.myparking.controller.portal;

import com.gec.myparking.domain.*;
import com.gec.myparking.dto.ParkingOrderDTO;
import com.gec.myparking.service.*;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
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
	private HostHolder hostHolder;

	@Autowired
	private ParkingPortService portService;

	@Autowired
	private CarService carService;

	@Autowired
	private ParkingOrderService orderService;


	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private WebSocket webSocket;


	@RequestMapping("loginPage")
	public String loginPage() {
		return "portal/login";
	}

	@RequestMapping("addCarPage")
	public String addCarPage(Model model) {
		//获取绑定车辆
		List<Car> cars =carService.getCarsByUserId(hostHolder.getUser().getId());
		model.addAttribute("cars",cars);
		model.addAttribute("carUserId",hostHolder.getUser().getId());


		return "portal/addCarPage";
	}

	@RequestMapping("orderPage")
	public String orderPage(Model model) {
		//获取用户关联订单
		List<ParkingOrderDTO> orders = orderService.getOrdersByUserId(hostHolder.getUser().getId());

		model.addAttribute("orders",orders);
		return "portal/orderPage";
	}

	//需要关注公众号！！！！！！！！！
	@RequestMapping("indexPage")
	public String indexPage(Model model)  {

		try {
			//二维码页面js调用
			//调用js的页面
			WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature("http://abc.21java.xyz/portal/user/indexPage");
			long timestamp = jsapiSignature.getTimestamp();
			String nonceStr = jsapiSignature.getNonceStr();
			String signature = jsapiSignature.getSignature();
			model.addAttribute("timestamp", String.valueOf(timestamp));
			model.addAttribute("nonceStr", nonceStr);
			model.addAttribute("signature", signature);



		}catch (WxErrorException e) {
			e.printStackTrace();
		}
		return "portal/index";
	}

	@RequestMapping("registerPage")
	public String registerPage() {
		return "portal/register";
	}

	/**
	 * 预订车位的页面（显示各种车位状态，不同状态车位触发事件不一样。）
	 * 已修复bug：取消原本车位后确认其他车位，显示的路线是之前取消的车位的引导路径
	 * @param model
	 * @return
	 */
	@RequestMapping("bookPortPage")
	public String bookPortPage(Model model)  {
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

		//查询是否已经绑定车位
		List<ParkingPort> portList = portService.getPortsByUserIdAndStatus(hostHolder.getUser().getId(), MyparkingUtil.PORT_STATUS_BOOKING);
		model.addAttribute("portList",portList);

		//二维码页面js调用
		//调用js的页面
		WxJsapiSignature jsapiSignature = null;
		try {
			//LOGGER.error("accesstoken:"+wxMpService.getAccessToken());
			String currentUrl = "http://abc.21java.xyz/portal/user/bookPortPage";
			jsapiSignature = wxMpService.createJsapiSignature(currentUrl);
			//LOGGER.error("jsticket:"+wxMpService.getJsapiTicket());
			//LOGGER.error(jsapiSignature.toString());
			long timestamp = jsapiSignature.getTimestamp();
			String nonceStr = jsapiSignature.getNonceStr();
			String signature = jsapiSignature.getSignature();
			model.addAttribute("timestamp", String.valueOf(timestamp));
			model.addAttribute("nonceStr", nonceStr);
			model.addAttribute("signature", signature);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}


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


		String string =
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
				"        $('"+ StringUtils.join(Arrays.asList(emptyPortNameArray))+"').on('click', confirmFun);";

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

				return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS, map, "注册成功");
			}
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, map, "注册失败"); //注册失败，返回错误信息
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, "发生异常，注册失败");
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
				return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, "上传图片失败");
			}
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS, fileUrl);
		} catch (Exception e) {
			LOGGER.error("上传图片异常");
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, "上传图片失败");
		}
	}



	@RequestMapping("pay/{orderId}")
	@ResponseBody
	public String payOrder(@PathVariable Integer orderId){
		try {
			ParkingOrder order = orderService.getOrdersByOrderId(orderId);
			if (order!=null) {
				orderService.payForOrder(order);
			}
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS,  "支付成功");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, "发生异常，支付失败");
		}
	}


}
