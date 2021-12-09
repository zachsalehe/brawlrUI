package com.example.test2.screens;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HomeScreenTest {
    HomeScreen homeTest;

    @Before
    public void setUp() throws Exception {
        homeTest = new HomeScreen();
        // this is a user that swiped right on a lot of people
        homeTest.setCurrentUid("gKnijFV4NAdCpTWlCVRfyot9AcQ2");
    }

    @Test
    public void isMatch() {
        // this is a user that was swiped right on
        homeTest.isMatch("0idSAZRmzgRT8TZaYl1UGYgDIKo2");
        assertTrue(homeTest.matchSuccess);
    }
    @Test
    public void isMatch2() {
        // this is a user that was not swiped right on
        homeTest.isMatch("0idSAZRmzgRT8TZaYl1UGYgDIKo5");
        assertFalse(homeTest.matchSuccess);
    }
}