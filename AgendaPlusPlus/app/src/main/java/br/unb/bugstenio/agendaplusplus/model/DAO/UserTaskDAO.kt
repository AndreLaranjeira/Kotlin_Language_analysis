package br.unb.bugstenio.agendaplusplus.model.DAO

import br.unb.bugstenio.agendaplusplus.model.Object.*
import br.unb.bugstenio.agendaplusplus.model.Util.*
import org.json.JSONArray
import org.json.JSONObject

class UserTaskDAO: NetworkHandler() {
    val classPath = "/user/task"

    fun createUserTask(newTask: Task){
        val path = "/"
        val params = JSONObject()

        params.put("id_task", newTask.id)
        params.put("task_title", newTask.title)
        params.put("task_description", newTask.description)
        params.put("task_limit", newTask.limitDate)
        params.put("task_done", newTask.taskDone)
        params.put("fk_user", newTask.externalId)

        apiController.post(classPath+path, params){response ->
        }
    }

    fun getUserTask(id: Long, completionHandler: (JSONObject?) -> Unit) {
        val path = "/$id"

        apiController.get(classPath+path, completionHandler)
    }

    fun getAllUserTasks(completionHandler: (JSONArray?) -> Unit) {
        val path = "/all"

        apiController.getMany(classPath+path, completionHandler)
    }

    fun updateUserTask(task: Task){
        val path = "/"
        val params = JSONObject()

        params.put("id_task", task.id)
        params.put("task_title", task.title)
        params.put("task_description", task.description)
        params.put("task_limit", task.limitDate)
        params.put("task_done", task.taskDone)
        params.put("fk_user", task.externalId)

        apiController.update(classPath+path, params){response ->
        }
    }

    fun deleteUserTask(task: Task){
        val path = "/%d".format(task.id)
        apiController.delete(classPath+path){response ->
        }
    }
}