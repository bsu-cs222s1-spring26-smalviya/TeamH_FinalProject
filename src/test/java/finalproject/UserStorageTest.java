package finalproject;

import edu.bsu.cs222.finalproject.UserStorage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
    UserStorage storage = new UserStorage();

    @Test
    void addUser() {
        assertTrue(storage.addUser("userNonExist", "password1"));
    }

    @Test
    void login() {assertTrue(storage.addUser("userNonExist","password1"));
    }

    @Test
    void userExists() {
        storage.addUser("userNonExist", "password1");
        assertTrue(storage.userExists("userNonExist"));
    }

    @Test
    void getAllUsers() {
        Map<String, String> users = new HashMap<>();
        users.put("userNonExist", "password1");

        storage.addUser("userNonExist", "password1");
        assertEquals(users, storage.getAllUsers());
    }
}