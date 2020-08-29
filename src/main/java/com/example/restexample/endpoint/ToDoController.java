package com.example.restexample.endpoint;

import com.example.restexample.dto.Photo;
import com.example.restexample.dto.ToDo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    String baseUrl = "https://jsonplaceholder.typicode.com";

    @GetMapping("/todos")
    public List<ToDo> toDos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDo[]> response = restTemplate.getForEntity(baseUrl + "/todos", ToDo[].class);
        ToDo[] body = response.getBody();
        if (body == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(body);
    }

    @GetMapping("/photos")
    public List<Photo> photo() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Photo[]> response = restTemplate.getForEntity(baseUrl + "/photos", Photo[].class);
        Photo[] body = response.getBody();
        if (body == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(body);
    }

}
