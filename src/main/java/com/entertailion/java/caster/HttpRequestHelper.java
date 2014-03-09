/*
 * Copyright (C) 2013 ENTERTAILION, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.entertailion.java.caster;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.nio.charset.Charset;

import static org.apache.http.config.ConnectionConfig.custom;
import static org.apache.http.conn.params.ConnManagerParams.DEFAULT_MAX_TOTAL_CONNECTIONS;
import static org.apache.http.conn.params.ConnPerRouteBean.DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

/*
 * HTTP utility
 * 
 * @author leon_nicholls
 */
public class HttpRequestHelper {

  HttpClient httpClient;
  HttpContext localContext;
  private String ret;
  private String TAG = "HttpRequestHelper";

  HttpGet httpGet = null;

  public HttpRequestHelper() {
    httpClient = createHttpClient(new DefaultRedirectStrategy());
    localContext = new BasicHttpContext();
  }

  public static HttpClient createHttpClient(DefaultRedirectStrategy redirectStrategy) {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
    connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    connectionManager.setDefaultConnectionConfig(custom().setCharset(Charset.defaultCharset())
                                                     .build());
    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
        .setConnectTimeout(20000);

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        .setDefaultCookieStore(new BasicCookieStore())
        .setRedirectStrategy(redirectStrategy)
        .setDefaultRequestConfig(requestConfigBuilder.build());

    return httpClientBuilder.build();
/*
    HttpParams params = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(params, 20000);
    HttpConnectionParams.setSoTimeout(params, 20000);
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);

    SchemeRegistry schReg = new SchemeRegistry();
    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

    return new DefaultHttpClient(conMgr, params);
*/
  }

  public HttpResponse sendHttpGet(String url) {
    httpGet = new HttpGet(url);

    try {
      return httpClient.execute(httpGet);
    }
    catch (Throwable e) {
      Log.e(TAG, "sendHttpGet exception: ", e);
    }
    return null;
  }
}
