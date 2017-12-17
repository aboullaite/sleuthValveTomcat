package me.aboullaite.edgeservice.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessLogConfiguration {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccessLogConfiguration.class);

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(SleuthValve sleuthValve) {
        return (ConfigurableEmbeddedServletContainer container)  -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;

                        tomcat.addContextCustomizers(
                                (context) -> {
                                    context.getPipeline().addValve(sleuthValve);
                                });
            } else {
                log.warn("no access-log auto-configuration for container: {}", container);
            }
        };
    }

    @Bean
    public SleuthValve sleuthValve(Tracer tracer) {
        return new SleuthValve(tracer);
    }

}