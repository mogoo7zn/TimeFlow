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
## model

