package com.mobenga.wallie.listeners;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotTestExecutionListener extends AbstractTestExecutionListener {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd-HH-mm-ss-SSSS" );

    @Value( "${selenium.screenshot.path:target/screenshots/}" )
    private String screenshotPath;

    @Override
    public void beforeTestClass( TestContext testContext ) throws Exception {
        testContext.getApplicationContext()
                .getAutowireCapableBeanFactory()
                .autowireBean( this );

    }

    @Override
    public void afterTestMethod( TestContext testContext ) throws Exception {
        if( isTestFailed( testContext ) ) {
            String fileName = testContext.getTestMethod().getName() + "-" + LocalDateTime.now().format( formatter );
            RemoteWebDriver driver = testContext.getApplicationContext().getBean( RemoteWebDriver.class );
            ScreenshotUtils.takeScreenshot( driver, screenshotPath, fileName );
        }
    }

    private boolean isTestFailed( TestContext testContext ) {
        return testContext.getTestException() != null;

    }
}
