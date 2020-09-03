package io.balasky.saleatsbackend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.balasky.saleatsbackend.persistence.models.YelpResult;
import io.balasky.saleatsbackend.persistence.repositories.UserRepo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@Controller
public class FavoritesController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/favorites")
    public ModelAndView get_favorites(HttpServletRequest req) {

        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");

        //Retrieves a list of the users favorites
        //Make an API call for each
        List<String> favorites = userRepo.getFavorites(username);
        List<YelpResult> userFavoriteObjects = new ArrayList<>();



        for(String yelpBizId: favorites) {
            try {

                //Call business api with unique identifier
                HttpResponse<JsonNode> request = Unirest.get("https://api.yelp.com/v3/businesses/{businessId}")
                    .header("Authorization", "Bearer eNKRVGf3fyPXqAsY0JTsA66pEUshFWE_LHFDSL03_" +
                        "ZkYBO6M-S-sM5ffCGB6l1QunpR0UvlrFBhlJ1EZrlXQACrJCOC6m5M2v6wqN93f7HXs02iXYVs" +
                        "-agSfWWYbX3Yx")
                    .routeParam("businessId", yelpBizId)
                    .asJson();

                //Get an array of allresults
                JSONObject result = request.getBody().getObject();

                //Turn into map for easy reading
                HashMap<String, Object> jsonMap = new Gson().fromJson(result.toString(),HashMap.class);

                //Get neccessary fields
                String yelpId = (String) jsonMap.get("id");
                String name = (String) jsonMap.get("name");
                String imgUrl = (String) jsonMap.get("image_url");
                String yelpUrl = (String) jsonMap.get("url");
                LinkedTreeMap<String, Object> addressMap = (LinkedTreeMap<String, Object>) jsonMap.get("location");
                String address = (String) addressMap.get("address1");

                //Create YelpResult POJO
                YelpResult yelpResult = new YelpResult(yelpId, name, address, yelpUrl, imgUrl);

                //Add to list
                userFavoriteObjects.add(yelpResult);

            }
            catch(Exception e) {
                System.out.println("An api call failed");
            }
        }



        ModelAndView mv = new ModelAndView("favorites");
        mv.addObject("userFavorites", userFavoriteObjects);
        mv.addObject("username", username);

        return mv;
    }
}
