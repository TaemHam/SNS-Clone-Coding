package com.practice.sns.repository;

import com.practice.sns.domain.Comment;
import com.practice.sns.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPost(Post post, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Comment entity SET deleted_at = NOW() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}
