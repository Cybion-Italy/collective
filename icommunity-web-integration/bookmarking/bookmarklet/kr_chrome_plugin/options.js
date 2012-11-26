function loadCredentials() {    
    var username = localStorage["username"];        
    var password = localStorage["password"];
    
    if (username == undefined || password == undefined) {
        console.log("Please, fill the form and enter your iCommunity login and password");        
    }
       
    var username_element = document.getElementById("username");
    username_element.value = username;
    var password_element = document.getElementById("password");
    password_element.value = password;    
}

function saveCredentials() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    //TODO Input check!     
    
    localStorage["username"] = username;
    localStorage["password"] = password;
    //TODO: manage alert that says: success! 
}

function eraseCredentials() {
    localStorage.removeItem("username");
    localStorage.removeItem("password");
    location.reload();
    //TODO manage message that says: you erased the credentials! 
}
