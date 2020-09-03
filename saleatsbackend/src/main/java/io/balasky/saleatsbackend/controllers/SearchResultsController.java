package io.balasky.saleatsbackend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import io.balasky.saleatsbackend.persistence.models.YelpResult;

@Controller
public class SearchResultsController {

    //Takes 3 query params to call Yelp api
    //@returns the top 10 results as an object to the view
    @GetMapping("/search-results")
    public ModelAndView get_home(@RequestParam String searchTerm, @RequestParam
        String searchLat, @RequestParam String searchLong,
        @RequestParam String sort_by, HttpServletRequest req) {

        //Get session/username
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        //Create list of yelpResults that will be passed to the view
        List<YelpResult> yelpResults = new ArrayList<>();

        try {
            //Construct a unirest request using the query params
            HttpResponse<JsonNode> request = Unirest.get("https://api.yelp" +
                        ".com/v3/businesses/search")
                        .header("Authorization", "Bearer eNKRVGf3fyPXqAsY0JTsA66pEUshFWE_LHFDSL03_" +
                                "ZkYBO6M-S-sM5ffCGB6l1QunpR0UvlrFBhlJ1EZrlXQACrJCOC6m5M2v6wqN93f7HXs02iXYVs" +
                                "-agSfWWYbX3Yx")
                        .queryString("term", searchTerm)
                        .queryString("latitude", searchLat)
                        .queryString("longitude", searchLong)
                        .queryString("sort_by", sort_by)
                        .asJson();


            //Return an errror if API call failed
            if (request.getStatus() != 200) {
                ModelAndView mv = new ModelAndView("index");
                mv.addObject("apiError", "Could not fetch results. Check coordinates");

                if(username != null) {
                    mv.addObject("username", username);
                }
                return mv;
            }
            //Get an array of allresults
            JSONArray allResults =
                    request.getBody().getObject().getJSONArray("businesses");

            //Get top 10 results
            for(int i = 0; i < 10; i++) {
                JSONObject result = allResults.getJSONObject(i);

                //Convert it to a dictionary for easy reading
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
                yelpResults.add(yelpResult);

            }
        }
        catch(Exception e) {
            ModelAndView mv = new ModelAndView("index");
            if(username != null) {
                mv.addObject("username", username);
            }
            mv.addObject("apiError", "Could not fetch results. Check coordinates");
            return mv;
        }

        //Create the ModelAndView
        ModelAndView mv = new ModelAndView("search-results");
        if(username != null) {
            mv.addObject("username", username);
        }
        mv.addObject("yelpResults", yelpResults);

        return mv;
    }
}
