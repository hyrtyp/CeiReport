package com.hyrt.cei.vo;

public class WeatherInfo {

	//<string>直辖市 重庆</string>
	private String province;
	// <string>巴南</string>
	private String city;
	// <string>1601</string>
	private String cityCode;
	// <string>2012/06/18 19:31:14</string>
	private String time;
	// <string>今日天气实况：气温：20℃；风向/风力：西南风 2级；湿度：90%</string>
	private String scene;
	// <string>空气质量：中；紫外线强度：最弱</string>
	private String air;
	// <string>穿衣指数：天气较热，建议着短裙、短裤、短套装、T恤等夏季服装。年老体弱者宜着长袖衬衫和单裤。
		//	感冒指数：各项气象条件适宜，无明显降温过程，发生感冒机率较低。
		//	运动指数：阴天，天气较热，请减少运动时间并降低运动强度。
		//	洗车指数：较不宜洗车，过去12小时有降雨，路面少量积水，如果执意擦洗汽车，要做好溅上泥水的心理准备。
		//	晾晒指数：天气阴沉，不利于水分的迅速蒸发，不太适宜晾晒。若需要晾晒，请尽量选择通风的地点。
		//	旅游指数：阴天，温度适宜，总体来说还是好天气哦，这样的天气很适宜旅游，您可以尽情地享受大自然的风光。
		//	路况指数：阴天，路面比较干燥，路况较好。
		//	舒适度指数：天气较热，空气湿度较大，会使您感到有点儿闷热，不过大部分人仍会有比较舒适的感觉。
		//	空气污染指数：气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
		//	紫外线指数：属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。</string>*/
	private String des;
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getAir() {
		return air;
	}
	public void setAir(String air) {
		this.air = air;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
	
}
