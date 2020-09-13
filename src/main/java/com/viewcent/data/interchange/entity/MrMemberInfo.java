package com.viewcent.data.interchange.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 人员信息表
 * </p>
 *
 * @author jobob
 * @since 2020-09-04
 */
@TableName("mr_member_info")
public class MrMemberInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long              id;
    
    /**
     * 会员编号
     */
    private String            memberNo;
    
    /**
     * 姓名
     */
    private String            memberName;
    
    /**
     * 会员状态(1：正常会员，2：过期会员）
     */
    private Integer           memberStatus;
    
    /**
     * 性别（1：男，2：女，3：未知）
     */
    private Integer           sex;
    
    /**
     * 手机
     */
    private String            phone;
    
    /**
     * 生日
     */
    private String            birthday;
    
    /**
     * 详细地址
     */
    private String            address;
    
    /**
     * 邮箱
     */
    private String            email;
    
    /**
     * 人脸ID
     */
    private Long              picId;
    
    /**
     * 会员所属店员ID
     */
    private Long              employeeId;
    
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
    
    /**
     * 是否修改
     */
    private Integer           updateStatus;
    
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
    
    public String getMemberNo()
    {
        return memberNo;
    }
    
    public void setMemberNo(String memberNo)
    {
        this.memberNo = memberNo;
    }
    
    public String getMemberName()
    {
        return memberName;
    }
    
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }
    
    public Integer getMemberStatus()
    {
        return memberStatus;
    }
    
    public void setMemberStatus(Integer memberStatus)
    {
        this.memberStatus = memberStatus;
    }
    
    public Integer getSex()
    {
        return sex;
    }
    
    public void setSex(Integer sex)
    {
        this.sex = sex;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getBirthday()
    {
        return birthday;
    }
    
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public Long getPicId()
    {
        return picId;
    }
    
    public void setPicId(Long picId)
    {
        this.picId = picId;
    }
    
    public Long getEmployeeId()
    {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
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
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public Integer getUpdateStatus()
    {
        return updateStatus;
    }
    
    public void setUpdateStatus(Integer updateStatus)
    {
        this.updateStatus = updateStatus;
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
