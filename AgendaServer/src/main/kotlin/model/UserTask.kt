package model

import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

object TB_User_task : Table() {
    val id_task = integer("id_task").primaryKey().autoIncrement()
    val task_title = varchar("task_title", 50)
    val task_description = text("task_description")
    val task_limit = datetime("task_limit").nullable()
    val task_done = datetime("task_done").nullable()
    val fk_user = integer("fk_user").references(TB_User.id_user)
}

data class UserTask (
        val id_task : Int,
        val task_title : String,
        val task_description : String,
        val task_limit : DateTime?,
        val task_done : DateTime?,
        val fk_user : Int
)

data class NewUserTask(
        val id_task : Int?,
        val task_title : String,
        val task_description : String,
        val task_limit : DateTime?,
        val task_done : DateTime?,
        val fk_user : Int
)