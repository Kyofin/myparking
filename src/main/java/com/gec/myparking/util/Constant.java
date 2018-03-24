package com.gec.myparking.util;

public class Constant {

	public static final String CONTEXT_URL = "http://abc.21java.xyz";

	public static final Integer RESULT_STATUS_SUCCESS = 0;
	public static final Integer RESULT_STATUS_FAIL = 1;

	public interface orderStatus{
		public final Integer ORDER_STATUS_NOPAY = 0;
		public final Integer ORDER_STATUS_PAYED = 1;
		public final Integer ORDER_STATUS_CANCEL = 2;
	}

}
