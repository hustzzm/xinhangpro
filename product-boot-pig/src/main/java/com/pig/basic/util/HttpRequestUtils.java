package com.pig.basic.util;

import cn.hutool.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
public class HttpRequestUtils {

    private static final int connectTimeout = 1000 * 120;    // 连接超时时间
    private static final int socketTimeout = 1000 * 180;    // 读取数据超时时间

    /**
     * 向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数，格式 name1=value1&name2=value2
     * @return 远程资源的响应结果
     */
    public static String sendPost(String strUrl, String requestParams,String tokenJson, String methodType) {

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(methodType); //POST or DELETE
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            httpURLConnection.setRequestProperty("accept", "*/*");    // 设置接收数据的格式
            httpURLConnection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
//            httpURLConnection.setRequestProperty("Accept", "application/json");    // 设置接收数据的格式
//            httpURLConnection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
            httpURLConnection.addRequestProperty("x-app-auth",tokenJson);
            httpURLConnection.connect();    // 建立连接
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.append(requestParams);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // 使用BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String strLine = "";
            while ((strLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(strLine);
            }
            bufferedReader.close();
            String responseParams = stringBuffer.toString();

            return responseParams;
        } catch (IOException e) {

            log.error(e.toString());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * 向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数，格式 name1=value1&name2=value2
     * @param requestJson   請求的json參數
     * @return 远程资源的响应结果
     */
    public static String sendJsonPost(String strUrl, String requestParams,String requestJson,String tokenJson, String methodType) {

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(methodType); //POST or DELETE
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            httpURLConnection.setRequestProperty("accept", "*/*");    // 设置接收数据的格式
            httpURLConnection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
//            httpURLConnection.setRequestProperty("Accept", "application/json");    // 设置接收数据的格式
//            httpURLConnection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
            if(!StringUtil.isNull(tokenJson)){
                httpURLConnection.addRequestProperty("x-app-auth",tokenJson);
            }
            httpURLConnection.connect();    // 建立连接
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.append(requestParams);
            outputStreamWriter.append(requestJson);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // 使用BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String strLine = "";
            while ((strLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(strLine);
            }
            bufferedReader.close();
            String responseParams = stringBuffer.toString();

            return responseParams;
        } catch (IOException e) {

            log.error(e.toString());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * HttpClientPost 方式，向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String doPost(String strUrl, List<BasicNameValuePair> requestParams) {

        String responseParams = "";
        StringBuffer stringBuffer = new StringBuffer();
        long startTime = 0, endTime = 0;

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();    // 设置请求和传输超时时间

        HttpPost httpPost = new HttpPost(strUrl);
        httpPost.setConfig(requestConfig);
        HttpEntity httpEntity;

        try {
            if (requestParams != null) {
                // 设置相关参数
                httpEntity = new UrlEncodedFormEntity(requestParams, "UTF-8");
                httpPost.setEntity(httpEntity);


            }
            startTime = System.nanoTime();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            int code = closeableHttpResponse.getStatusLine().getStatusCode();



            if (code == 200 || code == 500) {
                try {
                    httpEntity = closeableHttpResponse.getEntity();
                    if (httpEntity != null) {
                        long length = httpEntity.getContentLength();
                        // 当返回值长度较小的时候，使用工具类读取
                        if (length != -1 && length < 2048) {
                            stringBuffer.append(EntityUtils.toString(httpEntity));
                        } else {    // 否则使用IO流来读取
                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(httpEntity.getContent(), "UTF-8"));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuffer.append(line);
                            }
                            bufferedReader.close();
                            responseParams = stringBuffer.toString();
                        }
                        endTime = System.nanoTime();
                    }
                } catch (Exception e) {
                    endTime = System.nanoTime();

                } finally {
                    closeableHttpResponse.close();
                }
            } else {
                endTime = System.nanoTime();
                httpPost.abort();

            }
        } catch (IOException e) {
            endTime = System.nanoTime();

        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
            }
        }

        return responseParams;
    }

    /**
     * 向指定 URL 发送 GET请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String sendGet(String strUrl, String requestParams) {

        String responseParams = "";
        BufferedReader bufferedReader = null;
        try {
            String strRequestUrl = strUrl;
            if(!strRequestUrl.endsWith("?")){
                strRequestUrl += "?";
            }
            URL url = new URL(strRequestUrl + requestParams);
            URLConnection urlConnection = url.openConnection();    // 打开与 URL 之间的连接

            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            urlConnection.connect();    // 建立连接

            Map<String, List<String>> map = urlConnection.getHeaderFields();    // 获取所有响应头字段

            // 使用BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                responseParams += strLine;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        return responseParams;
    }

    /**
     * 向指定 URL 发送 GET请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String sendGetWithHeader(String strUrl, String requestParams,String tokenJson) {

        String responseParams = "";
        BufferedReader bufferedReader = null;
        try {
            String strRequestUrl = strUrl;
            if(!strRequestUrl.endsWith("?")){
                strRequestUrl += "?";
            }
            URL url = new URL(strRequestUrl + requestParams);
            URLConnection urlConnection = url.openConnection();    // 打开与 URL 之间的连接

            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            urlConnection.addRequestProperty("x-app-auth",tokenJson);

            urlConnection.connect();    // 建立连接

            Map<String, List<String>> map = urlConnection.getHeaderFields();    // 获取所有响应头字段

            // 使用BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                responseParams += strLine;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        return responseParams;
    }

//    /**
//     * 向指定 URL 发送 GET请求
//     *
//     * @param strUrl        发送请求的 URL
//     * @param requestParams 请求参数
//     * @return 远程资源的响应结果
//     */
//    public static String sendGetWithHeader(String strUrl, String requestParams,String tokenJson) {
//
//        // 获取连接客户端工具
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        String entityStr = null;
//        CloseableHttpResponse response = null;
//
//        try {
//            String strRequestUrl = strUrl;
//            if(!strRequestUrl.endsWith("?")){
//                strRequestUrl += "?";
//            }
//            /*
//             * 由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
//             */
//            URIBuilder uriBuilder = new URIBuilder(strUrl);
//            /** 第一种添加参数的形式 offset=0&max=1&dir=fwd&incl=posts|stats&merge=shares
//             * */
//          uriBuilder.addParameter("offset", "0");
//          uriBuilder.addParameter("max", "1");
//            uriBuilder.addParameter("dir", "fwd");
//            uriBuilder.addParameter("incl", "posts");
//            uriBuilder.addParameter("merge", "shares");
////            uriBuilder.addParameter("max", "1");
//            /** 第二种添加参数的形式 */
////            List<NameValuePair> list = new LinkedList<>();
////            BasicNameValuePair param1 = new BasicNameValuePair("page", page);
////            BasicNameValuePair param2 = new BasicNameValuePair("size", size);
////            list.add(param1);
////            list.add(param2);
////            uriBuilder.setParameters(list);
//
//            // 根据带参数的URI对象构建GET请求对象
//            HttpGet httpGet = new HttpGet(uriBuilder.build());
//
//            /*
//             * 添加请求头信息
//             */
//            // 浏览器表示
//            httpGet.addHeader("x-app-auth", tokenJson);
//            // 传输的类型
//            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
//
//            // 执行请求
//            response = httpClient.execute(httpGet);
//            // 获得响应的实体对象
//            HttpEntity entity = response.getEntity();
//            // 使用Apache提供的工具类进行转换成字符串
//            entityStr = EntityUtils.toString(entity, "UTF-8");
//        } catch (ClientProtocolException e) {
//            System.err.println("Http协议出现问题");
//            e.printStackTrace();
//        } catch (ParseException e) {
//            System.err.println("解析错误");
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            System.err.println("URI解析异常");
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("IO异常");
//            e.printStackTrace();
//        } finally {
//            // 释放连接
//            if (null != response) {
//                try {
//                    response.close();
//                    httpClient.close();
//                } catch (IOException e) {
//                    System.err.println("释放连接出错");
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return entityStr;
//
//    }
}