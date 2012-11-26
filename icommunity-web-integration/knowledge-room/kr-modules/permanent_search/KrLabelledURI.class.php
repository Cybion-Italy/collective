<?php

/*
 * should reflect the class package com.collective.permanentsearch.model.LabelledURI 
 */

class KrLabelledURI {
    
    var $label;

    var $url;
    
    function __construct($label, $url) {
            $this->label = $label;
            $this->url = $url;
	}
    
    public function get_label(){
        return $this->label;
    }
    
    public function get_url() {
        return $this->url;
    }
    
    public function set_label($label) {
        $this->label = $label;
    }
    
    public function set_url($url) {
        $this->url = $url;
    }    
    
}


?>
