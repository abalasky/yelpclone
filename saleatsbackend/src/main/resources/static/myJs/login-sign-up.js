const googleBtn = document.querySelector("#customBtn");
const createAcctBtn = document.querySelector("#create-acct-btn");
const loginForm = document.querySelector("#login-form");

let googleUser = {};
let profile;
let auth2;

function start() {
    gapi.load('auth2', function(){
        // Retrieve the singleton for the GoogleAuth library and set up the client.
        auth2 = gapi.auth2.getAuthInstance({
            client_id: '792358818782-qlu34fnrf5t41v68ohua8ua32mm293dh.apps.googleusercontent.com',
            cookiepolicy: 'single_host_origin',
            // Request scopes in addition to 'profile' and 'email'
            discoveryDocs: ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"],
            scope: 'https://www.googleapis.com/auth/calendar'
        });

        googleBtn.addEventListener("click", grantAccountAccess);
    });
};

// function attachSignin(element) {
//     console.log(element.id);
//     auth2.attachClickHandler(element, {},
//         function(googleUser) {
//             //Assgin global object
//             googleUserG = googleUser;

//             profile = googleUser.getBasicProfile();

//         }, function(error) {
//             alert(JSON.stringify(error, undefined, 2));
//         });
// }


async function grantAccountAccess() {
    let authresult = await auth2.grantOfflineAccess();
    profile = auth2.currentUser.get().getBasicProfile();

    let body = {
        username: profile.getName(),
        password: "google",
        email: profile.getEmail(),
    }

    let endpoint = window.location.origin + "/google-login";
    fetch(endpoint, {
            method: "POST",
            credentials:"same-origin",
            headers: {
                'Content-Type': 'application/json'
            },
            redirect: 'follow',
            body: JSON.stringify(body)
        })
        .then( (response) => {
            window.location = window.location.origin;
        })
        .catch( (err) => {

        });
}


async function signInCallback(authResult) {
    console.log(authResult);

    // if(authResult['code']) {
    //     let endpoint = window.location.origin + "/google-login";
    //     let response = await fetch(endpoint, {
    //         method: "POST",
    //         credentials:"same-origin",
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         redirect: 'follow',
    //         body: JSON.stringify(authResult['code'])
    //     });

    // }
}

