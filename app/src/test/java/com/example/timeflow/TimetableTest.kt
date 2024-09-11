import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.util.TimeTable
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class TimeTableTest {

    @Test
    fun getAvailableTime_noTasks_returnsFullTimeRange() {
        val start = LocalDateTime.of(2024, 9, 1, 0, 0)
        val end = LocalDateTime.of(2024, 9, 1, 23, 59, 59)
        val timeTable = TimeTable(null, start, end)
        timeTable.tasks = mutableListOf()

        val availableTime = timeTable.getAvailableTime()

        assertEquals(1, availableTime.size)
        assertEquals(Pair(start, end), availableTime[0])
    }

    @Test
    fun getAvailableTime_withNonOverlappingTasks_returnsCorrectAvailableTime() {
        val start = LocalDateTime.of(2024, 9, 1, 0, 0)
        val end = LocalDateTime.of(2024, 9, 1, 23, 59, 59)
        val timeTable = TimeTable(null, start, end)
        timeTable.tasks = mutableListOf(
            Task(start.plusHours(1), start.plusHours(2), "Task 1", 1, "Good", false, 1),
            Task(start.plusHours(3), start.plusHours(4), "Task 2", 1, "Good", false, 2)
        )

        val availableTime = timeTable.getAvailableTime()

        assertEquals(3, availableTime.size)
        assertEquals(Pair(start, start.plusHours(1)), availableTime[0])
        assertEquals(Pair(start.plusHours(2), start.plusHours(3)), availableTime[1])
        assertEquals(Pair(start.plusHours(4), end), availableTime[2])
    }

    @Test
    fun getAvailableTime_withOverlappingTasks_returnsCorrectAvailableTime() {
        val start = LocalDateTime.of(2024, 9, 1, 0, 0)
        val end = LocalDateTime.of(2024, 9, 1, 23, 59, 59)
        val timeTable = TimeTable(null, start, end)
        timeTable.tasks = mutableListOf(
            Task(start.plusHours(1), start.plusHours(3), "Task 1", 1, "Good", false, 1),
            Task(start.plusHours(2), start.plusHours(4), "Task 2", 1, "Good", false, 2)
        )

        val availableTime = timeTable.getAvailableTime()

        assertEquals(2, availableTime.size)
        assertEquals(Pair(start, start.plusHours(1)), availableTime[0])
        assertEquals(Pair(start.plusHours(4), end), availableTime[1])
    }

    @Test
    fun getAvailableTime_withTasksCoveringFullRange_returnsNoAvailableTime() {
        val start = LocalDateTime.of(2024, 9, 1, 0, 0)
        val end = LocalDateTime.of(2024, 9, 1, 23, 59, 59)
        val timeTable = TimeTable(null, start, end)
        timeTable.tasks = mutableListOf(
            Task(start, end, "Task 1", 1, "Good", false, 1)
        )

        val availableTime = timeTable.getAvailableTime()

        assertTrue(availableTime.isEmpty())
    }

    @Test
    fun getAvailableTime_withContainedTasks_returnsCorrectAvailableTime() {
        val start = LocalDateTime.of(2024, 9, 1, 0, 0)
        val end = LocalDateTime.of(2024, 9, 1, 23, 59, 59)
        val timeTable = TimeTable(null, start, end)
        timeTable.tasks = mutableListOf(
            Task(start.plusHours(1), start.plusHours(5), "Task 1", 1, "Good", false, 1),
            Task(start.plusHours(2), start.plusHours(3), "Task 2", 1, "Good", false, 2)
        )

        val availableTime = timeTable.getAvailableTime()

        assertEquals(2, availableTime.size)
        assertEquals(Pair(start, start.plusHours(1)), availableTime[0])
        assertEquals(Pair(start.plusHours(5), end), availableTime[1])
    }

}