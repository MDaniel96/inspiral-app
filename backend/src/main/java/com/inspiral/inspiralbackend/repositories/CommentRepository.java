package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByEntryid(Integer entryid);

    Comment getById(Integer id);

}
