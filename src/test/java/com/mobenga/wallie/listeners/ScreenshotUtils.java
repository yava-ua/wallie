package com.mobenga.wallie.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

@Slf4j
public final class ScreenshotUtils {

    public static boolean takeScreenshot( RemoteWebDriver driver, String outputPath, String filename ) {
        try {
            File screenShot = driver.getScreenshotAs( OutputType.FILE );
            String fileOutput = outputPath + filename + ".png";

            FileUtils.copyFile( screenShot, new File( fileOutput ) );
            log.info( "Saved screenshot at: {}", fileOutput );
        } catch ( IOException e ) {
            log.warn( "Couldn't save screenshot", e );
            return false;
        }
        return true;
    }
}
