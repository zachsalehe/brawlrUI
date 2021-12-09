package com.example.test2.screens;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignInScreenTest {
    SignInScreen signInTest;

    @Before
    public void setUp() throws Exception {
        signInTest = new SignInScreen();
    }

    @Test
    public void signInTest1() {
        signInTest.signIn("pierreSa@gmail.com", "123456");
        assertTrue(signInTest.isSignedIn);
    }
    @Test
    public void signInTest2() {
        signInTest.signIn("pierreSa@gmail.com", "1234567");
        assertFalse(signInTest.isSignedIn);
    }
    @Test
    public void signInTest3() {
        signInTest.signIn("pierre@gmail.com", "123456");
        assertFalse(signInTest.isSignedIn);
    }
}