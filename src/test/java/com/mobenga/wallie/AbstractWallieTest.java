package com.mobenga.wallie;

import com.mobenga.wallie.configuration.WallieConfiguration;
import com.mobenga.wallie.driver.SeleniumDriver;
import com.mobenga.wallie.listeners.ScreenshotTestExecutionListener;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith( SpringRunner.class )
@TestExecutionListeners( listeners = {DependencyInjectionTestExecutionListener.class,
        ScreenshotTestExecutionListener.class} )
@ContextConfiguration( classes = {WallieConfiguration.class} )
public abstract class AbstractWallieTest {

    @Autowired
    protected SeleniumDriver seleniumDriver;

    @Value( "${sportsbook.host}:${sportsbook.port}" )
    protected String sportsbookUrl;

    @Value( "${selenium.timeout:500}" )
    protected long defaultTimeout;

    protected By overlayPopupBtn = By.xpath( "//*[@data-ta='overlay-visit-now-button']" );

}
