package Data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pennytracker.data.Expense


@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesBetweenDate(startDate: String, endDate: String): List<Expense>

}