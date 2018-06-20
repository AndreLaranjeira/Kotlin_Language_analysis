package web

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import model.NewUserGroup
import service.UserGroupService

fun Route.usergroup(userGroupService: UserGroupService) {

    route("/usergroup") {

        get("/") {
            call.respond(userGroupService.getAllUserGroups())
        }

        get("/{id_group}/{id_user}") {
            val usergroup = userGroupService.getUserGroup(call.parameters["id_group"]?.toInt()!!,
                                                          call.parameters["id_user"]?.toInt()!!)
            if (usergroup == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(usergroup)
        }

        post("/") {
            val usergroup = call.receive<NewUserGroup>()
            if(userGroupService.addUserGroup(usergroup) == null) call.respond(HttpStatusCode.Conflict)
            else call.respond(userGroupService.addUserGroup(usergroup)!!)
        }

        put("/") {
            val usergroup = call.receive<NewUserGroup>()
            call.respond(userGroupService.updateUserGroup(usergroup))
        }

        delete("/{id_group}/{id_user}") {
            val removed = userGroupService.deleteUserGroup(call.parameters["id_group"]?.toInt()!!,
                                                           call.parameters["id_user"]?.toInt()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

    }
}