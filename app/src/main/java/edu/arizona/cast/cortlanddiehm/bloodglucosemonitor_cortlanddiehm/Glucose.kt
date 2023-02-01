package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import java.util.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Glucose(@PrimaryKey var date: Date = GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
    Calendar.getInstance().get(Calendar.MONTH),
    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).time,
                   var fasting:Int = -1,
                   var breakfast:Int = -1,
                   var lunch:Int = -1,
                   var dinner:Int = -1
)