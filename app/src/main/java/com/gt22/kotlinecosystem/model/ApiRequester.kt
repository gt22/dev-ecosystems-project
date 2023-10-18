package com.gt22.kotlinecosystem.model

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.lang.IllegalStateException

object ApiRequester {

    val ktor = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = object: Logger {
                override fun log(message: String) {
                    Log.v("HTTP Client", message)
                }
            }
            level = LogLevel.BODY
        }
        defaultRequest {
            url("http://192.168.178.27:8080/")
        }
    }

    suspend fun getTaskCount(): Int {
        return ktor.get("api/tasks").body()
    }

    suspend fun getTask(id: Int): Task? {
        val r = ktor.get("api/tasks/$id")
        return when(r.status) {
            HttpStatusCode.OK -> r.body()
            HttpStatusCode.NotFound -> null
            else -> throw IllegalStateException("Unexpected status code ${r.status}")
        }
    }

    suspend fun deleteTask(id: Int): Boolean {
        return when(ktor.delete("api/tasks/$id").status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.NotFound -> false
            else -> throw IllegalStateException("Unexpected status code ${ktor.delete("api/tasks/$id").status}")
        }
    }

    suspend fun createTask(t: Task): Int {
        val r = ktor.post("api/tasks/new") {
            setBody(t)
            contentType(ContentType.Application.Json)
        }
        return when(r.status) {
            HttpStatusCode.Created -> r.bodyAsText().removePrefix("Task created with id ").toInt()
            else -> throw IllegalStateException("Unexpected status code ${r.status}")
        }
    }

    suspend fun updateTask(id: Int, t: Task): Boolean {
        val r = ktor.put("api/tasks/$id") {
            setBody(t)
            contentType(ContentType.Application.Json)
        }
        return when(r.status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.NotFound -> false
            else -> throw IllegalStateException("Unexpected status code ${r.status}")
        }
    }
}