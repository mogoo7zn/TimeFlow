package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Task
import java.time.LocalDateTime

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id: Int): Task

    @Query("SELECT * FROM task WHERE action_id = :actionId")
    fun getByActionId(actionId: Int): List<Task>

    @Query("SELECT * FROM task WHERE task_start >= :startDate AND task_end <= :endDate")
    fun getByTime(startDate: LocalDateTime, endDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE task_start >= :startDate")
    fun getByStartDate(startDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE task_end <= :endDate")
    fun getByEndDate(endDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE finished = :finished")
    fun getByFinished(finished: Boolean): List<Task>

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM task WHERE action_id = :actionId")
    fun deleteByActionId(actionId: Int)


}