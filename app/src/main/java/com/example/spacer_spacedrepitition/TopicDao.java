package com.example.spacer_spacedrepitition;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TopicDao {

    @Insert
    void insert(Topic topic);

    @Update
    void update(Topic topic);

    @Query("DELETE FROM topics WHERE id = :topicId")
    void delete(int topicId);

    @Query("SELECT * FROM topics ORDER BY nextReviewDate ASC")
    List<Topic> getAllTopics();

    @Query("SELECT * FROM topics WHERE nextReviewDate <= :currentTime ORDER BY nextReviewDate ASC")
    List<Topic> getTopicsDueForReview(long currentTime);

    @Query("SELECT * FROM topics WHERE id = :topicId")
    Topic getTopicById(int topicId);

}
