package com.example.test2;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    public static User createUser(String email, String username, String password){
        HashMap<String, String> loginInfo = new HashMap<>();
        loginInfo.put("email", email);
        loginInfo.put("username", username);
        loginInfo.put("password", password);
        User newUser = new User(username, loginInfo);
        return newUser;
    }

    public static User createUser(String username, HashMap loginInfo,
                                  String fightStyle,
                                  String biography, String opinion){

        User newUser = new User(username, loginInfo, fightStyle, biography, opinion);
        return newUser;
    }

    public static void setBioInfo(User user, String fightStyle, String size, String biography,
                                  String opinion){
        user.setFightingStyle(fightStyle);
        user.setBiography(biography);
        user.setControversialOpinions(opinion);
    }
}
