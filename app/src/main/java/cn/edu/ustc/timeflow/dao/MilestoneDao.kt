package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.util.DateTimeConverter
import java.time.LocalDateTime

@Dao
interface MilestoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(milestone: Milestone)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(milestone: Milestone)

    @Delete
    fun delete(milestone: Milestone)

    @Query("SELECT * FROM milestone")
    fun getAll(): List<Milestone>

    @Query("SELECT * FROM milestone WHERE id = :id")
    fun getById(id: Int): Milestone

    @Query("SELECT * FROM milestone WHERE goal_id = :goalId")
    fun getByGoalId(goalId: Int): List<Milestone>

    @Query("SELECT * FROM milestone WHERE milestone_time >= :time")
    @TypeConverters(DateTimeConverter::class)
    fun getByTime(time: LocalDateTime): List<Milestone>
    @Query("DELETE FROM milestone WHERE id = :id")
    fun deleteById(id: Int)
    @Query("DELETE FROM milestone WHERE goal_id = :goalId")
    fun deleteByGoalId(goalId: Int)
}