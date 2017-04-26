package com.share.json;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.share.exception.BusinessException;

public class GsonUtil {

	/**
	 * formJson
	 * 
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static <T> T fromJson(String json, Type typeOfT) {
		T t = null;
		try {
			t = new Gson().fromJson(json, typeOfT);
		} catch (Exception e) {
			throw new BusinessException("00000015", e, null);
		}
		return t;
	}

	/**
	 * to json
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}

	/**
	 * to json
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj, Gson gson) {
		return gson.toJson(obj);
	}

}
