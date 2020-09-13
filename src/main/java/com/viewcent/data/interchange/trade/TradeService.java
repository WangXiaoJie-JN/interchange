package com.viewcent.data.interchange.trade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viewcent.data.interchange.entity.*;
import com.viewcent.data.interchange.hupun.HopiRequest;
import com.viewcent.data.interchange.service.impl.*;
import com.viewcent.data.interchange.utils.CalculateUtil;
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

import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TradeService
{
    Logger                                     logger       = LoggerFactory
            .getLogger(TradeService.class);
    
    @Value("${wanliniu.common_url:https://open-api.hupun.com/api}")
    private String                             wanliniu_common_url;
    
    @Value("${wanliniu.trade.query_url:/nr/openpostrade/querypostrade}")
    private String                             wanliniu_trade_query_url;
    
    @Value("${wanliniu._app:3023429349}")
    private String                             _app;
    
    @Value("${wanliniu._sign_kind:md5}")
    private String                             _sign_kind;
    
    @Value("${wanliniu._api_secret:011e00486efc6a85dc383b27cb177bc3}")
    private String                             apiSecret;
    
    @Value("${wanliniu.companyId:77482944481736}")
    private String                             company_id;
    
    @Value("${wanliniu.goods.shop_query_url:/nr/goods/shop/query}")
    private String                             wanliniu_goods_shop_query_url;
    
    @Value("${member.tenantId:065361}")
    private String                             tenantId;
    
    @Autowired
    private MrOrderInfoModelServiceImpl        mrOrderInfoModelService;
    
    @Autowired
    private MrOrderInfoServiceImpl             mrOrderInfoService;
    
    @Autowired
    private MrOrderProductInfoModelServiceImpl mrOrderProductInfoModelService;
    
    @Autowired
    private MrOrderProductInfoServiceImpl      mrOrderProductInfoService;
    
    @Autowired
    private MrStoreInfoServiceImpl             mrStoreInfoService;
    
    @Autowired
    private MrMemberInfoServiceImpl            mrMemberInfoService;
    
    @Autowired
    private MrStoreSyncInfoServiceImpl         mrStoreSyncInfoService;
    
    // 万里牛店铺信息
    public Map<String, MrStoreInfo>            storeInfoMap = new HashMap<>();
    
    @PostConstruct
    public void initStoreInfo()
    {
        // 条件：系统内的店铺编码必须和万里牛一致
        // 万里牛店铺信息保存
        Map<String, MrStoreInfo> wanliniuStoreMap = queryStoreSyncCode();
        // 系统内保存的店铺信息
        Map<String, MrStoreInfo> sysStoreMap = querySysStoreList();
        
        for (Map.Entry<String, MrStoreInfo> store : wanliniuStoreMap.entrySet())
        {
            String storeNo = store.getKey();
            if (sysStoreMap.containsKey(storeNo))
            {
                MrStoreInfo storeInfo = store.getValue();
                long sysStoreId = sysStoreMap.get(storeNo).getId();
                storeInfo.setId(sysStoreId);
                storeInfoMap.put(storeNo, storeInfo);
                logger.info(storeNo + "-" + storeInfoMap.get(storeNo).getStoreName());
            }
        }
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
    
    /**
     * @param modify_time:上次拉取时间，只能查近3个月
     * @param is_spit:是否拆分组合商品，默认不拆分
     * @param page_no:分页号
     * @param page_size:分页大小最大允许100
     * @param dataType:返回格式json/xml
     * @param tenantId:租户
     * @param dataSource:数据源
     * @return
     */
    
    public Response queryTradesInfo(Date modify_time,
            boolean is_spit,
            Integer page_no,
            Integer page_size,
            String dataType,
            String tenantId,
            String dataSource)
    {
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        requestParams.put("modify_time", modify_time);
        requestParams.put("is_spit", is_spit);
        requestParams.put("page_no", page_no);
        requestParams.put("page_size", page_size);
        
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_trade_query_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回数据：\n" + responseResult);
            JSONObject tradesObject = JSON.parseObject(responseResult);
            // code 返回0，执行成功
            if (tradesObject.getInteger("code") == 0)
            {
                JSONArray tradeOrderList = tradesObject.getJSONArray("data");
                int orderAcquiredSize = tradeOrderList.size();
                if (orderAcquiredSize > 0)
                {
                    response = analyzeData(responseResult, dataSource, dataType, tenantId);
                    if (response.getStatus() == ResponseStatus.ERROR)
                    {
                        return response;
                    }
                }
                logger.info("同步订单数据：page_no[" + page_no + "];page_size[" + page_size + "];acquired_size[" + orderAcquiredSize + "]");
                
                // 是否继续同步
                if (orderAcquiredSize < page_size)
                {
                    response.setStatus(ResponseStatus.OK);
                    response.setData(false);
                }
                else
                {
                    response.setStatus(ResponseStatus.OK);
                    response.setData(true);
                }
                
            }
            else
            {
                logger.error("万里牛返回交易信息失败：" + tradesObject.getString("message"));
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage(tradesObject.getString("message"));
            }
        }
        catch (Exception ex)
        {
            logger.error("error occurred while obtaining trade information:" + ex.getMessage());
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("获取交易信息出错");
        }
        return response;
    }
    
    /**
     * 查询万里牛对应店铺编码
     * 直接从万里牛获取
     * 
     * @return
     */
    public Map queryGoodsStoreCode()
    {
        Map<String, Object> requestParams = getInitParams();
        Map<String, MrStoreInfo> storeInfoList = new HashMap<>();
        // 公司ID
        requestParams.put("com_uid", company_id);
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_goods_shop_query_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回商铺编码：\n" + responseResult);
            JSONObject storeObject = JSON.parseObject(responseResult);
            // code 返回0，执行成功
            if (storeObject.getInteger("code") == 0)
            {
                JSONArray storeList = storeObject.getJSONArray("data");
                int storeSize = storeList.size();
                Iterator storeIterator = storeList.iterator();
                while (storeIterator.hasNext())
                {
                    JSONObject storeEntry = (JSONObject) storeIterator.next();
                    MrStoreInfo wanliniuStoreInfo = new MrStoreInfo();
                    // store_no
                    String store_no = storeEntry.getString("shop_uid");
                    wanliniuStoreInfo.setStoreNo(store_no);
                    // store_name
                    String store_name = storeEntry.getString("shop_name");
                    Integer shop_type = storeEntry.getInteger("shop_type");
                    wanliniuStoreInfo.setStoreName(store_name);
                    wanliniuStoreInfo.setRemark(shop_type.toString());
                    // status:1启用；0停用
                    Integer status = storeEntry.getInteger("status");
                    if (status != null && status == 1)
                    {
                        storeInfoList.put(store_no, wanliniuStoreInfo);
                    }
                }
                return storeInfoList;
            }
            else
            {
                logger.error("万里牛查询店铺信息出错：" + storeObject.getString("message"));
                return null;
            }
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining goods shop information:" + exception.getMessage());
            return null;
        }
    }
    
    /**
     * 直接在数据库取同步的门店数据
     * 
     * @return
     */
    public Map queryStoreSyncCode()
    {
        Map<String, MrStoreInfo> storeInfoList = new HashMap<>();
        
        QueryWrapper<MrStoreSyncInfo> storeSyncQueryWrapper = new QueryWrapper<>();
        storeSyncQueryWrapper.eq("tenant_id", tenantId);
        List<MrStoreSyncInfo> sysStoreList = mrStoreSyncInfoService.list(storeSyncQueryWrapper);
        for (MrStoreSyncInfo storeSyncInfo : sysStoreList)
        {
            MrStoreInfo wanliniuStoreInfo = new MrStoreInfo();
            // store_no
            String store_no = storeSyncInfo.getStoreNo();
            wanliniuStoreInfo.setStoreNo(store_no);
            // store_name
            String store_name = storeSyncInfo.getStoreName();
            String shop_type = storeSyncInfo.getStoreType();
            wanliniuStoreInfo.setStoreName(store_name);
            wanliniuStoreInfo.setRemark(shop_type);
            
            storeInfoList.put(store_no, wanliniuStoreInfo);
        }
        return storeInfoList;
        
    }
    
    /**
     * 查询系统店铺信息
     *
     * @return
     */
    public Map querySysStoreList()
    {
        Map<String, MrStoreInfo> sysStoreMap = new HashMap<>();
        
        QueryWrapper<MrStoreInfo> storeQueryWrapper = new QueryWrapper<>();
        storeQueryWrapper.eq("tenant_id", tenantId);
        List<MrStoreInfo> sysStoreList = mrStoreInfoService.list(storeQueryWrapper);
        for (MrStoreInfo sysStore : sysStoreList)
        {
            sysStoreMap.put(sysStore.getStoreNo(), sysStore);
        }
        return sysStoreMap;
    }
    
    /**
     * 数据插入数据库
     *
     * @param data
     * @param dataSource
     * @param dataType
     * @param tenantId
     * @return
     * @throws Exception
     */
    public Response analyzeData(String data, String dataSource, String dataType, String tenantId) throws Exception
    {
        logger.info("开始解析返回数据：");
        Response response = new Response();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 查询订单model
        QueryWrapper<MrOrderInfoModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_source", dataSource);
        queryWrapper.eq("data_type", dataType);
        queryWrapper.last("LIMIT 1");
        MrOrderInfoModel orderInfoModel = mrOrderInfoModelService.getOne(queryWrapper);
        if (orderInfoModel == null)
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不存在订单字段匹配模板");
            logger.error("不存在订单字段匹配模板");
            return response;
        }
        
        // 同步订单明细
        QueryWrapper<MrOrderProductInfoModel> productQueryWrapper = new QueryWrapper();
        productQueryWrapper.eq("data_source", dataSource);
        productQueryWrapper.eq("data_type", dataType);
        productQueryWrapper.last("LIMIT 1");
        MrOrderProductInfoModel orderProductInfoModel = mrOrderProductInfoModelService.getOne(productQueryWrapper);
        if (orderProductInfoModel == null)
        {
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage("不存在订单明细字段匹配模板；未插入订单产品明细");
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
        
        // 订单根目录
        String xpath = orderInfoModel.getOuterName();
        // 遍历订单
        List<Node> list = document.selectNodes(xpath);
        
        for (Node orderNode : list)
        {
            MrOrderInfo orderInfo = new MrOrderInfo();
            // ORDER_NO
            if (StringUtils.isNotBlank(orderInfoModel.getOrderNo()))
            {
                String orderNo = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getOrderNo());
                orderInfo.setOrderNo(orderNo);
            }
            // STORE_NO
            if (StringUtils.isNotBlank(orderInfoModel.getStoreNo()))
            {
                if (dataSource.equals("wanliniu"))
                {
                    // [线下门店]大兴店
                    String storeName = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getStoreNo());
                    boolean isExist = false;
                    for (Map.Entry<String, MrStoreInfo> store : storeInfoMap.entrySet())
                    {
                        MrStoreInfo currentEntry = store.getValue();
                        String entryName = currentEntry.getStoreName();
                        String remark = currentEntry.getRemark();
                        if ("-2".equals(remark))
                        {
                            entryName = "[线下门店]" + entryName;
                        }
                        else if ("106".equals(remark))
                        {
                            entryName = "[微盟网店]" + entryName;
                        }
                        if (entryName.equals(storeName))
                        {
                            String storeNo = store.getKey();
                            orderInfo.setStoreNo(storeNo);
                            isExist = true;
                            break;
                        }
                    }
                    // 没有对应的店铺
                    if (!isExist)
                    {
                        logger.info("没有对应店铺信息：" + storeName);
                        continue;
                    }
                }
                else
                {
                    String storeNo = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getStoreNo());
                    orderInfo.setStoreNo(storeNo);
                }
                
            }
            // TENANT_ID
            orderInfo.setTenantId(tenantId);
            // DATA_SOURCE
            orderInfo.setDataSource(dataSource);
            // ORDER_STATUS
            if (StringUtils.isNotBlank(orderInfoModel.getOrderStatus()))
            {
                String orderStatus = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getOrderStatus());
                orderInfo.setOrderStatus(orderStatus);
            }
            // ORDER_CREATE_TIME
            if (StringUtils.isNotBlank(orderInfoModel.getOrderCreateTime()))
            {
                String format = orderInfoModel.getOrderCreateTimeFormat();
                String orderCreateTime = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getOrderCreateTime());
                Date orderCreateDate = null;
                if ("timestamp".equals(format))
                {
                    orderCreateDate = new Date(Long.parseLong(orderCreateTime));
                }
                // 自带格式的日期字符串
                else
                {
                    SimpleDateFormat sdfformat = new SimpleDateFormat(format);
                    orderCreateDate = sdfformat.parse(orderCreateTime);
                }
                orderInfo.setOrderCreateTime(sdf.format(orderCreateDate));
            }
            // PAY_TYPE
            if (StringUtils.isNotBlank(orderInfoModel.getPayType()))
            {
                String payType = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getPayType());
                orderInfo.setPayType(payType);
            }
            // REAL_FEE
            if (StringUtils.isNotBlank(orderInfoModel.getRealFee()))
            {
                BigDecimal realFee = new BigDecimal(DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getRealFee()));
                orderInfo.setRealFee(realFee);
            }
            // TOTAL_FEE
            if (StringUtils.isNotBlank(orderInfoModel.getTotalFee()))
            {
                BigDecimal totalFee = acculateRealValue(orderNode, orderInfoModel.getTotalFee());
                orderInfo.setTotalFee(totalFee);
            }
            // OPER_NAME
            if (StringUtils.isNotBlank(orderInfoModel.getOperName()))
            {
                String operName = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getOperName());
                orderInfo.setOperName(operName);
            }
            // SALESMAN_NAME
            if (StringUtils.isNotBlank(orderInfoModel.getSalesmanName()))
            {
                String salesmanName = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getSalesmanName());
                orderInfo.setSalesmanName(salesmanName);
            }
            // BUYER_PHONE
            if (StringUtils.isNotBlank(orderInfoModel.getBuyerPhone()))
            {
                String buyerPhone = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getBuyerPhone());
                orderInfo.setBuyerPhone(buyerPhone);
            }
            // TRADE_NO
            if (StringUtils.isNotBlank(orderInfoModel.getTradeNo()))
            {
                String tradeNo = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getTradeNo());
                orderInfo.setTradeNo(tradeNo);
            }
            // PAY_ACCOUNT
            if (StringUtils.isNotBlank(orderInfoModel.getPayAccount()))
            {
                String payAccount = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getPayAccount());
                orderInfo.setPayAccount(payAccount);
            }
            // MEMBER_NO
            if (StringUtils.isNotBlank(orderInfoModel.getMemberNo()))
            {
                
                String memberNo = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getMemberNo());
                
                if (dataSource.equals("wanliniu") && "散客".equals(memberNo))
                {
                    orderInfo.setMemberNo(null);
                }
                else if (dataSource.equals("wanliniu") && !"散客".equals(memberNo))
                {
                    // 查询mr_member_info的member_no
                    QueryWrapper<MrMemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
                    memberInfoQueryWrapper.like("member_name", memberNo);
                    memberInfoQueryWrapper.eq("tenant_id", tenantId);
                    memberInfoQueryWrapper.last("LIMIT 1");
                    MrMemberInfo mrMemberInfo = mrMemberInfoService.getOne(memberInfoQueryWrapper);
                    if (mrMemberInfo != null)
                    {
                        orderInfo.setMemberNo(mrMemberInfo.getMemberNo());
                    }
                }
                else
                {
                    orderInfo.setMemberNo(memberNo);
                }
                
            }
            // REMARK
            if (StringUtils.isNotBlank(orderInfoModel.getRemark()))
            {
                String remark = DomUtil.getSingleNodeValue(orderNode, orderInfoModel.getRemark());
                orderInfo.setRemark(remark);
            }
            // CREATE_TIME
            // CREATE_USER
            // UPDATE_TIME
            // UPDATE_USER
            // tenant_id, order_no
            QueryWrapper<MrOrderInfo> tradeQueryWrapper = new QueryWrapper<>();
            tradeQueryWrapper.eq("order_no", orderInfo.getOrderNo());
            tradeQueryWrapper.eq("tenant_id", tenantId);
            mrOrderInfoService.saveOrUpdate(orderInfo, tradeQueryWrapper);
            logger.debug("保存订单数据：[" + orderInfo.getOrderNo() + "]");
            
            if (StringUtils.isNotBlank(orderInfoModel.getProductList()))
            {
                List<Node> orderProductNodes = orderNode.selectNodes(orderInfoModel.getProductList());
                for (Node orderProductNode : orderProductNodes)
                {
                    MrOrderProductInfo orderProductInfo = new MrOrderProductInfo();
                    orderProductInfo.setOrderNo(orderInfo.getOrderNo());
                    
                    String productName = orderProductInfoModel.getProductNo();
                    if (StringUtils.isNotBlank(productName))
                    {
                        String productEntryName = DomUtil.getSingleNodeValue(orderProductNode, productName);
                        orderProductInfo.setProductNo(productEntryName);
                    }
                    String productNumName = orderProductInfoModel.getProductNum();
                    if (StringUtils.isNotBlank(productNumName))
                    {
                        Double productEntryNum = Double.parseDouble(DomUtil.getSingleNodeValue(orderProductNode, productNumName));
                        int intValue = (int) productEntryNum.doubleValue();
                        orderProductInfo.setProductNum(intValue);
                    }
                    // order_no, product_no
                    QueryWrapper<MrOrderProductInfo> orderProductQueryWrapper = new QueryWrapper<>();
                    orderProductQueryWrapper.eq("order_no", orderProductInfo.getOrderNo());
                    orderProductQueryWrapper.eq("product_no", orderProductInfo.getProductNo());
                    mrOrderProductInfoService.saveOrUpdate(orderProductInfo, orderProductQueryWrapper);
                    logger.debug("保存订单产品数据：[" + orderInfo.getOrderNo() + "]");
                }
            }
        }
        response.setStatus(ResponseStatus.OK);
        return response;
    }
    
    /**
     * 同步万里牛店铺编码到数据库
     * 
     * @param dataSource
     * @param tenantId
     * @return
     */
    public Response syncGoodsStoreCode(String dataSource, String tenantId)
    {
        Response response = new Response();
        Map<String, Object> requestParams = getInitParams();
        // 公司ID
        requestParams.put("com_uid", company_id);
        HopiRequest request = new HopiRequest(wanliniu_common_url, _app, apiSecret);
        try
        {
            HttpURLConnection connect = request.connect(wanliniu_goods_shop_query_url);
            String content = request.parameter(requestParams);
            request.post(connect, content);
            String responseResult = request.read(connect);
            logger.info("万里牛返回商铺编码：\n" + responseResult);
            JSONObject storeObject = JSON.parseObject(responseResult);
            // code 返回0，执行成功
            if (storeObject.getInteger("code") == 0)
            {
                JSONArray storeList = storeObject.getJSONArray("data");
                int storeSize = storeList.size();
                Iterator storeIterator = storeList.iterator();
                while (storeIterator.hasNext())
                {
                    JSONObject storeEntry = (JSONObject) storeIterator.next();
                    MrStoreSyncInfo storeInfo = new MrStoreSyncInfo();
                    // store_no
                    String store_no = storeEntry.getString("shop_uid");
                    storeInfo.setStoreNo(store_no);
                    // store_name
                    String store_name = storeEntry.getString("shop_name");
                    storeInfo.setStoreName(store_name);
                    // shop_type:-2线下门店；106 微盟网店
                    String shopType = storeEntry.getString("shop_type");
                    storeInfo.setStoreType(shopType);
                    // status:1启用；0停用
                    String storeStatus = storeEntry.getString("status");
                    if (storeStatus == null || storeStatus.equals("0"))
                    {
                        continue;
                    }
                    storeInfo.setStoreStatus(storeStatus);
                    // tenantId
                    storeInfo.setTenantId(tenantId);
                    storeInfo.setDataSource(dataSource);
                    
                    QueryWrapper<MrStoreSyncInfo> storeQueryWrapper = new QueryWrapper<>();
                    storeQueryWrapper.eq("store_no", store_no);
                    storeQueryWrapper.eq("tenant_id", tenantId);
                    mrStoreSyncInfoService.saveOrUpdate(storeInfo, storeQueryWrapper);
                    logger.info("保存店铺信息：" + storeInfo.getStoreName());
                }
                response.setStatus(ResponseStatus.OK);
            }
            else
            {
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage(storeObject.getString("message"));
            }
            
        }
        catch (Exception exception)
        {
            logger.error("error occurred while obtaining goods shop information:" + exception.getMessage());
            exception.printStackTrace();
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage(exception.getMessage());
        }
        
        return response;
    }
    
    /**
     * 计算数值
     *
     * @param orderNode
     * @param cloumn
     * @return
     * @throws ScriptException
     */
    public BigDecimal acculateRealValue(Node orderNode, String cloumn) throws ScriptException
    {
        BigDecimal value = null;
        
        String[] col = cloumn.split("[\\+\\-\\*\\/\\(\\)]");
        String calString = cloumn;
        
        for (String c : col)
        {
            if (StringUtils.isNotBlank(c))
            {
                String realcValue = DomUtil.getSingleNodeValue(orderNode, c);
                calString = calString.replace(c, realcValue);
            }
        }
        value = CalculateUtil.calculate(calString);
        return value;
    }
}
