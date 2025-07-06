package com.humanoid.emobin.test;


import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {
    public Map<String, String> getPong() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        return response;
    }
}
