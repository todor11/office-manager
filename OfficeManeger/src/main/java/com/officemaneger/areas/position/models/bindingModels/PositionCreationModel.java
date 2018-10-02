package com.officemaneger.areas.position.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

public class PositionCreationModel {

    @NotEmpty(message = "Името на длъжността е задължително")
    private String name;

    public PositionCreationModel() {
    }

    public PositionCreationModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
