package top.jpower.core.boot.config;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.CompositeTransactionAttributeSource;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import top.jpower.core.boot.transaction.TransactionProperties;


/**
 * 全局事务配置
 *
 * @author mr.g
 */
@Aspect
@AutoConfiguration
@EnableTransactionManagement
@EnableConfigurationProperties(TransactionProperties.class)
@ConditionalOnProperty(prefix = "jpower.transaction", name = "auto", havingValue = "true", matchIfMissing = true)
public class TransactionConfig {

    @Bean
    public TransactionAttributeSource source(ObjectProvider<AnnotationTransactionAttributeSource> annotationSourceProvider) {

        DefaultTransactionAttribute attributeWrite = new DefaultTransactionAttribute();
        attributeWrite.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute attributeReadOnly = new DefaultTransactionAttribute();
        attributeReadOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        attributeReadOnly.setReadOnly(true);

        NameMatchTransactionAttributeSource nameSource = new NameMatchTransactionAttributeSource();
        nameSource.addTransactionalMethod("add*", attributeWrite);
        nameSource.addTransactionalMethod("save*", attributeWrite);
        nameSource.addTransactionalMethod("del*", attributeWrite);
        nameSource.addTransactionalMethod("update*", attributeWrite);
        nameSource.addTransactionalMethod("exec*", attributeWrite);
        nameSource.addTransactionalMethod("set*", attributeWrite);
        nameSource.addTransactionalMethod("insert*", attributeWrite);
        nameSource.addTransactionalMethod("import*", attributeWrite);

        nameSource.addTransactionalMethod("get*", attributeReadOnly);
        nameSource.addTransactionalMethod("query*", attributeReadOnly);
        nameSource.addTransactionalMethod("find*", attributeReadOnly);
        nameSource.addTransactionalMethod("list*", attributeReadOnly);
        nameSource.addTransactionalMethod("count*", attributeReadOnly);
        nameSource.addTransactionalMethod("is*", attributeReadOnly);

        // 组合：同时支持 @Transactional 注解识别和方法名模式匹配
        // 确保共享的 TransactionInterceptor 能同时处理两种事务声明方式
        AnnotationTransactionAttributeSource annotationSource = annotationSourceProvider.getIfAvailable(AnnotationTransactionAttributeSource::new);
        return new CompositeTransactionAttributeSource(annotationSource, nameSource);
    }

    @Bean
    @ConditionalOnBean(TransactionInterceptor.class)
    public Advisor txAdviceAdvisor(TransactionAttributeSource source, TransactionInterceptor transactionInterceptor, TransactionProperties transactionProperties) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(StrUtil.concat(true,"(", transactionProperties.getExecution(), ") && !@annotation(org.springframework.transaction.annotation.Transactional)"));

        transactionInterceptor.setTransactionAttributeSource(source);

        return new DefaultPointcutAdvisor(pointcut, transactionInterceptor);
    }
}
