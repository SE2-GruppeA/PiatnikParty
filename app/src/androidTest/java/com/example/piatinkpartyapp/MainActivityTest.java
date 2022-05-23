package com.example.piatinkpartyapp;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.example.piatinkpartyapp.screens.MainActivity;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    //Checking if Buttons are shown in MainActivity
    @Test
    public void testPlayGameButton(){
        onView(withId(R.id.BtnPlayGame)).check(matches(isDisplayed()));
        onView(withId(R.id.BtnPlayGame)).perform(click());
        onView(allOf(withId(R.id.fragment_play_game),withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));
    }
    @Test
    public void testCreateGameButton(){
        onView(withId(R.id.BtnCreateGame)).check(matches(isDisplayed()));
        onView(withId(R.id.BtnCreateGame)).perform(click());
        onView(allOf(withId(R.id.fragment_create_game),withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));
    }
    @Test
    public void testGameRulesButton(){
        onView(withId(R.id.BtnGameRules)).check(matches(isDisplayed()));
        onView(withId(R.id.BtnGameRules)).perform(click());
        onView(allOf(withId(R.id.fragment_game_rules),withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));
    }
    @Test
    public void testOptionsButton(){
        onView(withId(R.id.BtnOptions)).check(matches(isDisplayed()));
        onView(withId(R.id.BtnOptions)).perform(click());
        onView(allOf(withId(R.id.fragment_einstellungen_view),withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));
    }
    @Test
    public void testShowTableButton(){
        onView(withId(R.id.btnShowTable)).check(matches(isDisplayed()));
        onView(withId(R.id.btnShowTable)).perform(click());
        onView(allOf(withId(R.id.fragment_schnopsn),withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));
    }

}
