
const addFavoriteBtn = document.querySelector("#add-to-favorites-btn");
const notificationEl = document.querySelector("#insertHere");

addFavoriteBtn.addEventListener("click", async (event) => {

    event.preventDefault();
    //Get the business id from url path param
    let urlArr = window.location.href.split("/");
    let yelpBizId = urlArr[urlArr.length-1];

    //Get endpoint to POST
    let endpoint = window.location.href;

    //POST
    let response = await fetch(endpoint, {
        method: "POST",
        credentials:"same-origin",
        headers: {
            'Content-Type': 'application/json'
        },
    });

    let result = await response.text();

    //Display result
    notificationEl.textContent = result;

});
