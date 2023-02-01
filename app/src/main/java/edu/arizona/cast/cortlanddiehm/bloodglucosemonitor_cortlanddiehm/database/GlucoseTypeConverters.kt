package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.database

import androidx.room.TypeConverter
import java.util.Date

class GlucoseTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}