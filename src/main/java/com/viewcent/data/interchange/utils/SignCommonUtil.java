package com.viewcent.data.interchange.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class SignCommonUtil
{
    
    static Logger logger = LoggerFactory.getLogger(SignCommonUtil.class);
    
    public static String CreateNoncestr(int length)
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++)
        {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }
    
    /**
     * 创建请求签名
     * 
     * @param apiSecret
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createWanliniuSign(String apiSecret, String characterEncoding, SortedMap<Object, Object> parameters)
    {
        // 根据参数名称将所有请求参数按照字母先后顺序排序
        // 参数名和参数值链接后，得到拼装字符串bar=2&baz=3&foo=1(key和value需要进行url编码再拼接)
        // 系统目前支持MD5加密方式:将密钥 拼接到参数字符串头、尾进行md5加密后，得到32位结果，再转成十六进制字符串，再转化成大写
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        sb.append(apiSecret);
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v))
            {
                if (v instanceof String)
                {
                    v = urlEncodeUTF8(String.valueOf(v));
                }
                sb.append(urlEncodeUTF8(k) + "=" + v + "&");
            }
        }
        sb = new StringBuffer(sb.substring(0, sb.length() - 1));
        sb.append(apiSecret);
        logger.info("sign_params:" + sb.toString());
        String sign = SignMD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        logger.info("sign:" + sign);
        return sign;
    }
    
    public static String createSignBySHA256(String apiKey, SortedMap<Object, Object> parameters) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        String sign = EncryptUtils.encryptHmacSha256(sb.toString(), apiKey).toUpperCase();
        return sign;
    }
    
    public static String urlEncodeUTF8(String source)
    {
        String result = source;
        try
        {
            result = java.net.URLEncoder.encode(source, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
