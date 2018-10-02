package com.officemaneger.areas.position.repositories;

import com.officemaneger.areas.position.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {


    Position findOneByName(String name);
}
