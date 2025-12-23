package com.example.spacer_spacedrepitition; // Make sure this matches YOUR package name

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topics")
public class Topic {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String topicName;
    private long dateCreated;
    private long nextReviewDate;
    private int reviewCount;

    // Constructor
    public Topic(String topicName, long dateCreated, long nextReviewDate, int reviewCount) {
        this.topicName = topicName;
        this.dateCreated = dateCreated;
        this.nextReviewDate = nextReviewDate;
        this.reviewCount = reviewCount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(long nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}