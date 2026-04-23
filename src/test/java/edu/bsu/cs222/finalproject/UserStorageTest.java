package edu.bsu.cs222.finalproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

class UserStorageTest {
    UserStorage storage = new UserStorage();

    @BeforeEach
    void setUp() {
        File file = new File("users.txt");
        if (file.exists()) {
            file.delete();
        }
        storage = new UserStorage();
    }

    @Test
    void addUser() {
        assertTrue(storage.createAccount("userNonExist", "password1"));
    }

    @Test
    void testLogin() {
        storage.createAccount("userNonExist", "password1");
        assertTrue(storage.login("userNonExist", "password1"));
    }

    @Test
    void testCreateAccount() {
        assertEquals(true, storage.createAccount("userNonExist", "password1"));

    }
    @Test
    void wrongPasswordLogin() {
        storage.createAccount("userNonExist", "password1");
        assertFalse(storage.login("userNonExist", "wrongpassword"));
    }


    @Test
    void duplicateAccountCreation() {
        storage.createAccount("userNonExist", "password1");
        assertFalse(storage.createAccount("userNonExist", "password1"));
    }
}