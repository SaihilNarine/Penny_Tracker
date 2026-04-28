package data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pennytracker.data.MonthlyGoal

@Dao
interface MonthlyDao {
    @Insert
    suspend fun insertGoal(goal: MonthlyGoal)

    @Update
    suspend fun updateGoal(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals LIMIT 1")
    suspend fun getGoal(): MonthlyGoal?

}