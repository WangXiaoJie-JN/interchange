package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 门店信息表
 * </p>
 *
 * @author jobob
 * @since 2020-08-28
 */
@TableName("mr_store_info")
public class MrStoreInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long              id;
    
    /**
     * 门店编号
     */
    private String            storeNo;
    
    /**
     * 门店名称
     */
    private String            storeName;
    
    /**
     * 详细地址
     */
    private String            address;
    
    /**
     * 经度
     */
    private String            longitude;
    
    /**
     * 纬度
     */
    private String            latitude;
    
    /**
     * 面积
     */
    private String            area;
    
    /**
     * 门店图片
     */
    private String            storePicPath;
    
    /**
     * 租户ID
     */
    private String            tenantId;
    
    /**
     * 部门ID
     */
    private Long              deptId;
    
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
     * 是否已删除
     */
    @TableLogic
    private Integer           isDeleted;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getStoreNo()
    {
        return storeNo;
    }
    
    public void setStoreNo(String storeNo)
    {
        this.storeNo = storeNo;
    }
    
    public String getStoreName()
    {
        return storeName;
    }
    
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getLongitude()
    {
        return longitude;
    }
    
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
    
    public String getLatitude()
    {
        return latitude;
    }
    
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }
    
    public String getArea()
    {
        return area;
    }
    
    public void setArea(String area)
    {
        this.area = area;
    }
    
    public String getStorePicPath()
    {
        return storePicPath;
    }
    
    public void setStorePicPath(String storePicPath)
    {
        this.storePicPath = storePicPath;
    }
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }
    
    public Long getDeptId()
    {
        return deptId;
    }
    
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
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
    
    public Integer getIsDeleted()
    {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }
}
