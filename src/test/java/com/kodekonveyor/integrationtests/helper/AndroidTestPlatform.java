package com.kodekonveyor.integrationtests.helper;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kodekonveyor.annotations.InterfaceClass;

import io.appium.java_client.android.AndroidDriver;

@InterfaceClass
public class AndroidTestPlatform implements TestPlatform {

    private static final String ADB_EXEC_TIMEOUT = "adbExecTimeout";
	private static final String AUTO_WEBVIEW_TIMEOUT = "autoWebviewTimeout";
	private static final String AVD_READY_TIMEOUT = "avdReadyTimeout";
	private static final String AVD_LAUNCH_TIMEOUT = "avdLaunchTimeout";
	private static final String ANDROID_INSTALL_TIMEOUT = "androidInstallTimeout";
	private static final String ANDROID_DEVICE_READY_TIMEOUT = "androidDeviceReadyTimeout";
	private static final String DEVICE_READY_TIMEOUT = "deviceReadyTimeout";
	private static final String APP_WAIT_DURATION = "appWaitDuration";
	private static final String NEW_COMMAND_TIMEOUT = "newCommandTimeout";
	private static final int TIMEOUT_IN_MILLISEC = 60000;
	private static final int TIMEOUT_IN_SEC = 60;
	private static final int TIMEOUT = 10;
	private static final int MAX_TRIES = 3;
	private static final String HTTP_127_0_0_1_4723_WD_HUB = "http://127.0.0.1:4723/wd/hub";
	private static final String EXTRACT_CHROME_ANDROID_PACKAGE_FROM_CONTEXT_NAME = "extractChromeAndroidPackageFromContextName";
	private static final String COM_KODEKONVEYOR_ANGULARTEST_MAIN_ACTIVITY = "com.kodekonveyor.angulartest.MainActivity";
	private static final String APP_ACTIVITY = "appActivity";
	private static final String COM_KODEKONVEYOR_ANGULARTEST = "com.kodekonveyor.angulartest";
	private static final String APP_PACKAGE = "appPackage";
	private static final String TARGET_APP_DEBUG_APK = "target/app-debug.apk";
	private static final String APP = "app";
	private static final String ANDROID = "Android";
	private static final String PLATFORM_NAME = "platformName";
	private static final String COULD_NOT_WAIT_FOR_CONTEXT = "could not wait for context ";
	private AndroidDriver driver;

	@Override
	public void switchToContext(final String appContext) throws InterruptedException {
		int tries = 0;
		while (true) {
			if(tries >MAX_TRIES)
				fail(COULD_NOT_WAIT_FOR_CONTEXT+ appContext);
	    	Set<String> contextNames = driver.getContextHandles();
			if(contextNames.contains(appContext))
	    		break;
	    	TimeUnit.SECONDS.sleep(TIMEOUT);
	    	tries++;
    	}
    	driver.context(appContext);
	}
    
	@Override
	public WebDriver makeDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, ANDROID);
        caps.setCapability(APP, TARGET_APP_DEBUG_APK);
        caps.setCapability(APP_PACKAGE, COM_KODEKONVEYOR_ANGULARTEST);
        caps.setCapability(APP_ACTIVITY, COM_KODEKONVEYOR_ANGULARTEST_MAIN_ACTIVITY);
        caps.setCapability(EXTRACT_CHROME_ANDROID_PACKAGE_FROM_CONTEXT_NAME, true);
        caps.setCapability(NEW_COMMAND_TIMEOUT, TIMEOUT_IN_SEC);
        caps.setCapability(APP_WAIT_DURATION, TIMEOUT_IN_MILLISEC);
        caps.setCapability(DEVICE_READY_TIMEOUT, TIMEOUT_IN_SEC);
        caps.setCapability(ANDROID_DEVICE_READY_TIMEOUT, TIMEOUT_IN_SEC);
        caps.setCapability(ANDROID_INSTALL_TIMEOUT, TIMEOUT_IN_MILLISEC);
        caps.setCapability(AVD_LAUNCH_TIMEOUT, TIMEOUT_IN_MILLISEC);
        caps.setCapability(AVD_READY_TIMEOUT, TIMEOUT_IN_MILLISEC);
        caps.setCapability(AUTO_WEBVIEW_TIMEOUT, TIMEOUT_IN_MILLISEC);
        caps.setCapability(ADB_EXEC_TIMEOUT, TIMEOUT_IN_MILLISEC);
        driver = new AndroidDriver(new URL(HTTP_127_0_0_1_4723_WD_HUB), caps);
        return driver;
	}

	@Override
	public void takeScreenshot(final String reason, final Integer step, final WebElement element, final String cssSelector)
			throws IOException {
	    String origWidth = null;
	    String origColor = null;
	    String origStyle = null;

	    if (!SeleniumTestConstants.NO_CSS_SELECTOR.equals(cssSelector)) {
	      origWidth = element.getCssValue(SeleniumTestConstants.BORDER_WIDTH);
	      origColor = element.getCssValue(SeleniumTestConstants.BORDER_COLOR);
	      origStyle = element.getCssValue(SeleniumTestConstants.BORDER_STYLE);
	      String script = MessageFormat.format(
	    		  SeleniumTestConstants.RESTORE_BORDER_JS,
	          cssSelector, SeleniumTestConstants.NOTE_BORDER_WIDTH, SeleniumTestConstants.NOTE_BORDER_STYLE,
	          SeleniumTestConstants.NOTE_BORDER_COLOR);
	      driver.executeScript(script);
	    }
	    final File screenshot = driver.getScreenshotAs(OutputType.FILE);
	    final Integer stepNumber = step+1;
	    final File destFile = new File(MessageFormat.format(
	    		SeleniumTestConstants.SSREENSHOT_FILE_NAME,
	        stepNumber.toString()));
	    FileUtils.copyFile(screenshot, destFile);
	    if (!SeleniumTestConstants.NO_CSS_SELECTOR.equals(cssSelector)) {
	    	driver.executeScript(MessageFormat.format(
	    		  SeleniumTestConstants.RESTORE_BORDER_JS,
	          cssSelector, origWidth, origStyle, origColor));
	    }

	}

}