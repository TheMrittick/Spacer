package com.example.spacer_spacedrepitition;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText topicInput;
    private Button addTopicButton;
    private RecyclerView topicsRecyclerView;
    private TopicAdapter adapter;

    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        topicInput = findViewById(R.id.topicInput);
        addTopicButton = findViewById(R.id.addTopicButton);
        topicsRecyclerView = findViewById(R.id.topicsRecyclerView);

        // Setup RecyclerView
        adapter = new TopicAdapter(new TopicAdapter.OnTopicClickListener() {
            @Override
            public void onMarkReviewed(Topic topic) {
                MainActivity.this.onMarkReviewed(topic);
            }

            @Override
            public void onDelete(Topic topic) {
                MainActivity.this.onDelete(topic);
            }
        });

        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        topicsRecyclerView.setAdapter(adapter);

        // Load topics
        loadTopics();

        // Add topic button click
        addTopicButton.setOnClickListener(v -> addTopic());
    }

    private void addTopic() {
        String topicName = topicInput.getText().toString().trim();

        if (topicName.isEmpty()) {
            Toast.makeText(this, "Please enter a topic name", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            long currentTime = System.currentTimeMillis();
            long nextReview = currentTime + (24 * 60 * 60 * 1000); // 24 hours from now

            Topic topic = new Topic(topicName, currentTime, nextReview, 0);
            database.topicDao().insert(topic);

            runOnUiThread(() -> {
                topicInput.setText("");
                Toast.makeText(this, "Topic added!", Toast.LENGTH_SHORT).show();
                loadTopics();
            });
        });
    }

    private void loadTopics() {
        executorService.execute(() -> {
            List<Topic> topics = database.topicDao().getAllTopics();
            runOnUiThread(() -> adapter.setTopics(topics));
        });
    }

    private void onMarkReviewed(Topic topic) {
        executorService.execute(() -> {
            // Increase review count
            int newReviewCount = topic.getReviewCount() + 1;
            topic.setReviewCount(newReviewCount);

            // Calculate next review date based on spaced repetition
            long nextReview = calculateNextReviewDate(newReviewCount);
            topic.setNextReviewDate(nextReview);

            // Update in database
            database.topicDao().update(topic);

            runOnUiThread(() -> {
                Toast.makeText(this, "Marked as reviewed!", Toast.LENGTH_SHORT).show();
                loadTopics();
            });
        });
    }

    private void onDelete(Topic topic) {
        executorService.execute(() -> {
            database.topicDao().delete(topic.getId());

            runOnUiThread(() -> {
                Toast.makeText(this, "Topic deleted!", Toast.LENGTH_SHORT).show();
                loadTopics();
            });
        });
    }

    private long calculateNextReviewDate(int reviewCount) {
        long currentTime = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;

        // Spaced repetition intervals: 1 day, 3 days, 7 days, 14 days, 30 days
        switch (reviewCount) {
            case 1:
                return currentTime + (3 * dayInMillis); // 3 days
            case 2:
                return currentTime + (7 * dayInMillis); // 1 week
            case 3:
                return currentTime + (14 * dayInMillis); // 2 weeks
            default:
                return currentTime + (30 * dayInMillis); // 1 month
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}