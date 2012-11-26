
<?php

include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
include_once $dirname.$modules_directory.'/'.$moduleNames->get_custom_concepts()
        .'/KrCustomConceptsWSClient.class.php';

$this_directory = $stylesheet_directory.'/'. $moduleNames->get_knowledge_room_directory() 
        .'/'.$modules_directory.'/'.$moduleNames->get_permanent_search().'/';

//echo "javascript directory: ".$common_javascript_directory.'<br/>';
//echo "this directory: ".$this_directory.'<br/>';
//var_dump("common style dir: " . $common_stylesheet_directory);
?>

<!-- <script src="../../jquery-1.6.2.js"></script> -->
<script src="<?php echo $common_javascript_directory; ?>ui/jquery.ui.core.js"></script>
<script src="<?php echo $common_javascript_directory; ?>ui/jquery.ui.widget.js"></script>
<script src="<?php echo $common_javascript_directory; ?>ui/jquery.ui.position.js"></script>
<script src="<?php echo $common_javascript_directory; ?>ui/jquery.ui.autocomplete.js"></script>
<script src="<?php echo $common_javascript_directory; ?>jquery.validate.js"></script>

<script type="text/javascript" src="<?php echo $this_directory; ?>autocomplete_functions.js"></script>
<script type="text/javascript" src="<?php echo $this_directory; ?>validation_functions.js"></script>

<style>
.ui-autocomplete-loading { background: white url('<?php echo $common_stylesheet_directory; ?>jquery-ui/images/ui-anim_basic_16x16.gif') right center no-repeat; }
label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
</style>

<link rel='stylesheet' id='user_feedback_css' 
      href='<?php echo $common_stylesheet_directory; ?>jquery-ui/jquery.ui.all.css' 
      type='text/css' media='all' />

<div class="kr-section-name">
    Permanent Search
</div>

<div class ="kr-section-description">
    Permanent Search Creation
</div>
<div id="form_container">
<form class="form_style" name="create_permanent_search"
      action="<?php echo $moduleNames->get_knowledge_room_name(); ?>"
      method="POST" id="create_permanent_search">
    <input type="hidden" name="module" value="<?php echo $moduleNames->get_permanent_search();?>"/>
    <input type="hidden" name="action" value="create_permanent_search"/>
    
    <ul >
		<li id="li_1" >
    <label class="title" for="element_1">Permanent Search Name</label>
    <input type="text" name="permanent_search_name" class="required" />
    <input type="hidden" name="permanent_search_user_id" value="<?php echo $user_id; ?>"/>
		</li>
    <div id='SearchConceptsGroup'>    

 <div id="SearchBoxDiv1" class="kr-search-concepts">
 	<li id="li_2" >
  <label class="title" for="element_2">Concept #1</label>
  <input type="text" name="concept_1" id="concept_1" value="" 
         class="suggest_keys required" style="width:175px"/> 
    </li>
 </div>
</div>
    <?php 
    $permanent_search_link = $moduleNames->get_knowledge_room_name();    
    
    $configuration = new KrEndpointConfiguration();
    $custom_concepts_rest_client = new KrCustomConceptsWSClient($configuration);
    $custom_concepts_list = $custom_concepts_rest_client
                        ->get_custom_concepts($knowledge_template->get_user_id());
    
    /*
    echo 'list of concepts: <br/>';
    print_r($custom_concepts_list);
    echo '<br/>';     
    */
    
    ?>
    
    <?php 
    if (sizeof($custom_concepts_list) > 0) { ?>
    
    <!-- user defined concepts -->
	<li id="li_3" >
   <select name="custom_concepts[]" size="5" multiple="multiple" id="element_2">
       <?php 
       foreach ($custom_concepts_list as $key => $custom_concept) {  
           ?>
           <option value="<?php 
           echo $custom_concept['label'];
           echo "|";
           echo $custom_concept['url'];                       
           ?>">
           <?php echo $custom_concept['label']; ?></option>
       <?php } ?>       
    </select>
        </li>        
    <?php } //end if         
    ?>
</br></br>   
<!--<input type='button' value='Add Concept' id='addConcept'/>-->
<a  id="addConcept"><span>Add a Concept</span>
</a>
<!-- for debugging -->
<!-- <input type='button' value='Get Concepts Value' id='getConceptsValue'/> -->
<!--<input type="submit" value="Save" id="create_permanent_search_request"/>-->
<button type="submit" class="button-view-concept" id="create_permanent_search_request"><span>Save</span></button>

</form>
</div>

<?php 
if (sizeof($custom_concepts_list) == 0) {
    echo '<br/><br/><br/>';
    echo 'P. S. : If you want to use your custom concepts, define them in the <b>Concepts</b> section<br/>';
}
?>
