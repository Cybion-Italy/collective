<?php

include_once $dirname . $modules_directory . '/' . 'KrEndpointConfiguration.class.php';
include_once $dirname . $modules_directory . '/' . 'KrRestClientExtended.class.php';

class KrPermanentSearchWSClientException extends Exception {
    const NOT_MODIFIED = 304;
    const BAD_REQUEST = 400;
    const NOT_FOUND = 404;
    const NOT_ALOWED = 405;
    const CONFLICT = 409;
    const PRECONDITION_FAILED = 412;
    const INTERNAL_ERROR = 500;
}

/**
 * Description of KrCustomConceptsWSClient
 *
 * @author Matteo Moci
 */
class KrCustomConceptsWSClient {
    const BASE_DIR = 'recommendations-services/rest/';
    const CONCEPTS_ROOT = "concepts";
    const CREATE_CUSTOM_CONCEPT = '';

    var $endpointConfiguration;

    function __construct($endpointConfiguration) {
        $this->endpointConfiguration = $endpointConfiguration;
    }

    function create_custom_concept($company, $owner, $name, $label, 
                                   $user, $description, $keywords) {
        
        //build proper name and label to be used in url encoded strings
        $name  = urlencode(htmlentities($name));
        $label = urlencode(htmlentities($label));
        
        //echo 'encoded name: '.$name.'<br/>';
        //echo 'encoded label: '.$label.'<br/>';        

        $headers = array('Content-Type: application/x-www-form-urlencoded', 
                         'Accept: application/json');

        // set URL params /{company}/{owner}/{name}/{label}
        if (!isset($company)) {
            $company = 'no_company';
        }
        
        $url_parameters = '/'.$company.'/'.$owner.'/'.$name.'/'.$label;
        
        //echo 'url_parameters<br/>';
        //var_dump($url_parameters);
        //echo '<br/>';             
                
        $parameters = array();
        $parameters['user'] = $user;
        $parameters['description'] = $description;
        $parameters['keywords'] = $keywords;
                
        $url_encoded_string = '';
        
        foreach ($parameters as $param_name => $value) {           
            $encoded_value = urlencode($value);
            $url_encoded_string = $url_encoded_string."$param_name=$encoded_value&";            
        }        
        $url_encoded_string = substr($url_encoded_string, 0, -1);                
        
        $post_result = "";
        
        /*
        echo 'url: <br/>';
        var_dump(self::BASE_DIR.self::CONCEPTS_ROOT.$url_parameters);
        echo '<br/>';
        
        echo 'encoded params: <br/>';        
        var_dump($url_encoded_string);
        echo '<br/>';         
         */
        
        try {
            $post_result = KrRestClientExtended::connect(
                                    $this->endpointConfiguration->get_host(), 
                                    $this->endpointConfiguration->get_port())
                    ->silentMode(false)
                    ->setHeaders($headers)
                    ->post(self::BASE_DIR.self::CONCEPTS_ROOT.$url_parameters, 
                           $url_encoded_string)
                    ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: ' . $e;
            throw new KrPermanentSearchWSClientException($e->getMessage());
        }
        
        /*
        echo 'post result: ';
        var_dump($post_result[0]);
        echo '<br/>';        
         */
        
        $deserialized_response = array();
        
         if ($post_result[0] instanceof Http_Multiple_Error) {	
                echo "<p>Http error</p>" . $post_result[0]->getStatus(); 
                echo $post_result[0];
        } else {    
                $intermediate_result = json_decode($post_result[0], true);                        
                if ($intermediate_result['status'] !== 'FAIL') {                      
                    $deserialized_response = json_decode($post_result[0], true);
                } else {
                    echo 'error in ws response: '.$intermediate_result['message'];
                    throw new KrPermanentSearchWSClientException($intermediate_result['message']);
                }               
        }  
        /*
        echo 'response: <br/>';
        print_r($deserialied_response);
        echo '<br/>';         
         */
        
        return $deserialized_response;
    }
    
    
    function get_custom_concepts($user) {
        
        $user_url = '/'.$user;
        
        $get_result = array();
                        
        try {
            $get_result = KrRestClientExtended::connect(
                                    $this->endpointConfiguration->get_host(), 
                                    $this->endpointConfiguration->get_port())
                    ->silentMode(false)
                    ->setHeaders($headers)
                    ->get(self::BASE_DIR.self::CONCEPTS_ROOT.$user_url)
                    ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: ' . $e;
            throw new KrPermanentSearchWSClientException($e->getMessage());
        }
                                        
        $concepts_list = $this->deserialize_response($get_result[0]);        
        return $concepts_list;        
    }
           
    public function delete_custom_concept($company, $owner, $name, $label) {
                
        $response = '';        
        
        $name  = urlencode(htmlentities($name));
        $label = urlencode(htmlentities($label));

        $delete_path = self::BASE_DIR.self::CONCEPTS_ROOT.'/'.$company.'/'.$owner.'/'
                                                   .$name.'/'.$label;
        
        //echo 'encoded name: '.$name.'<br/>';
        //echo 'encoded label: '.$label.'<br/>';            
        //echo $delete_path.'<br/>';
        
        try {
            $delete_result = KrRestClientExtended::connect(
                                    $this->endpointConfiguration->get_host(), 
                                    $this->endpointConfiguration->get_port())
                    ->silentMode(false)
                    ->setHeaders($headers)
                    ->delete($delete_path)
                    ->run();
        } catch (Http_Exception $e) {
            echo 'http exception: ' . $e;
            throw new KrPermanentSearchWSClientException($e->getMessage());
        }                                                                  
        $response = $this->deserialize_response($delete_result[0]); 
        return $response;            
    }
        
     private function deserialize_response($serialized_response) {          
        $deserialized_response = array();        
        if ($serialized_response instanceof Http_Multiple_Error) {
            echo "<p>Http error in KrCustomConceptsWSClient</p>" 
            . $serialized_response->getStatus();
        } else {
            $deserialized_response = json_decode($serialized_response, true);
        }
        return $deserialized_response;
    } 
}

?>
