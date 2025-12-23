package com.example.spacer_spacedrepitition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> listItems = new ArrayList<>();
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onMarkReviewed(Topic topic);
        void onDelete(Topic topic);
    }

    public TopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_section_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_topic, parent, false);
            return new TopicViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem item = listItems.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(item.getHeaderTitle());
        } else if (holder instanceof TopicViewHolder) {
            ((TopicViewHolder) holder).bind(item.getTopic());
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setTopics(List<Topic> topics) {
        listItems.clear();

        if (topics.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        // Group topics by date categories
        List<Topic> todayTopics = new ArrayList<>();
        List<Topic> tomorrowTopics = new ArrayList<>();
        List<Topic> thisWeekTopics = new ArrayList<>();
        List<Topic> laterTopics = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        long endOfToday = today.getTimeInMillis();

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);
        tomorrow.set(Calendar.SECOND, 59);
        long endOfTomorrow = tomorrow.getTimeInMillis();

        Calendar weekEnd = Calendar.getInstance();
        weekEnd.add(Calendar.DAY_OF_YEAR, 7);
        weekEnd.set(Calendar.HOUR_OF_DAY, 23);
        weekEnd.set(Calendar.MINUTE, 59);
        weekEnd.set(Calendar.SECOND, 59);
        long endOfWeek = weekEnd.getTimeInMillis();

        for (Topic topic : topics) {
            long reviewDate = topic.getNextReviewDate();

            if (reviewDate <= endOfToday) {
                todayTopics.add(topic);
            } else if (reviewDate <= endOfTomorrow) {
                tomorrowTopics.add(topic);
            } else if (reviewDate <= endOfWeek) {
                thisWeekTopics.add(topic);
            } else {
                laterTopics.add(topic);
            }
        }

        // Add sections with topics
        if (!todayTopics.isEmpty()) {
            listItems.add(new ListItem("Review Today"));
            for (Topic topic : todayTopics) {
                listItems.add(new ListItem(topic));
            }
        }

        if (!tomorrowTopics.isEmpty()) {
            listItems.add(new ListItem("Review Tomorrow"));
            for (Topic topic : tomorrowTopics) {
                listItems.add(new ListItem(topic));
            }
        }

        if (!thisWeekTopics.isEmpty()) {
            listItems.add(new ListItem("Review This Week"));
            for (Topic topic : thisWeekTopics) {
                listItems.add(new ListItem(topic));
            }
        }

        if (!laterTopics.isEmpty()) {
            listItems.add(new ListItem("Review Later"));
            for (Topic topic : laterTopics) {
                listItems.add(new ListItem(topic));
            }
        }

        notifyDataSetChanged();
    }

    // Header ViewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionHeaderText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionHeaderText = itemView.findViewById(R.id.sectionHeaderText);
        }

        public void bind(String title) {
            sectionHeaderText.setText(title);
        }
    }

    // Topic ViewHolder
    class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView topicNameText;
        private TextView nextReviewText;
        private TextView reviewCountText;
        private Button markReviewedButton;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicNameText = itemView.findViewById(R.id.topicNameText);
            nextReviewText = itemView.findViewById(R.id.nextReviewText);
            reviewCountText = itemView.findViewById(R.id.reviewCountText);
            markReviewedButton = itemView.findViewById(R.id.markReviewedButton);
        }

        public void bind(Topic topic) {
            topicNameText.setText(topic.getTopicName());

            // Format the next review date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
            String dateStr = sdf.format(new Date(topic.getNextReviewDate()));
            nextReviewText.setText("Next review: " + dateStr);

            reviewCountText.setText("Reviewed: " + topic.getReviewCount() + " times");

            markReviewedButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMarkReviewed(topic);
                }
            });

            //delete button
            Button deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(topic);
                }
            });
        }
    }
}