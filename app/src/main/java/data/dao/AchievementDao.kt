package data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pennytracker.data.Achievement

@Dao
interface AchievementDao {

    @Insert
    suspend fun insert(achievement: Achievement)

    @Query("SELECT * FROM achievements")
    suspend fun getAll(): List<Achievement>
}