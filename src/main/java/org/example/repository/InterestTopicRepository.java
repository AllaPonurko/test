package org.example.repository;

import org.example.entity.InterestTopic;
import org.example.enums.GenreTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InterestTopicRepository extends JpaRepository<InterestTopic,Long> {
    @Query("select topic From InterestTopic topic where topic.topic=:genre and topic.user.id=:userUuid")
    Optional<InterestTopic> findByTopic(GenreTypeEnum genre, UUID userUuid);
}
