package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-09-04
 */
@TableName("mr_store_sync_info")
public class MrStoreSyncInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    
    /**
     * 门店名称
     */
    private String            storeName;
    
    /**
     * 门店编码
     */
    private String            storeNo;
    
    /**
     * 门店状态
     */
    private String            storeStatus;
    
    /**
     * 门店类型
     */
    private String            storeType;
    
    /**
     * 租户ID
     */
    private String            tenantId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime     createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime     updateTime;
    
    /**
     * 数据来源
     */
    private String            dataSource;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getStoreName()
    {
        return storeName;
    }
    
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }
    
    public String getStoreNo()
    {
        return storeNo;
    }
    
    public void setStoreNo(String storeNo)
    {
        this.storeNo = storeNo;
    }
    
    public String getStoreStatus()
    {
        return storeStatus;
    }
    
    public void setStoreStatus(String storeStatus)
    {
        this.storeStatus = storeStatus;
    }
    
    public String getStoreType()
    {
        return storeType;
    }
    
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
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
}
