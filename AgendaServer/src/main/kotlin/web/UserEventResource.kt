package web

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import model.NewUserEvent
import service.UserEventService

fun Route.userevent(userEventService: UserEventService) {

    route("/userevent") {

        get("/") {
            call.respond(userEventService.getAllUserEvents())
        }

        get("/{id}") {
            val userEvent = userEventService.getUserEvent(call.parameters["id"]?.toInt()!!)
            if (userEvent == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(userEvent)
        }

        post("/") {
            val userEvent = call.receive<NewUserEvent>()
            call.respond(userEventService.addUserEvent(userEvent))
        }

        put("/") {
            val userEvent = call.receive<NewUserEvent>()
            call.respond(userEventService.updateUserEvent(userEvent))
        }

        delete("/{id}") {
            val removed = userEventService.deleteUserEvent(call.parameters["id"]?.toInt()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

    }
}