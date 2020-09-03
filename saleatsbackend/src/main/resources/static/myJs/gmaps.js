let map;

//Grab lat and long fields
const lat = document.querySelector("#latitude");
const long = document.querySelector("#longitude");


function initMap() {
    var myLatlng = {lat: -25.363, lng: 131.044};

    map = new google.maps.Map(document.getElementById("map"), {
        center: myLatlng,
        zoom: 8
    });

    //Attempt to geolocate the user
    infoWindowGeo = new google.maps.InfoWindow();
    // Try HTML5 geolocation.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
        position => {
            const pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
            };
            infoWindowGeo.setPosition(pos);
            infoWindowGeo.setContent("Location found.");
            infoWindowGeo.open(map);
            map.setCenter(pos);
        },
        () => {
            handleLocationError(true, infoWindowGeo, map.getCenter());
        }
        );
    } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindowGeo, map.getCenter());
    }

    // Create the initial InfoWindow.
    let infoWindow = new google.maps.InfoWindow({content: 'Click the map to get Lat/Lng!', position: myLatlng});
    infoWindow.open(map);

    map.addListener("click", (mapsMouseEvent) => {
        infoWindow.close();

        infoWindow = new google.maps.InfoWindow({position:mapsMouseEvent.latLng});
        infoWindow.setContent(mapsMouseEvent.latLng.toString());
        infoWindow.open(map);

        //Get the lat and long coords
        let coords = parseLatLong(mapsMouseEvent.latLng.toString());
        //Set the lat/long fields in search form
        lat.value = coords[0];
        long.value = coords[1];
    });

}

//Handle open close of gmaps overlay

const gmapsBtn = document.querySelector("#gmaps-btn");
const closeMapBtn = document.querySelector("#close-overlay");
const overlayDiv = document.querySelector("#overlay");
//Trigger overlay on click
gmapsBtn.addEventListener("click", (event) => {
    document.getElementById("overlay").style.display = "block";
});

// Close overlay
closeMapBtn.addEventListener("click", (event) => {
    document.getElementById("overlay").style.display = "none";
});

//Takes string of lat long from gmaps api and converts to usable coords
//@returns lat/long in array
function parseLatLong(latlngStr) {
    latlngStr = latlngStr.replace("(","");
    latlngStr = latlngStr.replace(")","");
    latlngStr = latlngStr.replace(" ", "");
    let latlngArr = latlngStr.split(",");

    return latlngArr;
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(
      browserHasGeolocation
        ? "Error: The Geolocation service failed."
        : "Error: Your browser doesn't support geolocation."
    );
    infoWindow.open(map);
  }
