<?php

class KrEndpointConfiguration {

//        const host = 'cibionte.cybion.eu';
	const host = 'gaia.cybion.eu';
	const port = '8080';

	function get_host() {
  	return self::host;
	}
	
	function get_port() {
		return self::port;
	}
}

?>