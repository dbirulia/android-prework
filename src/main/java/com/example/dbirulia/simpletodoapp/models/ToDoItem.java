package com.example.dbirulia.simpletodoapp.models;

public class ToDoItem {

    public Long id;
    public String name;
    public String details;
    public String priority;
    public Integer dueDate;

    public ToDoItem(Long id, String name, String details, String priority, Integer duedate)
    {
        this.id = id;
        this.name = name;
        this.details = details;
        this.priority = priority;
        this.dueDate = duedate;
    }
}
