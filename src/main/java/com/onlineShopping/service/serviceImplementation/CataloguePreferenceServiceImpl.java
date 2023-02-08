package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.dto.PreferenceDTO;
import com.onlineShopping.exception.DuplicatePreferenceException;
import com.onlineShopping.exception.NoPreferenceToRemoveException;
import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.CataloguePreference;
import com.onlineShopping.repository.CataloguePreferenceRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.CataloguePreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CataloguePreferenceServiceImpl implements CataloguePreferenceService {
    @Autowired
    private CataloguePreferenceRepository cataloguePreferenceRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public CataloguePreference addPreference(PreferenceDTO preferenceDTO) {
        //check if user exists in database
        log.info("service=CataloguePreferenceServiceImpl; method=addPreference(); message=checking if user exists");
        if(userRepository.findByEmail(preferenceDTO.getEmail())!=null){
            CataloguePreference preference = cataloguePreferenceRepository.findByEmail(preferenceDTO.getEmail());
            //check if user has already saved preference(s)
            if(preference!=null){
                log.info("service=CataloguePreferenceServiceImpl; method=addPreference(); message=user already has preference, updating preferences");
                List<String> preferenceList = preference.getPreferredCategory();
                if(!preferenceList.contains(preferenceDTO.getPreferredCatalogue())){
                    preferenceList.add(preferenceDTO.getPreferredCatalogue());
                    preference.setPreferredCategory(preferenceList);
                    try {
                        log.info("service=CataloguePreferenceServiceImpl; method=addPreference(); message=updating the preference list");
                        cataloguePreferenceRepository.save(preference);
                        return preference;
                    }catch (Exception exception){
                        log.warn("service=CataloguePreferenceServiceImpl; method=addPreference(); message=PREFERENCE DID NOT GET SAVED");
                        throw new UnableToSaveException("Catalogue Preference did not get saved!");
                    }
                }else {
                    throw new DuplicatePreferenceException("Preference already added!");
                }
            }else {
                log.info("service=CataloguePreferenceServiceImpl; method=addPreference(); message=creating the preference list");
                CataloguePreference cataloguePreference = new CataloguePreference();
                List<String> preferenceList = new ArrayList<>();

                cataloguePreference.setEmail(preferenceDTO.getEmail());
                preferenceList.add(preferenceDTO.getPreferredCatalogue());
                cataloguePreference.setPreferredCategory(preferenceList);
                try{
                    log.info("service=CataloguePreferenceServiceImpl; method=addPreference(); message=creating preference for user");
                    cataloguePreferenceRepository.save(cataloguePreference);
                    return cataloguePreference;
                }catch (Exception exception){
                    log.warn("service=CataloguePreferenceServiceImpl; method=addPreference(); message=PREFERENCE DID NOT GET SAVED");
                    throw new UnableToSaveException("Catalogue Preference did not get saved!");
                }
            }
        }
        log.warn("service=CataloguePreferenceServiceImpl; method=addPreference(); message=USER NOT REGISTERED");
        throw new UserNotFoundException("User not registered, please register and try again!");
    }

    @Override
    public boolean removePreference(PreferenceDTO preferenceDTO) {
        //check if the user is registered,
        if (userRepository.findByEmail(preferenceDTO.getEmail())!=null){
            String preferenceToRemove = preferenceDTO.getPreferredCatalogue();
            CataloguePreference preference = cataloguePreferenceRepository.findByEmail(preferenceDTO.getEmail());
            //check if the preference list is empty or not
            if (!preference.getPreferredCategory().isEmpty()){
                log.warn("service=CataloguePreferenceServiceImpl; method=removePreference(); message=removing {} from list", preferenceDTO.getPreferredCatalogue());
                preference.getPreferredCategory().remove(preferenceDTO.getPreferredCatalogue());
                try {
                    log.info("service=CataloguePreferenceServiceImpl; method=removePreference(); message=updating the preference list");
                    cataloguePreferenceRepository.save(preference);
                    return true;
                }catch (Exception exception){
                 throw new UnableToSaveException("Catalogue Preference did not get saved!");
                }
            }
            throw new NoPreferenceToRemoveException("Catalogue preference is already empty!");
        }
        throw new UserNotFoundException("User not found, please register and try again!");
    }

}
