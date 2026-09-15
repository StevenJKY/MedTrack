/**
 * Selected implementation sample from MedTrack.
 *
 * Demonstrates how aggregated application data can be transformed
 * into context for AI-assisted clinician insights.
 */

suspend fun generateClinicianInsights(
    totalPatients: Int,
    averageMedications: Double,
    mostCommonSymptom: String,
    averageSeverity: Double,
    medicationSummary: String,
    symptomSummary: String,
    generateContent: suspend (String) -> String?
): String {

    val prompt = """
        You are helping a clinician understand medication and symptom patterns.

        Dataset summary:
        Total patients: $totalPatients
        Average medications per patient: $averageMedications
        Most common symptom: $mostCommonSymptom
        Average symptom severity: $averageSeverity

        Medication counts by patient:
        $medicationSummary

        Symptom summary:
        $symptomSummary

        Generate exactly 3 concise clinical observations or patterns from this dataset.
        Use a numbered list.
        Do not provide diagnosis or medical treatment advice.
    """.trimIndent()

    return generateContent(prompt)
        ?.trim()
        ?.ifBlank { "No insights generated." }
        ?: "No insights generated."
}