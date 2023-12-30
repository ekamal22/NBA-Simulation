package main.java.User;

public class User {
    private String nickname;
    private String password;
    private String realName;
    private String surname;
    private int age;
    private String email;
    private String profilePhoto; // Path to the profile photo file

    // Constructor
    public User(String nickname, String password, String realName, String surname, int age, String email) {
        this.nickname = nickname;
        this.password = password;
        this.realName = realName;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.profilePhoto = "C:\\Users\\Effendi Jabid Kamal\\eclipse-workspace\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\Default pfp.png"; // Assuming a default image
    }

    // Getters and Setters
    public String getNickname() {
        return nickname;
    }

    // Nickname and real name are not allowed to change, so no setters for them
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    // Method to display user information
    public String toString() {
        return "User{" +
               "Nickname='" + nickname + '\'' +
               ", Real Name='" + realName + '\'' +
               ", Surname='" + surname + '\'' +
               ", Age=" + age +
               ", Email='" + email + '\'' +
               ", Profile Photo='" + profilePhoto + '\'' +
               '}';
    }

    // Additional methods can be added as needed, such as validation methods
}
