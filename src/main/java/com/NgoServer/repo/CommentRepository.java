package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NgoServer.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
