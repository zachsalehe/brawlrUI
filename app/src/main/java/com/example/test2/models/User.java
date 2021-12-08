package com.example.test2.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is a representation of how we store our Users in our database
 */
public class User implements Serializable {
    private String id;
    private HashMap<String, String> loginInfo;
    private HashMap<String, String> personalStats;
    private List<Integer> pictures;
    private String fightingStyle;
    private String biography;
    private String controversialOpinions;
    private HashMap<String, User> seenUsers;
    private ArrayList<User> likes;
    private ArrayList<User> matches;
    private boolean isLoggedIn;

    /**
     * Creates a User object based on the inputted information for the User
     * attributes. There are multiple methods depending on the amount of information
     * the user has filled in.
     * @param id unique username created by new users
     * @param loginInfo username and password stored in Hashmap
     * @param personalStats user's inputted answers to weight, height, gender, etc.
     * @param fightingStyle user's indicated preferred fighting/sparring style
     * @param biography user's inputted biography
     * @param controversialOpinions user's inputted controversial opinions
     * @param isLoggedIn boolean regarding whether the client user is logged in or not
     */
    public User(String id, HashMap<String, String> loginInfo, HashMap<String, String> personalStats,
                String fightingStyle, String biography, String controversialOpinions, boolean isLoggedIn){
        this.id = id;
        this.loginInfo = loginInfo;
        this.personalStats = personalStats;
        this.fightingStyle = fightingStyle;
        this.biography = biography;
        this.controversialOpinions = controversialOpinions;
        this.seenUsers = new HashMap<String, User>();
        this.likes = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.isLoggedIn = false;
    }

    public User(String id, HashMap<String, String> loginInfo){
        this.id = id;
        this.loginInfo = loginInfo;
    }

    public User(){
    }

    /**
     * getters and setters for brawlr.User attributes
     *
     */

    public String getId(){
        return this.id;
    }

//    public ChatManager getuserCM(){
//        return this.userCM;
//    }

    public List<Integer> getPictures(){return this.pictures;}

    public HashMap<String, String> getLoginInfo(){
        return this.loginInfo;
    }

    public HashMap<String, String> getPersonalStats(){
        return this.personalStats;
    }

    public String getFightingStyle(){
        return this.fightingStyle;
    }

    public String getBiography(){
        return this.biography;
    }

    public String getControversialOpinions(){
        return this.controversialOpinions;
    }

    public HashMap<String, User> getSeenUsers(){
        return this.seenUsers;
    }

    public ArrayList<User> getLikes(){
        return this.likes;
    }

    public ArrayList<User> getMatches(){
        return this.matches;
    }

    public void setLoginInfo(HashMap<String, String> loginInfo){
        this.loginInfo = loginInfo;
    }

    public void setPersonalStats(HashMap<String, String> personalStats){
        this.personalStats = personalStats;
    }

    public void setFightingStyle(String fightingStyle){
        this.fightingStyle = fightingStyle;
    }

    public void setBiography(String biography){
        this.biography = biography;
    }

    public void setControversialOpinions(String controversialOpinions){
        this.controversialOpinions = controversialOpinions;
    }

    public void setPictures(List<Integer> pics){this.pictures = pics;}

    public void print(){
        System.out.println("Name is: " + this.id + "My biography is: " + this.biography +
                "My controversial opinion is: " + this.controversialOpinions);
    }

    /**
     *Takes a different instance of User, and to the seen users of this instance of User
     * @param user the user to add
     */

    public void addSeenUser(User user){
        this.seenUsers.put(user.getId(), user);
    }

    /**
     *akes a different instance of User, and to the likes of this instance of User
     * @param user the user to add
     */

    public void addLike(User user){
        this.likes.add(user);
    }

    /**
     * Takes a different instance of User, and to the matches of this instance of User
     * @param user the user to add
     */

    public void addMatch(User user){
        this.matches.add(user);
    }

    /**
     * Logs the user in
     */
    public void logIn(){
        this.isLoggedIn = true;
    }

    /**
     * Logs the user out
     */
    public void logOut(){
        this.isLoggedIn = false;
    }

    public boolean loggedIn(){
        return this.isLoggedIn;
    }
}
