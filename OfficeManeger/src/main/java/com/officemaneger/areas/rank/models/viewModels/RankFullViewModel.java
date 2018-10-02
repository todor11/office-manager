package com.officemaneger.areas.rank.models.viewModels;

public class RankFullViewModel {

    private Long id;

    private String name;

    private Integer rankRate;

    public RankFullViewModel() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
