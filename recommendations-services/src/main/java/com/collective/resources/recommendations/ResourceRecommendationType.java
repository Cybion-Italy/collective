package com.collective.resources.recommendations;

/**
 * This enum lists all the possible recommendation
 * types.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public enum ResourceRecommendationType {

    WEBMAGAZINE {
        public String toString() {
            return "webmagazines";
        }
    },
    RUMOURS {
        public String toString() {
            return "rumors";
        }

    },
    USERS {
        public String toString() {
            return "users";
        }
    }
    
}