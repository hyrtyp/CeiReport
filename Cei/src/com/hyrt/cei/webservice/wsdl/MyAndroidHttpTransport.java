package com.hyrt.cei.webservice.wsdl;

import java.io.IOException;

import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

public class MyAndroidHttpTransport extends HttpTransportSE {

	private int timeout = 10000; // 默认超时时间为30s

	public MyAndroidHttpTransport(String url) {
		super(url);
	}

	public MyAndroidHttpTransport(String url, int timeout) {
		super(url);
		this.timeout = timeout;
	}

	protected ServiceConnection getServiceConnection(String url)
			throws IOException {
		ServiceConnectionSE serviceConnection = new ServiceConnectionSE(url);
		serviceConnection.setConnectionTimeOut(timeout);
		return new ServiceConnectionSE(url);
	}

}
