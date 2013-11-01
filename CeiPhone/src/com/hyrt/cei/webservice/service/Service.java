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
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" + "<LOGINNAME>"
					+ columnEntry.getLoginName().trim() + "</LOGINNAME>"
					+ "<PASSWORD>" + columnEntry.getPassword().trim()
					+ "</PASSWORD>" + "<imagetype>phone</imagetype>"
					+ "<IMSITYPE>" + "aphone"+ "</IMSITYPE>"
					+ "<IMSICODE>" + (info.getMacAddress()+tm.getDeviceId())+ "</IMSICODE></ROOT>";
			rs = agent.loginUserInfo(xmlStr);
			WriteOrRead.write(rs, "/mnt/sdcard/yepeng/", "initResources_main");
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
			e.printStackTrace();

		}
		return rs;
	}

	public static String initSelfResources(ColumnEntry columnEntry) {
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" 
					+ "<USERID>" + columnEntry.getUserId() + "</USERID>"
					+ "<IMAGETYPE>" + "phone" + "</IMAGETYPE>"
					+ "</ROOT>";
			rs = agent.initSelfResources(xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
		}
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
			rs = agent.witSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>0000</functionid> " + "</ROOT>";
		}
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
			rs = agent.upDateWitSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE>" + "</ROOT>";
			e.printStackTrace();
		}
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
		String rs = "<ROOT>" 
							+ "<resourceid>" + item.getId() + "</resourceid>"
							+ "<userid>" + item.getUserId() + "</userid>"
							+ "<buytype>" + "yw" + "</buytype>" +
						  "</ROOT>";
		try {
			rs = agent.deleteWitSea(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryReport(String versionId, String type,String index) {
		String rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
				+ "<id>" + versionId + "</id> " + "<type>" + type + "</type>"
				+"<index>"+index+"</index>"
				+ "<imagetype>androidpad</imagetype>" + "</ROOT>";

		try {
			rs = agent.queryPhoneFunctionTree(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}

		return rs;
	}
	
	public static String queryPhoneFunctionTree(String versionId, String type) {
		String rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
				+ "<id>" + versionId + "</id> " + "<type>" + type + "</type>"
				+ "<imagetype>androidpad</imagetype>" + "</ROOT>";

		try {
			rs = agent.queryPhoneFunctionTree(rs);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}
		return rs;
	}

	public static String queryUserInfo(String s) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + s + "</userid > " + "</ROOT>";
			rs = agent.queryUserInfo(xmlStr);
		} catch (Exception e) {
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
			e.printStackTrace();
		}
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
					+ "</userid><buytype>bbs</buytype>" 
					+ "<imagetype>androidpad</imagetype>" +
					"</ROOT>";
			rs = agent.querySchoolForumInfo(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryBBSFollow(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.saveBBS(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.saveBBS(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryCourse(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	/**
	 * 注册用户
	 */
	public static String saveUserInfo(UserInfo userInfo) {
		String[] types = {"身份证","学生证","工作证","士兵证","军官证","护照"};
		for(int i=0;i<types.length;i++){
			if(types[i].equals(userInfo.getIdType())){
				userInfo.setIdType(i+"");
			}
		}
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<loginname>" + userInfo.getLoginName() + "</loginname>"
					+ "<password>" + userInfo.getPassword() + "</password>"
					+ "<name>" + userInfo.getName() + "</name>" + "<email>"
					+ userInfo.getEmail() + "</email>" + "<sex>"
					+ userInfo.getSex() + "</sex>"
					+ "<idtype>" + userInfo.getIdType() + "</idtype>"
					+ "<idnum>" + userInfo.getIdNum() + "</idnum>"
					+ "<phonenum>" + userInfo.getPhoneNum() + "</phonenum>"
					+ "</ROOT>";
			rs = agent.saveUserInfo(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
		return rs;
	}

	public static String updateUserInfoPassWord(String loginname, String email) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<loginname>" + loginname + "</loginname>" + "<email>"
					+ email + "</email >" + "</ROOT>";
			rs = agent.updateUserInfoPassWord(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String updatePassWord(String userid, String oldpassword,
			String newpassword,String str_email, String str_card_kind,
			String str_card_num, String str_phone_num) {
		String rs = "";
		int idType = -1;
		String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证", "护照" };
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(str_card_kind)) {
				 idType=i;
			}
		}
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userid + "</userid>" + "<oldpassword>"
					+ oldpassword + "</oldpassword>" + "<newpassword>"
					+ newpassword + "</newpassword>" + "<email>" + str_email
					+ "</email>" + (idType==-1?"":"<idtype>" + idType + "</idtype>")
					+ "<idnum>" + str_card_num + "</idnum>" + "<phonenum>"
					+ str_phone_num + "</phonenum>" + "</ROOT>";
			rs = agent.updatePassWord(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryPhoneFunctionTree(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryClassName(String name, String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<classname>" + name + "</classname>" + "<functionids>"
					+ functionids + "</functionids>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			rs = agent.queryClassName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryClassByTime(String versionId, String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<funid>" + versionId + "</funid>" + "<functionids>"
					+ functionids + "</functionids>" + "<imagetype>"
					+ "androidpad" + "</imagetype>" + "</ROOT>";
			rs = agent.queryClassByTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryClassByType(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + functionids + "</functionids>"
					+ "</ROOT>";
			rs = agent.queryClassByType(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryClassTypeByClass(String id, int index) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<id>" + id + "</id>" + "<imagetype>" + "androidpad"
					+ "</imagetype>" + "<index>" + index + "</index>"
					+ "</ROOT>";
			rs = agent.queryClassTypeByClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryBuyClass(String userId, String classIds) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userId>" + userId + "</userId>" + "<classIds>"
					+ classIds + "</classIds>" + "<imagetype>" + "androidpad"
					+ "</imagetype>" + "</ROOT>";
			rs = agent.queryMoneyClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryNewsImage(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryNewsList(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
	public static String queryNewsByFunctionId(String functionid, String num,String userId) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>" + functionid + "</functionid>" + "<num>"
					+ num + "</num>" +"<userId>"
					+ userId + "</userId>"+ "</ROOT>";
			rs = agent.queryNewsByFunctionId(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryNewsByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.saveCoolect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryCollect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryReportByType(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.clearCollect(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryMoneyClass(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	/**
	 * 查询所有免费报告
	 */
	/*public static String queryAllFreeReport(String number) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<num>" + number + "</num>"
					+ "<imagetype>androidpad</imagetype>" + "</ROOT>";
			rs = agent.queryAllFreeReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}*/
	public static String queryAllFreeReport(String ids, String number,
			String reportName) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + ids + "</functionids>" + "<num>"
					+ number + "</num>" + "<imagetype>androidpad</imagetype>"
					+ "<isdownsum>y</isdownsum>"
					+ "<isfree>1</isfree>"
					+ "<reportname>" + reportName+ "</reportname>" 
					+ "</ROOT>";
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	/**
	 * 查询所有排行报告
	 */
	public static String queryNewReport(String ids, String number) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids>" + ids + "</functionids>" 
					+ "<num>" + number + "</num>" 
					+ "<isnew>" + 1 + "</isnew>" 
					+ "<imagetype>androidpad</imagetype>"
					+ "<reportname></reportname>" + "</ROOT>";
			rs = agent.queryReportByName(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
			rs = agent.queryAllClassTypeReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	
	public static String queryNewReports(String id, String number) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<reporttypeid>" + id + "</reporttypeid>" 
					+ "<num>" + number + "</num>" 
					+ "<isnew>" + 1 + "</isnew>" 
					+ "<imagetype>androidpad</imagetype>"
					+ "</ROOT>";
			rs = agent.queryAllClassTypeReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String querydbsImage(String functionid) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionid + "</functionids >"
					+ "<newstype>db</newstype>" + "</ROOT>";
			rs = agent.queryNewsImage(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String querydbsList(String functionids) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionids >" + functionids + "</functionids >"
					+ "<newstype>db</newstype>" + "</ROOT>";
			rs = agent.queryNewsList(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String querydbsByFunctionId(String functionid, String num) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<functionid>" + functionid + "</functionid>" + "<num>"
					+ num + "</num>" + "<newstype>db</newstype>" + "</ROOT>";
			rs = agent.queryNewsByFunctionId(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
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
					+ "<userid >" + userId + "</userid >" 
					 +"<imagetype>androidpad</imagetype>"
					+ "</ROOT>";
			rs = agent.queryUserClassTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}

	public static String queryBuyReport(String userId) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userId + "</userid>" + "</ROOT>";
			rs = agent.queryBuyReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	
	public static String saveUserClassTime(String userId,String classId,String studyTime) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<userid>" + userId + "</userid>"
					+ "<classid>" + classId + "</classid>" 
					+ "<time>" + studyTime + "</time>" + "</ROOT>";
			rs = agent.saveUserClassTime(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	
	public static String queryPassKeyBuyReport(String paths) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<paths>" + paths + "</paths>"
					+ "</ROOT>";
			rs = agent.queryPassKeyBuyReport(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	
	public static String updatedownsum(String id,String type) {
		String rs = "";
		try {
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<id>" + id + "</id>"
					+ "<type>" + type + "</type>"
					+ "</ROOT>";
			rs = agent.updatedownsum(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
			rs = "<?xml version='1.0' encoding='UTF-8'?>" + "<ROOT>"
					+ "<RETURNCODE>-1</RETURNCODE> " + "</ROOT>";
		}
		return rs;
	}
	/*报告属性获取*/
	public static String queryReportName(String id) {
		String rs = "";
		try {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<ROOT>" 
					+ "<id>"+id + "</id>"
					+ "</ROOT>";
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryReportName");
			rs=agent.queryReportName(xmlStr);
			Log.i("sys", xmlStr);
		} catch (Exception e) {
			rs = "0";
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
			WriteOrRead.write(xmlStr, "/mnt/sdcard/yepeng/", "queryNewsList");
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
