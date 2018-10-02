package com.officemaneger.areas.dualNameTypes.repositories;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DualNameTypeRepository  extends JpaRepository<DualNameType, Long> {

    @Query(value = "SELECT d.shortName " +
            "FROM DualNameType AS d " +
            "WHERE d.id = :dualNameTypeId ")
    String getShortNameType(@Param("dualNameTypeId") Long dualNameTypeId);
}
