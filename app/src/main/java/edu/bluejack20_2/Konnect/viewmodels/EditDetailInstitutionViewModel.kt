package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.repositories.CityRepository
import edu.bluejack20_2.Konnect.repositories.InstitutionRepository

class EditDetailInstitutionViewModel {

    suspend fun getInstitutionById(instId: String): Institution? {
        return InstitutionRepository.getInstitutionById(instId)
    }

    suspend fun getAllCities(): MutableList<City> {
        return CityRepository.getAllCities()
    }

    suspend fun updateInstitution(institution: Institution) {
        InstitutionRepository.updateInstitution(institution)
    }

}