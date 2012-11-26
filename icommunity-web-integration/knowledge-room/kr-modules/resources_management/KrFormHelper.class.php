<?php

include_once 'KrRecommendation.class.php';

class KrFormHelper {
	
	var $action;
	var $method;
	var $name;
	var $input_list = array();
	var $select = array();
	
	function __construct($form_name) {
	       $this->name = $form_name;
	}
	
	function display_view_form($input_type, $input_name, $recommendation) {
				
		//TODO insert constants to prevent errors and typo in actions, methods and types
		
                $form_string = "";
                
		$form_string .= $this->print_action_method();
		
		foreach ($this->input_list as $input) {
		    $form_string .= '<input type="'.$input[0].'" ';
			$form_string .= 'name="'.$input[1].'" ';
			$form_string .= 'value="'.$input[2].'" ';
			$form_string .= '/>';
		}
		
		//dynamic value
		$form_string .= '<input type="' . $input_type .'" ';
		$form_string .= 'name="' . $input_name .'" ';
                $json_recommendation = htmlentities(json_encode($recommendation), ENT_QUOTES);
		$form_string .= "value='".$json_recommendation."'";
		$form_string .= '/>';
				
		$form_string .= $this->close_form();
                return $form_string;
	}
	
	function print_select_section() {
            $select_string = "";
            
		$select_string .= $this->print_action_method();
		
		$select_string .= '<fieldset>';
		
		foreach ($this->select as $key => $select_item) {
			$select_string .= '<select name = "'.$key.'">';
			$select_string .= $this->print_option_section($select_item);
			$select_string .= '</select>';
		}
		
		$select_string .= '</fieldset>';
		
		foreach ($this->input_list as $input) {
		    $select_string .= '<input type="'.$input[0].'" ';
			$select_string .= 'name="'.$input[1].'" ';
			$select_string .= 'value="'.$input[2].'" ';
			$select_string .= '/>';
		}
		
		$select_string .= $this->close_form();
                
                return $select_string;
	}
	
	function print_option_section($select_item) {
            
            $option_string = "";
		
		foreach ($select_item as $key => $item) {		
			$selection_string = '';
			
			if ($item[0] == true) {
				$selection_string = 'selected="selected"';
			}
                        
                        $label = $item[1];
                        //shorten
                        if (strlen ($label) > 20) {
                            $option_label = substr($item[1], 0, 20);
                            $option_label .= "...";
                            $label = $option_label;
                        }
                        
			$option_string .= '<option value="'.$key.'"'.$selection_string.'>'.$label.'</option>';
		}
                return $option_string;
	}
	
	function print_action_method() {
		return  '<form name   ="'    . $this->name 
						. '" action ="'    . $this->action . '"'
						. '  method ="'    . $this->method . '">';
	}
	
	function close_form() {
		return '</form>';
	}
	
	function add_input($type, $name, $value) {
		$this->input_list[] = array($type, $name, $value);
	}
	
	function add_encoded_input($type, $name, $value) {
		$this->input_list[] = array($type, $name, htmlspecialchars(json_encode($value)));
	}
	
	function set_action ($action) {
		$this->action = $action;
	}
	
	function set_method ($method) {
		$this->method = $method;
	}
	
	function add_select ($select_name) {
		$this->select[$select_name] = array();
	}

	function add_option_value($select_name, $value, $selected, $name) {
		$this->select[$select_name][$value] = array($selected, $name);
	}
}

?>