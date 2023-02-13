package com.onlineShopping.repository;

import com.onlineShopping.model.CataloguePreference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CataloguePreferenceRepository extends MongoRepository<CataloguePreference, String> {
    CataloguePreference findByEmail(String email);
}
