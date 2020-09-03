package io.balasky.saleatsbackend.persistence.repositories;

import java.util.List;

import io.balasky.saleatsbackend.persistence.models.User;

public interface UserRepo {
    public void addUser(User user);
    public User findUser(String username);
    public void addFavorite(String username, String yelpId);
    public Boolean userExists(String email);
    public Boolean userPasswordCorrect(String username, String password);

    public List<String> getFavorites(String username);
    public Boolean userHasFavorite(String yelpId);
}
