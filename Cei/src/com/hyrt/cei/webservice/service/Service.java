package com.hyrt.cei.webservice.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.vo.UserInfo;
import com.hyrt.cei.webservice.wsdl.AgentServiceSoapBindingImpl;
import com.hyrt.cei.webservice.wsdl.Configuration;

/**
 * 调用webservice
 * 
 */

public class Service {
	private static AgentServiceSoapBindingImpl agent;

	static {
		Configuration.setConfiguration(MyTools.url);
		agent = new AgentServiceSoapBindingImpl();
	}

	public static String initResources(ColumnEntry columnEntry, Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" + "<LOGINNAME>"
					+ columnEntry.getLoginName().trim() + "</LOGINNAME>"
					+ "<PASSWORD>" + columnEntry.getPassword().trim()
					+ "</PASSWORD>" + "<imagetype>pad</imagetype>"
					+ "<IMSITYPE>" + "apad"+ "</IMSITYPE>"
					+ "<IMSICODE>" + (info.getMacAddress() + tm.getDeviceId())
					+ "</IMSICODE></ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "loginUserInfo");
			rs = agent.loginUserInfo(xmlStr);
			Log.i("sys", xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
			e.printStackTrace();
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "loginUserInfo_result");
		return rs;
	}

	public static String initSelfResources(ColumnEntry columnEntry) {
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" + "<USERID>" + columnEntry.getUserId()
					+ "</USERID>" + "<IMAGETYPE>" + "pad" + "</IMAGETYPE>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryBusiness");
			rs = agent.initSelfResources(xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryBusiness_result");
		return rs;
	}

	public static String testFunction(String name, String password) {
		String rs = "";
		try {
			rs = agent.testFunction();
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
		}
		return rs;
	}

	/**
	 * 获取智慧海的业务列表
	 * 
	 * @param user_id
	 * @return
	 */
	public static String witSea(String user_id) {
		String rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
				+ "<functionid>" + user_id + "</functionid > " + "</ROOT>";
		try {
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryBrightness");
			rs = agent.witSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>0000</functionid> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryBrightness_result");
		return rs;
	}

	/**
	 * 修改业务类表中的字段值正确返回业务服务端ID错误返回-1或0；
	 * 
	 * @param versionId
	 * @param type
	 * @return
	 */
	public static String upDateWitSea(String userid, String resourceid) {
		String rs = "<ROOT> <userid>" + userid + "</userid>"
				+ "<buytype>yw</buytype><resourceid>" + resourceid
				+ "</resourceid></ROOT>";
		try {
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveCourse");
			rs = agent.upDateWitSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
			e.printStackTrace();
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveCourse_result");
		return rs;
	}

	/**
	 * 删除智慧海业务
	 * 
	 * @param userid
	 * @param resourceid
	 * @return
	 */
	public static String deleteWitSea(ColumnEntry item) {
		String rs = "<ROOT>" + "<resourceid>" + item.getId() + "</resourceid>"
				+ "<userid>" + item.getUserId() + "</userid>" + "<buytype>"
				+ "yw" + "</buytype>" + "</ROOT>";
		try {
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "delBusiness");
			rs = agent.deleteWitSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "delBusiness_result");
		return rs;
	}

	public static String queryReport(String versionId, String type, String index) {
		/*
		 * String result =
		 * WriteOrRead.read(MyTools.nativeData,Welcome.INITRESOURCES_FILENAME);
		 * String xmlStr1 = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
		 * + "<loginid>" + XmlUtil.parseLoginid(result) + "</loginid>" +
		 * "<functionid>" +versionId + "</functionid>" + "<functionname>" +
		 * "精彩课件" + "</functionname>" + "</ROOT>";
		 */

		String rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
				+ "<id>" + versionId + "</id> " + "<type>" + type + "</type>"
				+ "<index>" + index + "</index>"
				+ "<imagetype>androidpad</imagetype>" + "</ROOT>";

		try {
			// agent.addLog(xmlStr1);
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
					"queryPhoneFunctionTree");
			rs = agent.queryPhoneFunctionTree(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryPhoneFunctionTree_result");
		return rs;
	}

	public static String queryPhoneFunctionTree(String versionId, String type) {
		/*
		 * String result =
		 * WriteOrRead.read(MyTools.nativeData,Welcome.INITRESOURCES_FILENAME);
		 * String xmlStr1 = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
		 * + "<loginid>" + XmlUtil.parseLoginid(result) + "</loginid>" +
		 * "<functionid>" +versionId + "</functionid>" + "<functionname>" +
		 * "精彩课件" + "</functionname>" + "</ROOT>";
		 */
		String rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
				+ "<id>" + versionId + "</id> " 
				+ "<type>" + type + "</type>"
				+ "<imagetype>androidpad</imagetype>" + "</ROOT>";
		try {
			// agent.addLog(xmlStr1);
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
					"queryPhoneFunctionTree");
			rs = agent.queryPhoneFunctionTree(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryPhoneFunctionTree_result");
		return rs;
	}

	public static String queryUserInfo(String s) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + s + "</userid > " + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryUserInfo");
			rs = agent.queryUserInfo(xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryUserInfo_result");
		return rs;
	}

	/**
	 * 查询论坛的列表
	 * 
	 * @param s
	 * @return
	 */
	public static String querySchoolForumInfo(String s) {
		String rs = "";
		try {
			String xmlStr = "<ROOT><userid>" + s
					+ "</userid><buytype>bbs</buytype>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryCourse");
			rs = agent.querySchoolForumInfo(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryCourse_result");
		return rs;
	}

	/**
	 * 查询论坛的列表详细
	 * 
	 * @param s
	 * @return
	 */
	public static String queryBBSFollow(String[] s) {
		String rs = "";
		try {
			String xmlStr = "<ROOT><classid>" + s[0] + "</classid>"
					+ "<functionid>" + s[1] + "</functionid></ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryCourse");
			rs = agent.queryBBSFollow(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryCourse_result");
		return rs;
	}

	/**
	 * 新增论坛的列表
	 * 
	 * @param s
	 * @return
	 */
	public static String saveBBS(Forum forum) {
		String rs = "";
		try {
			String xmlStr = "<ROOT><functionid>" + forum.getFunctionid() + ""
					+ "</functionid ><content>" + forum.getContent()
					+ "</content><userid>" + forum.getUserid()
					+ "</userid><serial>" + forum.getSerial()
					+ "</serial><classid>" + forum.getClassId()
					+ "</classid></ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "saveBBSFollow");
			rs = agent.saveBBS(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveBBSFollow_result");
		return rs;
	}

	/**
	 * 新增论坛详细
	 * 
	 * @param s
	 * @return
	 */
	public static String saveBBSInfo(Forum forum) {
		String rs = "";
		try {
			String xmlStr = "<ROOT><functionid>" + forum.getFunctionid() + ""
					+ "</functionid ><content>" + forum.getContent()
					+ "</content><userid>" + forum.getUserid()
					+ "</userid><serial>" + forum.getSerial()
					+ "</serial><classid>" + forum.getClassId()
					+ "</classid><time>" + forum.getTime() + "</time></ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "saveBBSFollow");
			rs = agent.saveBBS(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveBBSFollow_result");
		return rs;
	}

	/**
	 * 栏目访问日志
	 * 
	 * @param s
	 * @return
	 */
	public static void addLog(ColumnEntry columnEntry) {
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<loginid>" + columnEntry.getLoginid() + "</loginid>"
					+ "<functionid>" + columnEntry.getId() + "</functionid>"
					+ "<functionname>" + columnEntry.getName()
					+ "</functionname>" + "</ROOT>";
			WriteOrRead
					.write(xmlStr, "/mnt/sdcard/yepeng/", "saveOperationlog");
			agent.addLog(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询自选课件
	 */
	public static String queryCourse(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>"
					+ "<buytype>kj</buytype>"
					+ "<imagetype>androidpad</imagetype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryCourse");
			rs = agent.queryCourse(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryCourse_result");
		return rs;
	}

	/**
	 * 注册用户
	 */
	public static String saveUserInfo(UserInfo userInfo) {
		String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证", "护照" };
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(userInfo.getIdType())) {
				userInfo.setIdType(i + "");
			}
		}
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<loginname>"
					+ userInfo.getLoginName()
					+ "</loginname>"
					+ "<password>"
					+ userInfo.getPassword()
					+ "</password>"
					+ "<name>"
					+ userInfo.getName()
					+ "</name>"
					+ "<email>"
					+ userInfo.getEmail()
					+ "</email>"
					+ "<sex>"
					+ userInfo.getSex()
					+ "</sex>"
					+ "<idtype>"
					+ userInfo.getIdType()
					+ "</idtype>"
					+ "<idnum>"
					+ userInfo.getIdNum()
					+ "</idnum>"
					+ "<phonenum>"
					+ userInfo.getPhoneNum() + "</phonenum>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "saveUserInfo");
			rs = agent.saveUserInfo(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveUserInfo_result");
		return rs;
	}

	/**
	 * 添加自选课
	 */
	public static String saveCourse(String classId, String userId) {
		String rs = "";
		if (userId != null) {
			try {
				String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>"
						+ "<ROOT>" + "<resourceid>" + classId + "</resourceid>"
						+ "<buytype>" + "kj" + "</buytype>" + "<userid>"
						+ userId + "</userid>" + "</ROOT>";
				WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "saveCourse");
				rs = agent.saveCourse(xmlStr);
			} catch (Exception e) {
				e.printStackTrace();
				rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
						+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			}
		} else {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveCourse_result");
		return rs;
	}

	public static String cancelCourse(String classId, String userId) {
		String rs = "";
		if (userId != null) {
			try {
				String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>"
						+ "<ROOT>" + "<resourceid>" + classId + "</resourceid>"
						+ "<buytype>" + "kj" + "</buytype>" + "<userid>"
						+ userId + "</userid>" + "</ROOT>";
				WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "delBusiness");
				rs = agent.cancelCourse(xmlStr);
			} catch (Exception e) {
				e.printStackTrace();
				rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
						+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			}
		} else {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "delBusiness_result");
		return rs;
	}

	public static String updateUserInfoPassWord(String loginname, String email) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<loginname>" + loginname + "</loginname>" + "<email>"
					+ email + "</email>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"updateUserInfoPassWord");
			rs = agent.updateUserInfoPassWord(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"updateUserInfoPassWord_result");
		return rs;
	}

	// 增加几个字段
	public static String updatePassWord(String userid, String oldpassword,
			String newpassword, String str_email, String str_card_kind,
			String str_card_num, String str_phone_num) {
		int idType = -1;
		String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证", "护照" };
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(str_card_kind)) {
				idType = i;
			}
		}
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>" + "<oldpassword>"
					+ oldpassword + "</oldpassword>" + "<newpassword>"
					+ newpassword + "</newpassword>" + "<email>" + str_email
					+ "</email>"
					+ (idType == -1 ? "" : "<idtype>" + idType + "</idtype>")
					+ "<idnum>" + str_card_num + "</idnum>" + "<phonenum>"
					+ str_phone_num + "</phonenum>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "updatePassWord");
			rs = agent.updatePassWord(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "updatePassWord_result");
		return rs;
	}

	/**
	 * 通知公告
	 * 
	 * @param id
	 * @param type
	 * @param imagetype
	 * @return
	 */
	public static String queryPhoneFunctionTree(String id, String type,
			String imagetype) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<id>" + id + "</id>" + "<type>" + type + "</type>"
					+ "<imagetype>" + imagetype + "</imagetype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryPhoneFunctionTree");
			rs = agent.queryPhoneFunctionTree(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryPhoneFunctionTree_result");
		return rs;
	}

	public static String queryClassName(String name, String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<classname>" + name + "</classname>" + "<functionids>"
					+ functionids + "</functionids>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			WriteOrRead
					.write(xmlStr, "/mnt/sdcard/yepeng/", "queryClassByName");
			rs = agent.queryClassName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryClassByName_result");
		return rs;
	}

	public static String queryClassByTime(String versionId, String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<funid>" + versionId + "</funid>" + "<functionids>"
					+ functionids + "</functionids>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			WriteOrRead
					.write(xmlStr, "/mnt/sdcard/yepeng/", "queryClassByTime");
			rs = agent.queryClassByTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryClassByTime_result");
		return rs;
	}

	public static String queryClassByType(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + functionids + "</functionids>"
					+ "</ROOT>";
			WriteOrRead
					.write(xmlStr, "/mnt/sdcard/yepeng/", "queryClassByType");
			rs = agent.queryClassByType(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryClassByType");
		return rs;
	}

	public static String queryClassTypeByClass(String id, int index) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<id>" + id + "</id>" + "<imagetype>" + "androidpad"
					+ "</imagetype>" + "<index>" + index + "</index>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryClassTypeByClass");
			rs = agent.queryClassTypeByClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryClassTypeByClass_result");
		return rs;
	}

	public static String queryBuyClass(String userId, String classIds) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userId>" + userId + "</userId>" + "<classIds>"
					+ classIds + "</classIds>" + "<imagetype>" + "androidpad"
					+ "</imagetype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryMoneyClass");
			rs = agent.queryMoneyClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryMoneyClass_result");
		return rs;
	}

	/**
	 * 政经资讯首页左上部
	 * 
	 * @param userId
	 * @return
	 */
	public static String queryNewsImage(String functionid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionid + "</functionids >"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsImage");
			rs = agent.queryNewsImage(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryNewsImage_result");
		return rs;
	}

	/**
	 * 政经资讯首页最新资讯
	 * 
	 * @param functionids
	 * @return
	 */
	public static String queryNewsList(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionids + "</functionids >"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsList");
			rs = agent.queryNewsList(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryNewsList_result");
		return rs;
	}

	/**
	 * 政经资讯首页其他业务查询信息列表
	 * 
	 * @param functionid
	 *            业务id
	 * @param 控制个数
	 * @return
	 */
	public static String queryNewsByFunctionId(String functionid, String num,
			String userId) {
		/*
		 * String result =
		 * WriteOrRead.read(MyTools.nativeData,Welcome.INITRESOURCES_FILENAME);
		 * String xmlStr1 = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
		 * + "<loginid>" + XmlUtil.parseLoginid(result) + "</loginid>" +
		 * "<functionid>" +functionid + "</functionid>" + "<functionname>" +
		 * "精彩课件" + "</functionname>" + "</ROOT>";
		 */
		String rs = "";
		try {
			// agent.addLog(xmlStr1);
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>" + functionid + "</functionid>" + "<num>"
					+ num + "</num>" + "<userId>" + userId + "</userId>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryNewsByFunctionId");
			rs = agent.queryNewsByFunctionId(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryNewsByFunctionId_result");
		return rs;
	}

	/**
	 * 政经资讯按名称查询信息queryNewsByName
	 * 
	 * @param functionid
	 * @param num
	 * @return
	 */
	public static String queryNewsByName(String functionids, String titlename) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionids + "</functionids >"
					+ "<titlename >" + titlename + "</titlename >" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsByName");
			rs = agent.queryNewsByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryNewsByName_result");
		return rs;
	}

	/**
	 * 新增资讯收藏信息saveCoolect
	 * 
	 * @param functionid
	 * 
	 * @param functionids
	 * @param titlename
	 * @return
	 */
	public static String saveCoolect(String userid, String resourceid,
			String functionid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid >" + userid + "</userid >" + "<resourceid  >"
					+ resourceid + "</resourceid  >" + "<functionid  >"
					+ functionid + "</functionid  >" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "saveCoolect");
			rs = agent.saveCoolect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "saveCoolect_result");
		return rs;
	}

	/**
	 * 查询资讯收藏信息queryCollect
	 * 
	 * @param userid
	 * @param resourceid
	 * @return
	 */
	public static String queryCollect(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid >" + userid + "</userid >" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryCollect");
			rs = agent.queryCollect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryCollect_result");
		return rs;
	}

	/**
	 * 查询阅读报告分类列表
	 */
	public static String queryReportPartition(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + functionids + "</functionids>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryReportByType");
			rs = agent.queryReportByType(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead
				.write(rs, "/mnt/sdcard/yepeng/", "queryReportByType_result");
		return rs;
	}

	/**
	 * 清空资讯收藏信息clearCollect
	 * 
	 * @param userid
	 * @return
	 */
	public static String clearCollect(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid >" + userid + "</userid >" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "clearCollect");
			rs = agent.clearCollect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "clearCollect_result");
		return rs;
	}

	/**
	 * 查询是否购买过此课件
	 */
	public static String queryMoneyClass(String userid, String classIds) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>" + "<classids>"
					+ classIds + "</classids>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryMoneyClass");
			rs = agent.queryMoneyClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryMoneyClass_result");
		return rs;
	}

	/**
	 * 查询所有免费报告
	 */
	/*
	 * public static String queryAllFreeReport(String number) { String rs = "";
	 * try { String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
	 * + "<num>" + number + "</num>" + "<imagetype>androidpad</imagetype>" +
	 * "</ROOT>"; rs = agent.queryAllFreeReport(xmlStr); } catch (Exception e) {
	 * e.printStackTrace(); rs = "<?xml version='1.0' encoding='UTF-8'?>" +
	 * "<ROOT>" + "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>"; } return rs; }
	 */
	public static String queryAllFreeReport(String ids, String number,
			String reportName) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + ids + "</functionids>" + "<num>"
					+ number + "</num>" + "<imagetype>androidpad</imagetype>"
					+ "<isdownsum>y</isdownsum>" + "<isfree>1</isfree>"
					+ "<reportname>" + reportName + "</reportname>" + "</ROOT>";
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryReportByName");
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead
				.write(rs, "/mnt/sdcard/yepeng/", "queryReportByName_result");
		return rs;
	}

	/**
	 * 查询所有排行报告
	 */
	public static String queryReportByName(String ids, String number,
			String reportName) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + ids + "</functionids>" + "<num>"
					+ number + "</num>" + "<imagetype>androidpad</imagetype>"
					+ "<isdownsum>y</isdownsum>" + "<reportname>" + reportName
					+ "</reportname>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryReportByName");
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead
				.write(rs, "/mnt/sdcard/yepeng/", "queryReportByName_result");
		return rs;
	}

	/**
	 * 查询所有排行报告
	 */
	public static String queryNewReport(String ids, String number) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + ids + "</functionids>" + "<num>"
					+ number + "</num>" + "<imagetype>androidpad</imagetype>"
					+ "<isnew>" + 1 + "</isnew>" + "<reportname></reportname>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryReportByName");
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead
				.write(rs, "/mnt/sdcard/yepeng/", "queryReportByName_result");
		return rs;
	}

	/**
	 * 取得某一分类下的所有报告列表
	 * 
	 * @param ids
	 * @param number
	 * @param reportName
	 * @return
	 */
	public static String queryAllClassTypeReport(String id, String number) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<reporttypeid>" + id + "</reporttypeid>" + "<num>"
					+ number + "</num>" + "<imagetype>androidpad</imagetype>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryAllClassTypeReport");
			rs = agent.queryAllClassTypeReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryAllClassTypeReport_result");
		return rs;
	}

	public static String querydbsImage(String functionid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionid + "</functionids >"
					+ "<newstype>db</newstype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsImage");
			rs = agent.queryNewsImage(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryNewsImage_result");
		return rs;
	}

	public static String querydbsList(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionids + "</functionids >"
					+ "<newstype>db</newstype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsList");
			rs = agent.queryNewsList(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryNewsList_result");
		return rs;
	}

	public static String querydbsByFunctionId(String functionid, String num) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>" + functionid + "</functionid>" + "<num>"
					+ num + "</num>" + "<newstype>db</newstype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryNewsByFunctionId");
			rs = agent.queryNewsByFunctionId(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryNewsByFunctionId_result");
		return rs;
	}

	public static String queryBuyNews(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>" + "</ROOT>";
			rs = agent.queryBuyNews(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryPassKey(String portPaths) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<paths>" + portPaths + "</paths>" + "</ROOT>";
			rs = agent.queryPassKey(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryNotice(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>" + "</ROOT>";
			rs = agent.queryNotice(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	/**
	 * 查询购买的经济数据
	 */
	public static String queryBuyDbNews(String userid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>"
					+ "<resourcetype>db</resourcetype>" + "</ROOT>";
			rs = agent.queryBuyNews(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String delCollect(String userId, String resourceid,
			String functionid) {
		// TODO Auto-generated method stub
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid >" + userId + "</userid >" + "<resourceid  >"
					+ resourceid + "</resourceid  >" + "<functionid  >"
					+ functionid + "</functionid  >" + "</ROOT>";
			rs = agent.delCollect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	/**
	 * 获取学习记录列表
	 */
	public static String queryUserClassTime(String userId) {
		// TODO Auto-generated method stub
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userId + "</userid>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"queryUserClassTime");
			rs = agent.queryUserClassTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryUserClassTime_result");
		return rs;
	}

	public static String queryBuyReport(String userId) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userId + "</userid>" + "</ROOT>";
			WriteOrRead
					.write(xmlStr, "/mnt/sdcard/yepeng/", "queryMoneyReport");
			rs = agent.queryBuyReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryMoneyReport_result");
		return rs;
	}

	public static String saveUserClassTime(String userId, String classId,
			String studyTime) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userId + "</userid>" + "<classid>" + classId
					+ "</classid>" + "<time>" + studyTime + "</time>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/",
					"saveUserClassTime");
			rs = agent.saveUserClassTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead
				.write(rs, "/mnt/sdcard/yepeng/", "saveUserClassTime_result");
		return rs;
	}

	public static String queryPassKeyBuyReport(String paths) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<paths>" + paths + "</paths>" + "</ROOT>";
			WriteOrRead
					.write(rs, "/mnt/sdcard/yepeng/", "queryPassKeyByReport");
			rs = agent.queryPassKeyBuyReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/",
				"queryPassKeyByReport_result");
		return rs;
	}

	/* 下载记录 */
	public static void updatedownsum(String id, String type) {
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" + "<id>" + id + "</id>" + "<type>" + type
					+ "</type>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "updatedownsum");
			agent.updatedownsum(xmlStr);
			Log.i("sys", xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 报告属性获取 */
	public static String queryReportName(String id) {
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" + "<id>" + id + "</id>" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "updatedownsum");
			rs = agent.queryReportName(xmlStr);
			Log.i("sys", xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<id>0</id>";
			e.printStackTrace();
		}
		return rs;
	}
	/*更新学习状态*/
	public static String saveUserClassTime(String userid, Courseware courseware) {
		String result = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" 
					+ "<userid>"+userid + "</userid>"
					+ "<classid>"+ courseware.getClassId()+"</classid>"
					+ "<time>" + courseware.getUploadTime()+"</time>"
					+ "<iscompleted>" + courseware.getIscompleted()+"</iscompleted>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "updateUserClassTime");
			result = agent.saveUserClassTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据版本id获取门户网站地址
	 * @param id
	 * @return
	 */
	public static String queryFunctionAddress(String id) {
		String result = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" 
					+ "<id>"+id + "</id>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryFunctionAddress");
			result = agent.queryFunctionAddress(xmlStr);
		} catch (Exception e) {
			result = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>";
			e.printStackTrace();
		}
		return result;
	}

	/* 系统更新 */
	public static String queryApkList(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "" + "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryApkList");
			rs = agent.queryApkList(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "queryApkList_result");
		return rs;
	}
}
