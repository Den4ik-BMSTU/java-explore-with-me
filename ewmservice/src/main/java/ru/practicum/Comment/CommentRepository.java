package ru.practicum.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentById(Long id);

    @Query(value = "SELECT C.* " +
            "FROM COMMENTS AS C " +
            "WHERE C.USER_ID = ?1 ", nativeQuery = true)
    List<Comment> findCommentsByUserId(long userId);

    @Query(value = "SELECT C.* " +
            "FROM COMMENTS AS C " +
            "WHERE C.EVENT_ID = ?1 ", nativeQuery = true)
    List<Comment> findCommentsByEventId(long eventId);
}