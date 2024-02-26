package org.scheduler.viewmodels;

public record User(String username, int userId, String password) {
    /**
     * constructor for user object
     *
     * @param username
     * @param userId
     * @param password
     */
    public User {
    }

    /**
     * getter/setter for User
     *
     * @return
     */
    @Override
    public String username() {
        return username;
    }

    /**
     * getter/setter for User
     *
     * @return
     */
    @Override
    public int userId() {
        return userId;
    }

    /**
     * getter/setter for User
     *
     * @return
     */
    @Override
    public String password() {
        return password;
    }
}
