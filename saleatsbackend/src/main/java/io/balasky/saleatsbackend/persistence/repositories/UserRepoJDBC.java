package io.balasky.saleatsbackend.persistence.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.balasky.saleatsbackend.persistence.models.User;

@Repository
public class UserRepoJDBC implements UserRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User user) {

        jdbcTemplate.update("INSERT INTO users VALUES(?,?,?)",
            user.getUsername(),
            user.getPassword(),
            user.getEmail());
    }

    @Override
    public User findUser(String username) {
        return new User();
    }

    @Override
    public void addFavorite(String username, String yelpId) {
        jdbcTemplate.update("INSERT INTO favorites (username, favorite) VALUES(?,?)",
            username,yelpId);

    }

    @Override
    public List<String> getFavorites(String username) {

         //Set up template query
         Map<String, String> valuesMap = new HashMap<String, String>();
         valuesMap.put("username", username);
         String templateQuery = "SELECT favorite FROM favorites WHERE username='${username}'";
         StringSubstitutor sub = new StringSubstitutor(valuesMap);
         String query = sub.replace(templateQuery);

        return jdbcTemplate.query(query,
                (rs, rowNum) ->
                        rs.getString("favorite"));

    }

    @Override
    public Boolean userHasFavorite(String yelpId) {

        //Set up template query
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("yelpId", yelpId);
        String templateQuery = "SELECT COUNT(*) FROM favorites WHERE favorite='${yelpId}'";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String query = sub.replace(templateQuery);

         //Execute query
         Integer result = jdbcTemplate.queryForObject(query,
            Integer.class);

        if(result != 0) {
            return true;
        }
        else {
            return false;
        }

    }


    @Override
    public Boolean userExists(String email) {

        //Set up template query
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("email", email);
        String templateQuery = "SELECT COUNT(*) FROM users WHERE email='${email}'";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String query = sub.replace(templateQuery);


        //Execute query
        Integer result = jdbcTemplate.queryForObject(query,
            Integer.class);

        if(result != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Boolean userPasswordCorrect(String username, String password) {

        //Set up template query
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("username", username);
        String templateQuery = "SELECT password FROM users WHERE username='${username}'";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String query = sub.replace(templateQuery);

        //Execute the query for the stored hash for a given user
        String dbPassword = jdbcTemplate.queryForObject(query, String.class);


        //Compare to the database result
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), dbPassword);


        //Compare to the provided password
        return result.verified;
    }

}
