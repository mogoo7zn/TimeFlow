package cn.edu.ustc.timeflow.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.edu.ustc.timeflow.dao.DateTimeConverter
import java.time.LocalDateTime

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

    @ColumnInfo(name = "DateTime")
    @TypeConverters(DateTimeConverter::class)
    var DateTime : LocalDateTime = LocalDateTime.now()
}