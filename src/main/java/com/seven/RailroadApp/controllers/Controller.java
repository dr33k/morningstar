package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.responses.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
public interface Controller<Request,Id> {
    @GetMapping
    ResponseEntity<Response> getAllResources();
    @GetMapping("/search")
    ResponseEntity<Response> getResource( Id id);
    @PostMapping("/create")
    ResponseEntity<Response> createResource(@RequestBody Request request);
    @PutMapping("/update")
    ResponseEntity<Response> updateResource(@RequestBody Request request);
    @DeleteMapping("/delete")
    ResponseEntity<Response> deleteResource(Id id);
}
