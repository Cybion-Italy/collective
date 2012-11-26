
<div class="kr-button-container">

    <form name="permanent_search_link"
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>"
          method="POST" id="permanent_search_link">
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_permanent_search(); ?>"/>
        <input type="hidden" name="action" value="list"/>
        <div>
            <!--<input class="button-menu" type="submit" value="Permanent Search"/>-->
			<button type="submit" class="button-menu"><span>Permanent Search</span></button>
        </div>
    </form>

    <form name="user_feedback_link"
          action="<?php echo $moduleNames->get_knowledge_room_name(); ?>"
          method="POST" id="user_feedback_link">
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_user_feedback(); ?>"/>
        <input type="hidden" name="action" value="list"/> 
        <div>
            <!--<input class="button-menu" type="submit" value="Recommendations"/>-->
			<button type="submit" class="button-menu"><span>Recommendations</span></button>
        </div>
    </form>
    
    <form name="custom_concepts_link"
          action=""
          method="POST" id="custom_concepts_link">
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_custom_concepts(); ?>"/>
        <input type="hidden" name="action" value="list"/> 
        <div>
            <!--<input class="button-menu" type="submit" value="Concepts" />-->
			<button type="submit" class="button-menu"><span>Concepts</span></button>
        </div>
    </form>    
    
    <form name="manage_resources_link"
          action=""
          method="POST" id="manage_resources_link">
        <input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management(); ?>"/>
        <input type="hidden" name="action" value="list"/> 
        <div>
            <!--<input class="button-menu" type="submit" value="Categories" />-->
			<button type="submit" class="button-menu"><span>Categories</span></button>
        </div>
    </form>

</div>
