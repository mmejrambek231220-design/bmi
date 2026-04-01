package com.example.bmi;

public class WorkoutItem {
    private String day;
    private String exercises;
    private String reps;
    private boolean isTip;

    public WorkoutItem(String day, String exercises, String reps, boolean isTip) {
        this.day = day;
        this.exercises = exercises;
        this.reps = reps;
        this.isTip = isTip;
    }

    public String getDay() { return day; }
    public String getExercises() { return exercises; }
    public String getReps() { return reps; }
    public boolean isTip() { return isTip; }
}
