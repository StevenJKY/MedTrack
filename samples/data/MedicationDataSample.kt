/**
 * Selected implementation sample from MedTrack.
 *
 * Demonstrates the Room persistence layer used for medication data,
 * including an entity, DAO and repository abstraction.
 */

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val scheduledTime: String,
    val medicationType: String,
    val notes: String,
    val isTaken: Boolean = false,
    val takenDate: String = ""
)

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: Medication)

    @Update
    suspend fun updateMedication(medication: Medication)

    @Query("SELECT * FROM medications WHERE patientId = :patientId")
    fun getMedicationsByPatient(patientId: String): Flow<List<Medication>>

    @Query("DELETE FROM medications WHERE id = :medicationId")
    suspend fun deleteMedicationById(medicationId: Int)
}

class MedicationRepository(
    private val medicationDao: MedicationDao
) {

    suspend fun addMedication(medication: Medication) {
        medicationDao.insertMedication(medication)
    }

    suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(medication)
    }

    fun observeMedications(patientId: String): Flow<List<Medication>> {
        return medicationDao.getMedicationsByPatient(patientId)
    }

    suspend fun deleteMedication(medicationId: Int) {
        medicationDao.deleteMedicationById(medicationId)
    }
}