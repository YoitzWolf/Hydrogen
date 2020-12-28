package com.example.hydro.request.models;

public class Task {
    public Connection connection;
    public String taskHtml;

    public Task(Connection connection, String taskHtml){
        this.taskHtml = taskHtml;
        this.connection = connection;
    }
}
