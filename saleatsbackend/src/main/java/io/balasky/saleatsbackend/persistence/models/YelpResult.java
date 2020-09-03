package io.balasky.saleatsbackend.persistence.models;

//Object representing the YelpResult that will be returned to the front end
//for display on search-results
public class YelpResult {

    public String yelpId;
    public String name;
    public String address;
    public String yelpPageUrl;
    public String imageUrl;

    public YelpResult() {

    }

    public YelpResult(String yelpId, String name, String address, String yelpPageUrl,
        String imageUrl) {
        this.yelpId = yelpId;
        this.name = name;
        this.address = address;
        this.yelpPageUrl = yelpPageUrl;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYelpPageUrl() {
        return this.yelpPageUrl;
    }

    public void setYelpPageUrl(String yelpPageUrl) {
        this.yelpPageUrl = yelpPageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imgUrl) {
        this.imageUrl = imgUrl;
    }


    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", yelpPageUrl='" + getYelpPageUrl() + "'" +
            "}";
    }

}
