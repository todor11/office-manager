package com.officemaneger.areas.annualLeave.models.viewModels;

public class AnnualLeaveEmployeeViewModel {

    private Long id;

    private int numberOfAnnualLeave;

    public AnnualLeaveEmployeeViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfAnnualLeave() {
        return this.numberOfAnnualLeave;
    }

    public void setNumberOfAnnualLeave(int numberOfAnnualLeave) {
        this.numberOfAnnualLeave = numberOfAnnualLeave;
    }
}
