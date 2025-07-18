package org.alexmond.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldRest {
    @GetMapping("/")
    public String HelloWorld(){
        return "Hello World";
    }
}
