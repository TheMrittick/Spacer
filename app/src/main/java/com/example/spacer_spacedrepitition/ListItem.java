package com.example.spacer_spacedrepitition;

public class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TOPIC = 1;

    private int type;
    private String headerTitle;
    private Topic topic;

    // Constructor for header
    public ListItem(String headerTitle) {
        this.type = TYPE_HEADER;
        this.headerTitle = headerTitle;
    }

    // Constructor for topic
    public ListItem(Topic topic) {
        this.type = TYPE_TOPIC;
        this.topic = topic;
    }

    public int getType() {
        return type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public Topic getTopic() {
        return topic;
    }
}