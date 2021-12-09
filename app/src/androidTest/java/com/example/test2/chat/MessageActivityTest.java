package com.example.test2.chat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MessageActivityTest {
    MessageActivity messageTest;

    @Before
    public void setUp() throws Exception {
        messageTest = new MessageActivity();
    }

    @Test
    public void sendMessageTest1() {
        // these two users are already connected so this should work
        messageTest.sendMessage("gKnijFV4NAdCpTWlCVRfyot9AcQ2",
                "0idSAZRmzgRT8TZaYl1UGYgDIKo2", "test");
        assertTrue(messageTest.sendSuccess);
    }
    @Test
    public void sendMessageTest2() {
        // these two users are not already connected so this should not work
        messageTest.sendMessage("gKnijFV4NAdCpTWlCVRfyot9AcQ2",
                "0idSAZRmzgRT8TZaYl1UGYgDIKo5", "test");
        assertFalse(messageTest.sendSuccess);
    }

    @Test
    public void getChatMessagesTest1() {
        // this user should have messages to receive
        messageTest.setCurrentUserID("gKnijFV4NAdCpTWlCVRfyot9AcQ2");
        messageTest.getChatMessages();
        assertTrue(messageTest.recieveSuccess);
    }
    @Test
    public void getChatMessagesTest2() {
        // this user should not have messages to receive
        messageTest.setCurrentUserID("gKnijFV4NAdCpTWlCVRfyot9AcQ4");
        messageTest.getChatMessages();
        assertFalse(messageTest.recieveSuccess);
    }
}