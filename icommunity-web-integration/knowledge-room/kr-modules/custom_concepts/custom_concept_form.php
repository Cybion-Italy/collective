
<?php
$this_directory = $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$moduleNames->get_custom_concepts().'/';
?>

<script src="<?php echo $common_javascript_directory; ?>jquery.validate.js"></script>
<script type="text/javascript" src="<?php echo $this_directory; ?>validation_functions.js"></script>

<style>
    label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
.small_title{
font-family:verdana;
font-size:11px;
font-weight:bold;
color:#800000;
}
.asterisk{
color:#800000;
font-family:verdana;
font-size:11px;
}
</style>

<div class="kr-section-name">
    Custom Concepts
</div>

<div class ="kr-section-description">
    Custom Concept Creation
</div>

<!-- TODO add jquery validation to fields -->
<form id="create_custom_concept" name ="create_custom_concept"
          method=POST 
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>">
    
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
        <input type="hidden" name="action" value="create_custom_concept"/>
        
        <!-- input fields -->
        
        <div id="resource-container">
            <div id="resource-row">
            <div class="label_new"><label>Label</label></div>
                <div id="resource-value">
                    <input type="text" name="label" size="30" class="required"/>
                </div>
            </div> <!-- resource-row -->


            <div id="resource-row">
            <div class="label_new"><label>Description</label></div>
                <div id="resource-value">
                    <input type="text" name="description" size="30" class="required"/>
                </div>
            </div> <!-- resource-row -->
           
            <div id="resource-row">
            <div class="label_new"><label>Keywords</label></div>
                <div id="resource-value">
                    <input type="text" name="keywords" size="50" class="required"/>
                </div>
            </div> <!-- resource-row -->
           </br> 
            <div id="resource-row">
            <div id="resource-field"></div>
                <div id="resource-value">
                    <button type="submit" value="Save" class="button-view-concept">
                        <span>Save</span>
                    </button>
                </div>
            </div> <!-- resource-row -->
        </div>
        
        <!-- TODO generate values of these fields -->
        <input type="hidden" name="company" value=""/>
        <input type="hidden" name="owner" value =""/>        
        <!-- name will be generated in the next step -->        
        
    </form>

<div>
   <div class="label_new">Instructions on how to compile this form</div>
    
    <div class="small_title">Label:</div><div class="p_new">This label will identify your concept<br/></div>
    
    <div class="small_title">Description: </div><div class="p_new">this is a longer description of what you mean by this concept. 
    It will be useful for yourself as documentation and eventually to
    other people to understand what you exactly mean. <br/></div>
    
    <div class="small_title">Keywords:</div><div class="p_new">This is the most important feature of the concept definition process. <br/></div>
    <div class="p_new">You should write down a list of keywords (separated by commas), that will enable the system to recognise your concept in free-text. <br/></div>
    <div class="p_new">For example, if you write in this field something like: Ireland, Republic of Ireland, Irish Republic, Eire<br/>
    If the system finds in the analysed documents one of these keywords, the document is annotated with your concept. <br/></div>
   <div class="p_new">This means that when you use one of your concepts in the Permanent Search definition, you will see in the results the documents containing one of the keywords you wrote.</div>
</div>
