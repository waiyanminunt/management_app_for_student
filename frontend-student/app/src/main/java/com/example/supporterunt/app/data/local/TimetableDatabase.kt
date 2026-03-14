package com.example.supporterunt.app.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Entity
@Entity(tableName = "timetable")
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val time: String,
    val room: String,
    val teacherName: String
)

// 2. DAO
@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetable")
    fun getAllClasses(): Flow<List<TimetableEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(classes: List<TimetableEntity>)
    
    @Query("DELETE FROM timetable")
    suspend fun clearAll()
}

// 3. Database Singleton
@Database(entities = [TimetableEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
