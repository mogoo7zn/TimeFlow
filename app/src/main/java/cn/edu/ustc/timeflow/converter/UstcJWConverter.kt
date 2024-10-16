package cn.edu.ustc.timeflow.converter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Course
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.database.ActionDB
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper.Companion.getString
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper.Companion.saveString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val TAG = "UstcJWConverter"

/**
 * A converter for USTC JW course table web page.
 */
class UstcJWConverter(html: String?,context: Context) : CourseTableWebConverter(html,context) {
    override fun parse() {
        val courses = parseCourses(html)
        //Log.d(TAG, "parse: ${html}")

        //建立Goal
        val goalDao = DBHelper(context).getGoalDao()

        var courseGoal = goalDao.getByContent("课程表")
        if (courseGoal==null) {
            val goal = Goal("课程表",null,null,"通过获取网络数据自动生成，不建议修改。若要添加新的课程，请创建新目标",0)
            goalDao.insert(goal)
        }

        courseGoal = goalDao.getByContent("课程表")

        val goal = courseGoal!!.id



        //转换并储存Action
        var actions = courses.map { convertCourseToAction(context,it,goal) }
        val actionDao = ActionDB.getDatabase(context).actionDao()

        actionDao.deleteByGoalId(goal)
        actionDao.insertAll(actions.flatten())


        val actions1 = actionDao.getByGoalId(goal)
        var str1 = ""
        for (action in actions1) {
            str1 += action.toString() + "\n"
        }

        Log.d(TAG, "parse: $str1")

        val dialog = AlertDialog.Builder(context)
            .setMessage("已获取数据: \n$str1")
            .setCancelable(true)
            .setPositiveButton("返回主页") { dialog, _ ->
                //结束WebActivity
                context.getActivity()?.finish()
            }
            .create()

        dialog.show()

    }

    //解析课程表
    private fun parseCourses(html: String): List<Course> {
        val courses = mutableListOf<Course>()
        val doc: Document = Jsoup.parse(html)
        val rows: Elements = doc.select("tbody tr.infoRow")
        Log.d(TAG, "parseCourses: ${rows.size}")
        for (row in rows) {
            val columns: Elements = row.select("td")
            val course = Course(
                index = columns[0].text().toInt(),
                classNumber = columns[1].select("span").text(),
                courseName = columns[2].text(),
                credits = columns[3].text().toDouble(),
                className = columns[4].text(),
                courseType = columns[5].text(),
                department = columns[6].text(),
                teachers = columns[7].select("a").joinToString(", ") { it.text() },
                schedule = columns[8].select("span").text(),
                scheduledHours = columns[9].text().toInt(),
                enrolledStudents = columns[10].text().toInt(),
                description = columns[11].select("button").text()
            )
            courses.add(course)
        }
        return courses
    }
}
//将课程转换为Action
fun convertCourseToAction(context: Context, course: Course,goal: Int): MutableList<Action> {

    var result = mutableListOf<Action>()

    val note : String = "${course.teachers}  ${course.courseType}  ${course.department}"

    val scheduleConverter = ScheduleConverter(course.schedule)

    Log.d(TAG, "convertCourseToAction: ${course.schedule}")
    val items = scheduleConverter.parse()
    items.map { Log.d(TAG, "convertCourseToAction: ${it.toString()}") }

    var location = ""

    if(items.isNotEmpty()){
        location = items[0].Room
        Log.d(TAG, "convertCourseToAction: $location")
    }

    val restrictions = mutableListOf<Restriction>()

    val courseItems =  ScheduleConverter(course.schedule).parse()

    for (courseItem in courseItems) {
        //添加时间限制
        restrictions.clear()
        restrictions.add(TimeConverter().convert(courseItem.Time))
        Log.d(TAG, "convertCourseToAction: ${restrictions[restrictions.size - 1].coding()}")
        for (i in 0..<courseItem.StartWeeks.size) {
            restrictions.addAll(WeekToDateConverter(context).convert(courseItem.StartWeeks[i], courseItem.EndWeeks[i], courseItem.EvenOrOddWeeks[i]))
            Log.d(TAG, "convertCourseToAction: ${restrictions[restrictions.size - 1].coding()}")
        }
        //restrictions去重
        val set = restrictions.map { it.coding() }.toSet()
        val list = set.toList()
        val newRestrictions = list.map { RestrictionFactory(it).create() }

        result.add(Action(
            0, goal,
            course.courseName,
            Duration.ofHours(course.scheduledHours.toLong()),
            location,
            note,
            false,
            "Fixed",
            false,
            newRestrictions
        ))
    }

    return result
}



/**
 * Convert week to date
 */
class WeekToDateConverter(var context: Context) {
    var startDate: LocalDate? = null

    init {
        if (getString(context, "startdate") == null) {
            startDate = LocalDate.of(2024, 9, 1)
            saveString(context, "startdate", startDate.toString())
        } else {
            startDate = LocalDate.parse(getString(context, "startdate"))
        }
    }

    fun retrieveStartDate(): LocalDate? {
        return startDate
    }

    fun updateStartDate(startDate: LocalDate) {
        this.startDate = startDate
        saveString(context, "startdate", startDate.toString())
    }

    fun convert(week: Int): TimeRestriction {

        val start = startDate!!.plusWeeks(week.toLong()-1)// Convert to 0-based index
        val end = start.plusDays(6)
        return TimeRestriction(LocalDateTime.of(start, LocalTime.of(0, 0, 1)), LocalDateTime.of(end, LocalTime.of(23, 59, 59)))
    }

    fun convert(startWeek: Int, endWeek: Int, evenOrOddWeek: Int): MutableList<TimeRestriction> {
        val restrictions = mutableListOf<TimeRestriction>()
        for (i in startWeek..endWeek) {
            if (evenOrOddWeek == 0) {
                restrictions.add(convert(i))
            } else if (evenOrOddWeek == 1 && i % 2 == 1) {
                restrictions.add(convert(i))
            } else if (evenOrOddWeek == 2 && i % 2 == 0) {
                restrictions.add(convert(i))
            }
        }
        return restrictions
    }

}

//处理类似 1(6,7) 的字符串，表示周一第6,7节课.示例：'2(3,4)', '4(3,4,5)', '5(1,2,3,4)'
class TimeConverter {
    val classTime = arrayOf(
        LocalTime.of(7,50),
        LocalTime.of(8,40),
        LocalTime.of(9,45),
        LocalTime.of(10,35),
        LocalTime.of(11,25),
        LocalTime.of(14,0),
        LocalTime.of(14,50),
        LocalTime.of(15,55),
        LocalTime.of(16,45),
        LocalTime.of(17,35),
        LocalTime.of(19,30),
        LocalTime.of(20,20),
        LocalTime.of(21,10)
    )

    fun convert(time: String): FixedTimeRestriction {
        val dayAndPeriods = time.split("(")
        val day = dayAndPeriods[0].toInt()// Convert to 0-based index
        val periods = dayAndPeriods[1].removeSuffix(")").split(",").map { it.toInt() }

        val start = classTime[periods[0] - 1]
        val end = classTime[periods[periods.size - 1]-1].plusMinutes(45)

        return FixedTimeRestriction(start, end,FixedTimeRestriction.FixedTimeRestrictionType.WEEKLY, listOf(day))
    }
}



fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is android.content.ContextWrapper -> baseContext.getActivity()
        else -> null
    }
}