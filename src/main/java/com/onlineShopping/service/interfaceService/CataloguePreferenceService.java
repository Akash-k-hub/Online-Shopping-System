package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.PreferenceDTO;
import com.onlineShopping.model.CataloguePreference;

public interface CataloguePreferenceService {

    CataloguePreference addPreference(PreferenceDTO preferenceDTO);

    boolean removePreference(PreferenceDTO preferenceDTO);
}
