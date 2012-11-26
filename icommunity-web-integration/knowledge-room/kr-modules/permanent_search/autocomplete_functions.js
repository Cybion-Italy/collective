$(document).ready(function() {

/* START debugging functions */
    $("#getConceptsValue").click(function() {
        var msg = '';
        for (var i = 1; i <= max_index; i++) {               
            if ($('#concept_' + i).length) {
                msg += "\n Concept #: "               + $('#concept_' + i).val();
                msg += "\n Concept hidden field # : " + $('#concept_' + i + '_hidden').val();        
            }
        }
        alert(msg);
    });
    
    function log( message ) {
        $( "<div/>" ).text( message ).prependTo( "#log" );
        $( "#log" ).scrollTop( 0 );
    }

/* END debugging functions */

    var host = "gaia.cybion.eu";
    // used initially, for DNS problems
//    var host = "31.169.104.152";
    var port = "8080";    
    
    var minimumChars = 2;    
    var gaia_autocompletion_url = "http://" + host + ":" + port 
                                + "/service-1.0-SNAPSHOT/suggest?callback=?" ;
    var owner_id = 0;
    var filter_status = "true";
		
    function autocompleteFunction () {             
        $(this).autocomplete({								
            //source function
            source: function( request, response ) {    
                        $.ajax({
                                url: gaia_autocompletion_url,
                                dataType: "jsonp",
                                data: {
                                        term: request.term, 
                                        owner: owner_id, 
                                        filter: filter_status
                                },
                                success: function( data ) {  
                                    recommendations = data.suggestions;                                                
                                        response( $.map( recommendations, function( item ) {                                                        
                                                return {
                                                    label: item.label,
                                                    value: item.url                                                                    
                                                }                                                
                                        }
                                        ));
                                }
                        });                               
                   },
            focus: function( event, ui ) {
                    $(this).val( ui.item.label );
				return false;
			}    
            ,
            minLength: minimumChars,
            select: function( event, ui ) {

                var _hidden_id   = $(this).attr("id")   + "_hidden";
                var _hidden_name = $(this).attr("name") + "_hidden";
                var _field = $('<input type="hidden"/>');

                if(_hidden_id)
                    _field.attr("id", _hidden_id);
                if(_hidden_name)
                    _field.attr("name", _hidden_name);
                if(ui.item)
                    _field.attr("value", ui.item.value);

                //append hidden field                
                $(this).after(_field);
                //rewrite text field
                $(this).val( ui.item.label );
                                
//                log( ui.item ?
//                    "Selected: " + ui.item.label + " aka " + ui.item.value :
//                    "Nothing selected, input was " + this.value );   
        
                return false;
                
            } //end select					
        } //end parameters of autocomplete
        );				
    }
        
    /* setup class for autocompletion */
    $(".suggest_keys").each(autocompleteFunction);
						   
    /* START functions to add and remove form fields */
			
    var counter = 1;
    var max_concepts = 10; 
    var max_index = 1;

    $("#addConcept").click(function () {
                
        counter++;    
        max_index += 1;
        
        var newTextBoxDiv = $(document.createElement('div'))
        .attr("id", 'SearchBoxDiv' + max_index)
        .attr("class", 'kr-search-concepts');

        newTextBoxDiv.html('<label>Concept #'+ max_index + ' : </label>' +
            '<input type="text" name="concept_' + max_index + 
            '" id="concept_' + max_index + 
            '" value="" class="suggest_keys" style="width:175px"/>' +        
            ' <a href="#" id="removeConceptId">Remove</a>');

        newTextBoxDiv.appendTo("#SearchConceptsGroup");
        
        $(".suggest_keys").each(autocompleteFunction);        
    });

    $("#removeConcept").click(function () {

        if(counter == 1){
            alert("No more concepts to remove");
            return false;
        }   

        $("#SearchBoxDiv"     + counter).remove(); 
        counter--;

    });

    $("#removeConceptId").live('click', function() { 

        if (counter == 1) {
            alert("Can't remove concept");
            return false;
        }

        var conceptId = $(this).closest("div").attr("id");       

        $("#" + conceptId.toString()).remove();        
        counter--;        
    });
    
    //build a request
    $("#create_permanent_search_request").click(function() {        
        var msg = '';
        var json_labeled_urls = [];
        var id_prefix = '#concept_';
        
        for (var i = 1; i <= max_index; i++) {            
            if ($('#concept_' + i).length) {
                
                json_labeled_urls.push({label: $(id_prefix + i).val(), 
                                        url: $(id_prefix + i + '_hidden').val()});                                                                                                                               
            }
        }                       
        
        var permanent_search_json = $(document.createElement('input'))
                                .attr('id', 'permanent_search_json')
                                .attr('name', 'permanent_search_json')
                                .attr('value',  escape(JSON.stringify(json_labeled_urls)))
                                .attr('type', 'hidden');
                                                                                               
        //alert(escape(JSON.stringify(json_labeled_urls)));
               
        permanent_search_json.appendTo("#create_permanent_search");        
             
        return true;
    });
    
    /* END functions to add and remove form fields */
	
});