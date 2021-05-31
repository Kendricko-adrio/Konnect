package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.Education
import edu.bluejack20_2.Konnect.models.EducationDegree
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.models.StudyField
import edu.bluejack20_2.Konnect.repositories.EducationDegreeRepository
import edu.bluejack20_2.Konnect.repositories.EducationRepository
import edu.bluejack20_2.Konnect.repositories.InstitutionRepository
import edu.bluejack20_2.Konnect.repositories.StudyFieldRepository

class EducationInputViewModel {

    suspend fun getAllInstitutions(): MutableList<Institution> {
        return InstitutionRepository.getAllInstitutions()
    }

    suspend fun getAllDegrees(): MutableList<EducationDegree> {
        return EducationDegreeRepository.getAllEducationDegrees()
    }

    suspend fun getAllFields(): MutableList<StudyField> {
        return StudyFieldRepository.getAllFields()
    }

    suspend fun getEducationById(id: String): Education {
        return EducationRepository.getEducationById(id)
    }

    suspend fun updateEducation(education: Education) {
        EducationRepository.updateEducation(education)
    }

    suspend fun addEducation(education: Education, userId: String) {
        EducationRepository.addEducation(education, userId)
    }

}