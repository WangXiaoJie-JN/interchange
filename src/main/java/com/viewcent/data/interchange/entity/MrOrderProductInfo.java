package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单产品信息表
 * </p>
 *
 * @author jobob
 * @since 2020-08-27
 */
@TableName("mr_order_product_info")
public class MrOrderProductInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long              id;
    
    /**
     * 订单ID
     */
    private String            orderNo;
    
    /**
     * 产品ID
     */
    private String            productNo;
    
    /**
     * 购买数量
     */
    private Integer           productNum;
    
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime     createTime;
    
    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime     updateTime;
    
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
    
    public String getProductNo()
    {
        return productNo;
    }
    
    public void setProductNo(String productNo)
    {
        this.productNo = productNo;
    }
    
    public Integer getProductNum()
    {
        return productNum;
    }
    
    public void setProductNum(Integer productNum)
    {
        this.productNum = productNum;
    }
    
    public LocalDateTime getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime)
    {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime)
    {
        this.updateTime = updateTime;
    }
}
