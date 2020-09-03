
const errorDiv = document.getElementById("error");
const searchBtn = document.getElementById("search-btn")
const searchFields = document.querySelectorAll(".form-control");
const sortBys = document.querySelectorAll(".sort_by");

searchBtn.addEventListener("click", (event) => {
    event.preventDefault();

    let fieldsArr = [];
    for(let field of searchFields) {

        //Check for missing fields
        if(field.value === "") {

            display_form_error(errorDiv, "Please fill all fields");
            return
        }

        //If exists push to array
        fieldsArr.push(field.value);
    }

    //Handle whitespace and concat for api call
    let searchTerm = fieldsArr[0].trim().replace(" ", "+");

    let lat = parseFloat(fieldsArr[1]);
    let long = parseFloat(fieldsArr[2]);

    if(isNaN(lat) || isNaN(long)) {
        display_form_error(errorDiv, "Invalid lat or long");
    }

    //Get radio button input
    let sortParam = "best_match"; //default if none selected
    for(let sort of sortBys) {
        if(sort.checked) {
            sortParam = sort.id;
        }
    }



    //Change the destination of <a>
    searchBtn.href = `/search-results?searchTerm=${searchTerm}&searchLat=${lat}&searchLong=${long}&sort_by=${sortParam}`;

    //Send user to new page
    location.href = searchBtn.href;

});




//Displays an error message for form validaiton
//@param domElement where message should be inserted
//@param message - text of error message
//@returns void
function display_form_error(domElement, message) {

    //Create a paragraph element
    let errorMessage = document.createElement("P");
    errorMessage.innerHTML = message;
    errorMessage.style.color = "red";

    domElement.appendChild(errorMessage);
}






