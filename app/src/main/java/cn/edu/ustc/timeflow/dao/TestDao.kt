package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import cn.edu.ustc.timeflow.entity.TestData
import java.time.LocalDateTime

@Dao
interface TestDao {
    @Query("SELECT * FROM test_data")
    fun getAll(): List<TestData>
    @Query("SELECT * FROM test_data WHERE id = :id")
    fun getById(id: Int): TestData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(testData: TestData)

}

