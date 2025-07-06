package com.humanoid.emobin.test;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().body(testService.getPong());
    }
}
