package com.example.test2.screens;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateAccountScreenTest {
    CreateAccountScreen signupTest;

    @Before
    public void setUp() throws Exception {
         signupTest = new CreateAccountScreen();
    }

    @Test
    public void createAccountTest1() {
        // I do this to make it basically impossible to use the same email address on 2 tests
        int salt = (int) (Math.random() * 1000000);
        String email = "pierreSarrailh@gmail.com" + salt;
        signupTest.createAccount("Pierre", "password",
                "password", email);
        assertTrue(signupTest.signupSuccess);
    }
    @Test
    public void createAccountTest2() {
        signupTest.createAccount("Pierre", "password",
                "notPassword", "pierreSarrailh@gmail.com");
        assertFalse(signupTest.signupSuccess);
    }
    @Test
    public void createAccountTest3() {
        //pierreSa is already registered in the DB so should be false
        signupTest.createAccount("Pierre", "password",
                "password", "pierreSa@gmail.com");
        assertFalse(signupTest.signupSuccess);
    }
    @Test
    public void createAccountTest4() {
        //length of the password is too short
        signupTest.createAccount("Pierre", "123",
                "123", "pierreSa@gmail.com");
        assertFalse(signupTest.signupSuccess);
    }
}