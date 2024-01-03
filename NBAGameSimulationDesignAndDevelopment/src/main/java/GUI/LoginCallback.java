package main.java.GUI;

import main.java.User.User;
/*
 * This interface is used to define a callback mechanism for handling the login operations.
 * The 'onLoginSuccess' method is invoked when a user successfully logs in.
 * Implementers of this interface should define the specific actions to be taken
 * upon a successful login, typically including updating user interface elements
 * and transitioning to a new application state with the authenticated user's context.
 */
public interface LoginCallback {
    void onLoginSuccess(User user);
}