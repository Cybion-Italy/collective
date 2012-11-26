<?php

/* 
should exactly reflect java Class 
from package com.collective.usertests.model.UserFeedback;
DO NOT rename fields
*/

class KrUserFeedback {
	
	var $id;
	var $userId;
	var $projectId;
	var $urlResource;
	var $like;
	var $reasonId;
	
	function __construct() {
	}
	
	function get_id() {
		return $this->id;
	}
	function get_user_id() {
		return $this->userId;
	}
	function set_user_id($user_id) {
		$this->userId = $user_id;
	}
	function get_project_id() {
		return $this->projectId;
	}
	function set_project_id($project_id) {
		$this->projectId = $project_id;
	}
	function get_url_resource() {
		return $this->urlResource;
	}
	function set_url_resource ($url_resource) {
		$this->urlResource = $url_resource;
	}
	function get_like() {
		return $this->like;
	}
	function set_like($like) {
		$this->like = $like;
	}
	function get_reason_id() {
		return $this->reasonId;
	}
	function set_reason_id ($reason_id) {
		$this->reasonId = $reason_id;
	}
}

?>