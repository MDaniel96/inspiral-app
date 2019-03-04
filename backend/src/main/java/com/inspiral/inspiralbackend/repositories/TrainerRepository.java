package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    Trainer findTrainerById(Integer id);

    Trainer findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update Trainer t set t.name = ?1, t.email = ?2, t.password = ?3, t.image = ?4 where t.id = ?5")
    void updateTrainerById(String name, String email, String password, byte[] image, Integer id);

}

