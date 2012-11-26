<?php

class KrRecommendation {

	var $id;
	var $title;
	var $url;
        var $description;
        var $topics;
		        
        function __construct($id, $title, $url, $description, $topics) {
                $this->id = $id;
		$this->title = $title;
		$this->url = $url;
                $this->description = $description;            
                $this->topics = $topics;
        }                 
	
	function get_id() {
		return $this->id;
	}
	
	function get_title() {
		return $this->title;
	}
	function get_url() {
		return $this->url;
	}
        function get_description() {
            return $this->description;
        }
        function get_topics() {
            return $this->topics;
        }
}

?>
