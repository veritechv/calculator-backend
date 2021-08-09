package org.challenge.calculator.utils;

/**
 * Utility class to deal with json formatting
 */
public class JsonUtil {
    public static  String buildJsonSimpleResponse(String response){
        return "{\"response\":\""+response+"\"}";
    }
}
