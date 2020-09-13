package com.viewcent.data.interchange.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viewcent.data.interchange.entity.MrProductInfo;
import com.viewcent.data.interchange.entity.MrProductInfoModel;
import com.viewcent.data.interchange.entity.MrProductTypeInfo;
import com.viewcent.data.interchange.entity.MrProductTypeInfoModel;
import com.viewcent.data.interchange.hupun.HopiRequest;
import com.viewcent.data.interchange.service.impl.*;
import com.viewcent.data.interchange.utils.DomUtil;
import com.viewcent.data.interchange.utils.Response;
import com.viewcent.data.interchange.utils.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GoodsService
{
    Logger                                    logger = LoggerFactory.getLogger(GoodsService.class);
    
    @Value("${wanliniu.common_url:https://open-api.hupun.com/api}")
    private String                            wanliniu_common_url;
    
    // 查询商品规格集合
    @Value("${wanliniu.goods.spec_query_url:/nr/goods/spec/open/query}")
    private String                            wanliniu_goods_specification_query_url;
    
    // 通过修改时间,查询出商品并带有规格列表
    @Value("${wanliniu.goods.query_with_date_url:/nr/goods/spec/open/query/goodswithspeclist}")
    private String                            wanliniu_goods_query_with_specilist_url;
    
    // 获取商品类目
    @Value("${wanliniu.goods.category_query_url:/nr/goods/catagory/querybyauth}")
    private String                            wanliniu_goods_category_query_url;
    
    @Value("${wanliniu._app:3023429349}")
    private String                            _app;
    
    @Value("${wanliniu._sign_kind:md5}")
    private String                            _sign_kind;
    
    @Value("${wanliniu._api_secret:011e00486efc6a85dc383b27cb177bc3}")
    private String                            apiSecret;
    
    @Autowired
    private MrProductInfoServiceImpl          mrProductInfoService;
    
    @Autowired
    private MrProductInfoModelServiceImpl     mrProductInfoModelService;
    
    @Autowired
    private MrProductTypeInfoServiceImpl      mrProductTypeInfoService;
    
    @Autowired
    private MrProductTypeInfoModelServiceImpl mrProductTypeInfoModelService;
    
    @Autowired
    private MrStoreInfoServiceImpl            mrStoreInfoService;
    
    /**
     * 查询商品规格集合
     * 
     * @param spec_code:规格编码
     * @param item_code:商品编码
     * @param modify_time:修改时间
     * @param bar_code:规格条码
     *            上面四个参数不能全部为空
     * @param page:当前页码
     * @param limit:每页大小
     * @return
     */
    public Response queryGoodsSpecification(Integer spec_code, String item_code, Date modify_time, String bar_code, Integer page, Integer limit)
    {
        Response response = new Response();
        
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("spec_code", spec_code);
        requestParams.put("item_code", item_code);
        requestParams.put("modify_time", modify_time);
        requestParams.put("bar_code", bar_code);
        requestParams.put("page", page);
        requestParams.put("limit", limit);
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_goods_specification_query_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("返回商品规格集合：\n" + responseResult);
            response.setStatus(ResponseStatus.OK);
        }
        catch (Exception ex)
        {
            logger.error("error occurred while obtaining goods specification information:" + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取商品规格出错");
        }
        
        return response;
    }
    
    /**
     * 获取商品类目
     * 无参数
     * 
     * @return
     */
    public Response queryGoodsCategoryInfo(String dataSource, String dataType)
    {
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_goods_category_query_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info(dataSource + "返回商品类别：\n" + responseResult);
            
            JSONObject productTypeObject = JSON.parseObject(responseResult);
            // code 返回0，执行成功
            if (productTypeObject.getInteger("code") == 0)
            {
                JSONArray productTypeList = productTypeObject.getJSONArray("data");
                int productTypeAcquiredSize = productTypeList.size();
                if (productTypeAcquiredSize > 0)
                {
                    response = analyzeProductTypeData(responseResult, dataSource, dataType);
                    if (response.getStatus() == ResponseStatus.ERROR)
                    {
                        return response;
                    }
                }
                logger.info("同步产品类别数据：[" + productTypeAcquiredSize + "]");
                response.setStatus(ResponseStatus.OK);
            }
            else
            {
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage(productTypeObject.getString("message"));
                logger.error(productTypeObject.getString("message"));
            }
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining goods category information:" + exception.getMessage());
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取商品类别出错");
            logger.error("获取商品类别出错");
        }
        return response;
    }
    
    /**
     * 查询出商品并带有规格列表
     *
     * @param spec_code:规格编码
     * @param item_code:商品编码
     * @param modify_time:修改时间
     * @param bar_code:规格条码
     *            上面四个参数不能全部为空
     * @param page:当前页码
     * @param limit:每页大小
     * @return
     */
    public Response queryGoodswithspeclist(Integer spec_code,
            String item_code,
            Date modify_time,
            String bar_code,
            Integer page,
            Integer limit,
            String dataSource,
            String dataType,
            String tenantId)
    {
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("spec_code", spec_code);
        requestParams.put("item_code", item_code);
        requestParams.put("modify_time", modify_time);
        requestParams.put("bar_code", bar_code);
        requestParams.put("page", page);
        requestParams.put("limit", limit);
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_goods_query_with_specilist_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回商品并带有规格列表：\n" + responseResult);
            JSONObject productsObject = JSON.parseObject(responseResult);
            // code 返回0，执行成功
            if (productsObject.getInteger("code") == 0)
            {
                JSONArray productList = productsObject.getJSONArray("data");
                int productAcquiredSize = productList.size();
                if (productAcquiredSize > 0)
                {
                    response = analyzeProductData(responseResult, dataSource, dataType, tenantId);
                    if (response.getStatus() == ResponseStatus.ERROR)
                    {
                        return response;
                    }
                }
                // 是否继续同步
                if (productAcquiredSize < limit)
                {
                    response.setStatus(ResponseStatus.OK);
                    response.setData(false);
                }
                else
                {
                    response.setStatus(ResponseStatus.OK);
                    response.setData(true);
                }
                logger.info("同步产品数据：page_no[" + page + "];page_size[" + limit + "];acquired_size[" + productAcquiredSize + "]");
                
            }
            else
            {
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage(productsObject.getString("message"));
                logger.error(productsObject.getString("message"));
            }
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining goods with specilist information:" + exception.getMessage());
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取商品并带有规格列表出错");
            logger.error("获取商品并带有规格列表出错");
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
    
    public Response analyzeProductData(String data, String dataSource, String dataType, String tenantId) throws Exception
    {
        Response response = new Response();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 查询订单model
        QueryWrapper<MrProductInfoModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_source", dataSource);
        queryWrapper.eq("data_type", dataType);
        queryWrapper.last("LIMIT 1");
        MrProductInfoModel productInfoModel = mrProductInfoModelService.getOne(queryWrapper);
        if (productInfoModel == null)
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不存在产品字段匹配模板");
            logger.error("不存在产品字段匹配模板");
            return response;
        }
        String xmlStr = "";
        Document document = null;
        if (dataType.equals("json"))
        {
            // json转dom
            xmlStr = DomUtil.json2Xml1(data);
            document = DomUtil.convertString2Dom(xmlStr);
        }
        else if (dataType.equals("xml"))
        {
            document = DomUtil.convertString2Dom(data);
        }
        else
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不支持此返回数据类型的解析");
            logger.error("不支持此返回数据类型的解析");
            return response;
        }
        
        // 产品根目录
        String xpath = productInfoModel.getOuterName();
        // 遍历产品
        List<Node> list = document.selectNodes(xpath);
        
        for (Node productNode : list)
        {
            MrProductInfo productInfo = new MrProductInfo();
            // PRODUCT_NO
            if (StringUtils.isNotBlank(productInfoModel.getProductNo()))
            {
                String productNo = DomUtil.getSingleNodeValue(productNode, productInfoModel.getProductNo());
                productInfo.setProductNo(productNo);
            }
            // TENANT_ID
            productInfo.setTenantId(tenantId);
            // DATA_SOURCE
            productInfo.setDataSource(dataSource);
            // PRODUCT_NAME
            if (StringUtils.isNotBlank(productInfoModel.getProductName()))
            {
                String productName = DomUtil.getSingleNodeValue(productNode, productInfoModel.getProductName());
                productInfo.setProductName(productName);
            }
            // PRICE
            if (StringUtils.isNotBlank(productInfoModel.getPrice()))
            {
                
                BigDecimal price = new BigDecimal(DomUtil.getSingleNodeValue(productNode, productInfoModel.getPrice()));
                productInfo.setPrice(price);
                
            }
            // UNIT
            if (StringUtils.isNotBlank(productInfoModel.getUnit()))
            {
                String unit = DomUtil.getSingleNodeValue(productNode, productInfoModel.getUnit());
                productInfo.setUnit(unit);
            }
            // PIC
            if (StringUtils.isNotBlank(productInfoModel.getPic()))
            {
                String pic = DomUtil.getSingleNodeValue(productNode, productInfoModel.getPic());
                productInfo.setPic(pic);
            }
            // PRODUCT_CREATE_TIME
            if (StringUtils.isNotBlank(productInfoModel.getProductCreateTime()))
            {
                String format = productInfoModel.getProductCreateTimeFormat();
                String productCreateTime = DomUtil.getSingleNodeValue(productNode, productInfoModel.getProductCreateTime());
                Date productCreateDate = null;
                if ("timestamp".equals(format))
                {
                    productCreateDate = new Date(Long.parseLong(productCreateTime));
                }
                // 自带格式的日期字符串
                else
                {
                    SimpleDateFormat sdfformat = new SimpleDateFormat(format);
                    productCreateDate = sdfformat.parse(productCreateTime);
                }
                productInfo.setProductCreateTime(sdf.format(productCreateDate));
            }
            // PRODUCT_TYPE_NO
            if (StringUtils.isNotBlank(productInfoModel.getProductTypeNo()))
            {
                String productTypeNo = DomUtil.getSingleNodeValue(productNode, productInfoModel.getProductTypeNo());
                productInfo.setProductTypeNo(productTypeNo);
            }
            // REMARK
            if (StringUtils.isNotBlank(productInfoModel.getRemark()))
            {
                String remark = DomUtil.getSingleNodeValue(productNode, productInfoModel.getRemark());
                productInfo.setRemark(remark);
            }
            // CREATE_TIME
            // mrProductInfoService.save(productInfo);
            QueryWrapper<MrProductInfo> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("product_no", productInfo.getProductNo());
            productQueryWrapper.eq("tenant_id", tenantId);
            boolean saveResult = mrProductInfoService.saveOrUpdate(productInfo, productQueryWrapper);
            logger.info("保存产品信息：[" + productInfo.getProductName() + "];result=" + saveResult);
        }
        response.setStatus(ResponseStatus.OK);
        return response;
    }
    
    public Response analyzeProductTypeData(String data, String dataSource, String dataType) throws Exception
    {
        Response response = new Response();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 查询订单model
        QueryWrapper<MrProductTypeInfoModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_source", dataSource);
        queryWrapper.eq("data_type", dataType);
        queryWrapper.last("LIMIT 1");
        MrProductTypeInfoModel productTypeInfoModel = mrProductTypeInfoModelService.getOne(queryWrapper);
        
        if (productTypeInfoModel == null)
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不存在产品类型字段匹配模板");
            logger.error("不存在产品类型字段匹配模板");
            return response;
        }
        String xmlStr = "";
        Document document = null;
        if (dataType.equals("json"))
        {
            // json转dom
            xmlStr = DomUtil.json2Xml1(data);
            document = DomUtil.convertString2Dom(xmlStr);
        }
        else if (dataType.equals("xml"))
        {
            document = DomUtil.convertString2Dom(data);
        }
        else
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不支持此返回数据类型的解析");
            logger.error("不支持此返回数据类型的解析");
            return response;
        }
        
        // 产品类别根目录
        String xpath = productTypeInfoModel.getOuterName();
        // 遍历产品
        List<Node> list = document.selectNodes(xpath);
        
        for (Node productTypeNode : list)
        {
            MrProductTypeInfo productTypeInfo = new MrProductTypeInfo();
            // TYPE_NO
            if (StringUtils.isNotBlank(productTypeInfoModel.getTypeNo()))
            {
                String typeNo = DomUtil.getSingleNodeValue(productTypeNode, productTypeInfoModel.getTypeNo());
                productTypeInfo.setTypeNo(typeNo);
            }
            // TYPE_NAME
            if (StringUtils.isNotBlank(productTypeInfoModel.getTypeName()))
            {
                String typeName = DomUtil.getSingleNodeValue(productTypeNode, productTypeInfoModel.getTypeName());
                productTypeInfo.setTypeName(typeName);
            }
            // DATA_SOURCE
            productTypeInfo.setDataSource(dataSource);
            // REMARK
            if (StringUtils.isNotBlank(productTypeInfoModel.getRemark()))
            {
                String remark = DomUtil.getSingleNodeValue(productTypeNode, productTypeInfoModel.getRemark());
                productTypeInfo.setRemark(remark);
            }
            // CREATE_TIME
            // UPDATE_TIME
            
            QueryWrapper<MrProductTypeInfo> productTypeQueryWrapper = new QueryWrapper<>();
            productTypeQueryWrapper.eq("type_no", productTypeInfo.getTypeNo());
            mrProductTypeInfoService.saveOrUpdate(productTypeInfo, productTypeQueryWrapper);
        }
        response.setStatus(ResponseStatus.OK);
        return response;
    }
}
