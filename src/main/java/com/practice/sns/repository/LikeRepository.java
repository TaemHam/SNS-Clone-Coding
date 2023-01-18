package com.practice.sns.repository;

import com.practice.sns.domain.Like;
import com.practice.sns.domain.Post;
import com.practice.sns.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    long countAllByPost(Post post);

    @Transactional
    @Modifying
    @Query("UPDATE Like entity SET deleted_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}
