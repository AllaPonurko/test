package org.example.service.service;

import jakarta.transaction.Transactional;
import org.example.entity.InterestTopic;
import org.example.entity.dialer.DialerGenre;
import org.example.entity.user.User;
import org.example.enums.GenreTypeEnum;
import org.example.repository.DialerGenreRepository;
import org.example.repository.InterestTopicRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class InterestTopicService {
    @Autowired
    private final InterestTopicRepository interestTopicRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final DialerGenreRepository dialerGenreRepository;

    public InterestTopicService(InterestTopicRepository interestTopicRepository, UserRepository userRepository, DialerGenreRepository dialerGenreRepository) {
        this.interestTopicRepository = interestTopicRepository;
        this.userRepository = userRepository;
        this.dialerGenreRepository = dialerGenreRepository;
    }

    @Transactional
    public boolean addInterestTopicToUser(UUID userUuid, long[] topicIds) {
        User user = userRepository.findById(userUuid).orElseThrow();
        if (topicIds.length > 0) {
            List<InterestTopic> interestTopicList = user.getListOfInterested() != null ? user.getListOfInterested() : new ArrayList<>();
            List<InterestTopic> newInterestTopicList = new ArrayList<>();
            Arrays.stream(topicIds).forEach(topicId -> {
                GenreTypeEnum genreType = dialerGenreRepository.findById(topicId).orElseThrow().getEnumValue();
                boolean alreadyInterested = isInterestTopicAlreadyExist(interestTopicList, genreType);
                if (!alreadyInterested) {
                    InterestTopic interestTopic = createNewTopic(topicId, user);
                    interestTopicList.add(interestTopic);
                    newInterestTopicList.add(interestTopic);
                }
            });
            if (!newInterestTopicList.isEmpty()) {
                user.setListOfInterested(interestTopicList);
                interestTopicRepository.saveAll(newInterestTopicList);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private InterestTopic createNewTopic(long topicId, User user) {
        InterestTopic interestTopic = new InterestTopic();
        interestTopic.setUser(user);
        DialerGenre dialerGenre=dialerGenreRepository.findById(topicId).orElseThrow();
        interestTopic.setDialerGenre(dialerGenre);
        interestTopic.setTopic(dialerGenre.getEnumValue());
        return interestTopic;
    }

    @Transactional
    public boolean removeInterestTopicFromUser(UUID userUuid, long[] topicIds) {
        User user = userRepository.findById(userUuid).orElseThrow();
        if (topicIds.length > 0) {
            List<InterestTopic> interestTopicList = user.getListOfInterested() != null ? user.getListOfInterested() : new ArrayList<>();
            List<InterestTopic> oldTopicList = interestTopicList;
            Arrays.stream(topicIds).forEach(topicId -> {
                GenreTypeEnum genreType = dialerGenreRepository.findById(topicId).orElseThrow().getEnumValue();
                boolean alreadyInterested = isInterestTopicAlreadyExist(interestTopicList, genreType);
                if (alreadyInterested) {
                    InterestTopic interestTopic = interestTopicRepository.findByTopic(genreType, userUuid).orElseThrow();
                    interestTopicList.remove(interestTopic);
                }
            });
            if (oldTopicList.size()>interestTopicList.size()) {
                user.setListOfInterested(interestTopicList);
                interestTopicRepository.saveAll(interestTopicList);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private boolean isInterestTopicAlreadyExist(List<InterestTopic> interestTopicList, GenreTypeEnum genreType) {
        return interestTopicList.stream()
                .anyMatch(interestTopic -> interestTopic.getTopic().equals(genreType));
    }

    private GenreTypeEnum getGenreTypeEnum(long topicId) {
        switch ((int) topicId) {
            case 1:
                return GenreTypeEnum.SCIENCE_FICTION_GENRE;
            case 2:
                return GenreTypeEnum.ADVENTURES;
            case 3:
                return GenreTypeEnum.DRAMA;
            case 4:
                return GenreTypeEnum.DETECTIVE;
            case 5:
                return GenreTypeEnum.NOVEL;
            case 6:
                return GenreTypeEnum.POETRY;
            case 7:
                return GenreTypeEnum.SHORT_STORY;
            default:
                return null;
        }
    }


}
