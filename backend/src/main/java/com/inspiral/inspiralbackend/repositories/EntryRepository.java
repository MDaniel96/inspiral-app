package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

    List<Entry> findAll();

    List<Entry> findAllByTrainerid(Integer trainerid);

    Entry getById(Integer id);
    void deleteById(Integer id);
}
