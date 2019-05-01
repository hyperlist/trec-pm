package de.julielab.ir.model;

import at.medunigraz.imi.bst.trec.model.Challenge;
import at.medunigraz.imi.bst.trec.model.Task;

public class Query {
    protected Challenge challenge;
    protected Task task;
    protected int year;
    protected int number;

    public Query() {
    }

    public Query(Challenge challenge, Task task, int year, int number) {

        this.challenge = challenge;
        this.task = task;
        this.year = year;
        this.number = number;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
