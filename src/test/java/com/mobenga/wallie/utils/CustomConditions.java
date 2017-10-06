package com.mobenga.wallie.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomConditions {

    public static ExpectedCondition<List<WebElement>> countOfElementsToBe( By locator, int count ) {
        return new ExpectedCondition<List<WebElement>>() {

            @Override
            public List<WebElement> apply( WebDriver driver ) {
                List<WebElement> elements = driver.findElements( locator );
                if( elements.size() == count ) {
                    return elements;
                } else {
                    throw new NoSuchElementException( "Not found " + count + " elements by " + locator + ". Found " + elements.size() + " elements instead" );
                }
            }

            @Override
            public String toString() {
                return count + " elements by: " + locator;
            }
        };
    }

    public static ExpectedCondition<String> attributeValueToBe( By locator, String attributeName, String attributeValue ) {
        return new ExpectedCondition<String>() {

            @Override
            public String apply( WebDriver driver ) {
                WebElement element = driver.findElement( locator );
                String elementAttribute = element.getAttribute( attributeName );
                if( ( attributeValue == null && elementAttribute == null ) || ( attributeValue != null && attributeValue.equals( elementAttribute ) ) ) {
                    return String.valueOf( attributeValue );
                } else {
                    throw new InvalidElementStateException( "Not found attribute " + attributeName + " with value " + attributeValue
                                                                    + " for element " + locator );
                }
            }

            @Override
            public String toString() {
                return locator + "/[@" + attributeName + "='" + attributeValue + "']";
            }
        };
    }

    public static ExpectedCondition<Boolean> performClick( By locator ) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply( WebDriver driver ) {
                WebElement element = driver.findElement( locator );
                try {
                    element.click();
                    return Boolean.TRUE;
                } catch ( Exception e ) {
                    throw new InvalidElementStateException( "Cannot click element " + locator + ". Reason: " + e.getMessage(), e );
                }
            }

            @Override
            public String toString() {
                return "Performing click on element " + locator;
            }
        };
    }

    public static ExpectedCondition<Boolean> anySelectorExists( List<By> locators ) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply( WebDriver driver ) {
                long count = locators.stream()
                        .map( locator -> {
                            try {
                                return driver.findElement( locator );
                            } catch ( NoSuchElementException e ) {
                                return null;
                            }
                        } ).filter( Objects::nonNull )
                        .count();
                return count > 0;
            }

            @Override
            public String toString() {
                return "Checking for any element " + locators;
            }
        };
    }

    public static ExpectedCondition<Alert> alertToBePresent() {
        return driver -> {
            try {
                return driver.switchTo().alert();
            } catch ( Exception e ) {
                throw new InvalidElementStateException( "Alert not found", e );
            }
        };
    }

    public static ExpectedCondition<Boolean> alertToBeClosed() {
        return driver -> {
            try {
                driver.switchTo().alert();
                throw new InvalidElementStateException( "Alert is present" );
            } catch ( Exception e ) {
                return true;
            }
        };
    }

    public static ExpectedCondition<String> currentUrlToBe( String url ) {
        return new ExpectedCondition<String>() {

            @Override
            public String apply( WebDriver driver ) {
                String currentUrl = driver.getCurrentUrl();
                if( currentUrl.equals( url ) ) {
                    return currentUrl;
                } else {
                    throw new InvalidElementStateException( "Current url is " + currentUrl );
                }
            }

            @Override
            public String toString() {
                return "current url to be " + url;
            }
        };
    }

    public static ExpectedCondition<Boolean> scrollTo( By locator, boolean scrollToTop ) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply( WebDriver driver ) {
                try {
                    WebElement element = driver.findElement( locator );
                    ( (RemoteWebDriver) driver ).executeScript( "arguments[0].scrollIntoView(" + scrollToTop + ")", element );
                    return Boolean.TRUE;
                } catch ( Exception e ) {
                    throw new InvalidElementStateException( "Cannot click element " + locator + ". Reason: " + e.getMessage(), e );
                }
            }

            @Override
            public String toString() {
                return "Performing click on element " + locator;
            }
        };
    }
}
