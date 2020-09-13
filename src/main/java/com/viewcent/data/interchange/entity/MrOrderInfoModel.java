package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author jobob
 * @since 2020-08-26
 */
@TableName("mr_order_info_model")
public class MrOrderInfoModel implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    
    /**
     * 订单号
     */
    private String            orderNo;
    
    /**
     * 订单状态
     */
    private String            orderStatus;
    
    /**
     * 订单创建时间
     */
    private String            orderCreateTime;
    
    /**
     * 支付类型
     */
    private String            payType;
    
    /**
     * 实际支付费用
     */
    private String            realFee;
    
    /**
     * 总费用
     */
    private String            totalFee;
    
    /**
     * 收银员
     */
    private String            operName;
    
    /**
     * 导购员
     */
    private String            salesmanName;
    
    /**
     * 买家电话
     */
    private String            buyerPhone;
    
    /**
     * 支付交易号
     */
    private String            tradeNo;
    
    /**
     * 卡号
     */
    private String            payAccount;
    
    /**
     * 会员编号
     */
    private String            memberNo;
    
    /**
     * 门店编号
     */
    private String            storeNo;
    
    /**
     * 租户ID
     */
    private String            tenantId;
    
    /**
     * 备注
     */
    private String            remark;
    
    /**
     * 数据来源
     */
    private String            dataSource;
    
    private String            productList;
    
    private String            dataType;
    
    private String            outerName;
    
    private String            orderCreateTimeFormat;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getOrderNo()
    {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    
    public String getOrderStatus()
    {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }
    
    public String getOrderCreateTime()
    {
        return orderCreateTime;
    }
    
    public void setOrderCreateTime(String orderCreateTime)
    {
        this.orderCreateTime = orderCreateTime;
    }
    
    public String getPayType()
    {
        return payType;
    }
    
    public void setPayType(String payType)
    {
        this.payType = payType;
    }
    
    public String getRealFee()
    {
        return realFee;
    }
    
    public void setRealFee(String realFee)
    {
        this.realFee = realFee;
    }
    
    public String getTotalFee()
    {
        return totalFee;
    }
    
    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }
    
    public String getOperName()
    {
        return operName;
    }
    
    public void setOperName(String operName)
    {
        this.operName = operName;
    }
    
    public String getSalesmanName()
    {
        return salesmanName;
    }
    
    public void setSalesmanName(String salesmanName)
    {
        this.salesmanName = salesmanName;
    }
    
    public String getBuyerPhone()
    {
        return buyerPhone;
    }
    
    public void setBuyerPhone(String buyerPhone)
    {
        this.buyerPhone = buyerPhone;
    }
    
    public String getTradeNo()
    {
        return tradeNo;
    }
    
    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }
    
    public String getPayAccount()
    {
        return payAccount;
    }
    
    public void setPayAccount(String payAccount)
    {
        this.payAccount = payAccount;
    }
    
    public String getMemberNo()
    {
        return memberNo;
    }
    
    public void setMemberNo(String memberNo)
    {
        this.memberNo = memberNo;
    }
    
    public String getStoreNo()
    {
        return storeNo;
    }
    
    public void setStoreNo(String storeNo)
    {
        this.storeNo = storeNo;
    }
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getDataSource()
    {
        return dataSource;
    }
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public String getProductList()
    {
        return productList;
    }
    
    public void setProductList(String productList)
    {
        this.productList = productList;
    }
    
    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
    
    public String getOuterName()
    {
        return outerName;
    }
    
    public void setOuterName(String outerName)
    {
        this.outerName = outerName;
    }
    
    public String getOrderCreateTimeFormat()
    {
        return orderCreateTimeFormat;
    }
    
    public void setOrderCreateTimeFormat(String orderCreateTimeFormat)
    {
        this.orderCreateTimeFormat = orderCreateTimeFormat;
    }
    
}
