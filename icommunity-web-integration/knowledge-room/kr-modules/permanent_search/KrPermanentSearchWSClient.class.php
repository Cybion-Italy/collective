<?php

include_once $dirname.$modules_directory.'/'.'KrEndpointConfiguration.class.php';
include_once $dirname.$modules_directory.'/'.'KrRestClientExtended.class.php';
include_once 'KrSearchCreationBean.class.php';

class KrPermanentSearchWSClientException extends Exception {
    const NOT_MODIFIED = 304; 
    const BAD_REQUEST  = 400; 
    const NOT_FOUND    = 404; 
    const NOT_ALOWED   = 405; 
    const CONFLICT     = 409; 
    const PRECONDITION_FAILED = 412; 
    const INTERNAL_ERROR      = 500; 
}


class KrPermanentSearchWSClient {
    
    const BASE_DIR = 'recommendations-services/rest/';
    
    /* Permanent Search constants and actions */
    const PERMANENT_SEARCH_BASE = 'FAKE';
    const USER_PERMANENT_SEARCH_LIST   = 'user/searches';
    const USER_PERMANENT_SEARCH_CREATE = 'user/search';
    const PERMANENT_SEARCH_RESULTS     = 'searchrecommendation/webmagazines';
    const USER_RECOMMENDATION          = 'userrecommendation/webmagazines';
    const SHORT_TERM_RECOMMENDATIONS   = 'userrecommendation/short-term';    
    const SEARCH ='search';
    const CONCEPTS = 'concepts';
    const USER_FEEDBACK      = 'user-feedback-test/userfeedback/list';
    const USER_PROFILE_LONG  = 'user-profile/long-term';
    const USER_PROFILE_SHORT = 'user-profile/short-term';
    
    var $endpointConfiguration;
    
    function __construct($endpointConfiguration) {
        $this->endpointConfiguration = $endpointConfiguration;
    }
    
    function get_all_recommendations($user_id, $amount) {              
        $get_recs_for_user_path = 
                    self::BASE_DIR.self::USER_RECOMMENDATION."/$user_id/$amount";
        //var_dump($get_recs_for_user_path);
        //get recommendations for the user
        $headers = array('Accept: application/json');
        $get_result = '';
        try {
            $get_result = KrRestClientExtended::connect(
                                           $this->endpointConfiguration->get_host(), 
                                           $this->endpointConfiguration->get_port())
                                                ->silentMode(false)
                                                ->setHeaders($headers)
                                                ->get($get_recs_for_user_path)
                                                ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
        }
        
        $deserialized_resources = array();
         if ($get_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $get_result[0]->getStatus();                
        } else {    
                $intermediate_result = json_decode($get_result[0], true);                        
                if ($intermediate_result['status'] !== 'FAIL') {                      
                    $deserialized_resources = json_decode($get_result[0], true);
                }                
        }        
        
        return $deserialized_resources;
    }
    
    function get_short_term_recommendations($user_id, $amount) {
        //TODO test functioning 
        $short_term_recs_path = 
            self::BASE_DIR.self::SHORT_TERM_RECOMMENDATIONS."/$user_id/$amount";
                
        //get short term recs for the user
        $headers = array('Accept: application/json');
        $get_result = '';
        try {
            $get_result = KrRestClientExtended::connect(
                                           $this->endpointConfiguration->get_host(), 
                                           $this->endpointConfiguration->get_port())
                                                ->silentMode(false)
                                                ->setHeaders($headers)
                                                ->get($short_term_recs_path)
                                                ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
        }
        
        $deserialized_resources = array();
         if ($get_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $get_result[0]->getStatus();                
        } else {    
                $intermediate_result = json_decode($get_result[0], true);                        
                if ($intermediate_result['status'] !== 'FAIL') {                      
                    $deserialized_resources = json_decode($get_result[0], true);
                }                
        }       
        
        return $deserialized_resources;        
    }
    
    function get_user_feedbacks($user_id) {

        $user_feedbacks_path = self::BASE_DIR.self::USER_FEEDBACK.'/'.$user_id;
        
        //var_dump($user_feedbacks_path);
        
        $headers_list = array('Accept: application/json');

        $get_result = '';
        
        try {
            $get_result = KrRestClientExtended::connect($this->endpointConfiguration->get_host(), 
                                           $this->endpointConfiguration->get_port())
                                                ->setHeaders($headers_list)
                                                ->get($user_feedbacks_path)
                                                ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
        }        
        
        $deserialized_feedbacks = array();
         if ($get_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $get_result[0]->getStatus();                
        } else {    
                $intermediate_result = json_decode($get_result[0], true);                        
                if ($intermediate_result['status'] !== 'FAIL') {                      
                    $deserialized_feedbacks = json_decode($get_result[0], true);
                }                
        }   
        
        //var_dump($deserialized_feedbacks);
        //echo '<br/>';
        return $deserialized_feedbacks;
    }
    
    function get_permanent_search_results($search_id, $limit) {
    
        $headers = array('Accept: application/json');               

        $get_result = "";                  
         
         try {         
            $get_result = KrRestClientExtended::connect(
                                       $this->endpointConfiguration->get_host(), 
                                       $this->endpointConfiguration->get_port())
                                ->silentMode(false)
				->setHeaders($headers)
				->get(self::BASE_DIR
                                        .self::PERMANENT_SEARCH_RESULTS
                                        .'/'.$search_id.'/'.$limit)
                                ->run();
         } catch (Http_Exception $e) {
                echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
         } 
         
         $deserialized_resources = array();
         if ($get_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $get_result[0]->getStatus();                
        } else {    
            $intermediate_result = json_decode($get_result[0], true);                        
                if ($intermediate_result['status'] !== 'FAIL') {                      
                    $deserialized_resources = json_decode($get_result[0], true);
                }                
        }   
        return $deserialized_resources;        
    }
    
    function get_permanent_search($search_id) {
        
        $headers = array('Accept: application/json');               

        $get_result = "";                  
         
         try {         
            $get_result = KrRestClientExtended::connect($this->endpointConfiguration->get_host(), 
                                              $this->endpointConfiguration->get_port())
                                ->silentMode(false)
				->setHeaders($headers)
				->get(self::BASE_DIR.self::SEARCH.'/'.$search_id)
                                ->run();
         } catch (Http_Exception $e) {
                echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
         } 
         
         $deserialized_search = "";
         if ($get_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $get_result[0]->getStatus();
        } else {
                $deserialized_search = json_decode($get_result[0], true);
        }   
        return $deserialized_search;        
    }
    
    function create_permanent_search($user_id, $title, $common_concepts, $custom_concepts) {        
                        
        $headers = array('Accept: application/json', 'Content-Type: application/json');
                
        $search_creation_bean = new KrSearchCreationBean($user_id, $title, 
                                            $common_concepts, $custom_concepts);
                
        $encoded_request = json_encode($search_creation_bean);
        
        /*
        echo 'encoded request: ';
        var_dump($encoded_request);
        echo '<br/>';
        */
        
        $post_result = "";                  
                 
        try {         
            $post_result = KrRestClientExtended::connect($this->endpointConfiguration->get_host(), 
                                              $this->endpointConfiguration->get_port())
                                ->silentMode(false)
				->setHeaders($headers)
				->post(self::BASE_DIR.self::USER_PERMANENT_SEARCH_CREATE, 
                                        $encoded_request)
                                ->run();
        } catch (Http_Exception $e) {
                echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());
        }         
         
        if ($post_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $post_result[0]->getStatus();
        } else {
                $deserialized_response = json_decode($post_result[0], true);
        }   
        return $deserialized_response;
    }
    
    function delete_permanent_search($search_id) {
        $delete_search_path = self::BASE_DIR.self::SEARCH.'/'.$search_id;
        
        $headers = array('Accept: application/json');               
                
        try {
        $result = KrRestClientExtended::connect(
                                $this->endpointConfiguration->get_host(),                                                  
                                $this->endpointConfiguration->get_port())
                                        ->silentMode(false)
					->setHeaders($headers)
					->delete($delete_search_path)
                                        ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: '. $e;
                throw new KrPermanentSearchWSClientException($e->getMessage());            
        }
                
        if ($result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $result[0]->getStatus();
        } else {
                $deserialized_response = json_decode($result[0], true);
        }   
        return $deserialized_response;
    }
    
    
    function get_all_permanent_searches($user_id) {
        // build url for request
        $get_all_permanent_searches = self::BASE_DIR
                                     .self::USER_PERMANENT_SEARCH_LIST.'/'
                                     .$user_id;
                                
        $headers = array('Accept: application/json');
        
        $json_recs = KrRestClientExtended::connect(
                                $this->endpointConfiguration->get_host(),                                                  
                                $this->endpointConfiguration->get_port())
                                        ->silentMode(false)
					->setHeaders($headers)
					->get($get_all_permanent_searches)
                                        ->run();
                                       
        $recommendations_array_list []= array();
        if ($json_recs[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>";
        } else {
                $recommendations_array_list = json_decode($json_recs[0], true);
        }                                
        return $recommendations_array_list;
    }
    
    /* gets a list of users concepts */
    public function get_concepts($user_id) {
                
        $get_concepts_path = self::BASE_DIR
                                     .self::CONCEPTS.'/'
                                     .$user_id;
                                
        $headers = array('Accept: application/json');
        
        $json_response = KrRestClientExtended::connect(
                                $this->endpointConfiguration->get_host(),                                                  
                                $this->endpointConfiguration->get_port())
                                        ->silentMode(false)
					->setHeaders($headers)
					->get($get_concepts_path)
                                        ->run();
                                       
        $concepts = array();
        if ($json_response[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>";
        } else {
                $concepts = json_decode($json_response[0], true);
        }           
        
        return $concepts;
    }
    
    public function get_short_profile($user_id) {
        
        $get_profile_short_path = self::BASE_DIR
                                 .self::USER_PROFILE_SHORT.'/'
                                 .$user_id;
                                
        $headers = array('Accept: application/json');
        
        $json_response = KrRestClientExtended::connect(
                                $this->endpointConfiguration->get_host(),                                                  
                                $this->endpointConfiguration->get_port())
                                        ->silentMode(false)
					->setHeaders($headers)
					->get($get_profile_short_path)
                                        ->run();
                                       
        $short_user_profile = array();
        if ($json_response[0] instanceof Http_Multiple_Error) {	
                echo "<!-- <p>Http error when asking short term profile</p> -->";
        } else {
                $short_user_profile = json_decode($json_response[0], true);
        }           
        
        return $short_user_profile;                
    }
    
    public function get_user_profile_long($user_id) {
        
    }

}

?>
