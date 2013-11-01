package com.hyrt.cei.webservice.wsdl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public final class AgentServiceSoapBindingImpl {

	public java.lang.String testFunction() throws Exception {
		SoapObject _client = new SoapObject("", "testFunction");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 终端首次登陆
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String loginUserInfo(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "loginUserInfo");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_client.addProperty("xmlStr", xmlStr);
		_envelope.bodyOut = _client;
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl(),15000);
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 智慧海业务
	 * 
	 * @param sendXml
	 * @return
	 * @throws Exception
	 */
	public String witSea(String sendXml) throws Exception {
		SoapObject _client = new SoapObject("", "queryBrightness");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", sendXml);// 传参用
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询栏目业务选项
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String queryPhoneFunctionTree(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryPhoneFunctionTree");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 智慧海业务修改
	 * 
	 * @param sendXml
	 * @return
	 * @throws Exception
	 */
	public String upDateWitSea(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveCourse");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);// 传参用
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 用户详细信息
	 */
	public String queryUserInfo(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryUserInfo");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 获取用户定制的一些业务
	 * 
	 * @param xmlStr
	 * @return
	 */
	public String initSelfResources(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryBusiness");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 论坛接口
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String querySchoolForumInfo(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryCourse");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 智慧海业务删除
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String deleteWitSea(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "delBusiness");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryGoodsReport(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryReportIsTop");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 增加日志
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String addLog(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveOperationlog");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 增加评论列表
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String saveBBS(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveBBSFollow");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 评论列表详细
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String queryBBSFollow(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryBBSFollow");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询精彩课件
	 */
	public String queryCourse(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryCourse");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 用户注册
	 * 
	 * @param xmlStr
	 * @return
	 */
	public String saveUserInfo(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveUserInfo");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryClassInfo(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryClassInfo");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 添加自选课
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String saveCourse(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveCourse");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String cancelCourse(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "delBusiness");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 重置密码的接口
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String updateUserInfoPassWord(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "updateUserInfoPassWord");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 修改密码的接口
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String updatePassWord(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "updatePassWord");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 搜索课件
	 */
	public String queryClassName(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryClassByName");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 搜索课件分类
	 */
	public String queryClassByType(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryClassByType");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 根据分类id查询课件列表
	 */
	public String queryClassTypeByClass(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryClassTypeByClass");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 政经资讯首页查询图片信息queryNewsImage
	 */
	public String queryNewsImage(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryNewsImage");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 政经资讯首页查询图片信息queryNewsImage
	 */
	public String queryNewsList(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryNewsList");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 政经资讯首页业务查询信息queryNewsByFunctionId
	 */
	public String queryNewsByFunctionId(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryNewsByFunctionId");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 政经资讯按名称查询信息queryNewsByName
	 */
	public String queryNewsByName(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryNewsByName");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 新增资讯收藏信息saveCoolect
	 */
	public String saveCoolect(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "saveCoolect");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 删除资讯收藏信息delCollect
	 */
	public String delCollect(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "delCollect");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询资讯收藏信息queryCollect
	 */
	public String queryCollect(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryCollect");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询阅读报告分类列表
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String queryReportByType(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryReportByType");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 清空资讯收藏信息clearCollect
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String clearCollect(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "clearCollect");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询是否购买过此课件
	 */
	public String queryMoneyClass(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryMoneyClass");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	/**
	 * 查询免费报告
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String queryAllFreeReport(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryAllFreeReport");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryReportByName(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryReportByName");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryAllClassTypeReport(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryAllClassTypeReport");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryClassByTime(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryClassByTime");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryBuyNews(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryBuyNews");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
	
	public String queryPassKey(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryPassKey");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
	
	
	public String queryNotice(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryNotice");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryUserClassTime(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "queryUserClassTime");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryBuyReport(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "queryMoneyReport");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
	
	public String saveUserClassTime(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "saveUserClassTime");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
	
	public String queryPassKeyBuyReport(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "queryPassKeyByReport");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
//统计
	public String updatedownsum(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "updatedownsum");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	public String queryReportName(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "queryReportName");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}

	
	public String queryFunctionAddress(String xmlStr) throws Exception{
		SoapObject _client = new SoapObject("", "queryFunctionAddress");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
	/* 系统更新 */
	public String queryApkList(String xmlStr) throws Exception {
		SoapObject _client = new SoapObject("", "queryApkList");
		SoapSerializationEnvelope _envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		_envelope.bodyOut = _client;
		_client.addProperty("xmlStr", xmlStr);
		MyAndroidHttpTransport _ht = new MyAndroidHttpTransport(
				Configuration.getWsUrl());
		_ht.call("", _envelope);
		return (java.lang.String) _envelope.getResponse();
	}
}