package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.Glucose

@Database(entities = [Glucose::class], version = 1)
@TypeConverters(GlucoseTypeConverters::class)
abstract class GlucoseDatabase : RoomDatabase() {
    abstract fun glucoseDao(): GlucoseDao
}