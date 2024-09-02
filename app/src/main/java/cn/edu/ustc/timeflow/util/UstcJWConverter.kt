package cn.edu.ustc.timeflow.util
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Course
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.database.ActionDB
import cn.edu.ustc.timeflow.restriction.Restriction
import com.google.android.material.internal.ContextUtils.getActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.Duration

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
        if (courseGoal.isEmpty()) {
            val goal = Goal("课程表",null,null,"",0)
            goalDao.insert(goal)
        }

        courseGoal = goalDao.getByContent("课程表")
        val goal = courseGoal.get(0).id



        //转换并储存Action
        var actions = convertCoursesToActions(courses, goal)
        val actionDao = ActionDB.getDatabase(context).actionDao()

        actionDao.deleteByGoalId(goal)
        actionDao.insertAll(actions)


        actions = actionDao.getByGoalId(goal)
        var str1 = ""
        for (action in actions) {
            str1 += action.toString() + "\n"
        }

        Log.d(TAG, "parse: $str1")

//        val dialog = AlertDialog.Builder(context)
//            .setMessage("已获取数据: \n$str1")
//            .setCancelable(true)
//            .setPositiveButton("返回主页") { dialog, _ ->
//                //结束WebActivity
//                context.getActivity()?.finish()
//            }
//            .create()
//
//        dialog.show()

    }

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
fun convertCourseToAction(course: Course,goal: Int): Action {

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


    return Action(
        0, goal,// Set appropriate goal_id
        course.courseName,
        Duration.ofHours(course.scheduledHours.toLong()),
        location,
        note,
        false,
        "Fixed",
        false,
        emptyList() // TODO: Set appropriate restrictions if needed
    )
}


fun convertCoursesToActions(courses: List<Course>, goal: Int): List<Action> {
    return courses.map { convertCourseToAction(it,goal) }
}

fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is android.content.ContextWrapper -> baseContext.getActivity()
        else -> null
    }
}