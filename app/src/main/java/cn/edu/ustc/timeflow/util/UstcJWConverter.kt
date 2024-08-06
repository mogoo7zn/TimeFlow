package cn.edu.ustc.timeflow.util
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Course
import cn.edu.ustc.timeflow.database.ActionDB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.Duration

val TAG = "UstcJWConverter"
class UstcJWConverter(html: String?,context: Context) : CourseTableWebConverter(html,context) {
    override fun parse() {
        Log.d(TAG, "parse: $html")
        val courses = parseCourses(html)
        Log.d(TAG, "course: "+courses.size)


        //TODO: Save actions to database
        //val actionDB = ActionDB.getDatabase(context)

        var str: String = ""
        for (course in courses) {
            str += course.toString() + "\n"
        }
        Log.d(TAG, "parse: $str")

        val actions = convertCoursesToActions(courses)

        var str1 = ""
        for (action in actions) {
            str1 += action.name + "\n"
        }
        Log.d(TAG, "parse: $str1")

        //TODO: 测试用对话, 之后删除
        val dialog = AlertDialog.Builder(context)
            .setMessage("已获取数据: \n$str1")
            .setCancelable(true)
            .create()
        dialog.show()

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
fun convertCourseToAction(course: Course): Action {

    val note : String = "${course.teachers}  ${course.courseType}  ${course.department}"

    return Action(
        0, -1,// Set appropriate goal_id
        course.courseName,
        Duration.ofHours(course.scheduledHours.toLong()),
        "", // Extract location from schedule if needed
        note,
        false,
        "Fixed",
        false,
        emptyList() // Set appropriate restrictions if needed
    )
}

fun convertCoursesToActions(courses: List<Course>): List<Action> {
    return courses.map { convertCourseToAction(it) }
}