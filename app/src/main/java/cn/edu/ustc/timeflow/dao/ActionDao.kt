package cn.edu.ustc.timeflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cn.edu.ustc.timeflow.bean.Action

@Dao
interface ActionDao {
    @Query("SELECT * FROM 'action' ")
    fun getAll(): List<Action>
    @Query("SELECT * FROM 'action' WHERE id = :id")
    fun getById(id: Int): Action
    @Query("SELECT * FROM 'action' WHERE action_name = :name")
    fun getByName(name: String): Action
    @Query("SELECT * FROM 'action' WHERE action_type = :type")
    fun getByType(type: String): List<Action>
    @Query("SELECT * FROM 'action' WHERE action_finished = :finished")
    fun getByFinished(finished: Boolean): List<Action>
    @Query("SELECT * FROM 'action' WHERE goal_id = :goalId")
    fun getByGoalId(goalId: Int): List<Action>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: Action)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(action: Action)

    @Delete
    fun delete(action: Action)

    @Query("DELETE FROM 'action' WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM 'action' WHERE action_name = :name")
    fun deleteByName(name: String)
}