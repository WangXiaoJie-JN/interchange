package com.viewcent.data.interchange.goods;

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
public class WanliniuInventoryService
{
    Logger         logger = LoggerFactory.getLogger(WanliniuInventoryService.class);
    
    @Value("${wanliniu.common_url:https://open-api.hupun.com/api}")
    private String wanliniu_common_url;
    
    @Value("${wanliniu.goods.inventory_query_url:/nr/open/inventory/items/get/by/modifytime}")
    private String wanliniu_inventory_query_bymodifytime_url;
    
    @Value("${wanliniu._app:3023429349}")
    private String _app;
    
    @Value("${wanliniu._sign_kind:md5}")
    private String _sign_kind;
    
    @Value("${wanliniu._api_secret:011e00486efc6a85dc383b27cb177bc3}")
    private String apiSecret;
    
    /**
     * 根据修改时间获取库存
     * 
     * @param storage:仓库编码
     * @param modify_time
     * @param page_no
     * @param page_size
     * @return
     */
    public Response queryInventoryByModifytime(String storage, Date modify_time, Integer page_no, Integer page_size)
    {
        
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("storage", storage);
        requestParams.put("modify_time", modify_time);
        requestParams.put("page_no", page_no);
        requestParams.put("page_size", page_size);
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_inventory_query_bymodifytime_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回库存：\n" + responseResult);
            response.setStatus(ResponseStatus.OK);
        }
        catch (Exception ex)
        {
            logger.error("error occurred while obtaining  information:" + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取库存出错");
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
