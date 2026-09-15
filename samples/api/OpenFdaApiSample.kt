/**
 * Selected implementation sample from MedTrack.
 *
 * Demonstrates the OpenFDA integration used to retrieve drug label
 * information with a brand-name to generic-name fallback strategy.
 */

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApiService {

    @GET("drug/label.json")
    suspend fun searchDrugByName(
        @Query("search") search: String,
        @Query("limit") limit: Int = 1
    ): DrugLabelResponse
}

data class DrugInfo(
    val brandName: String,
    val genericName: String,
    val purpose: String,
    val warnings: String,
    val dosage: String,
    val activeIngredient: String
)

suspend fun searchDrug(
    drugName: String,
    api: OpenFdaApiService
): DrugInfo? {

    val trimmedName = drugName.trim()

    if (trimmedName.isBlank()) {
        return null
    }

    val brandQuery = "openfda.brand_name:\"$trimmedName\""

    var result = api
        .searchDrugByName(brandQuery)
        .results
        .firstOrNull()

    if (result == null) {
        val genericQuery = "openfda.generic_name:\"$trimmedName\""

        result = api
            .searchDrugByName(genericQuery)
            .results
            .firstOrNull()
    }

    return result?.let { label ->
        DrugInfo(
            brandName = label.openfda?.brandName?.firstOrNull() ?: drugName,
            genericName = label.openfda?.genericName?.firstOrNull() ?: "Not available",
            purpose = label.purpose?.firstOrNull() ?: "Not available",
            warnings = label.warnings?.firstOrNull() ?: "Not available",
            dosage = label.dosageAndAdministration?.firstOrNull() ?: "Not available",
            activeIngredient = label.activeIngredient?.firstOrNull() ?: "Not available"
        )
    }
}