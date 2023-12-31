package main.java.GUI;

import main.java.User.User;

public interface LoginCallback {
    void onLoginSuccess(User user);
}