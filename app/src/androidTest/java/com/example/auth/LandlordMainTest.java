package com.example.auth;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LandlordMainTest {

    @Rule
    public ActivityScenarioRule<LandlordMain> LandlordMainRule
            = new ActivityScenarioRule<>(LandlordMain.class);

    @Test
    public void settingTest() {
        onView(withId(R.id.landlordSettingsBtn)).perform(click());
    }

    public void refreshTest() {
        onView(withId(R.id.landlordRefresh)).perform(click());
    }

    public void addPropertyTest() {
        onView(withId(R.id.addPropertyBtn)).perform(click());
    }
}