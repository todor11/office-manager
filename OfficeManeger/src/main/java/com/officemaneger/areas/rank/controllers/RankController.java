package com.officemaneger.areas.rank.controllers;

import com.officemaneger.areas.rank.models.bindingModels.RankCreationModel;
import com.officemaneger.areas.rank.models.viewModels.RankFullViewModel;
import com.officemaneger.areas.rank.services.RankService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RankController {

    private RankService rankService;

    public RankController() {
    }

    @GetMapping("/ranks")
    public String getRanksPage(Model model){
        List<RankFullViewModel> ranks = this.rankService.getAllRanks();
        model.addAttribute("ranks", ranks);
        model.addAttribute("title", "Звания");
        model.addAttribute("view", "rank/ranks");

        return "base-layout";
    }

    @PostMapping("/ranks/add")
    public ResponseEntity<RankFullViewModel> saveRank(@Valid @RequestBody RankCreationModel rankCreationModel){
        boolean isNameOccupied = this.rankService.isNameOccupied(rankCreationModel.getName());
        if (isNameOccupied) {
            FieldError fieldError = new FieldError("name", "name", "error.rank.nameOccupied");
            throw new CustomFieldError("name", fieldError);
        }

        RankFullViewModel rankFullViewModel = this.rankService.create(rankCreationModel);
        return new ResponseEntity(rankFullViewModel, HttpStatus.OK);
    }

    @PutMapping("/ranks/{rankId}")
    public ResponseEntity update(@PathVariable long rankId, @Valid @RequestBody RankCreationModel rankCreationModel){
        boolean isNameOccupied = this.rankService.isNameOccupied(rankCreationModel.getName(), rankId);
        if (isNameOccupied) {
            FieldError fieldError = new FieldError("name", "name", "error.rank.nameOccupied");
            throw new CustomFieldError("name", fieldError);
        }

        this.rankService.update(rankId, rankCreationModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/ranks/delete/{rankId}")
    public ResponseEntity delete(@PathVariable long rankId){
        this.rankService.delete(rankId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setRankService(RankService rankService) {
        this.rankService = rankService;
    }
}

