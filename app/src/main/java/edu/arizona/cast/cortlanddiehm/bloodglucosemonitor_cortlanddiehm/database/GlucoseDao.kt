package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.database

import androidx.room.*
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.Glucose
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface GlucoseDao {
    @Query("SELECT * FROM glucose")
    fun getGlucoses(): Flow<List<Glucose>>

    @Query("SELECT * FROM glucose WHERE date=(:date)")
    suspend fun getGlucose(date: Date): Glucose

    @Delete
    suspend fun deleteGlucose(glucose: Glucose)

    @Update
    suspend fun updateGlucose(glucose: Glucose)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGlucose(glucose: Glucose)


}