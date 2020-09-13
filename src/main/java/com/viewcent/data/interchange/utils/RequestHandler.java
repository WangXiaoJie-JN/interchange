
package com.viewcent.data.interchange.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class RequestHandler
{
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    
    public String doGet(String url) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("GET: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("GET request for url " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        return getResultString(client, request);
    }
    
    public String doGet(String url, RequestConfig config) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("GET: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("GET request for url " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setConfig(config);
        return getResultString(client, request);
    }
    
    public String doGet(String url, Map<String, String> requestHeaders) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("GET: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("GET request for url " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        if (requestHeaders != null)
        {
            for (String key : requestHeaders.keySet())
            {
                request.setHeader(key, requestHeaders.get(key));
            }
        }
        return getResultString(client, request);
    }
    
    public String doGet(String url, CloseableHttpClient client) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("GET: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("GET request for url " + url);
        HttpGet request = new HttpGet(url);
        return getResultString(client, request);
    }
    
    public String doGet(String url, Map<String, String> requestHeaders, Map<String, String> requestParam)
            throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("GET: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("GET request for url " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(buildUrlWithParam(url, requestParam));
        if (requestHeaders != null)
        {
            for (String key : requestHeaders.keySet())
            {
                request.setHeader(key, requestHeaders.get(key));
            }
        }
        return getResultString(client, request);
    }
    
    private String buildUrlWithParam(String url, Map<String, String> param) throws UnsupportedEncodingException
    {
        if (StringUtils.isEmpty(url))
        {
            return null;
        }
        if (null != param)
        {
            StringBuilder sbQuery = new StringBuilder();
            for (Entry<String, String> query : param.entrySet())
            {
                if (0 < sbQuery.length())
                {
                    sbQuery.append("&");
                }
                if (StringUtils.isEmpty(query.getKey()) && !StringUtils.isEmpty(query.getValue()))
                {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isEmpty(query.getKey()))
                {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isEmpty(query.getValue()))
                    {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length())
            {
                url = url + "?" + sbQuery;
            }
        }
        return url;
    }
    
    public String doPost(String url, Map<String, String> parameters) throws Exception
    {
        return doPost(url, new UrlEncodedFormEntity(convert(parameters), "utf-8"), null);
    }
    
    public String doPost(String url, List<NameValuePair> parameters) throws Exception
    {
        return doPost(url, new UrlEncodedFormEntity(parameters, "utf-8"), null);
    }
    
    public String doPost(String url, List<NameValuePair> parameters, CloseableHttpClient client) throws Exception
    {
        return doPost(url, new UrlEncodedFormEntity(parameters, "utf-8"), client);
    }
    
    public String doPostWithJson(String url, String param) throws Exception
    {
        return doPost(url, param, "application/json");
    }
    
    public String doPostWithSoap(String url, String param) throws Exception
    {
        return doPost(url, param, "text/xml");
    }
    
    public String doPost(String url, String param, String mime) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("POST: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("POST request for url: " + url + " parameters: " + param);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-type", new StringBuffer(mime).append(";charset=utf-8").toString());
        httpPost.setHeader("Accept", mime);
        
        HttpEntity entity = new StringEntity(param, "utf-8");
        httpPost.setEntity(entity);
        return getResultString(client, httpPost);
    }
    
    public String doPut(String url, String jsonParameter) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("PUT: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("PUT request for url: " + url + " parameters: " + jsonParameter);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        httpPut.addHeader("Content-type", "application/json; charset=utf-8");
        httpPut.setHeader("Accept", "application/json");
        
        HttpEntity entity = new StringEntity(jsonParameter, "utf-8");
        httpPut.setEntity(entity);
        return getResultString(client, httpPut);
    }
    
    public String doDelete(String url) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("DELETE: url is empty, just return null");
            return null;
        }
        
        LOGGER.debug("DELETE request for url " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        return getResultString(client, httpDelete);
    }
    
    private String getResultString(CloseableHttpClient client, HttpRequestBase method) throws Exception
    {
        CloseableHttpResponse response = null;
        StringBuffer textView = new StringBuffer();
        
        int responseCode = 0;
        try
        {
            response = client.execute(method);
            if (response == null)
            {
                return null;
            }
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200 || responseCode == 204)
            {
                HttpEntity httpEntity = response.getEntity();
                // Get the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "utf-8"));
                
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    textView.append(line);
                }
                EntityUtils.consume(httpEntity);
                return textView.toString();
            }
            else
            {
                // only record log here
                LOGGER.error("Failed to request resource, response code: " + responseCode + " url: "
                        + method.getURI().getPath());
                
                JSONObject returnResponse = new JSONObject();
                returnResponse.put("responseCode", responseCode);
                
                HttpEntity httpEntity = response.getEntity();
                // Get the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "utf-8"));
                
                StringBuffer rspText = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    rspText.append(line);
                }
                EntityUtils.consume(httpEntity);
                
                returnResponse.put("response", rspText.toString());
                LOGGER.info("url={}, set the response={}", method.getURI(), returnResponse);
                return returnResponse.toString();
            }
            
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (null != response)
                {
                    response.close();
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
        
    }
    
    private List<NameValuePair> convert(Map<String, String> parameters)
    {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Entry<String, String> parameter : parameters.entrySet())
        {
            if (parameter.getValue() == null)
            {
                continue;
            }
            String value = String.valueOf(parameter.getValue());
            System.err.println(parameter.getKey() + "-" + value);
            NameValuePair pair = new BasicNameValuePair(parameter.getKey(), value);
            pairs.add(pair);
        }
        
        return pairs;
    }
    
    private String doPost(String url, HttpEntity entity, CloseableHttpClient client) throws Exception
    {
        if (StringUtils.isEmpty(url))
        {
            LOGGER.error("POST: url is empty, just return null");
            return null;
        }
        LOGGER.debug("POST request for url: {}", url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return getResultString(client == null ? HttpClients.createDefault() : client, httpPost);
    }
    
}
