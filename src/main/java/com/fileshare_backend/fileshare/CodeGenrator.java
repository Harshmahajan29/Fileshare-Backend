package com.fileshare_backend.fileshare;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@CrossOrigin(origins = "*")
public class CodeGenrator {
    @PostMapping ("/generatedCode")
    public String generatedCode(){
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(randomNumber);
    }
}
