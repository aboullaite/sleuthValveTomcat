package me.aboullaite.edgeservice.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import ch.qos.logback.core.util.ContextUtil;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessLogAutoConfiguration {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccessLogAutoConfiguration.class);

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(SleuthValve sleuthValve, LogbackValve logbackValve) {
        return (ConfigurableEmbeddedServletContainer container)  -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;

                        tomcat.addContextCustomizers(
                                (context) -> {
                                    context.getPipeline().addValve(sleuthValve);
                                    context.getPipeline().addValve(logbackValve);
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

    @Bean
    public LogbackValve logbackValve() {
        LogbackValve logbackValve = new LogbackValve();
        logbackValve.putProperty("HOSTNAME", new ContextUtil(null).safelyGetLocalHostName());
        logbackValve.setQuiet(true);
        return logbackValve;
    }
}