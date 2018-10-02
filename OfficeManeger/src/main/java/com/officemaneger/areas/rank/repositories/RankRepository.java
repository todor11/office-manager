package com.officemaneger.areas.rank.repositories;

import com.officemaneger.areas.rank.entities.Rank;
import com.officemaneger.areas.rank.models.interfaces.RankNameView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

    Rank findOneByName(String name);

    List<Rank> findAllByOrderByRankRate();

    @Query("SELECT r.id AS id, r.name AS name  " +
            "FROM Rank AS r " +
            "ORDER BY r.rankRate DESC ")
    List<RankNameView> getAllRankNameView();
}
