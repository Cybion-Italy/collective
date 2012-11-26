package com.collective.activity.model;

import org.apache.abdera2.activities.model.ASObject;

import java.util.List;
import java.util.Map;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class WebResourceEnhancedObject extends ASObject {

    public static final String TOPICS = "topics";
    public static final String COLLECTIVE_URL = "collective-url";
    public static final String OBJECT_TYPE = "web-resource-enhanced";

    public WebResourceEnhancedObject(Map<String,Object> map) {
        super(map, WebResourceBuilder.class, WebResourceEnhancedObject.class);
    }

    public <X extends WebResourceEnhancedObject, M extends Builder<X,M>>WebResourceEnhancedObject(Map<String,Object> map, Class<M> _class, Class<X>_obj) {
        super(map,_class,_obj);
    }

    /* getters using constants */
    //example:
    public ASObject getTopics() {
        return getProperty(TOPICS);
    }
    
    public String getCollectiveUrl() {
        return getProperty(COLLECTIVE_URL);
    }

    public static WebResourceBuilder makeWebResourceEnhanced() {
        return new WebResourceBuilder(OBJECT_TYPE);
    }

    public static WebResourceBuilder makeWebResourceEnhanced(String url) {
        return makeWebResourceEnhanced().url(url);
    }

    @org.apache.abdera2.common.anno.Name(OBJECT_TYPE)
    public static final class WebResourceBuilder
            extends Builder<WebResourceEnhancedObject, WebResourceBuilder> {

        public WebResourceBuilder() {
            super(WebResourceEnhancedObject.class,WebResourceBuilder.class);
        }

        public WebResourceBuilder(Map<String, Object> map) {
            super(map, WebResourceEnhancedObject.class,WebResourceBuilder.class);
        }

        public WebResourceBuilder(String objectType) {
            super(objectType, WebResourceEnhancedObject.class,WebResourceBuilder.class);
        }     
    }

    /* fluent-style builder */
    public static abstract class Builder<X extends WebResourceEnhancedObject, M extends Builder<X,M>>
            extends ASObject.Builder<X,M> {

        public Builder(Class<X>_class,Class<M>_builder) {
            super(_class,_builder);
        }
        public Builder(String objectType,Class<X>_class,Class<M>_builder) {
            super(objectType,_class,_builder);
        }
        public Builder(Map<String,Object> map,Class<X>_class,Class<M>_builder) {
            super(map,_class,_builder);
        }

        public M topics(ASObject fn) {
            set(TOPICS,fn);
            return (M)this;
        }

        public M collectiveUrl(String s) {
            set(COLLECTIVE_URL, s);
            return (M)this;
        }
    }
}
