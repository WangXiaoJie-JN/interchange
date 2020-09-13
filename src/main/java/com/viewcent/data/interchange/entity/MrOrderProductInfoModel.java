package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("mr_order_product_info_model")
public class MrOrderProductInfoModel implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    private String            productNum;
    
    private String            dataSource;
    
    /**
     * 创建日期
     */
    private LocalDateTime     createTime;
    
    /**
     * 更新日期
     */
    private LocalDateTime     updateTime;
    
    private String            dataType;
    
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
    
    public String getProductNum()
    {
        return productNum;
    }
    
    public void setProductNum(String productNum)
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
    
    public String getDataSource()
    {
        return dataSource;
    }
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
}
