package com.officemaneger.areas.rank.services;

import com.officemaneger.areas.rank.models.bindingModels.RankCreationModel;
import com.officemaneger.areas.rank.models.viewModels.RankFullViewModel;
import com.officemaneger.areas.rank.models.viewModels.RankNameViewModel;

import java.util.List;

public interface RankService {

    RankFullViewModel create(RankCreationModel rankCreationModel);

    List<RankFullViewModel> getAllRanks();

    List<RankNameViewModel> getAllRankNames();

    boolean isNameOccupied(String name);

    boolean isNameOccupied(String name, Long id);

    void update(Long id, RankCreationModel rankCreationModel);

    void delete(Long id);

    List<RankNameViewModel> getAllRankNameViewModels();
}
