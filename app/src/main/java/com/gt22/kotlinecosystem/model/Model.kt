package com.gt22.kotlinecosystem.model

object Model {

    suspend fun fetchTasks(): List<TaskRef> = getTasks()

    private suspend fun getTask(id: Int): TaskRef? {
        return TaskRef(id, ApiRequester.getTask(id) ?: return null)
    }

    private suspend fun getTasks(): List<TaskRef> {
        val count = ApiRequester.getTaskCount()
        var curId = 0
        return buildList {
            while(size < count) {
                add(getTask(curId++) ?: continue)
            }
        }
    }

    suspend fun deleteTask(id: Int) = ApiRequester.deleteTask(id)

    suspend fun createTask(task: Task) = ApiRequester.createTask(task)

    suspend fun updateTask(task: TaskRef) = ApiRequester.updateTask(task.id, task.task)
}