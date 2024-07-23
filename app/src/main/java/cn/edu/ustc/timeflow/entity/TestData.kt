package cn.edu.ustc.timeflow.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "test_data")
class TestData {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "age")
    var age: Int = 0
    @ColumnInfo(name = "Sex")
    var sex: Boolean = false
    @ColumnInfo(name = "address")
    var address: String? = ""
}