package com.officemaneger.areas.computers.repositories;

import com.officemaneger.areas.computers.entities.Computer;
import com.officemaneger.areas.computers.models.interfaces.ComputerEditView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {


    @Query("SELECT c.id FROM Computer AS c " +
            "WHERE c.ip = :ip ")
    Long getComputerIdByIP(@Param("ip") String ip);

    @Query("SELECT c.businessUnit.id FROM Computer AS c " +
            "WHERE c.ip = :ip ")
    Long getBusinessUnitIdByIP(@Param("ip") String ip);

    @Query("SELECT c.businessUnit.id FROM Computer AS c " +
            "WHERE c.id = :computerId ")
    Long getBusinessUnitIdByComputerId(@Param("computerId") Long computerId);

    @Query(value = "SELECT c.id AS id, " +
            "c.ip AS ip, " +
            "c.name AS name, " +
            "bu.id AS businessUnitId " +
            "FROM Computer AS c " +
            "JOIN c.businessUnit AS bu ")
    List<ComputerEditView> getAllComputersView();
}
