package com.tapura.kitchenization.main;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private UiDevice mDevice;

    @Before
    public void openApp() {
        Intent intent = new Intent("android.intent.category.LAUNCHER");
        String packageName = "com.tapura.kitchenization";
        ComponentName cn = new ComponentName(packageName, packageName + ".main.MainActivity");
        intent.setComponent(cn);

        Context c = InstrumentationRegistry.getContext();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        c.startActivity(intent);

        mDevice.wait(Until.hasObject(
                By.pkg(packageName).depth(0)), 5000);
    }

    @Test
    public void mainActivityTest() {
        // Wait sometime to load the json from network
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String textToMatch = "Nutella Pie";

        UiObject object = mDevice.findObject(new UiSelector().text(textToMatch));
        if (object.exists()) {
            try {
                object.clickAndWaitForNewWindow();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            UiObject toolbar = null;
            try {
                toolbar = mDevice.findObject(new UiSelector().resourceId("com.tapura.kitchenization:id/toolbar")).getChild(new UiSelector().className("android.widget.TextView"));
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(toolbar);
            try {
                assertTrue(toolbar.getText().equals(textToMatch));
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
