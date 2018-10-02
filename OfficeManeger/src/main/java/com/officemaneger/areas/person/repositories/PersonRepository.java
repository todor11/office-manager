package com.officemaneger.areas.person.repositories;

import com.officemaneger.areas.person.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p.EGN FROM Person AS p " +
            "WHERE p.EGN = :egn ")
    String getEGNByEGN(@Param("egn") String egn);

    @Query("SELECT p.id FROM Person AS p " +
            "WHERE p.EGN = :egn ")
    Long getIdByEGN(@Param("egn") String egn);
}
