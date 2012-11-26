package com.collective.activity.model;

import org.apache.abdera2.activities.model.ASObject;

import java.util.Map;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class TopicObject extends ASObject {

    private static final String OBJECT_TYPE = "topic";

    public TopicObject(Map<String,Object> map) {
        super(map, TopicBuilder.class, TopicObject.class);
    }

    public <X extends TopicObject, M extends Builder<X,M>>TopicObject(Map<String,Object> map, Class<M> _class, Class<X>_obj) {
        super(map,_class,_obj);
    }

    public static TopicBuilder makeTopic() {
        return new TopicBuilder(OBJECT_TYPE);
    }
    
    public static  TopicBuilder makeTopic(String dbpediaUrl) {
        return new TopicBuilder(OBJECT_TYPE).url(dbpediaUrl);
    }

    @org.apache.abdera2.common.anno.Name(OBJECT_TYPE)
    public static final class TopicBuilder
            extends Builder<TopicObject, TopicBuilder> {

        public TopicBuilder() {
            super(TopicObject.class,TopicBuilder.class);
        }

        public TopicBuilder(Map<String, Object> map) {
            super(map, TopicObject.class,TopicBuilder.class);
        }

        public TopicBuilder(String objectType) {
            super(objectType, TopicObject.class,TopicBuilder.class);
        }
    }

    /* fluent-style builder */
    public static abstract class Builder<X extends TopicObject, M extends Builder<X,M>>
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
    }
}
