package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.converter.DateTimeConverter
import java.time.LocalDateTime

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goal: Goal)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(goal: Goal)

    @Delete
    fun delete(goal: Goal)

    @Query("SELECT * FROM goal")
    fun getAll(): List<Goal>

    @Query("SELECT * FROM goal WHERE id = :id")
    fun getById(id: Int): Goal

    @Query("SELECT * FROM goal WHERE goal_priority = :priority")
    fun getByPriority(priority: Int): List<Goal>

    @Query("SELECT * FROM goal WHERE goal_start >= :startDate")
    @TypeConverters(DateTimeConverter::class)
    fun getByStartDate(startDate: LocalDateTime): List<Goal>

    @Query("SELECT * FROM goal WHERE goal_end <= :endDate")
    @TypeConverters(DateTimeConverter::class)
    fun getByEndDate(endDate: LocalDateTime): List<Goal>

    @Query("SELECT * FROM goal WHERE goal_name = :content")
    fun getByContent(content: String): List<Goal>

    @Query("DELETE FROM goal WHERE id = :id")
    fun deleteById(id: Int)
    @Query("DELETE FROM goal WHERE goal_name = :name")
    fun deleteByName(name: String)

    @Query("SELECT COUNT(*) FROM goal")
    fun getCount(): Int

    @Query("SELECT goal_name FROM goal WHERE id = :id")
    fun getNameById(id: Int): String

    @Query("DELETE FROM goal")
    fun deleteAll()
}