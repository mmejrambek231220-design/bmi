package com.example.bmi;

public class BmiHistoryItem {
    private String date;
    private float bmi;
    private String category;
    private long timestamp;

    public BmiHistoryItem(String date, float bmi, String category, long timestamp) {
        this.date = date;
        this.bmi = bmi;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String getDate() { return date; }
    public float getBmi() { return bmi; }
    public String getCategory() { return category; }
    public long getTimestamp() { return timestamp; }
}
