package com.viewcent.data.interchange;

import com.viewcent.data.interchange.entity.*;
import com.viewcent.data.interchange.service.impl.MrOrderInfoModelServiceImpl;
import com.viewcent.data.interchange.service.impl.MrOrderProductInfoModelServiceImpl;
import com.viewcent.data.interchange.service.impl.MrProductInfoModelServiceImpl;
import com.viewcent.data.interchange.service.impl.MrProductTypeInfoModelServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest(classes = InterchangeApplication.class)
@RunWith(SpringRunner.class)
public class InterchangeSyncTest
{
    Logger                                     logger = LoggerFactory.getLogger(InterchangeSyncTest.class);
    
    @Autowired
    private MrOrderInfoModelServiceImpl        mrOrderInfoModelService;
    
    @Autowired
    private MrOrderProductInfoModelServiceImpl mrOrderProductInfoModelService;
    
    @Autowired
    private MrProductInfoModelServiceImpl      mrProductInfoModelService;
    
    @Autowired
    private MrProductTypeInfoModelServiceImpl  mrProductTypeInfoModelService;
    
    /**
     * 可作为页面选项插入字段
     */
    @Test
    public void saveOrderModel()
    {
        
        MrOrderInfoModel orderInfoModel = new MrOrderInfoModel();
        orderInfoModel.setOrderNo("trade_no");
        orderInfoModel.setStoreNo("shop_name");
        orderInfoModel.setTenantId(null);
        orderInfoModel.setDataSource("万里牛");
        orderInfoModel.setOrderStatus(null);
        orderInfoModel.setOrderCreateTime("create_time");
        orderInfoModel.setPayType("reciver_money_account_list.pay_type_name");
        
        orderInfoModel.setRealFee("total_money");
        orderInfoModel.setTotalFee("total_money+discount_fee");
        orderInfoModel.setOperName("oper_name");
        orderInfoModel.setSalesmanName("salesman_name");
        orderInfoModel.setBuyerPhone("buyer_moblie");
        orderInfoModel.setTradeNo(null);
        orderInfoModel.setPayAccount("reciver_money_account_list.account_name");
        orderInfoModel.setMemberNo("buyer_name");
        orderInfoModel.setRemark("buyer_remark");
        
        boolean insertResult = mrOrderInfoModelService.save(orderInfoModel);
        if (!insertResult)
        {
            logger.error("插入数据失败");
        }
    }
    
    @Test
    public void saveOrderProductModel()
    {
        
        MrOrderProductInfoModel orderProductInfoModel = new MrOrderProductInfoModel();
        orderProductInfoModel.setOrderNo("trade_no");
        orderProductInfoModel.setProductNo("pos_order_list.goods_code");
        orderProductInfoModel.setProductNum("pos_order_list.num");
        orderProductInfoModel.setDataSource("万里牛");
        boolean insertResult = mrOrderProductInfoModelService.save(orderProductInfoModel);
        if (!insertResult)
        {
            logger.error("插入数据失败");
        }
    }
    
    @Test
    public void saveProductModel()
    {
        MrProductInfoModel productInfoModel = new MrProductInfoModel();
        // PRODUCT_NO
        productInfoModel.setProductNo("goods_code");
        // TENANT_ID
        productInfoModel.setTenantId(null);
        // DATA_SOURCE
        productInfoModel.setDataSource("万里牛");
        // PRODUCT_NAME
        productInfoModel.setProductName("goods_name");
        // PRICE
        productInfoModel.setPrice("specs.sale_price");
        // UNIT
        productInfoModel.setUnit("unit_name");
        // PIC
        productInfoModel.setPic("pic");
        // PRODUCT_CREATE_TIME
        productInfoModel.setProductCreateTime(null);
        // product_type_no
        productInfoModel.setProductTypeNo("catagory_id");
        // REMARK
        productInfoModel.setRemark("remark");
        // CREATE_TIME
        productInfoModel.setCreateTime(LocalDateTime.now());
        
        mrProductInfoModelService.save(productInfoModel);
    }
    
    @Test
    public void saveProductTypeModel()
    {
        MrProductTypeInfoModel mrProductTypeInfoModel = new MrProductTypeInfoModel();
        // TYPE_NO
        mrProductTypeInfoModel.setTypeNo("catagoryid");
        // TYPE_NAME
        mrProductTypeInfoModel.setTypeName("catagory_name");
        // DATA_SOURCE
        mrProductTypeInfoModel.setDataSource("万里牛");
        // REMARK
        mrProductTypeInfoModel.setRemark(null);
        mrProductTypeInfoModelService.save(mrProductTypeInfoModel);
    }
}
