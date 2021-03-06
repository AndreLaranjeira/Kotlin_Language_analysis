package br.unb.bugstenio.agendaplusplus

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import br.unb.bugstenio.agendaplusplus.model.DAO.ProjectTaskDAO
import br.unb.bugstenio.agendaplusplus.model.DAO.UserTaskDAO
import br.unb.bugstenio.agendaplusplus.model.Object.Project
import br.unb.bugstenio.agendaplusplus.model.Object.Task
import br.unb.bugstenio.agendaplusplus.model.Object.parseProjectTask
import br.unb.bugstenio.agendaplusplus.model.Object.parseUserTask
import kotlinx.android.synthetic.main.activity_task_create_edit.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class TaskCreateEditActivity : AppCompatActivity() {

    var taskId: Long? = null
    var task: Task? = null
    var projectId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create_edit)

        this.intent.let {
            taskId = it.getLongExtra(ARG1, 0)
            val isFromProject = it.getBooleanExtra(ARG2, false)
            if(isFromProject){
                projectId = it.getLongExtra(ARG3, 0)
            }
        }

        if(taskId != 0.toLong()){
            if(projectId == null){
                UserTaskDAO().getUserTask(taskId!!) {
                    task = it?.parseUserTask()
                    if(task != null){
                        task_create_title.setText(task!!.title, TextView.BufferType.EDITABLE)
                        task_create_description.setText(task!!.description, TextView.BufferType.EDITABLE)
                        val limit = task!!.limitDate
                        task_create_limit_date.updateDate(limit.year, limit.monthOfYear, limit.dayOfMonth)
                        task_create_limit_time.hour = limit.hourOfDay
                        task_create_limit_time.minute = limit.minuteOfHour
                        val done = task!!.taskDone
                        if (done != null) {
                            task_create_done.toggle()
                            task_create_done_date.updateDate(done.year, done.monthOfYear, done.dayOfMonth)
                            task_create_done_time.hour = done.hourOfDay
                            task_create_done_time.minute = done.minuteOfHour
                        }
                    }
                }
            } else {
                ProjectTaskDAO().getProjectTask(taskId!!) {
                    task = it?.parseProjectTask()
                    if(task != null){
                        task_create_title.setText(task!!.title, TextView.BufferType.EDITABLE)
                        task_create_description.setText(task!!.description, TextView.BufferType.EDITABLE)
                        val limit = task!!.limitDate
                        task_create_limit_date.updateDate(limit.year, limit.monthOfYear, limit.dayOfMonth)
                        task_create_limit_time.hour = limit.hourOfDay
                        task_create_limit_time.minute = limit.minuteOfHour
                        val done = task!!.taskDone
                        if (done != null) {
                            task_create_done.toggle()
                            task_create_done_date.updateDate(done.year, done.monthOfYear, done.dayOfMonth)
                            task_create_done_time.hour = done.hourOfDay
                            task_create_done_time.minute = done.minuteOfHour
                        }
                    }
                }
            }
        }

        val now = DateTime.now(DateTimeZone.getDefault())
        task_create_limit_date.updateDate(now.year, now.monthOfYear, now.dayOfMonth)
        task_create_limit_time.hour = now.hourOfDay
        task_create_limit_time.minute = now.minuteOfHour
        task_create_done_date.updateDate(now.year, now.monthOfYear, now.dayOfMonth)
        task_create_done_time.hour = now.hourOfDay
        task_create_done_time.minute = now.minuteOfHour

        task_create_done.setOnCheckedChangeListener { _, _ ->
            if(task_create_done.isChecked){
                task_create_done_layout.visibility = View.VISIBLE
            } else {
                task_create_done_layout.visibility = View.GONE
            }
        }

        task_create_confirm_button.setOnClickListener {
            val title = task_create_title.text.toString()
            val description = task_create_description.text.toString()
            val limitDate = DateTime(
                    task_create_limit_date.year,
                    task_create_limit_date.month,
                    task_create_limit_date.dayOfMonth,
                    task_create_limit_time.hour,
                    task_create_limit_time.minute
            )
            var doneDate: DateTime? = null
            if(task_create_done.isChecked){
                doneDate = DateTime(
                        task_create_done_date.year,
                        task_create_done_date.month,
                        task_create_done_date.dayOfMonth,
                        task_create_done_time.hour,
                        task_create_done_time.minute
                )
            }
            var externalId: Long? = projectId
            if(externalId == null){
                externalId = Session.user?.id
            }
            val newTask = Task(
                    taskId!!,
                    title,
                    description,
                    limitDate,
                    doneDate,
                    externalId
            )

            if(projectId == null){
                val dao = UserTaskDAO()
                if(taskId == 0.toLong()) {
                    dao.createUserTask(newTask)
                } else {
                    dao.updateUserTask(newTask)
                }
            } else {
                val dao = ProjectTaskDAO()
                if(taskId == 0.toLong()) {
                    dao.createProjectTask(newTask)
                } else {
                    dao.updateProjectTask(newTask)
                }
            }
            this.finish()
        }
    }

    companion object {

        val ARG1 = "task_id"
        val ARG2 = "is_from_project"
        val ARG3 = "project_id"

        fun createTask(context: Context, isFromProject: Boolean, project: Project?) {
            val intent = Intent(context, TaskCreateEditActivity::class.java).apply {
                putExtra(ARG1, 0)
                putExtra(ARG2, isFromProject)
                putExtra(ARG3, project?.id)
            }
            context.startActivity(intent)
        }

        fun editTask(context: Context, taskId: Long, isFromProject: Boolean, project: Project?) {
            val intent = Intent(context, TaskCreateEditActivity::class.java).apply {
                putExtra(ARG1, taskId)
                putExtra(ARG2, isFromProject)
                putExtra(ARG3, project?.id)
            }
            context.startActivity(intent)
        }
    }
}
