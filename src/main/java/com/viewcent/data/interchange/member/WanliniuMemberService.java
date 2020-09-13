package com.viewcent.data.interchange.member;

import com.viewcent.data.interchange.hupun.HopiRequest;
import com.viewcent.data.interchange.utils.Response;
import com.viewcent.data.interchange.utils.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WanliniuMemberService
{
    Logger         logger = LoggerFactory.getLogger(WanliniuMemberService.class);
    
    @Value("${wanliniu.common_url:https://open-api.hupun.com/api}")
    private String wanliniu_common_url;
    
    @Value("${wanliniu.member.query_url:/crm/open/customer/querycustom}")
    private String wanliniu_member_query_url;
    
    @Value("${wanliniu.member.detail_query_url:/crm/open/membercenter/getmemberdetail}")
    private String wanliniu_member_detail_query_url;
    
    @Value("${wanliniu._app:3023429349}")
    private String _app;
    
    @Value("${wanliniu._sign_kind:md5}")
    private String _sign_kind;
    
    @Value("${wanliniu._api_secret:011e00486efc6a85dc383b27cb177bc3}")
    private String apiSecret;
    
    /**
     * 获取会员信息
     * 
     * @param nick_type:会员平台类型
     * @param nick_name:会员昵称
     * @param mobile:手机号
     * @param start_create_time:会员创建开始时间
     * @param end_create_time:会员创建结束时间
     * @param page_no:页码,取值范围:大于零的整数,默认1
     * @param page_size:每页条数。取值范围:大于零的整数,最大值50
     * @return
     */
    public Response queryMemberList(Integer nick_type,
            String nick_name,
            String mobile,
            Date start_create_time,
            Date end_create_time,
            Integer page_no,
            Integer page_size)
    {
        Response response = new Response();
        
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("nick_type", nick_type);
        requestParams.put("nick_name", nick_name);
        requestParams.put("mobile", mobile);
        requestParams.put("start_create_time", start_create_time);
        requestParams.put("end_create_time", end_create_time);
        requestParams.put("page_no", page_no);
        requestParams.put("page_size", page_size);
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_member_query_url);
            String content = request.parameter(requestParams);
            logger.info("params:" + content);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回会员信息：" + responseResult);
            response.setStatus(ResponseStatus.OK);
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining member information:" + exception.getMessage());
            exception.printStackTrace();
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取会员信息出错");
        }
        return response;
    }
    
    public Response queryMemberDetails(String custom_id, String source_type)
    {
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("custom_id", custom_id);
        requestParams.put("source_type", "");
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_member_detail_query_url);
            String content = request.parameter(requestParams);
            logger.info("params:" + content);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回会员详细信息：" + responseResult);
            response.setStatus(ResponseStatus.OK);
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining member details information:" + exception.getMessage());
            exception.printStackTrace();
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取会员信息出错");
        }
        return response;
    }
    
    public Map getInitParams()
    {
        Calendar calendar = Calendar.getInstance();
        Long _t = calendar.getTimeInMillis();
        Map<String, Object> commonParams = new HashMap<String, Object>();
        commonParams.put("_app", _app);
        commonParams.put("_t", _t);
        // commonParams.put("_sign_kind", _sign_kind);
        return commonParams;
    }
    
}
