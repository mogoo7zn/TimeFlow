package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.util.DateTimeConverter
import java.time.LocalDateTime

/**
 * The task data access object
 */
@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    fun insert(tasks: List<Task>) {
        tasks.forEach { insert(it) }
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id: Int): Task

    @Query("SELECT * FROM task WHERE action_id = :actionId ORDER BY task_end")
    fun getByActionId(actionId: Int): List<Task>

    @Query("SELECT * FROM task WHERE task_start >= :startDate AND task_end <= :endDate")
    @TypeConverters(DateTimeConverter::class)
    fun getByTime(startDate: LocalDateTime, endDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE task_start >= :startDate")
    @TypeConverters(DateTimeConverter::class)
    fun getByStartDate(startDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE task_end <= :endDate")
    @TypeConverters(DateTimeConverter::class)
    fun getByEndDate(endDate: LocalDateTime): List<Task>

    @Query("SELECT * FROM task WHERE finished = :finished")
    fun getByFinished(finished: Boolean): List<Task>

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM task WHERE action_id = :actionId")
    fun deleteByActionId(actionId: Int)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM task WHERE action_id = :actionId AND task_start >= :start AND task_end <= :end")
    @TypeConverters(DateTimeConverter::class)
    fun countByActionIdWithTime(actionId: Int, start: LocalDateTime, end: LocalDateTime): Int

    @Query("DELETE FROM task WHERE task_start >= :start AND task_end <= :end")
    @TypeConverters(DateTimeConverter::class)
    fun deleteByTime(start: LocalDateTime, end: LocalDateTime)

    fun updateFinished(taskId: Int, isFinished: Boolean) {
        val task = getById(taskId)
        task.finished = isFinished
        update(task)

    }

}