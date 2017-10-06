package com.mobenga.wallie.suite.highlights;

import com.mobenga.wallie.AbstractWallieTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HighlightedEventsTest extends AbstractWallieTest {

    @Autowired
    private HighlightedEventsDriver highlightedEventsDriver;

    @Test
    public void checkAvailability() {
        seleniumDriver.goTo( sportsbookUrl );
        seleniumDriver.assertExist( overlayPopupBtn, defaultTimeout );
        seleniumDriver.click( overlayPopupBtn, defaultTimeout );
        seleniumDriver.assertNotVisible( overlayPopupBtn, defaultTimeout );
        seleniumDriver.assertExist( highlightedEventsDriver.swiper, defaultTimeout );
    }
}
