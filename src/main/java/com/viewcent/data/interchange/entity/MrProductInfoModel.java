package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 产品信息表
 * </p>
 *
 * @author jobob
 * @since 2020-08-27
 */
@TableName("mr_product_info_model")
public class MrProductInfoModel implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    
    /**
     * 产品编码
     */
    private String            productNo;
    
    /**
     * 产品名
     */
    private String            productName;
    
    /**
     * 产品价格
     */
    private String            price;
    
    /**
     * 单位
     */
    private String            unit;
    
    /**
     * 产品图片
     */
    private String            pic;
    
    /**
     * 产品创建时间
     */
    private String            productCreateTime;
    
    /**
     * 产品分类编号
     */
    private String            productTypeNo;
    
    /**
     * 租户ID
     */
    private String            tenantId;
    
    /**
     * 备注
     */
    private String            remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime     createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime     updateTime;
    
    /**
     * 创建人
     */
    private Long              createUser;
    
    /**
     * 修改人
     */
    private Long              updateUser;
    
    /**
     * 数据来源
     */
    private String            dataSource;
    
    private String            dataType;
    
    private String            outerName;
    
    private String            productCreateTimeFormat;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getProductNo()
    {
        return productNo;
    }
    
    public void setProductNo(String productNo)
    {
        this.productNo = productNo;
    }
    
    public String getProductName()
    {
        return productName;
    }
    
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public String getUnit()
    {
        return unit;
    }
    
    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    
    public String getPic()
    {
        return pic;
    }
    
    public void setPic(String pic)
    {
        this.pic = pic;
    }
    
    public String getProductCreateTime()
    {
        return productCreateTime;
    }
    
    public void setProductCreateTime(String productCreateTime)
    {
        this.productCreateTime = productCreateTime;
    }
    
    public String getProductTypeNo()
    {
        return productTypeNo;
    }
    
    public void setProductTypeNo(String productTypeNo)
    {
        this.productTypeNo = productTypeNo;
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
    
    public Long getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }
    
    public Long getUpdateUser()
    {
        return updateUser;
    }
    
    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }
    
    public String getDataSource()
    {
        return dataSource;
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
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public String getProductCreateTimeFormat()
    {
        return productCreateTimeFormat;
    }
    
    public void setProductCreateTimeFormat(String productCreateTimeFormat)
    {
        this.productCreateTimeFormat = productCreateTimeFormat;
    }
}
