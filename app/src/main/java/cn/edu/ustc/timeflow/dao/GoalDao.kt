package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Goal
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
    fun getByStartDate(startDate: LocalDateTime): List<Goal>

    @Query("SELECT * FROM goal WHERE goal_end <= :endDate")
    fun getByEndDate(endDate: LocalDateTime): List<Goal>

    @Query("DELETE FROM goal WHERE id = :id")
    fun deleteById(id: Int)
    @Query("DELETE FROM goal WHERE goal_name = :name")
    fun deleteByName(name: String)
}