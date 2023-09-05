package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.model.Cities;
import com.keremcengiz0.devbankapp.repository.CitiesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitiesService {
    private final CitiesRepository citiesRepository;

    public CitiesService(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public List<Cities> getAllCities() {
        return citiesRepository.findAll();
    }

    public Cities getCitiesById(Long id) {
        return citiesRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find city by id: " + id));
    }

    public Cities createCities(Cities cities) {
        return citiesRepository.save(cities);
    }

    public Cities updateCities(Long id, Cities cities) {
        Cities oldCity = getCitiesById(id);

        oldCity.setName(cities.getName());
        oldCity.setPlateCode(cities.getPlateCode());

        return citiesRepository.save(oldCity);
    }

    public void deleteCities(Long id) {
        Cities city = getCitiesById(id);
        citiesRepository.delete(city);
    }
}
