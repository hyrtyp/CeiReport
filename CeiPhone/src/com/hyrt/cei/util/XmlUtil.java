package com.hyrt.cei.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ClassType;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.cei.vo.PhonefunctionInf;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.vo.ReportColumn;
import com.hyrt.cei.vo.ReportpaitElement;
import com.hyrt.cei.vo.Updata;
import com.hyrt.cei.vo.WeatherInfo;
import com.hyrt.cei.vo.WitSea;
import com.hyrt.cei.vo.funId;

public class XmlUtil {
	
	public static boolean isFree;
	
	public static String parseContent(String xml) {
		String returnCode = "";
		try {
			String tag_name = "";
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("content")) {
						returnCode = someValue;
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnCode;
	}

	public static String parseReturnCode(String xml) {
		String returnCode = "";
		try {
			String tag_name = "";
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("returncode")) {
						returnCode = someValue;
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnCode;
	}

	public static void parsePhoneFunctionTree(String xml,
			List<Map<String, String>> treeData) {
		try {
			String tag_name = "";
			int i = -1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("tree")) {
						Map<String, String> dataItem = new HashMap<String, String>();
						treeData.add(dataItem);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("name")) {
						treeData.get(i).put("text", someValue);
					}
					if (tag_name.equals("id")) {
						treeData.get(i).put("id", someValue);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<New> getNews(String xml) throws Exception {
		List<New> news = null;
		New n = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				news = new ArrayList<New>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("new")) {
					n = new New();
				}
				if (n != null) {
					if (parser.getName().equals("id")) {
						n.setId(parser.nextText());
					} else if (parser.getName().equals("time")) {
						n.setTime(parser.nextText());
					} else if (parser.getName().equals("title")) {
						n.setTitle(parser.nextText());
					} else if (parser.getName().equals("subhead")) {
						n.setSubhead(parser.nextText());
					} else if (parser.getName().equals("author")) {
						n.setAuthor(parser.nextText());
					} else if (parser.getName().equals("imagepath")) {
						n.setPpath(parser.nextText());
					} else if (parser.getName().equals("funName")) {
						n.setFunname(parser.nextText());
					} else if (parser.getName().equals("isfree")) {
						if(isFree)
							n.setIsfree("1");
						else	
							n.setIsfree(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("new")) {
					news.add(n);
					n = null;
				}
				break;
			}
			event = parser.next();
		}
		return news;

	}

	public static List<PhonefunctionInf> loginUserInfo(String xml)
			throws Exception {
		List<PhonefunctionInf> ponefunctionInfs = null;
		PhonefunctionInf phonefunctionInf = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				ponefunctionInfs = new ArrayList<PhonefunctionInf>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("phonefunction")) {
					phonefunctionInf = new PhonefunctionInf();
				}
				if (phonefunctionInf != null) {
					if (parser.getName().equals("id")) {
						phonefunctionInf.setId(parser.nextText());
					} else if (parser.getName().equals("name")) {
						phonefunctionInf.setName(parser.nextText());
					} else if (parser.getName().equals("operationimage")) {
						phonefunctionInf.setOperationimage(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if ("phonefunction".equals(parser.getName())) {// 判断结束标签元素是否是book
					ponefunctionInfs.add(phonefunctionInf);// 将book添加到books集合
					phonefunctionInf = null;
				}
				break;
			}
			event = parser.next();
		}
		return ponefunctionInfs;
	}

	public static ArrayList<WitSea> witSea(String xml) throws Exception {

		ArrayList<WitSea> witSeas = null;
		WitSea sea = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				witSeas = new ArrayList<WitSea>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("bright")) {
					sea = new WitSea();
				}
				if (sea != null) {
					if (parser.getName().equals("funid")) {
						sea.setFunid(parser.nextText());
					} else if (parser.getName().equals("name")) {
						sea.setName(parser.nextText());
					} else if (parser.getName().equals("operationimage")) {
						sea.setOperationimage(parser.nextText());
					} else if (parser.getName().equals("isorder")) {
						sea.setIsCustom(parser.nextText());
					} else if (parser.getName().equals("issuetime")) {
						sea.setIssuetime(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if ("bright".equals(parser.getName())) {
					witSeas.add(sea);
					sea = null;
				}
				break;
			}
			event = parser.next();
		}// end while
		return witSeas;
	}

	public static String updateWitSea(String xml) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(bis, "UTF-8");
		String returnCode = "";
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("RETURNCODE")) {
					returnCode = parser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}// end while
		return returnCode;
	}

	public static void parseInitResources(String result, ColumnEntry columnEntry) {
		try {
			List<ColumnEntry> columnEntryChilds = columnEntry.getColumnEntryChilds();
			String tag_name = "";
			int i = -1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					result.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("phonefunction")) {
						ColumnEntry columnEntryChild = new ColumnEntry();
						columnEntryChilds.add(columnEntryChild);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("userid")) {
						columnEntry.setUserId(someValue);
					}
					if (tag_name.equals("loginid")) {
						columnEntry.setLoginid(someValue);
					}
					if (tag_name.equals("login")) {
						columnEntry.setLogo(someValue);
					}
					if (tag_name.equals("background")) {
						columnEntry.setBackground(someValue);
					}
					if (tag_name.equals("wcolor")) {
						columnEntry.setwColor(someValue);
					}
					if (tag_name.equals("parentid")) {
						columnEntryChilds.get(i).setParentId(someValue);
					}
					if (tag_name.equals("name")) {
						columnEntryChilds.get(i).setName(someValue);
					}
					if (tag_name.equals("id")) {
						columnEntryChilds.get(i).setId(someValue);
					}
					if (tag_name.equals("operationimage")) {
						columnEntryChilds.get(i).setOperationImage(someValue);
					}
					if (tag_name.equals("type")) {
						columnEntryChilds.get(i).setType(someValue);
					}
					if (tag_name.equals("path")) {
						columnEntryChilds.get(i).setPath(someValue);
					}
					if (tag_name.equals("issuetime")) {
						columnEntryChilds.get(i).setIssueTime(someValue);
					}
					if (tag_name.equals("description")) {
						columnEntryChilds.get(i).setDescription(someValue);
					}
					if (tag_name.equals("agiorate")) {
						if("0.0".equals(someValue))
							isFree = true;
					}
					
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void parseInitSelfResources(String result,ColumnEntry columnEntry) {
		try {
			List<ColumnEntry> columnEntryChilds = columnEntry.getSelectColumnEntryChilds();
			String tag_name = "";
			int i = -1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					result.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("phonefunction")) {
						ColumnEntry columnEntryChild = new ColumnEntry();
						columnEntryChilds.add(columnEntryChild);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("parentid")) {
						columnEntryChilds.get(i).setParentId(someValue);
					}
					if (tag_name.equals("name")) {
						columnEntryChilds.get(i).setName(someValue);
					}
					if (tag_name.equals("resourceid")) {
						columnEntryChilds.get(i).setId(someValue);
					}
					if (tag_name.equals("operationimage")) {
						columnEntryChilds.get(i).setOperationImage(someValue);
					}
					if (tag_name.equals("type")) {
						columnEntryChilds.get(i).setType(someValue);
					}
					if (tag_name.equals("description")) {
						columnEntryChilds.get(i).setDescription(someValue);
					}
					if (tag_name.equals("issuetime")) {
						columnEntryChilds.get(i).setIssueTime(someValue);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Forum> parseForumInfo(String xml, String classId,
			String userId, String functionId) {
		List<Forum> dataList = new ArrayList<Forum>();
		try {
			String tag_name = "";
			int i = dataList.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("bbsfollow")) {
						Forum forum = new Forum();
						forum.setClassId(classId);
						forum.setUserid(userId);
						forum.setFunctionid(functionId);
						dataList.add(forum);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("content")) {
						dataList.get(i).setContent(someValue);
					} else if (tag_name.equals("name")) {
						dataList.get(i).setName(someValue);
					} else if (tag_name.equals("serial")) {
						dataList.get(i).setSerial(someValue);
					} else if (tag_name.equals("time")) {
						dataList.get(i).setTime(someValue);
					} else if (tag_name.equals("stringtime")) {
						dataList.get(i).setStrTime(someValue);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 个人中心
	 */
	public static PersonCenterInf personCenter(String xml) throws Exception {
		PersonCenterInf personCenterInf = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("user")) {
					personCenterInf = new PersonCenterInf();
				}
				if (personCenterInf != null) {
					if (parser.getName().equals("name")) {
						personCenterInf.setName(parser.nextText());
					} else if (parser.getName().equals("loginname")) {
						personCenterInf.setLgoinname(parser.nextText());
					} else if (parser.getName().equals("sex")) {
						personCenterInf.setSex(parser.nextText());
					} else if (parser.getName().equals("email")) {
						personCenterInf.setEmail(parser.nextText());
					} else if (parser.getName().equals("phone")) {
						personCenterInf.setPhone(parser.nextText());
					} else if (parser.getName().equals("unitname")) {
						personCenterInf.setUnitname(parser.nextText());
					} else if (parser.getName().equals("integral")) {
						personCenterInf.setIntegral(parser.nextText());
					} else if (parser.getName().equals("certype")) {
						personCenterInf.setCertype(parser.nextText());
					} else if (parser.getName().equals("cardno")) {
						personCenterInf.setCardno(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return personCenterInf;
	}

	/**
	 * @param xml
	 * @param coursewares
	 */
	public static void parseCoursewares(String xml, List<Courseware> coursewares) {
		try {
			String tag_name = "";
			int i = coursewares.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("class")) {
						Courseware courseware = new Courseware();
						coursewares.add(courseware);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("classid") || tag_name.equals("id")) {
						coursewares.get(i).setClassId(someValue);
					}
					if (tag_name.equals("name")) {
						coursewares.get(i).setName(someValue.length()>10?someValue.substring(0
								,9)+"...":someValue);
						coursewares.get(i).setFullName(someValue);
					}
					if (tag_name.equals("path")) {
						coursewares.get(i).setPath(someValue);
					}
					if (tag_name.equals("downpath")) {
						coursewares.get(i).setDownPath(someValue);
					}
					if (tag_name.equals("lookpath")) {
						coursewares.get(i).setLookPath(someValue);
					}
					if (tag_name.equals("teachername")) {
						coursewares.get(i).setTeacherName(someValue);
					}
					if (tag_name.equals("protime")) {
						coursewares.get(i).setProTime(someValue.length()>5?someValue.substring(0
								,someValue.indexOf(" ")):someValue);
					}
					if (tag_name.equals("classlength")) {
						try{
							coursewares.get(i).setClassLength((Integer.parseInt(someValue)*60)+"");
						}catch(Exception e){
							coursewares.get(i).setClassLength("0");
							e.printStackTrace();
						}
					}
					if (tag_name.equals("intro")) {
						coursewares.get(i).setIntro(someValue);
					}
					if (tag_name.equals("setnum")) {
						coursewares.get(i).setClassLevel(someValue);
					}
					if (tag_name.equals("isfree")) {
						if(isFree || "1".equals(someValue))
							coursewares.get(i).setFree(true);
					}
					if (tag_name.equals("passkey")) {
						coursewares.get(i).setKey(someValue);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @param xml
	 * @param coursewares
	 */
	public static void parseErrorCoursewares(String xml, List<Courseware> coursewares) {
		try {
			String tag_name = "";
			int i = coursewares.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("class")) {
						Courseware courseware = new Courseware();
						coursewares.add(courseware);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("classid") || tag_name.equals("id")) {
						coursewares.get(i).setClassId(someValue);
					}
					if (tag_name.equals("name")) {
						coursewares.get(i).setName(someValue.length()>10?someValue.substring(0
								,9)+"...":someValue);
						coursewares.get(i).setFullName(someValue);
					}
					if (tag_name.equals("path")) {
						coursewares.get(i).setPath(someValue);
					}
					if (tag_name.equals("lookpath")) {
						coursewares.get(i).setDownPath(someValue);
					}
					if (tag_name.equals("downpath")) {
						coursewares.get(i).setLookPath(someValue);
					}
					if (tag_name.equals("teachername")) {
						coursewares.get(i).setTeacherName(someValue);
					}
					if (tag_name.equals("protime")) {
						coursewares.get(i).setProTime(someValue.length()>5?someValue.substring(0
								,someValue.indexOf(" ")):someValue);
					}
					if (tag_name.equals("classlength")) {
						try{
							coursewares.get(i).setClassLength((Integer.parseInt(someValue)*60)+"");
						}catch(Exception e){
							coursewares.get(i).setClassLength("0");
							e.printStackTrace();
						}
					}
					if (tag_name.equals("intro")) {
						coursewares.get(i).setIntro(someValue);
					}
					if (tag_name.equals("setnum")) {
						coursewares.get(i).setClassLevel(someValue);
					}
					if (tag_name.equals("isfree")) {
						if(isFree || "1".equals(someValue))
							coursewares.get(i).setFree(true);
					}
					if (tag_name.equals("passkey")) {
						coursewares.get(i).setKey(someValue);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @param xml
	 * @param coursewares
	 */
	public static void parseCoursewareTimes(String xml, List<Courseware> coursewares) {
		try {
			String tag_name = "";
			int i = coursewares.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("classtime")) {
						Courseware courseware = new Courseware();
						coursewares.add(courseware);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("classid")) {
						coursewares.get(i).setClassId(someValue);
					}
					if (tag_name.equals("name")) {
						coursewares.get(i).setName(someValue.length()>10?someValue.substring(0
								,9)+"...":someValue);
						coursewares.get(i).setFullName(someValue);
					}
					if (tag_name.equals("path")) {
						coursewares.get(i).setPath(someValue);
					}
					if (tag_name.equals("downpath")) {
						coursewares.get(i).setDownPath(someValue);
					}
					if (tag_name.equals("lookpath")) {
						coursewares.get(i).setLookPath(someValue);
					}
					if (tag_name.equals("teachername")) {
						coursewares.get(i).setTeacherName(someValue);
					}
					if (tag_name.equals("protime")) {
						coursewares.get(i).setProTime(someValue.length()>5?someValue.substring(0
								,someValue.indexOf(" ")):someValue);
					}
					if (tag_name.equals("classlength")) {
						try{
							coursewares.get(i).setClassLength((Integer.parseInt(someValue)*60)+"");
						}catch(Exception e){
							coursewares.get(i).setClassLength("0");
							e.printStackTrace();
						}
					}
					if (tag_name.equals("intro")) {
						coursewares.get(i).setIntro(someValue);
					}
					if (tag_name.equals("setnum")) {
						coursewares.get(i).setClassLevel(someValue.equals("null")?"1":someValue);
					}
					if (tag_name.equals("isfree")) {
							coursewares.get(i).setFree(true);
					}
					if (tag_name.equals("passkey")) {
						coursewares.get(i).setKey(someValue);
					}
					if (tag_name.equals("time")) {
						coursewares.get(i).setStudyTime(someValue);
					}
					if (tag_name.equals("iscompleted")) {
						coursewares.get(i).setIscompleted(someValue);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 阅读报告解析
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static List<Report> parseReport(String xml) throws Exception {
		List<Report> reports = null;
		Report report = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				reports = new ArrayList<Report>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("report")
						|| parser.getName().equals("class")) {
					report = new Report();
				}
				if(parser.getName().equals("RETURNCODE")){
					return null;
				}
				if (report != null) {
					if (parser.getName().equals("price")) {
						report.setPrice(parser.nextText());
					} else if (parser.getName().equals("classid")
							|| parser.getName().equals("id")) {
						report.setId(parser.nextText());
					} else if (parser.getName().equals("ischeck")) {
						report.setIsCheck(parser.nextText());
					} else if (parser.getName().equals("name")) {
						report.setName(parser.nextText());
					} else if (parser.getName().equals("teachername")
							|| parser.getName().equals("author")) {
						report.setAuthor(parser.nextText());
					} else if (parser.getName().equals("path")) {
						report.setPpath(parser.nextText());
					} else if (parser.getName().equals("protime")
							|| parser.getName().equals("creattime")) {
						String protime = parser.nextText();
						report.setProtime(protime.length()>5?
								protime.substring(0,protime.indexOf(" ")):protime);
					} else if (parser.getName().equals("downpath")) {
						report.setDownpath(parser.nextText());
					} else if (parser.getName().equals("intro")) {
						report.setIntro(parser.nextText());
					} else if (parser.getName().equals("scroll")) {
						report.setMulu(parser.nextText());
					} else if (parser.getName().equals("passkey")) {
						report.setKey(parser.nextText());
					} else if (parser.getName().equals("isfree")) {
						if(isFree)
							report.setIsFree("1");
						else
							report.setIsFree(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("report")
						|| parser.getName().equals("class")) {
					reports.add(report);
					report = null;
				}
				break;
			}
			event = parser.next();
		}
		return reports;

	}

	public static String parseCoursewareId(String xml) {
		String id = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(bis, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("id")) {
						id = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}// end while
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int updateUserInfoPassWord(String xml) {
		int id = 0;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(bis, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("returncode")) {
						id = Integer.parseInt(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}// end while
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int updatePassWord(String xml) {
		int id = 0;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(bis, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("returncode")) {
						id = Integer.parseInt(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}// end while
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static List<AnnouncementNews> getAnnouncement(String xml)
			throws Exception {
		List<AnnouncementNews> announcementNews = null;
		AnnouncementNews n = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				announcementNews = new ArrayList<AnnouncementNews>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("new")) {
					n = new AnnouncementNews();
				}
				if (n != null) {
					if (parser.getName().equals("id")) {
						n.setId(parser.nextText());
					} else if (parser.getName().equals("title")) {
						n.setTitle(parser.nextText());
					} else if (parser.getName().equals("subhead")) {
						n.setSubhead(parser.nextText());
					} else if (parser.getName().equals("time")) {
						n.setTime(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("new")) {
					announcementNews.add(n);
					n = null;
				}
				break;
			}
			event = parser.next();
		}
		return announcementNews;

	}

	public static void parseClassType(String xml, List<ClassType> classTypes) {
		try {
			String tag_name = "";
			int i = classTypes.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("classtype")) {
						ClassType classType = new ClassType();
						classTypes.add(classType);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("id")) {
						classTypes.get(i).setClassId(someValue);
					}
					if (tag_name.equals("name")) {
						classTypes.get(i).setContent(someValue);
					}
					if (tag_name.equals("parentid")) {
						classTypes.get(i).setParentId(someValue);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 政经资讯 资讯列表的解析 getNews
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static void getNewsList(String xml, List<InfoNew> infoNews) {
		try {
			String tag_name = "";
			int i = infoNews.size() - 1;
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					if (tag_name.equals("new")) {
						InfoNew infoNew = new InfoNew();
						infoNews.add(infoNew);
						i++;
					}
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("imagepath")) {
						infoNews.get(i).setImagepath(someValue);
					}
					if (tag_name.equals("id")) {
						infoNews.get(i).setId(someValue);
					}
					if (tag_name.equals("title")) {
						infoNews.get(i).setTitle(someValue);
					}
					if (tag_name.equals("subhead")) {
						infoNews.get(i).setSubhead(someValue);
					}
					if (tag_name.equals("time")) {
						infoNews.get(i).setTime(someValue.length()>5
								?someValue.substring(0,someValue.indexOf(" ")):someValue);
					}
					if (tag_name.equals("isfree")) {
						if(isFree)
							infoNews.get(i).setIsfree("1");
						else
							infoNews.get(i).setIsfree(someValue);
					}
					if (tag_name.equals("functionid")) {
						infoNews.get(i).setFunctionId(someValue);
					}
					if (tag_name.equals("iscollect")) {
						infoNews.get(i).setIsCollect(someValue);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析城市或省份集合 <string>广西,31125</string> <string>云南,31126</string>
	 * <string>海南,31127</string> <string>新疆,31128</string>
	 * <string>西藏,31129</string> <string>台湾,31130</string>
	 * <string>北京,311101</string> <string>上海,311102</string>
	 * <string>天津,311103</string> <string>重庆,311104</string>
	 * <string>香港,311201</string> <string>澳门,311202</string>
	 * 
	 * @return
	 */
	public static List<String[]> parsePCCodes(String xml) {
		List<String[]> provinces = new ArrayList<String[]>();
		try {
			String tag_name = "";
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toUpperCase().trim();
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("STRING")) {
						String[] province = someValue.split(",");
						provinces.add(province);
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return provinces;
	}

	/**
	 * 解析城市天气
	 * 
	 * @return <string>直辖市 重庆</string> <string>巴南</string> <string>1601</string>
	 *         <string>2012/06/18 19:56:35</string>
	 *         <string>今日天气实况：气温：20℃；风向/风力：西南风 2级；湿度：90%</string>
	 *         <string>空气质量：中；紫外线强度：最弱</string>
	 *         <string>穿衣指数：天气较热，建议着短裙、短裤、短套装、T恤等夏季服装。年老体弱者宜着长袖衬衫和单裤。
	 *         感冒指数：各项气象条件适宜，无明显降温过程，发生感冒机率较低。 运动指数：阴天，天气较热，请减少运动时间并降低运动强度。
	 *         洗车指数：较不宜洗车，过去12小时有降雨，路面少量积水，如果执意擦洗汽车，要做好溅上泥水的心理准备。
	 *         晾晒指数：天气阴沉，不利于水分的迅速蒸发，不太适宜晾晒。若需要晾晒，请尽量选择通风的地点。
	 *         旅游指数：阴天，温度适宜，总体来说还是好天气哦，这样的天气很适宜旅游，您可以尽情地享受大自然的风光。
	 *         路况指数：阴天，路面比较干燥，路况较好。
	 *         舒适度指数：天气较热，空气湿度较大，会使您感到有点儿闷热，不过大部分人仍会有比较舒适的感觉。
	 *         空气污染指数：气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
	 *         紫外线指数：属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。</string>
	 */
	public static WeatherInfo parseWeatherInfo(String xml) {
		WeatherInfo wi = new WeatherInfo();
		int index = 0;
		try {
			String tag_name = "";
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toUpperCase().trim();
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("STRING")) {
						index++;
						switch (index) {
						case 1:
							wi.setProvince(someValue);
							break;
						case 2:
							wi.setCity(someValue);
							break;
						case 3:
							wi.setCityCode(someValue);
							break;
						case 4:
							wi.setTime(someValue);
							break;
						case 5:
							wi.setScene(someValue);
							break;
						case 6:
							wi.setAir(someValue);
							break;
						case 7:
							wi.setDes(someValue);
							break;
						}
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wi;
	}

	/**
	 * 解析报告分类列表
	 * 
	 * @param retCord
	 */
	public static List<ReportpaitElement> parseReportPart(String xml)
			throws Exception {
		List<ReportpaitElement> reportList = null;
		ReportpaitElement element = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				reportList = new ArrayList<ReportpaitElement>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("classtype")) {
					element = new ReportpaitElement();
				}
				if(parser.getName().equals("RETURNCODE")){
					return null;
				}
				if (element != null) {
					if (parser.getName().equals("id")) {
						element.setId(parser.nextText());
					} else if (parser.getName().equals("name")) {
						element.setOutlineTitle(parser.nextText());
					} else if (parser.getName().equals("parentid")) {
						element.setParent(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("classtype")) {
					reportList.add(element);
					element = null;
				}
				break;
			}
			event = parser.next();
		}
		return reportList;
	}

	public static List<funId> queryBuyNews(String xml) throws Exception {
		List<funId> funids = null;
		funId funid = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				funids = new ArrayList<funId>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("funid")) {
					funid = new funId();
					funid.setFunid(parser.nextText());
					funids.add(funid);
					funid = null;
				}
			}
			event = parser.next();
		}
		return funids;
	}
	public static List<funId> queryBuyReports(String xml) throws Exception {
		List<funId> funids = null;
		funId funid = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				funids = new ArrayList<funId>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("id")) {
					funid = new funId();
					funid.setFunid(parser.nextText());
					funids.add(funid);
					funid = null;
				}
			}
			event = parser.next();
		}
		return funids;
	}
	/*解析报告属性*/
	public static List<ReportColumn> parseReportColumn(String xml)
			throws Exception {
		List<ReportColumn> reportList = null;
		ReportColumn element = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				reportList = new ArrayList<ReportColumn>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("reportname")) {
					element = new ReportColumn();
				}
				if (element != null) {
					if (parser.getName().equals("name")) {
						element.setName(parser.nextText());
					} else if (parser.getName().equals("intro")) {
						element.setIntro(parser.nextText());
					} else if (parser.getName().equals("author")) {
						element.setAuthro(parser.nextText());
					}else if (parser.getName().equals("unit")) {
						element.setUnit(parser.nextText());
					} else if (parser.getName().equals("list")) {
						element.setList(parser.nextText());
					}else if (parser.getName().equals("price")) {
						element.setPrice(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("reportname")) {
					reportList.add(element);
					element = null;
				}
				break;
			}
			event = parser.next();
		}
		return reportList;
	}
	
	public static String parseAddress(String xml) {
		String returnCode = "-1";
		try {
			String tag_name = "";
			ByteArrayInputStream bis = new ByteArrayInputStream(
					xml.getBytes("UTF-8"));
			KXmlParser parser = new KXmlParser();
			parser.setInput(bis, "UTF-8");
			while (parser.next() > 1) {
				int eventType = parser.getEventType();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					tag_name = "";
					break;
				case XmlPullParser.START_TAG:
					tag_name = parser.getName().toLowerCase().trim();
					break;
				case XmlPullParser.TEXT:
					String someValue = parser.getText().trim();
					if (tag_name.equals("address")) {
						returnCode = someValue;
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnCode;
	}
	
	
	public static Updata queryApkList(String xml)
			throws Exception {
		Updata u = null;
		XmlPullParser parser = Xml.newPullParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		parser.setInput(bis, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("update")) {
					u = new Updata();
				}
				if (u != null) {
					if (parser.getName().equals("aphoneversion")) {
						u.setAphoneversion(Integer.parseInt(parser.nextText()));
					} else if (parser.getName().equals("aphonename")) {
						u.setAphonename(parser.nextText());
					} else if (parser.getName().equals("aphoneurl")) {
						u.setAphoneurl(parser.nextText());
					}else if (parser.getName().equals("apadversion")) {
						u.setApadversion(Integer.parseInt(parser.nextText()));
					} else if (parser.getName().equals("apadname")) {
						u.setApadname(parser.nextText());
					}else if (parser.getName().equals("apadurl")) {
						u.setApadurl(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("update")) {
				}
				break;
			}
			event = parser.next();
		}
		return u;
	}
	
}
