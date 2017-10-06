package com.mobenga.wallie.suite;

import com.mobenga.wallie.AbstractWallieTest;
import org.junit.Test;
import org.openqa.selenium.By;

public class BasicTest extends AbstractWallieTest {

    @Test
    public void checkAvailability() {
        seleniumDriver.goTo( sportsbookUrl );
        seleniumDriver.assertExist( By.xpath( "//body" ), defaultTimeout );
    }

}
