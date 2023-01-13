package com.practice.sns.repository;

import com.practice.sns.domain.Comment;
import com.practice.sns.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPost(Post post, Pageable pageable);
}