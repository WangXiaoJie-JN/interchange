package com.viewcent.data.interchange.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 配合@TableField(fill = FieldFill.INSERT)
 */
@Component
public class MybatisFiledsHandler implements MetaObjectHandler
{
    @Override
    public void insertFill(MetaObject metaObject)
    {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
    }
    
    @Override
    public void updateFill(MetaObject metaObject)
    {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
