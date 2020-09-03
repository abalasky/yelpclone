package io.balasky.saleatsbackend.persistence.models;

public class SignUpForm {

    private String email;
    private String username;
    private String password;
    private String confirmPassword;


    public SignUpForm() {
    }

    public SignUpForm(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public SignUpForm email(String email) {
        this.email = email;
        return this;
    }

    public SignUpForm username(String username) {
        this.username = username;
        return this;
    }

    public SignUpForm password(String password) {
        this.password = password;
        return this;
    }

    public SignUpForm confirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " email='" + getEmail() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", confirmPassword='" + getConfirmPassword() + "'" +
            "}";
    }


}
