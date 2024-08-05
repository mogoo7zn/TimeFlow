# 核心模块说明
## bean
+ Goal.java: 目标类，用于存储用户的目标信息
+ Action.java: 行为类，用于存储用户的行为信息
+ Task.java: 任务类，用于存储用户的任务信息
+ Milestone.java: 里程碑类，用于存储用户的里程碑信息
## restriction
+ Restriction.java: 限制类，用于存储用户的限制信息

#### 必选
+ FixedTimeRestriction.java: 固定时间限制类，用于存储用户的固定时间限制信息(包含每天起止时间，重复周期)
+ RepeatRestriction.java: 重复限制类，用于存储用户的重复限制信息（包含完成次数，总共次数，间隔时间，间隔时间内次数）
+ TimeRestriction.java: 时间限制类，用于存储用户的时间限制信息（起止时间）

#### 可选，用于安排任务时参考
+ PriorityRestriction.java: 优先级限制类，用于存储用户的优先级限制信息
+ ResourceRestriction.java: 资源限制类，用于存储用户的资源限制信息
+ LocationRestriction.java: 地点限制类，用于存储用户的地点限制信息

> 限制类用于存储用户的限制信息，限制信息包括固定时间限制、优先级限制、重复限制、资源限制、时间限制、地点限制等，限制类的属性包括限制类型、限制内容、限制描述等。
> 示例；
> 固定时间任务：在每年/月/周/日的某个时间点执行的任务，包含固定时间限制与时间限制（可选）。
> 重复任务：重复执行的任务，包含重复限制和时间限制。
 
## Dao
## Database
(略)

## model
+ `interface Valuer`: 评估一项行为的优先级 
+ `abstract Scheduler` : 根据传入的Valuer, 按照特定规则进行规划,产生一个对应时间的`TimeTable`

+ `SimpleValuer`: 一个简单的实现, 按目标重要性安排
+ `SimpleScheduler`: 

+ `DeadlineValuer`: 按结束时间来安排

## util

+ `TimeTable`: 提供与TaskDB最直接的交互, 包括: 
  - 任务时间展示
  - 任务时间调整
  - 添加/删除任务
  - 任务完成度反馈
  - 数据同步
  - 如果可以, 反馈其他东西(优先级等限制的调整)
  
可以把他看成特定时间段的taskDB接口.
  
## Converter

用于存入数据库时类型的转换.
略.

# 如何使用？


## 数据存入
直接调用数据库存。
示例：
```kotlin
    val db = TestDB.getDatabase(appContext)
    val dao = db.testDao()
    val testData = TestData(para1,para2,para3)
    dao.insert(testData)
```

## 数据查询

示例:
```kotlin
    val db = TestDB.getDatabase(appContext)
    val dao = db.testDao()
    val testData = dao.getAll()
```
> 注: 用这种方式处理Goal/Action/Milestone的数据
> `Task` 不推荐手动操作

## 自动规划

```kotlin
import cn.edu.ustc.timeflow.model.Scheduler
import cn.edu.ustc.timeflow.util.TimeTable

val scheduler: Scheduler = new()//用具体某种Scheduler来初始化
var timeTable: TimeTable = scheduler.getTimeTable(start,end)//获得规划后的TimeTable

```



