package com.collective.resources.jersey;

import java.net.URI;
import java.util.Map;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface RESTFrontendConfig {

    URI getURI();

    Map<String,String> getParams();
}
