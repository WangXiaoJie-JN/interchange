/*
 * @(#)HopiRequest.java 2018-02-07
 *
 * Copyright (c) 2011-2018 杭州湖畔网络技术有限公司
 * 保留所有权利
 * 本软件为杭州湖畔网络技术有限公司所有及包含机密信息，须遵守其相关许可证条款进行使用。
 * Copyright (c) 2011-2014 HUPUN Network Technology CO.,LTD.
 * All rights reserved.
 * This software is the confidential and proprietary information of HUPUN
 * Network Technology CO.,LTD("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with HUPUN.
 * Website：http://www.hupun.com
 */
package com.viewcent.data.interchange.hupun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 接口请求顺
 * 
 * @author demon 2018-02-07
 */
public class HopiRequest
{
    
    static final Charset                   charset = StandardCharsets.UTF_8;
    
    static final Logger                    logger  = LoggerFactory.getLogger(HopiRequest.class);
    
    private static Reference<ObjectMapper> ref;
    
    protected final String                 gateway;
    
    protected final String                 app;
    
    protected final String                 sercet;
    
    protected final String                 auth;
    
    protected final String                 kind;
    
    /**
     * 构造函数
     * 
     * @param gateway
     *            网关地址
     * @param app
     *            应用键值
     * @param sercet
     *            应用密钥
     */
    public HopiRequest(String gateway, String app, String sercet)
    {
        this(gateway, app, sercet, null);
    }
    
    /**
     * 构造函数
     * 
     * @param gateway
     *            网关地址
     * @param app
     *            应用键值
     * @param sercet
     *            应用密钥
     * @param auth
     *            授权码
     */
    public HopiRequest(String gateway, String app, String sercet, String auth)
    {
        this(gateway, app, sercet, auth, null);
    }
    
    /**
     * 构造函数
     * 
     * @param gateway
     *            网关地址
     * @param app
     *            应用键值
     * @param sercet
     *            应用密钥
     * @param auth
     *            授权码
     * @param kind
     *            签名方式
     */
    public HopiRequest(String gateway, String app, String sercet, String auth, String kind)
    {
        this.gateway = gateway;
        this.app = app;
        this.sercet = sercet;
        this.auth = auth;
        this.kind = kind;
    }
    
    /**
     * 执行请求
     * 
     * @param path
     *            接口路径
     * @param parameters
     *            参数集
     * @return 请求结果
     * @throws IOException
     */
    public String request(String path, Map<String, Object> parameters) throws IOException
    {
        HttpURLConnection conn = connect(path);
        String content = parameter(parameters);
        post(conn, content);
        String r = read(conn);
        logger.debug("Response : " + r);
        return r;
    }
    
    /**
     * 连接服务
     * 
     * @param path
     *            接口路径
     * @return 连接
     * @throws IOException
     */
    public HttpURLConnection connect(String path) throws IOException
    {
        String url = url(path);
        logger.debug("Connect to " + url);
        URL u = new URL(url);
        HttpURLConnection conn = null;
        if ("https".equals(u.getProtocol()))
        {
            SSLContext ctx = ssl();
            HttpsURLConnection connHttps = (HttpsURLConnection) u.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;// 默认都认证通过
                }
            });
            conn = connHttps;
        }
        else
        {
            conn = (HttpURLConnection) u.openConnection();
        }
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "Jave-IO");
        return conn;
    }
    
    /**
     * 生成请求报文
     * 
     * @param parameters
     *            参数集
     * @return 报文
     * @throws IOException
     */
    public String parameter(Map<String, Object> parameters) throws IOException
    {
        Map<String, String> tree = new TreeMap();
        tree.put("_app", app);
        tree.put("_t", String.valueOf(System.currentTimeMillis() / 1000));
        if (auth != null)
            tree.put("_s", auth);
        for (Entry<String, Object> en : parameters.entrySet())
        {
            Object v = en.getValue();
            if (v == null || en.getKey() == null)
                continue;
            String s = null;
            if (v instanceof CharSequence)
                s = String.valueOf(v);
            else
                s = string(v);
            tree.put(en.getKey(), s);
        }
        sign(tree);
        return join(tree);
    }
    
    /**
     * 发送请求报文
     * 
     * @param conn
     *            连接
     * @param content
     *            报文内容
     * @throws IOException
     */
    public void post(HttpURLConnection conn, String content) throws IOException
    {
        byte[] bs = content.getBytes(charset);
        logger.info("POST : " + content);
        conn.setDoOutput(true);
        boolean gzip = true;
        conn.setRequestProperty("Accept-Encoding", "compress,gzip");
        if ((gzip = bs.length > 180))
            conn.setRequestProperty("Content-Encoding", "gzip"); // 超过 180 个字符使用 GZIP 压缩报文
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        OutputStream os = null;
        try
        {
            os = conn.getOutputStream();
            if (gzip)
                os = new GZIPOutputStream(os);
            int len = bs.length, p = 512;
            for (int i = 0; i < len; i += p)
            {
                os.write(bs, i, Math.min(p, len - i));
                os.flush();
            }
        }
        finally
        {
            if (os != null)
                os.close();
        }
    }
    
    /**
     * 读取响应内容
     * 
     * @param conn
     *            连接
     * @return 响应内容
     * @throws IOException
     */
    public String read(HttpURLConnection conn) throws IOException
    {
        int code = conn.getResponseCode();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            if (code < 300)
                read(conn, bos);
            else
                read(conn.getErrorStream(), bos);
            if (bos.size() < 1 || code >= 500)
                return String.valueOf(code);
            else
                return new String(bos.toByteArray(), charset);
        }
        finally
        {
            bos.close();
        }
    }
    
    /**
     * 生成签名
     * 
     * @param tree
     *            参数集
     * @throws IOException
     */
    void sign(Map<String, String> tree) throws IOException
    {
        String s = null;
        if ("hmac".equals(kind))
        {
            s = hmac(tree);
            tree.put("_sign_kind", "hmac");
        }
        else
        {
            s = md5(tree);
        }
        logger.debug("Sign : " + s);
        tree.put("_sign", s);
    }
    
    /**
     * 对象转换 JSON 串
     * 
     * @param v
     *            数据对象
     * @return JSON 串
     */
    String string(Object v)
    {
        ObjectMapper om = ref == null ? null : ref.get();
        if (om == null)
        {
            om = new ObjectMapper();
            om.configure(com.fasterxml.jackson.databind.MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, false);
            om.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            om.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            om.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
            om.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
            ref = new WeakReference(om);
        }
        try
        {
            return om.writeValueAsString(v);
        }
        catch (JsonProcessingException e)
        {
            throw new UnsupportedOperationException(e);
        }
    }
    
    /**
     * 转换十六进制字符串
     * 
     * @param bs
     *            字节集
     * @return 字符串
     */
    private String hex(byte[] bs)
    {
        StringBuilder buf = new StringBuilder(bs.length * 2);
        int bit = 0;
        for (byte b : bs)
        {
            bit = (b & 0x0f0) >> 4;
            buf.append(Integer.toHexString(bit));
            bit = (b & 0x0f);
            buf.append(Integer.toHexString(bit));
        }
        return buf.toString();
    }
    
    /**
     * 生成 HMAC 签名
     * 
     * @param tree
     *            参数集
     * @return 签名
     * @throws IOException
     */
    private String hmac(Map<String, String> tree) throws IOException
    {
        String content = join(tree); // 按参数名字母顺序拼接 URL 参数串，如 key1=value1&key2=value2
        try
        {
            Mac mac = Mac.getInstance("HmacMD5"); // 采用 HmacMD5 加密算法
            SecretKeySpec secretKey = new SecretKeySpec(sercet.getBytes(charset), mac.getAlgorithm());
            mac.init(secretKey); // 以应用密钥作为加密密钥
            
            byte[] hash = mac.doFinal(content.getBytes(charset)); // 计算加密 Hmac 值
            return hex(hash); // 将结果转为十六进制字符串
        }
        catch (Exception e)
        {
            if (e instanceof IOException)
                throw (IOException) e;
            else
                throw new IOException(e);
        }
    }
    
    /**
     * 是否 GZIP 压缩
     * 
     * @param ec
     *            类型
     * @return 是、否
     */
    private boolean isGzip(String ec)
    {
        return ec != null && Pattern.compile("(?<=^|[\\s\\p{Punct}])gzip(?=$|[\\s\\p{Punct}])").matcher(ec.toLowerCase()).find();
    }
    
    /**
     * 连接参数串
     * 
     * @param cs
     *            参数集
     * @return 参数串
     * @throws IOException
     */
    private String join(Map<String, String> cs) throws IOException
    {
        StringBuilder buf = new StringBuilder();
        for (Entry<String, String> en : cs.entrySet())
        {
            if (buf.length() > 0)
                buf.append('&');
            buf.append(URLEncoder.encode(en.getKey(), charset.name()));
            buf.append('=');
            buf.append(URLEncoder.encode(en.getValue(), charset.name()));
        }
        return buf.toString();
    }
    
    /**
     * 生成 MD5 签名
     * 
     * @param tree
     *            参数集
     * @return 签名
     * @throws IOException
     */
    private String md5(Map<String, String> tree) throws IOException
    {
        String content = join(tree); // 按参数名字母顺序拼接 URL 参数串，如 key1=value1&key2=value2
        content = sercet + content + sercet; // 将参数串前后拼上密钥 如 ${secret}key1=value1&key2=value2${secret}
        try
        {
            byte[] bs = MessageDigest.getInstance("MD5").digest(content.getBytes(charset)); // 计算 MD5 值
            return hex(bs); // 将 MD5 值转为十六进制字符串
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new UnsupportedOperationException(e);
        }
    }
    
    /**
     * 读取响应报文
     * 
     * @param conn
     *            连接
     * @param os
     *            输出流
     * @throws IOException
     */
    private void read(HttpURLConnection conn, OutputStream os) throws IOException
    {
        InputStream is = conn.getInputStream();
        try
        {
            if (isGzip(conn.getHeaderField("Content-Encoding")))
                is = new GZIPInputStream(is); // 检查是否 GZIP 压缩
            read(is, os);
        }
        finally
        {
            if (is != null)
                is.close();
        }
    }
    
    /**
     * 读取内容
     * 
     * @param is
     *            内容流
     * @param os
     *            输出流
     * @throws IOException
     */
    private void read(InputStream is, OutputStream os) throws IOException
    {
        byte[] bs = new byte[64];
        try
        {
            for (int r = 0; (r = is.read(bs)) != -1;)
            {
                os.write(bs, 0, r);
                os.flush();
            }
        }
        finally
        {
            if (is != null)
                is.close();
        }
    }
    
    /**
     * 生成 SSL 内容
     * 
     * @return SSL 内容
     * @throws IOException
     */
    private SSLContext ssl() throws IOException
    {
        SSLContext ctx = null;
        try
        {
            try
            {
                ctx = SSLContext.getInstance("TLSv1.2");
            }
            catch (NoSuchAlgorithmException e)
            {
                ctx = SSLContext.getInstance("TLS");
            }
            ctx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                {
                }
                
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                {
                }
                
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            } }, new SecureRandom());
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        return ctx;
    }
    
    /**
     * 连接网址
     * 
     * @param path
     *            路径
     * @return 网址
     */
    private String url(String path)
    {
        StringBuilder buf = new StringBuilder();
        buf.append(gateway);
        buf.append(path);
        return buf.toString();
    }
}
