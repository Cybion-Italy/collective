<?php

class KrReason {

	var $id;
	var $title;
		
	function __construct($id, $title) {
		$this->id = $id;
		$this->title = $title;
	}
	
	function get_id() {
		return $this->id;
	}
	
	function set_id($id) {
		$this->id = $id;
	}
	
	function get_title() {
		return $this->title;
	}
	
	function set_title($title) {
		$this->title = $title;
	}
}
?>