package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
public class ApiController {

    @GetMapping("/")
    public String root() {
        return "FastAPI is more straightforward XD";
    }

    @GetMapping("/randomnumbers")
    public int Random() { return new Random().nextInt(100000000);}

    @GetMapping("/{name}")
    public String getByName(@PathVariable String name) {
        return name;
    }
}
