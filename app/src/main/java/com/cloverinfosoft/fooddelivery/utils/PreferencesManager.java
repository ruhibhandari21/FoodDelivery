package com.cloverinfosoft.fooddelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

	public static final String SHARED_PREFERENCES_NAME = "FoodDeliveryPref";

	/**
	 * Instance
	 */
	private static PreferencesManager preferencesManager = null;

	/**
	 * Shared Preferences
	 */
	private SharedPreferences sharedPreferences;


	private static String UserId = "UserId";
	private static String Firstname = "Firstname";
	private static String Lastname = "Lastname";
	private static String email = "email";
	private static String phone = "phone";
	private static String token = "token";
	private static String deviceIP = "deviceIP";
	private static String selected_cat = "selected_cat";



	public String getselected_cat() {
		return sharedPreferences.getString(selected_cat, "");
	}

	public void setselected_cat(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(selected_cat, userId);
		editor.commit();
	}

	public String getdeviceIP() {
		return sharedPreferences.getString(deviceIP, "");
	}

	public void setdeviceIP(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(deviceIP, userId);
		editor.commit();
	}

	public String gettoken() {
		return sharedPreferences.getString(token, "");
	}

	public void settoken(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(token, userId);
		editor.commit();
	}

	public String getUserId() {
		return sharedPreferences.getString(UserId, "");
	}

	public void setUserId(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(UserId, userId);
		editor.commit();
	}


	public String getFirstname() {
		return sharedPreferences.getString(Firstname, "");
	}

	public void setFirstname(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Firstname, userId);
		editor.commit();
	}


	public String getLastname() {
		return sharedPreferences.getString(Lastname, "");
	}

	public void setLastname(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Lastname, userId);
		editor.commit();
	}


	public String getemail() {
		return sharedPreferences.getString(email, "");
	}

	public void setemail(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(email, userId);
		editor.commit();
	}


	public String getphone() {
		return sharedPreferences.getString(phone, "");
	}

	public void setphone(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(phone, userId);
		editor.commit();
	}




	public void clearPrefrences() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("ACCESS_TOKEN", "");
		editor.putString("ACCESS_TOKEN_SECRET", "");
		editor.clear();
		editor.commit();
	}


	private PreferencesManager(Context context) {
		sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	public static PreferencesManager getInstance(Context context) {
		if (preferencesManager == null) {
			preferencesManager = new PreferencesManager(context);
		}
		return preferencesManager;
	}


}
