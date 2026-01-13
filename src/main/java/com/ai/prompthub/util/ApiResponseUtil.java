// ApiResponseUtil.java
package com.ai.prompthub.util;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseUtil {
    
    public static Map<String, Object> success() {
        return success("操作成功");
    }
    
    public static Map<String, Object> success(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        return result;
    }
    
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("data", data);
        return result;
    }
    
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}