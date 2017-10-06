package com.mobenga.wallie.driver;

import com.mobenga.wallie.utils.CustomConditions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@Slf4j
@Getter
public class SeleniumDriver {

    private WebDriver webDriver;

    public SeleniumDriver( WebDriver webDriver ) {
        this.webDriver = webDriver;
    }

    public void click( By by, long timeout ) {
        waitUntil( CustomConditions.performClick( by ), timeout );
    }

    public WebElement findElement( By by, long timeout ) {
        return waitUntil( ExpectedConditions.presenceOfElementLocated( by ), timeout );
    }

    private <T> T waitUntil( ExpectedCondition<T> condition, long timeout ) {
        return new FluentWait<>( webDriver )
                .withTimeout( timeout, TimeUnit.MILLISECONDS )
                .pollingEvery( timeout > 10_000 ? 500 : timeout / 10, TimeUnit.MILLISECONDS )
                .ignoring( Exception.class )
                .until( condition );

    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public void assertChecked( By by, long timeout ) {
        WebElement element = findElement( by, timeout );
        assertThat( element.isSelected(), is( true ) );
    }

    public void assertNotChecked( By by, long timeout ) {
        WebElement element = findElement( by, timeout );
        assertThat( element.isSelected(), is( false ) );
    }

    public void setCheckbox( By by, boolean state, long timeout ) {
        WebElement element = findElement( by, timeout );
        if( element.isSelected() != state ) {
            element.click();
        }
    }

    public void assertSelectedOption( By by, String expected, long timeout ) {
        doWithRetry( () -> {
            WebElement element = findElement( by, timeout );
            String selectedValue = element.getAttribute( "value" );
            List<WebElement> options = ( (RemoteWebElement) element ).findElementsByTagName( "option" );
            Optional<WebElement> selected = options.stream().filter( o -> selectedValue.equals( o.getAttribute( "value" ) ) ).findFirst();
            if( selected.isPresent() ) {
                assertEquals( expected, selected.get().getText() );
            } else {
                fail( "Selected option " + expected + " not found" );
            }
        }, 3 );
    }

    public void assertOptions( By by, String[] expected, long timeout ) {
        doWithRetry( () -> {
            WebElement element = findElement( by, timeout );
            List<WebElement> options = ( (RemoteWebElement) element ).findElementsByTagName( "option" );
            assertEquals( expected.length, options.size() );
            for( int i = 0; i < expected.length; i++ ) {
                assertEquals( expected[i], options.get( i ).getText() );
            }
        }, 3 );
    }

    public void assertVisible( By by, long timeout ) {
        waitUntil( ExpectedConditions.visibilityOfElementLocated( by ), timeout );
    }

    public void assertNotVisible( By by, long timeout ) {
        waitUntil( ExpectedConditions.invisibilityOfElementLocated( by ), timeout );
    }

    public void assertExist( By by, long timeout ) {
        findElement( by, timeout );
    }

    public void assertText( By by, String expected, long timeout ) {
        waitUntil( ExpectedConditions.textToBePresentInElementLocated( by, expected ), timeout );
    }

    public void assertTextContains( By by, String expected, long timeout ) {
        WebElement element = findElement( by, timeout );
        assertThat( element.getText(), containsString( expected ) );
    }

    public void assertTextInput( By by, String expected, long timeout ) {
        waitUntil( ExpectedConditions.textToBePresentInElementValue( by, expected ), timeout );
    }

    public void putText( By by, String text, long timeout ) {
        sendKeys( findElement( by, timeout ), text );
    }

    private void sendKeys( WebElement element, String keys ) {
        log.debug( "Setting keys {} to {}", keys, element );
        for( char c : keys.toCharArray() ) {
            element.sendKeys( String.valueOf( c ) );
        }
    }

    public void setText( By by, String text, long timeout ) {
        setText( findElement( by, timeout ), text );
    }

    private void setText( WebElement element, String text ) {
        clear( element );
        sendKeys( element, text );
    }

    public void clearText( By by, long timeout ) {
        clear( findElement( by, timeout ) );
    }

    public void setTextSilently( By by, String text, long timeout ) {
        WebElement element = findElement( by, timeout );
        setTextSilently( element, text );
    }

    private void setTextSilently( WebElement element, String text ) {
        clearSilently( element );
        sendKeys( element, text );
        webDriver.findElement( By.cssSelector( "body" ) ).sendKeys( Keys.TAB );
    }

    private void clearSilently( WebElement element ) {
        log.debug( "Clearing element silently {}", element );
        element.sendKeys( Keys.DELETE ); // magic. but it helps sending the following CTRL+A + DELETE (otherwise it fails sometimes)
        element.sendKeys( Keys.chord( Keys.CONTROL, "a" ) );
        element.sendKeys( Keys.DELETE );
    }

    private void clear( WebElement element ) {
        log.debug( "Clearing element {}", element );
        element.clear();
    }

    private void selectByText( WebElement dropdown, String option ) {
        log.debug( "Selecting dropdown {} by visible text {}", dropdown, option );
        new Select( dropdown ).selectByVisibleText( option );
    }

    public void selectByText( By by, String option, long timeout ) {
        selectByText( findElement( by, timeout ), option );
    }

    public void goTo( String url ) {
        log.debug( "Going to {}", url );
        webDriver.get( url );
    }

    public void refresh() {
        webDriver.navigate().refresh();
    }

    public String getText( By by, long timeout ) {
        return findElement( by, timeout ).getText();
    }

    /**
     * Will try to rerun doAction.run retryNumber times. Next rerun will be started in case of doAction.run throws
     * exception. Criteria of success execution of doAction.run is completed with no exception thrown
     * @param doAction action to run
     * @param retryNumber number of retry
     */
    private void doWithRetry( Runnable doAction, int retryNumber ) {
        for( int i = 0; i < retryNumber; i++ ) {
            try {
                doAction.run();
                break;
            } catch ( Exception e ) {
                if( i >= retryNumber - 1 ) {
                    throw e;
                }
            }
        }
    }

}
