package io.coworking.audit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}
