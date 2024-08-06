package cn.edu.ustc.timeflow.bean

data class Course(
    val index: Int,
    val courseName: String,
    val classNumber: String,
    val credits: Double,
    val className: String,
    val courseType: String,
    val department: String,
    val teachers: String,
    val schedule: String,
    val scheduledHours: Int,
    val enrolledStudents: Int,
    val description: String){

    override fun toString(): String {
        return "Course(index=$index, courseName='$courseName', classNumber='$classNumber', credits=$credits, className='$className', courseType='$courseType', department='$department', teachers='$teachers', schedule='$schedule', scheduledHours=$scheduledHours, enrolledStudents=$enrolledStudents, description='$description')"
    }
}

