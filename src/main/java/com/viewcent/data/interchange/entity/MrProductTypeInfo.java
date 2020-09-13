package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 产品类别表
 * </p>
 *
 * @author jobob
 * @since 2020-08-27
 */
@TableName("mr_product_type_info")
public class MrProductTypeInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long              id;
    
    /**
     * 产品类别编号号
     */
    private String            typeNo;
    
    /**
     * 产品类型名称
     */
    private String            typeName;
    
    /**
     * 备注
     */
    private String            remark;
    
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
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getTypeNo()
    {
        return typeNo;
    }
    
    public void setTypeNo(String typeNo)
    {
        this.typeNo = typeNo;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
}
