package data.database

//imports
//import Data.dao.AchievementDao
import data.dao.ExpenseDao
import data.dao.MonthlyDao
import data.dao.UserDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pennytracker.data.Expense
import com.example.pennytracker.data.User
import com.example.pennytracker.data.MonthlyGoal
//import com.example.pennytracker.data.Achievement

@Database(
    entities = [User::class, Expense::class, MonthlyGoal::class], //Achievement::class
    version = 2,
    exportSchema = false
)

abstract class AppDatabase() : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun monthlyGoalDao(): MonthlyDao
    //abstract fun achievementDao(): AchievementDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "penny_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}