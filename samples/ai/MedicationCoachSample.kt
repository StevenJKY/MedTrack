/**
 * Selected implementation sample from MedTrack.
 *
 * Demonstrates how medication and symptom data are used as context
 * for Gemini-powered medication guidance.
 *
 * The complete application source code is not included in this repository.
 */

suspend fun generateMedicationTip(
    medicationNames: List<String>,
    symptomSummaries: List<String>,
    targetMedication: String,
    generateContent: suspend (String) -> String?
): String {

    val medicationContext = medicationNames
        .joinToString(", ")
        .ifBlank { "No medication history" }

    val symptomContext = symptomSummaries
        .joinToString(", ")
        .ifBlank { "No symptom history" }

    val prompt = """
        You are a supportive medication coach.

        Patient medications:
        $medicationContext

        Recent symptoms:
        $symptomContext

        Focus medication:
        $targetMedication

        Generate one short, supportive medication adherence tip in plain English.
        Keep it under 50 words.
    """.trimIndent()

    return generateContent(prompt)
        ?.trim()
        ?.ifBlank {
            "Remember to take $targetMedication consistently each day."
        }
        ?: "Remember to take $targetMedication consistently each day."
}