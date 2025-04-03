package com.engilyin.drools.config;

import org.drools.ruleunits.api.RuleUnitProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DroolsConfig {
    
    @Bean
    public RuleUnitProvider ruleUnitProvider() {
        return RuleUnitProvider.get();
    }
    
//    private List<Resource> getRuleResources() throws IOException {
//        // Use Spring's resource resolver to find all .drl files in the rules directory and its subdirectories
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = resourcePatternResolver.getResources("classpath:rules/**/*.drl");
//        
//        // Log found rule files for debugging
//        log.debug("Found rule files:");
//        Arrays.stream(resources).forEach(resource -> {
//            try {
//                log.debug(" - {}", resource.getURL());
//            } catch (IOException e) {
//                log.error("Unable to list drl resource", e);
//            }
//        });
//        
//        return Arrays.asList(resources);
//    }
}
