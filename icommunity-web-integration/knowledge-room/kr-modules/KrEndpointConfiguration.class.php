<?php

class KrEndpointConfiguration {

//        const host = 'cibionte.cybion.eu';
//	const host = 'gaia.cybion.eu';
    //gaia ip for emergency
        const host     = '31.169.104.152';
	const port = '8080';

	function get_host() {
  	return self::host;
	}
	
	function get_port() {
		return self::port;
	}
}

?>