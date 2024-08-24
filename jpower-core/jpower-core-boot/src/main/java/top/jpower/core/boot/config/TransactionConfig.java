package top.jpower.core.boot.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
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
@ConditionalOnProperty(prefix = "jpower.transaction", name = "enable", havingValue = "true", matchIfMissing = true)
public class TransactionConfig {

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    public TransactionInterceptor txAdvice(PlatformTransactionManager transactionManager) {

        DefaultTransactionAttribute attributeWrite = new DefaultTransactionAttribute();
        attributeWrite.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute attributeReadOnly = new DefaultTransactionAttribute();
        attributeReadOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        attributeReadOnly.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("add*", attributeWrite);
        source.addTransactionalMethod("save*", attributeWrite);
        source.addTransactionalMethod("del*", attributeWrite);
        source.addTransactionalMethod("update*", attributeWrite);
        source.addTransactionalMethod("exec*", attributeWrite);
        source.addTransactionalMethod("set*", attributeWrite);
        source.addTransactionalMethod("insert*", attributeWrite);
        source.addTransactionalMethod("import*", attributeWrite);

        source.addTransactionalMethod("get*", attributeReadOnly);
        source.addTransactionalMethod("query*", attributeReadOnly);
        source.addTransactionalMethod("find*", attributeReadOnly);
        source.addTransactionalMethod("list*", attributeReadOnly);
        source.addTransactionalMethod("count*", attributeReadOnly);
        source.addTransactionalMethod("is*", attributeReadOnly);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        transactionInterceptor.setTransactionAttributeSource(source);
        return transactionInterceptor;
    }

    @Bean
    @ConditionalOnBean(TransactionInterceptor.class)
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice, TransactionProperties transactionProperties) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionProperties.getExecution());
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
