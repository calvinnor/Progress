package com.calvinnor.progress.data_layer

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Conversion between DB store and Application classes
 */
class RoomTypeConverters {

    @TypeConverter
    fun toCalendar(value: Long?): Calendar? {
        val calendar = GregorianCalendar()
        return value?.run {
            calendar.timeInMillis = value
            return@run calendar
        }
    }

    @TypeConverter
    fun toMillis(date: Calendar?): Long? {
        return date?.timeInMillis
    }
}
