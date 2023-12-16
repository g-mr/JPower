package top.jpower.jpower.module.mp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import lombok.AllArgsConstructor;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.mp.methods.*;
import top.jpower.jpower.module.tenant.JpowerTenantProperties;

import java.util.List;

/**
 * @ClassName CustomSqlInjector
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-11 15:13
 * @Version 1.0
 */
@AllArgsConstructor
public class CustomSqlInjector extends DefaultSqlInjector {

    private JpowerTenantProperties tenantProperties;

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass,tableInfo);

        if (tableInfo.havePK()) {
            methodList.add(new InsertBatchSomeColumn());
            if (Fc.notNull(tenantProperties) && tenantProperties.getEnable()){
                methodList.add(new UpdateAllById(field -> !Fc.equalsValue(field.getFieldFill(), FieldFill.INSERT) && !Fc.equalsValue(tenantProperties.getColumn(), field.getColumn())));
            }else {
                methodList.add(new UpdateAllById(field -> !Fc.equalsValue(field.getFieldFill(), FieldFill.INSERT)));
            }
            methodList.add(new DeleteReal());
            methodList.add(new DeleteRealBatchByIds());
            methodList.add(new DeleteRealById());
            methodList.add(new DeleteRealByMap());
        }else {
            methodList.add(new InsertBatchSomeColumn());
            methodList.add(new DeleteReal());
            methodList.add(new DeleteRealByMap());
        }
        return methodList;
    }
}
