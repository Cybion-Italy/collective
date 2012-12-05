<?php

class KrDbConfiguration {

//        const host     = 'cibionte.cybion.eu';
//        const host     = 'gaia.cybion.eu';
        //gaia ip for emergency
        const host     = '31.169.104.152';
	const user     = 'collective-cat';
        const password = 'categories';
        const db       = 'collective-categories';
        
	function get_host() {
          	return self::host;
	}
	
	function get_user() {
		return self::user;
	}
        
        function get_password() {
		return self::password;
	}
        
        function get_db() {
		return self::db;
	}
}
?>
