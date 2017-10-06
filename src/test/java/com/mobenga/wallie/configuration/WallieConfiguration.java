package com.mobenga.wallie.configuration;

import com.mobenga.wallie.driver.SeleniumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

@Slf4j
@Configuration
@ComponentScan( basePackages = "com.mobenga.wallie.suite" )
public class WallieConfiguration {

    @Bean( destroyMethod = "quit" )
    public WebDriver webDriver( @Value( "${selenium.driver.path:tools/chromedriver.exe}" ) String driverPath ) {
        System.setProperty( "webdriver.chrome.driver", driverPath );
        log.info( "Chrome driver path: {}", new File( driverPath ).getAbsolutePath() );
        return new ChromeDriver();
    }

    @Bean
    public SeleniumDriver seleniumDriver( WebDriver driver ) {
        return new SeleniumDriver( driver );
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources( new ClassPathResource( "application.yml" ) );
        propertySourcesPlaceholderConfigurer.setProperties( yaml.getObject() );
        return propertySourcesPlaceholderConfigurer;
    }

}
