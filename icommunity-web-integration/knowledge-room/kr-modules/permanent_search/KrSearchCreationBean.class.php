<?php

/*
 * This class should map exactly the java one, 
 * com.collective.resources.search.model.SearchCreationBean
 */

class KrSearchCreationBean {
    
    var $userId;
    var $title;
    var $commonConcepts = array();
    var $customConcepts = array();
    
    function __construct($userId, $title, $common_concepts, $custom_concepts) {
        $this->userId = $userId;
        $this->title = $title;
        $this->commonConcepts = $common_concepts;
        $this->customConcepts = $custom_concepts;
    }

    
    public function get_userId() {
        return $this->userId;
    }

    public function set_userId($userId) {
        $this->userId = $userId;
    }

    public function get_title() {
        return $this->title;
    }

    public function set_title($title) {
        $this->title = $title;
    }

    public function get_common_concepts() {
        return $this->commonConcepts;
    }

    public function set_common_concepts($concepts) {
        $this->commonConcepts = $concepts;
    }
    
    public function add_common_concept($concept) {
        $this->commonConcepts[]= $concept;
    }
    
    public function get_custom_concepts() {
        return $this->customConcepts;
    }

    public function set_custom_concepts($concepts) {
        $this->customConcepts = $concepts;
    }
    
    public function add_custom_concept($concept) {
        $this->customConcepts[]= $concept;
    }

}

?>
