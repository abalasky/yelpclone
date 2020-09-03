package io.balasky.saleatsbackend.controllers;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import io.balasky.saleatsbackend.persistence.repositories.UserRepo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@Controller
public class DetailsController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/details/{yelpId}")
    public ModelAndView get_details(@PathVariable String yelpId, HttpServletRequest req) {

        ModelAndView mv = new ModelAndView("details");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        //Call business api with unique identifier
        HttpResponse<JsonNode> request = Unirest.get("https://api.yelp.com/v3/businesses/{businessId}")
                    .header("Authorization", "Bearer eNKRVGf3fyPXqAsY0JTsA66pEUshFWE_LHFDSL03_" +
                            "ZkYBO6M-S-sM5ffCGB6l1QunpR0UvlrFBhlJ1EZrlXQACrJCOC6m5M2v6wqN93f7HXs02iXYVs" +
                            "-agSfWWYbX3Yx")
                    .routeParam("businessId", yelpId)
                    .asJson();

        //Get an array of allresults
        JSONObject result = request.getBody().getObject();

        //Turn into map for easy reading
        HashMap<String, Object> jsonMap = new Gson().fromJson(result.toString(),HashMap.class);

        //Pass the required attributes to the view
        mv.addObject("Name", (String) jsonMap.get("name"));
        mv.addObject("Phone", (String) jsonMap.get("display_phone"));
        mv.addObject("Price", (String) jsonMap.get("price"));
        mv.addObject("Rating", jsonMap.get("rating"));
        mv.addObject("Image", jsonMap.get("image_url"));

        //Address
        LinkedTreeMap<String, Object> addressMap = (LinkedTreeMap<String, Object>) jsonMap.get("location");
        try {
            //TODO: Figure out why null??? exception
            mv.addObject("Address", addressMap.get("address1"));
        } catch (Exception e) {
            System.out.println("Idk why this is throwing");
        }



        //TODO: Cuisine
        mv.addObject("Cuisine", "Asian");


        if(username !=null) {
            mv.addObject("username", username);
        }



        return mv;
    }

    //Adds favorite to logged in users list of favorites
    @PostMapping("/details/{yelpId}")
    @ResponseBody
    public String post_favorite(@PathVariable String yelpId, HttpServletRequest req) {

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        if(username != null) {

            //Check if favroite already added
            if(userRepo.userHasFavorite(yelpId)) {
                return "Already a favorite";
            }

            //Insert the favorite for the user
            try {
                userRepo.addFavorite(username, yelpId);
            } catch (Exception e) {
                return "Error: Could not add to favorites";
            }


            return "Added to favorites";
        }
        else {
            return "Please log in to add to favorites";
        }


     }
}
