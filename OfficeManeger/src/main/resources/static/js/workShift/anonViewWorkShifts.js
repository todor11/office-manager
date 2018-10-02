var DAYS_LEFT = 7;
var DAYS_RIGHT = 8;
var DAYS_SMALL_BUTTON = 5;
var DAYS_BIG_BUTTON = 30;
var currentStartDate;
var currentEndDate;
var employeesAnnualLeavesMapById;
var employeeShiftMapById;
var employeePossibleInputValues;
var annualLeaveTypes;
var notRegularShiftTypesMapObj;

var newDates;
var newWorkShifts;

function init() {
    setEmployeesAnnualLeavesAndShiftsMaps();

    var today = new Date();
    currentStartDate = DateCalculator.getDateMinusDays(today, DAYS_LEFT);
    currentEndDate = DateCalculator.getDatePlusDays(today, DAYS_RIGHT);

    //set date buttons
    $('#leftBigButton').click(leftBigButtonPushed);
    $('#leftSmallButton').click(leftSmallButtonPushed);
    $('#rightBigButton').click(rightBigButtonPushed);
    $('#rightSmallButton').click(rightSmallButtonPushed);

    // update year in main label
    var year = dates[7].date.split('.')[2];
    var newText = 'Разход ' + year;
    $('#mainYearLabel').text(newText);
}

function setEmployeesAnnualLeavesAndShiftsMaps() {
    employeesAnnualLeavesMapById = {};
    employeeShiftMapById = {};
    employeePossibleInputValues = {};
    annualLeaveTypes = {};
    notRegularShiftTypesMapObj = {};

    for (var i = 0; i < workShifts.length; i++) {
        var workShift = workShifts[i];
        var employeeId = workShift.employee.id;
        var employeeProps = {};
        employeeProps.employeeNames = workShift.employee.fullName;
        employeeProps.annualLeaves = {};
        for (var j = 0; j < workShift.employee.annualLeaves.length; j++) {
            var annualLeave = workShift.employee.annualLeaves[j];
            var currentAnnualLeave = {};
            currentAnnualLeave.id = annualLeave.id;
            currentAnnualLeave.annualLeaveType = annualLeave.annualLeaveType;
            currentAnnualLeave.numberOfAnnualLeave = annualLeave.numberOfAnnualLeave;
            currentAnnualLeave.annualLeaveTypeId = annualLeave.annualLeaveTypeId;
            employeeProps.annualLeaves[annualLeave.annualLeaveTypeShort] = currentAnnualLeave;
        }
        employeesAnnualLeavesMapById[employeeId] = employeeProps;

        // set employeeShiftMapById
        for (var j = 0; j < workShift.employeeShifts.length; j++) {
            var employeeShift = workShift.employeeShifts[j];
            var currentEmployeeShift = {};
            currentEmployeeShift.date = employeeShift.date;
            currentEmployeeShift.shortDate = employeeShift.shortDate;
            currentEmployeeShift.startTime = employeeShift.startTime;
            currentEmployeeShift.endTime = employeeShift.endTime;
            currentEmployeeShift.secondStartTime = employeeShift.secondStartTime;
            currentEmployeeShift.secondEndTime = employeeShift.secondEndTime;
            currentEmployeeShift.isEndOnNextDay = employeeShift.isEndOnNextDay;

            employeeShiftMapById[employeeShift.id] = currentEmployeeShift;
        }

        //set possible shift types to employeePossibleInputValues
        var employeePossibleShifts = {};
        for (var j = 0; j < allShiftTypes.length; j++) {
            var shiftType = allShiftTypes[j];
            employeePossibleShifts[shiftType.shortName] = true;
        }
        //add annual leave
        for (var j = 0; j < workShift.employee.annualLeaves.length; j++) {
            var annualLeave = workShift.employee.annualLeaves[j];
            employeePossibleShifts[annualLeave.annualLeaveTypeShort] = true;
        }
        employeePossibleInputValues[employeeId] = employeePossibleShifts;

        //set annualLeaveTypes
        for (var j = 0; j < annualLeaveTypeViewModels.length; j++) {
            var annualLeaveTypeViewModel = annualLeaveTypeViewModels[j];
            var annualLeaveType = {};
            annualLeaveType.id = annualLeaveTypeViewModel.id;
            annualLeaveType.fullName = annualLeaveTypeViewModel.fullName;
            var employeeAnnualLeaveIds = {};
            for (var k = 0; k < annualLeaveTypeViewModel.employeeIds.length; k++) {
                var employeeId = annualLeaveTypeViewModel.employeeIds[k];
                var annualLeaveId = annualLeaveTypeViewModel.annualLeaveIds[k];
                employeeAnnualLeaveIds[employeeId] = annualLeaveId;
            }
            annualLeaveType.employeeAnnualLeaveIds = employeeAnnualLeaveIds;
            annualLeaveTypes[annualLeaveTypeViewModel.shortName] = annualLeaveType;
        }

        // set notRegularShiftTypesMapObj
        for (var j = 0; j < notRegularShiftTypes.length; j++) {
            var notRegularShiftType = notRegularShiftTypes[j];
            notRegularShiftTypesMapObj[notRegularShiftType.shortName] = notRegularShiftType.fullName;
        }
    }
}

function leftBigButtonPushed() {
    $('#scrollerWorkShift').css('display', 'block');
    var startDate = DateCalculator.getDateMinusDays(currentStartDate, DAYS_BIG_BUTTON);
    startDate = DateCalculator.convertDateToString(startDate, '.');
    var endDate = DateCalculator.getDateMinusDays(currentEndDate, DAYS_BIG_BUTTON);
    endDate = DateCalculator.convertDateToString(endDate, '.');
    getWorkShiftsFromDB(startDate, endDate, leftBigButtonExecute);
}

function leftBigButtonExecute() {
    $('#scrollerWorkShift').css('display', 'none');
    currentStartDate = DateCalculator.getDateMinusDays(currentStartDate, DAYS_BIG_BUTTON);
    currentEndDate = DateCalculator.getDateMinusDays(currentEndDate, DAYS_BIG_BUTTON);
    addNewWorkShiftsToDome();
}

function leftSmallButtonPushed() {
    $('#scrollerWorkShift').css('display', 'block');
    var startDate = DateCalculator.getDateMinusDays(currentStartDate, DAYS_SMALL_BUTTON);
    startDate = DateCalculator.convertDateToString(startDate, '.');
    var endDate = DateCalculator.getDateMinusDays(currentEndDate, DAYS_SMALL_BUTTON);
    endDate = DateCalculator.convertDateToString(endDate, '.');
    getWorkShiftsFromDB(startDate, endDate, leftSmallButtonExecute);
}

function leftSmallButtonExecute() {
    $('#scrollerWorkShift').css('display', 'none');
    currentStartDate = DateCalculator.getDateMinusDays(currentStartDate, DAYS_SMALL_BUTTON);
    currentEndDate = DateCalculator.getDateMinusDays(currentEndDate, DAYS_SMALL_BUTTON);
    addNewWorkShiftsToDome();
}

function rightBigButtonPushed() {
    $('#scrollerWorkShift').css('display', 'block');
    var startDate = DateCalculator.getDatePlusDays(currentStartDate, DAYS_BIG_BUTTON);
    startDate = DateCalculator.convertDateToString(startDate, '.');
    var endDate = DateCalculator.getDatePlusDays(currentEndDate, DAYS_BIG_BUTTON);
    endDate = DateCalculator.convertDateToString(endDate, '.');
    getWorkShiftsFromDB(startDate, endDate, rightBigButtonExecute);
}

function rightBigButtonExecute() {
    $('#scrollerWorkShift').css('display', 'none');
    currentStartDate = DateCalculator.getDatePlusDays(currentStartDate, DAYS_BIG_BUTTON);
    currentEndDate = DateCalculator.getDatePlusDays(currentEndDate, DAYS_BIG_BUTTON);
    addNewWorkShiftsToDome();
}

function rightSmallButtonPushed() {
    $('#scrollerWorkShift').css('display', 'block');
    var startDate = DateCalculator.getDatePlusDays(currentStartDate, DAYS_SMALL_BUTTON);
    startDate = DateCalculator.convertDateToString(startDate, '.');
    var endDate = DateCalculator.getDatePlusDays(currentEndDate, DAYS_SMALL_BUTTON);
    endDate = DateCalculator.convertDateToString(endDate, '.');
    getWorkShiftsFromDB(startDate, endDate, rightSmallButtonExecute);
}

function rightSmallButtonExecute() {
    $('#scrollerWorkShift').css('display', 'none');
    currentStartDate = DateCalculator.getDatePlusDays(currentStartDate, DAYS_SMALL_BUTTON);
    currentEndDate = DateCalculator.getDatePlusDays(currentEndDate, DAYS_SMALL_BUTTON);
    addNewWorkShiftsToDome();
}

function addNewWorkShiftsToDome() {
    dates = newDates;
    workShifts = newWorkShifts;
    setEmployeesAnnualLeavesAndShiftsMaps();

    //clear old data
    $('td input').val('');
    $('.employeeShiftCustomTime').css('display', 'none');
    $('.wsh-table-head div').text('');
    $('.wsh-table-head').removeClass('holiday-in-table');
    $('.emplShift').removeClass('holiday-in-table');
    $('.wsh-table-head').removeClass('today-in-table');
    $('.emplShift').removeClass('today-in-table');

    // update year in main label
    var year = dates[7].date.split('.')[2];
    var newText = 'Разход ' + year;
    $('#mainYearLabel').text(newText);


    // add new data to date cells
    var dateArrCells = $('.wsh-table-head');
    for (var i = 0; i < dates.length; i++) {
        var date = dates[i];
        var thDomEl = dateArrCells[i];

        $(thDomEl).attr('class','wsh-table-head');
        $(thDomEl).addClass('col_' + date.shortDateCl);
        if (date.isToday) {
            $(thDomEl).addClass('today-in-table');
        } else if (date.isHoliday) {
            $(thDomEl).addClass('holiday-in-table');
        }

        var divDom = $(thDomEl).find('div');
        $(divDom).text(date.htmlText);
    }

    // fill cell new employee data
    for (var i = 0; i < workShifts.length; i++) {
        var workShift = workShifts[i];
        var employeeId = workShift.employee.id;
        var employeeCellsArr = $('#employeeRow_' + employeeId).children();
        for (var j = 0; j < workShift.employeeShifts.length; j++) {
            var employeeShift = workShift.employeeShifts[j];
            var tdEmployeeDomCell = employeeCellsArr[j + 1];
            $(tdEmployeeDomCell).attr('id', employeeId + '_' + employeeShift.date);
            $(tdEmployeeDomCell).attr('class', 'emplShift');
            $(tdEmployeeDomCell).addClass('col_' + employeeShift.shortDate);
            $(tdEmployeeDomCell).addClass('row_' + employeeId);
            if (employeeShift.isToday) {
                $(tdEmployeeDomCell).addClass('today-in-table');
            } else if (employeeShift.isHoliday) {
                $(tdEmployeeDomCell).addClass('holiday-in-table');
            }
            $(tdEmployeeDomCell).attr('isHoliday', employeeShift.isHoliday);
            $(tdEmployeeDomCell).attr('inputOldValue', employeeShift.typeAsString);
            $(tdEmployeeDomCell).attr('col', 'col_' + employeeShift.shortDate);
            $(tdEmployeeDomCell).attr('employeeShiftId', employeeShift.id);

            var inputDomEl = $(tdEmployeeDomCell).find('input');
            $(inputDomEl).attr('value', employeeShift.typeAsString);
            $(inputDomEl).val(employeeShift.typeAsString);
            $(inputDomEl).attr('title', employeeShift.tooltipText);
            $(inputDomEl).attr('employeeShiftId', employeeShift.id);

            if (employeeShift.hasCustomTime) {
                $(tdEmployeeDomCell).find('i').css('display', 'inline-block');
            }

        }
    }
}

function getWorkShiftsFromDB(startDate, endDate, callback) {
    var dateInterval = {};
    dateInterval.startDate = startDate;
    dateInterval.endDate = endDate;

    $.ajax({
        type: 'POST',
        url: '/all/workShifts/getNewDates',
        data: JSON.stringify(dateInterval),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                } else if (errorJSON.hasOwnProperty('errors') && Array.isArray(errorJSON.errors)) {
                    for (var i = 0; i < errorJSON.errors.length; i++) {
                        var error = errorJSON.errors[i];
                        if (error.hasOwnProperty('code') && error.code == 'Pattern') {
                            negativeAlertBox(null, error.defaultMessage, null, null);
                        }
                    }
                }
            }
        },
        success: function (dates) {
            newDates = dates;

            //get WorkShiftViewAllModel
            $.ajax({
                type: 'POST',
                url: '/all/getWorkShiftsByDates',
                data: JSON.stringify(dateInterval),
                contentType: 'application/json',
                error: function (response) {
                    var errorJSON = response.responseJSON;
                    if (errorJSON != undefined) {
                        if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                            && errorJSON.type == 'ERROR') {
                            negativeAlertBox(null, errorJSON.message, null, null);
                        } else if (errorJSON.hasOwnProperty('errors') && Array.isArray(errorJSON.errors)) {
                            for (var i = 0; i < errorJSON.errors.length; i++) {
                                var error = errorJSON.errors[i];
                                if (error.hasOwnProperty('code') && error.code == 'Pattern') {
                                    negativeAlertBox(null, error.defaultMessage, null, null);
                                }
                            }
                        }
                    }
                },
                success: function (workShiftViewAllModel) {
                    newWorkShifts = workShiftViewAllModel;

                    if (callback instanceof Function) {
                        callback();
                    }
                }
            });
        }
    });
}

