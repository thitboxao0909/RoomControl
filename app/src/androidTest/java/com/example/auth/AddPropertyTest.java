package com.example.auth;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddPropertyTest {
    @Rule
    public ActivityScenarioRule<AddProperty> AddPropertyRule
            = new ActivityScenarioRule<>(AddProperty.class);

    String address = "10 Dunne st, Bundoora";
    @Test
    public void setupDataTest() {
        onView(withId(R.id.inputAddress)).perform(typeText(address), closeSoftKeyboard());
        onView(withId(R.id.inputPrice)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.addPropertyBtn)).perform(click());
    }
}