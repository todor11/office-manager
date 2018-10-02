package com.officemaneger.areas.rank.services;

import com.officemaneger.areas.rank.entities.Rank;
import com.officemaneger.areas.rank.models.bindingModels.RankCreationModel;
import com.officemaneger.areas.rank.models.interfaces.RankNameView;
import com.officemaneger.areas.rank.models.viewModels.RankFullViewModel;
import com.officemaneger.areas.rank.models.viewModels.RankNameViewModel;
import com.officemaneger.areas.rank.repositories.RankRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RankServiceImpl implements RankService {

    private RankRepository rankRepository;

    private ModelMapper modelMapper;

    public RankServiceImpl() {
    }

    @Override
    public RankFullViewModel create(RankCreationModel rankCreationModel) {
        Rank newRank = this.modelMapper.map(rankCreationModel, Rank.class);
        Rank rank = this.rankRepository.save(newRank);
        return this.modelMapper.map(rank, RankFullViewModel.class);
    }

    @Override
    public List<RankFullViewModel> getAllRanks() {
        List<Rank> ranks = this.rankRepository.findAllByOrderByRankRate();
        List<RankFullViewModel> resultList = new ArrayList<>();
        for (Rank rank : ranks) {
            RankFullViewModel rankFullViewModel = this.modelMapper.map(rank, RankFullViewModel.class);
            resultList.add(rankFullViewModel);
        }

        return resultList;
    }

    @Override
    public List<RankNameViewModel> getAllRankNames() {
        List<Rank> ranks = this.rankRepository.findAllByOrderByRankRate();
        List<RankNameViewModel> resultList = new ArrayList<>();
        for (Rank rank : ranks) {
            RankNameViewModel rankNameViewModel = this.modelMapper.map(rank, RankNameViewModel.class);
            resultList.add(rankNameViewModel);
        }

        return resultList;
    }

    @Override
    public boolean isNameOccupied(String name) {
        Rank rank = this.rankRepository.findOneByName(name);
        return rank != null;
    }

    @Override
    public boolean isNameOccupied(String name, Long id) {
        Rank rank = this.rankRepository.findOneByName(name);
        if (rank != null && rank.getId() != id) {
            return true;
        }

        return false;
    }

    @Override
    public void update(Long id, RankCreationModel rankCreationModel) {
        Rank rank = this.rankRepository.findOne(id);
        rank.setName(rankCreationModel.getName());
        rank.setRankRate(rankCreationModel.getRankRate());
    }

    @Override
    public void delete(Long id) {
        this.rankRepository.delete(id);
    }

    @Override
    public List<RankNameViewModel> getAllRankNameViewModels() {
        List<RankNameView> rankNameViewList = this.rankRepository.getAllRankNameView();
        List<RankNameViewModel> viewModels = new ArrayList<>();
        for (RankNameView rankNameView : rankNameViewList) {
            viewModels.add(this.modelMapper.map(rankNameView, RankNameViewModel.class));
        }

        return viewModels;
    }

    @Autowired
    public void setRankRepository(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
