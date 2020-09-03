package io.balasky.saleatsbackend.persistence.models;

public class LoginForm {
    public String username;
    public String password;


    public LoginForm() {
    }

    public LoginForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginForm username(String username) {
        this.username = username;
        return this;
    }

    public LoginForm password(String password) {
        this.password = password;
        return this;
    }


    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

}
