package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import android.content.Context
import android.provider.Settings
import androidx.room.Room
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.database.GlucoseDatabase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val DATABASE_NAME = "glucose-database.db"

class GlucoseRepository private constructor(context: Context, private val coroutineScope: CoroutineScope = GlobalScope) {

    private val database: GlucoseDatabase = Room
        .databaseBuilder(
        context.applicationContext,
        GlucoseDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    fun getGlucoses(): Flow<List<Glucose>> = database.glucoseDao().getGlucoses()

    suspend fun getGlucose(date: Date): Glucose = database.glucoseDao().getGlucose(date)

    fun updateGlucose(glucose: Glucose) {
        coroutineScope.launch {
            database.glucoseDao().updateGlucose(glucose)
        }
    }

    suspend fun addGlucose(glucose: Glucose) {
        database.glucoseDao().addGlucose(glucose)
    }

    suspend fun deleteGlucose(glucose: Glucose) {
        database.glucoseDao().deleteGlucose(glucose)
    }

    companion object {
        private var INSTANCE: GlucoseRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GlucoseRepository(context)
            }
        }

        fun get(): GlucoseRepository {
            return INSTANCE
                ?: throw IllegalStateException("GlucoseRepository Must be initialized")
        }
    }
}