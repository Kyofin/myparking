package com.gec.myparking.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyparkingUtil {

    private static  final Logger LOGGER = LoggerFactory.getLogger(MyparkingUtil.class);

    public static final Integer PORT_STATUS_EMPTY=0;
    public  static final Integer PORT_STATUS_USED=1;
    public static final Integer PORT_STATUS_BOOKING=2;

    public static final Integer LOGINTICKET_STATUS_USEFUL=0;
    public static final Integer LOGINTICKET_STATUS_NOTUSE=1;







    public static String getJsonString(int code )
    {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("code",code);
        return  jsonObject.toString();
    }

    public static String getJsonString(int code , String msg)
    {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return  jsonObject.toString();
    }
    public static String getJsonString(int code , Map<String,Object> map,String msg)
    {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("code",code);
        for(Map.Entry<String,Object> entry : map.entrySet())
        {
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        jsonObject.put("msg",msg);
        return  jsonObject.toString();
    }


    /**
     * 获取两个时间的间隔
     * @param endDate
     * @param beginDate
     * @return
     */
    public static long getDatePoor(Date endDate, Date beginDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - beginDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
         long sec = diff % nd % nh % nm / ns;
        return sec;
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error("生成MD5失败", e);
            return null;
        }


    }
}
