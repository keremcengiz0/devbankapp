package com.keremcengiz0.devbankapp.controller;

import com.keremcengiz0.devbankapp.model.Cities;
import com.keremcengiz0.devbankapp.service.CitiesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CitiesController {
    private final CitiesService citiesService;

    public CitiesController(CitiesService citiesService) {
        this.citiesService = citiesService;
    }

    @GetMapping
    public ResponseEntity<List<Cities>> getAllCities() {
        return ResponseEntity.ok(citiesService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cities> getCitiesById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(citiesService.getCitiesById(id));
    }

    @PostMapping
    public ResponseEntity<Cities> createCities(@RequestBody Cities cities) {
        return ResponseEntity.ok(citiesService.createCities(cities));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cities> updateCities(@PathVariable(name = "id") Long id, @RequestBody Cities cities) {
        return ResponseEntity.ok(citiesService.updateCities(id, cities));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCities(@PathVariable(name = "id") Long id) {
        citiesService.deleteCities(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
