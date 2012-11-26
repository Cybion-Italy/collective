
var id = "iCommunity-bookmarking-extension-1.0";
        
//TODO move to external configuration file        
var host = "http://cibionte.cybion.eu/collective/backend/";
//var host = "http://gaia.cybion.eu/collective/backend/";
        
function reloadExtension(id) 
{
    chrome.management.setEnabled(id, true, function() 
    {
        chrome.management.setEnabled(id, true);
    });
}        
        
function loadUserCategories()
{    
    var u = localStorage["username"];        
    var p = localStorage["password"];
    
    //TODO alert if null
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", host + "authentication.php?username=" + u + "&password=" + p, true);
    
    xmlhttp.onreadystatechange = function()
    {	
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            var resp = xmlhttp.responseText;
            
            allarray = resp.split(",");
            var lengthall = allarray.length;            
            categoryarray = allarray.splice(0, lengthall - 1);
            
            var lengthcategories = categoryarray.length;
            //print error in a node
            document.getElementById("auth_error_container").innerHTML = categoryarray;
            
            var user_id = allarray.splice(-1, 1);
                        
            if( lengthcategories < 1) 
            {                  
                //if user didn't create yet categories
                document.getElementById("form-container").style.display   = 'none';
                document.getElementById("auth_error_container").innerHTML = 
                "Authentication failed: check your credentials in this extension's options.<br/>" + 
            "Did you create some categories yet?";                
            }
            else 
            {
                chrome.tabs.getSelected(null, function(tab) {
                    // When the browser action is clicked, set the popup for |tab|.
                    // chrome.browserAction.onClicked.addListener(function(tab) {
                    chrome.browserAction.setPopup({
                        tabId: tab.id,          // Set the new popup for this tab.                        
                        popup: 'popup.html'   // Open this html file within the popup.
                    });
                });
                
                //
                reloadExtension(id);
                localStorage["currentUserID"] = user_id[0];
                
                //populate ESISTING form with data
                document.getElementById("auth_error_container").innerHTML = "";
                document.getElementById("action").innerHTML = "Bookmark this resource";
                document.getElementById("tab1").innerHTML = "Title";
                
                console.log("arrived here, i should have built a form");
                
                chrome.tabs.getSelected(null, function (tab) {
                    var x                     = document.getElementById("username");
                    x.value                   = tab.title;   
                    var link_title            = tab.title;       
                    localStorage["linktitle"] = tab.title;                    
                });
                
                //populate existing form with url
                document.getElementById("tab2").innerHTML="URL";
                
                chrome.tabs.getSelected(null, function (tab) {
                    var x                   = document.getElementById("password");
                    x.type                  = "text";
                    x.value                 = tab.url;  
                    localStorage["linkurl"] = tab.url;        
                });
                
                document.getElementById("auth").setAttribute("type","hidden");
                
                var categories_list_div = document.getElementById('categories_list_container');
                var form_save_categories = document.createElement("form");
                
                form_save_categories.setAttribute("method", "POST");
                form_save_categories.setAttribute("id", "myform2");
                
                categories_list_div.appendChild(form_save_categories);
                
                var zform = document.getElementById('myform2');
                
                var chbx = new Array();
                
                var i = 0;
                
                //add a checkbox for each category
                while(i < lengthcategories - 1) 
                {
                    var chk = document.createElement("INPUT");
                    chk.setAttribute("type","checkbox");
                    chk.setAttribute("id",categoryarray[i]);
                    chk.setAttribute("name","myCheck");
                    chk.setAttribute("value","myCheck");
                    
                    zform.appendChild(chk);
                    
                    chbx.splice(i, 0, categoryarray[i]);
                    //console.log(chbx);
                    
                    var lbl= document.createElement("label");
                    lbl.setAttribute("id","lbl"+i);
                    zform.appendChild(lbl);
                    document.getElementById("lbl"+i).innerHTML = categoryarray[i+1];
                    
                    zform.appendChild(document.createElement('BR'));
                    i += 2;
                }
                /*
                for(var j = 0; j < chbx.length; j++){
                    if((document.getElementById(chbx[j]).checked) == false){
                    console.log(chbx[j]);
                    }
                }
                */
                
                link_title = localStorage["linktitle"].replace(/’/g,'\'');
                var p_title = document.createElement("input");                			
                p_title.setAttribute("type","hidden");
                p_title.setAttribute("name","page_title");
                p_title.setAttribute("id","page_title");
                p_title.setAttribute("value",link_title);
                
                zform.appendChild(p_title);
                
                link_url = localStorage["linkurl"];
                var p_url = document.createElement("input");                			
                p_url.setAttribute("type","hidden");
                p_url.setAttribute("name","page_url");
                p_url.setAttribute("id","page_url");
                p_url.setAttribute("value",link_url);
                
                zform.appendChild(p_url);
                zform.appendChild(document.createElement('BR'));
                
                var map = document.createElement("input");
                map.setAttribute("type","button");
                map.setAttribute("value","Bookmark it!");
                map.setAttribute("name","add_mapping");   
                map.setAttribute("onclick","mapResource();");  
                
                zform.appendChild(map);                
            }            
        }
    }
    xmlhttp.send();    
}
  								
function getChecked(array)
{
    var catmapping = new Array();
    var j = 0;
    //console.log(array.length);
    while ( j < array.length ) {
        if((document.getElementById(array[j]).checked) == true) 
        {
            console.log(array[j]);
            catmapping.splice(j, 0, array[j]);
        }
        j++;
    }
}   			
				
function mapResource()
{
    //get checked categories
    var chbx1 = new Array();
    var cats  = "";
    var elDiv = document.getElementById('categories_list_container').getElementsByTagName("*");
    for (var i = 0; i < elDiv.length; i++)
    {
        if(elDiv[i].checked == true){ 
            chbx1.push( elDiv[i].id );
            cats += elDiv[i].id + "and";
            console.log(cats);
        }
    }

    var categories_sent = cats;
    
    if (cats != "") {
        var x = localStorage["linktitle"].replace(/’/g,'\'');           				
        var y = localStorage["linkurl"];
        var xmlhttp = new XMLHttpRequest();

        xmlhttp.open("GET",host + "mapping.php?page_title=" + x + "&page_url=" + y + "&cats=" + categories_sent, true);

        xmlhttp.onreadystatechange = function()
        {	
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
            {
                var resp = xmlhttp.responseText; 							

                document.getElementById("mapping_result").innerHTML = resp;
                window.webkitNotifications.createNotification("agt_action_success.png","Mapping Added", 
                    "The mapping was added successfuly !").show();
            }
        }	
        xmlhttp.send();    
    } else {
        window.webkitNotifications.createNotification("agt_action_fail.png","Mapping Failed", 
                "You didn't select any category!").show();        
    }    
}			
					
function getCategories(userid)
{
    var xmlhttp = new XMLHttpRequest();
        
    xmlhttp.open("GET", host + "user_categories.php?userid=" + userid, true);

    xmlhttp.onreadystatechange=function()
    {	
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            var resp = xmlhttp.responseText;
            document.getElementById("user_categories").innerHTML=resp;   						 
        }
    }
    xmlhttp.send();
}
