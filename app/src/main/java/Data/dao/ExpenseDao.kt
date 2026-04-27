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

    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalSpent(): Float?

    @Query("""SELECT category, SUM(amount) AS total  FROM expenses 
    GROUP BY category""")
    suspend fun getCategoryTotals(): List<CategoryTotal>

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getCount(): Int


    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalAmount(): Double?
}