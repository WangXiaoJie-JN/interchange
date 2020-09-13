/*
 * @Copyright (c) 2011-2020 杭州湖畔网络技术有限公司
 * 保留所有权利
 * 本软件为杭州湖畔网络技术有限公司所有及包含机密信息，须遵守其相关许可证条款进行使用。
 * Copyright (c) 2011-2020 HUPUN Network Technology CO.,LTD.
 * All rights reserved.
 * This software is the confidential and proprietary information of HUPUN
 * Network Technology CO.,LTD("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with HUPUN.
 * Website：http://www.hupun.com
 */
package com.viewcent.data.interchange.hupun;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class HopiTest
{
    
    public static void main(String[] args) throws IOException
    {
        Map<String, Object> params = new HashMap<>(16);
        params.put("is_spit", false);
        params.put("page_no", 1);
        params.put("page_size", 100);
        HopiRequest request = new HopiRequest("https://open-api.hupun.com/api", "3023429349", "011e00486efc6a85dc383b27cb177bc3");
        HttpURLConnection connect = request.connect("/nr/openpostrade/querypostrade");
        String content = request.parameter(params);
        request.post(connect, content);
        String response = request.read(connect);
        System.out.println("response:" + response);
    }
    
}
