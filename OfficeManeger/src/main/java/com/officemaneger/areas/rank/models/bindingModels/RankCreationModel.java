package com.officemaneger.areas.rank.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class RankCreationModel {

    @NotEmpty(message = "error.rank.nameEmpty")
    private String name;

    @NotNull(message = "error.rank.rankRate")
    private Integer rankRate;

    public RankCreationModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRankRate() {
        return rankRate;
    }

    public void setRankRate(Integer rankRate) {
        this.rankRate = rankRate;
    }
}
