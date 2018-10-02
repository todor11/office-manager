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

var positiveBlock = ('<i class="fa fa-check fa-2x pickable boolean-picker boolean-positive" aria-hidden="true"></i>');
var negativeBlock = ('<i class="fa fa-times fa-2x pickable boolean-picker boolean-negative" aria-hidden="true"></i>');

function init() {
    setEmployeesAnnualLeavesAndShiftsMaps();

    //setup requests
    $('.execute-request-button').click(executeEmployeeRequest);
    $('.delete-request-button').click(deleteEmployeeRequest);
    $('.requestRow').on( "mouseover", focusInEmployeeRequest);
    $('.requestRow').on( "mouseout", focusOutEmployeeRequest);

    var today = new Date();
    currentStartDate = DateCalculator.getDateMinusDays(today, DAYS_LEFT);
    currentEndDate = DateCalculator.getDatePlusDays(today, DAYS_RIGHT);

    //set date buttons
    $('#leftBigButton').click(leftBigButtonPushed);
    $('#leftSmallButton').click(leftSmallButtonPushed);
    $('#rightBigButton').click(rightBigButtonPushed);
    $('#rightSmallButton').click(rightSmallButtonPushed);

    $("*[type='radio']").click(setValueToCell);
    $('.emplShift').focusin(changeSelectedRowCol);
    $('.emplShift').on('change', inputWasChanged);
    //$('body').click(deselectAllCells);
    $('#tableContainer').click(deselectAllCells);
    $('#addMultipleBlock').click(deselectAllCells);
    $('.addCustomTimeButtonContainerClass').click(showCustomTime);

    //set time picker
    var elements = $('.timepicker-input');
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        setTimePickerToElement($(element), firstTimeChanged);
    }

    //set datepicker
    $("#startDate").datepicker({dateFormat: 'dd.mm.yy'});
    $("#endDate").datepicker({dateFormat: 'dd.mm.yy'});
    $('.date-validate').on('keyup change', dateInputValidate);
    $('#employeeMultipleAdd').on('change', employeeInMultipleAddWasChanged);
    $('#employeeShiftMultipleAdd').on('change', employeeShiftInMultipleAddWasChanged);

    $('#addMultipleButton').click(addMultipleEmployeeShifts);

    $('.boolean-value-box').click(changeBoolean);

    $( '.employeeShiftCustomTime' ).tooltip({
        content: function() {
            var parent = $(this).parent();
            var input = $(parent).find('input');
            return $(input).attr('title');
        }
    });

    // update year in main label
    var year = dates[7].date.split('.')[2];
    var newText = 'Разход ' + year;
    $('#mainYearLabel').text(newText);

}

function focusInEmployeeRequest() {
    $(this).css('background-color', 'cyan');
    var employeeId = $(this).attr('employeeId');
    var startDate = $(this).attr('startDate');
    var endDate = $(this).attr('endDate');
    while (true) {
        var cellId = employeeId + '_' + startDate;
        if ($('#' + cellId).attr('isHoliday') == 'false') {
            $('#' + cellId).css('background-color', 'cyan');
        }

        if (startDate == endDate) {
            break;
        }

        startDate = DateCalculator.getNextDayAsStringFromDateAsString(startDate, '-');
    }
}

function focusOutEmployeeRequest() {
    $(this).css('background-color', 'inherit');
    var employeeId = $(this).attr('employeeId');
    var startDate = $(this).attr('startDate');
    var endDate = $(this).attr('endDate');
    while (true) {
        var cellId = employeeId + '_' + startDate;
        if ($('#' + cellId).attr('isHoliday') == 'false') {
            $('#' + cellId).css('background-color', '');
        }

        if (startDate == endDate) {
            break;
        }

        startDate = DateCalculator.getNextDayAsStringFromDateAsString(startDate, '-');
    }
}

function deleteEmployeeRequest() {
    var requestId = $(this).attr('requestId');
    var employee = $(this).attr('employee');
    var question = 'Сигурен ли си, че искаш да заявката на ' + employee + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmDeleteEmployeeRequest , requestId);
}

function confirmDeleteEmployeeRequest(requestId) {
    $.ajax({
        type: 'DELETE',
        url: '/allUsers/deleteAnnualLeaveRequest/' + requestId,
        error: function () {
        },
        success: function () {
            //remove from DOM
            $('#requestId_' + requestId).remove();
        }
    });
}

function executeEmployeeRequest() {
    //set animation to button and disable it
    var buttonEl = $(this);
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);
    var requestId = $(this).attr('requestId');
    var employeeId = $(this).attr('employeeId');

    $.ajax({
        type: 'POST',
        url: '/boss/executeEmployeeRequest/' + requestId,
        error: function (response) {
            stopAnimationAndEnableButton(buttonEl);
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
        success: function (annualLeaveDateIntervals) {

            //add annual leaves to table
            for (var i = 0; i < annualLeaveDateIntervals.length; i++) {
                var annualLeaveDateInterval = annualLeaveDateIntervals[i];
                var startDate = annualLeaveDateInterval.startDate;
                var endDate = annualLeaveDateInterval.endDate;
                var shiftTypeShortName = annualLeaveDateInterval.shiftTypeShortName;
                var currentDate = startDate;
                var dateToBreak = DateCalculator.getNextDayAsStringFromDateAsString(endDate, '-');
                while (currentDate != dateToBreak) {
                    var modifiedDate = currentDate.replace(/\./g, '-');
                    var tdId = employeeId + '_' + modifiedDate;
                    var td = document.getElementById(tdId);
                    if (td != undefined) {
                        var input = $(td).find('input');

                        // check prev value is annual leave
                        var inputOldValue = $(input).val();
                        if (inputOldValue != '' && annualLeaveTypes.hasOwnProperty(inputOldValue)) {
                            //update employeesAnnualLeavesMapById
                            var annualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                            var oldNumbOfAnnulLeave = annualLeave.numberOfAnnualLeave;
                            annualLeave.numberOfAnnualLeave = annualLeave.numberOfAnnualLeave + 1;

                            // update employeePossibleInputValues
                            if (annualLeave.numberOfAnnualLeave == 1) {
                                employeePossibleInputValues[employeeId][shiftTypeShortName] = true;
                            }

                            // update employee names tooltip
                            if (annualLeave.numberOfAnnualLeave == 1) {
                                var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                                var newText = '<br/>' + annualLeave.annualLeaveType + ' - 1дни';
                                var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText + newText;
                                $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                            } else {
                                var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                                var textToReplace = annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                                var newText = annualLeave.annualLeaveType + ' - ' + annualLeave.numberOfAnnualLeave + 'дни';
                                var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                                $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                            }
                        }

                        //set new value
                        if ($(td).attr('isHoliday') == 'true') {
                            if (currentDate == endDate) {
                                break;
                            } else {
                                currentDate = DateCalculator.getNextDayAsStringFromDateAsString(currentDate, '-');
                                continue;
                            }
                        }
                        $(input).val(shiftTypeShortName);
                        $(td).attr('inputOldValue', shiftTypeShortName);
                        $(input).attr('title', annualLeaveTypes[shiftTypeShortName].fullName);

                        // update employeeShiftMapById
                        var employeeShiftId = $(td).attr('employeeShiftId');
                        var employeeShift = employeeShiftMapById[employeeShiftId];
                        employeeShift.startTime = '';
                        employeeShift.endTime = '';
                        employeeShift.secondStartTime = '';
                        employeeShift.secondEndTime = '';
                        employeeShift.isEndOnNextDay = false;
                        $(td).find('i').css('display', 'none');
                    }

                    currentDate = DateCalculator.getNextDayAsStringFromDateAsString(currentDate, '-');
                }

                // update employee annual leave
                //update employeesAnnualLeavesMapById
                var annualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[shiftTypeShortName];
                var oldNumbOfAnnulLeave = annualLeave.numberOfAnnualLeave;
                var numbOfAnnualLeave = annualLeaveDateInterval.numbOfAnnualLeave;
                annualLeave.numberOfAnnualLeave = annualLeave.numberOfAnnualLeave - numbOfAnnualLeave;

                // update employeePossibleInputValues
                if (annualLeave.numberOfAnnualLeave == 0) {
                    employeePossibleInputValues[employeeId][shiftTypeShortName] = false;
                }

                // update employee names tooltip
                if (annualLeave.numberOfAnnualLeave == 0) {
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    var textToReplace = '<br/>' + annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                    var newText = '';
                    var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                    $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                } else {
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    var textToReplace = annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                    var newText = annualLeave.annualLeaveType + ' - ' + annualLeave.numberOfAnnualLeave + 'дни';
                    var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                    $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                }
            }


            //remove from DOM
            $('#requestId_' + requestId).remove();
        }
    });
}

function addMultipleEmployeeShifts() {
    //set animation to button and disable it
    var buttonEl = $('#addMultipleButton');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    var multipleAddEmployeeShift = {};
    //check is inputs
    var employeeId = $('#employeeMultipleAdd').val();
    if (employeeId == 0) {
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Избери служител за който да въведе !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    multipleAddEmployeeShift.employeeId = employeeId;

    var startDate = $('#startDate').val();
    if (startDate == '' || !RegExPatternConstants.DATE.test(startDate)) {
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Началната дата не е валидна !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    multipleAddEmployeeShift.startDate = startDate;

    var endDate = $('#endDate').val();
    if (endDate == '' || !RegExPatternConstants.DATE.test(endDate)) {
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Крайната дата не е валидна !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    multipleAddEmployeeShift.endDate = endDate;

    var shiftTypeId = $('#employeeShiftMultipleAdd').val();
    if (shiftTypeId == 0) {
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Не е избрана смяна !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    multipleAddEmployeeShift.shiftTypeId = shiftTypeId;
    multipleAddEmployeeShift.isAnnualLeave = false;

    var shiftType;
    var addToHolidayToo = checkBoolean($('#addToHolidayToo'));
    var shiftTypeShortName = $("#employeeShiftMultipleAdd").find(':selected').attr('shortName');
    var isAnnualLeave = false;
    var numbOfAnnualLeave = 0;
    if (shiftTypeShortName != undefined) {
        isAnnualLeave = true;
        addToHolidayToo = false;
        numbOfAnnualLeave = $("#numbOfAnnualLeave").val();
        if (numbOfAnnualLeave == 0 || !RegExPatternConstants.POSITIVE_NUMBER.test(numbOfAnnualLeave)) {
            stopAnimationAndEnableButton(buttonEl);
            var msg = 'Броя дни отпуск не е валиден !';
            negativeAlertBox(null, msg, null, null);
            return;
        }
        numbOfAnnualLeave = parseInt(numbOfAnnualLeave);
        if (employeesAnnualLeavesMapById[employeeId].annualLeaves[shiftTypeShortName].numberOfAnnualLeave < numbOfAnnualLeave) {
            stopAnimationAndEnableButton(buttonEl);
            var msg = 'Служителя няма толкова дни от тази отпуска !';
            negativeAlertBox(null, msg, null, null);
            return;
        }
        multipleAddEmployeeShift.isAnnualLeave = true;
    } else {
        for (var i = 0; i < allShiftTypes.length; i++) {
            var tempShiftType = allShiftTypes[i];
            if (tempShiftType.id == shiftTypeId) {
                shiftType = tempShiftType;
                break;
            }
        }
    }

    multipleAddEmployeeShift.addToHolidayToo = addToHolidayToo;
    multipleAddEmployeeShift.numbOfAnnualLeave = numbOfAnnualLeave;

    //send to DB
    $.ajax({
        type: 'POST',
        url: '/boss/addMultipleEmployeeShifts',
        data: JSON.stringify(multipleAddEmployeeShift),
        contentType: 'application/json',
        error: function (response) {
            stopAnimationAndEnableButton(buttonEl);
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
        success: function () {

            // add new employee shifts to dom
            var currentDate = startDate;
            var dateToBreak = DateCalculator.getNextDayAsStringFromDateAsString(endDate, '.');
            while (currentDate != dateToBreak) {
                var modifiedDate = currentDate.replace(/\./g, '-');
                var tdId = employeeId + '_' + modifiedDate;
                var td = document.getElementById(tdId);
                if (td != undefined) {
                    var input = $(td).find('input');

                    // check prev value is annual leave
                    var inputOldValue = $(input).val();
                    if (inputOldValue != '' && annualLeaveTypes.hasOwnProperty(inputOldValue)) {
                        //update employeesAnnualLeavesMapById
                        var annualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                        var oldNumbOfAnnulLeave = annualLeave.numberOfAnnualLeave;
                        annualLeave.numberOfAnnualLeave = annualLeave.numberOfAnnualLeave + 1;

                        // update employeePossibleInputValues
                        if (annualLeave.numberOfAnnualLeave == 1) {
                            employeePossibleInputValues[employeeId][shiftTypeShortName] = true;
                        }

                        // update employee names tooltip
                        if (annualLeave.numberOfAnnualLeave == 1) {
                            var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                            var newText = '<br/>' + annualLeave.annualLeaveType + ' - 1дни';
                            var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText + newText;
                            $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                        } else {
                            var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                            var textToReplace = annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                            var newText = annualLeave.annualLeaveType + ' - ' + annualLeave.numberOfAnnualLeave + 'дни';
                            var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                            $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                        }
                    }

                    //set new value
                    if (isAnnualLeave) {
                        if ($(td).attr('isHoliday') == 'true') {
                            if (currentDate == endDate) {
                                break;
                            } else {
                                currentDate = DateCalculator.getNextDayAsStringFromDateAsString(currentDate, '.');
                                continue;
                            }
                        }
                        $(input).val(shiftTypeShortName);
                        $(td).attr('inputOldValue', shiftTypeShortName);
                        $(input).attr('title', $('#employeeShiftMultipleAdd :selected').text());

                        // update employeeShiftMapById
                        var employeeShiftId = $(td).attr('employeeShiftId');
                        var employeeShift = employeeShiftMapById[employeeShiftId];
                        employeeShift.startTime = '';
                        employeeShift.endTime = '';
                        employeeShift.secondStartTime = '';
                        employeeShift.secondEndTime = '';
                        employeeShift.isEndOnNextDay = false;
                    } else {
                        if (!addToHolidayToo && $(td).attr('isHoliday') == 'true') {
                            if (currentDate == endDate) {
                                break;
                            } else {
                                currentDate = DateCalculator.getNextDayAsStringFromDateAsString(currentDate, '.');
                                continue;
                            }
                        }

                        $(input).val(shiftType.shortName);
                        $(td).attr('inputOldValue', shiftType.shortName);
                        if (shiftType.isRegularShift) {
                            var tooltipText = 'смяна:<br/>от: ' + shiftType.startTime + '<br/>до: ' + shiftType.endTime;
                            if (shiftType.secondEndTime != '') {
                                tooltipText = tooltipText + '<br/>от: ' + shiftType.secondStartTime + '<br/>до: ' + shiftType.secondEndTime;
                            }
                            $(input).attr('title', tooltipText);
                        } else {
                            $(input).attr('title', shiftType.fullName);
                        }

                        // update employeeShiftMapById
                        var employeeShiftId = $(td).attr('employeeShiftId');
                        var employeeShift = employeeShiftMapById[employeeShiftId];
                        employeeShift.startTime = shiftType.startTime;
                        employeeShift.endTime = shiftType.endTime;
                        employeeShift.secondStartTime = shiftType.secondStartTime;
                        employeeShift.secondEndTime = shiftType.secondEndTime;
                        employeeShift.isEndOnNextDay = shiftType.isEndOnNextDay;
                    }
                    $(td).find('i').css('display', 'none');
                }

                currentDate = DateCalculator.getNextDayAsStringFromDateAsString(currentDate, '.');
            }

            // update employee annual leave
            if (isAnnualLeave) {
                //update employeesAnnualLeavesMapById
                var annualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[shiftTypeShortName];
                var oldNumbOfAnnulLeave = annualLeave.numberOfAnnualLeave;
                annualLeave.numberOfAnnualLeave = annualLeave.numberOfAnnualLeave - numbOfAnnualLeave;

                // update employeePossibleInputValues
                if (annualLeave.numberOfAnnualLeave == 0) {
                    employeePossibleInputValues[employeeId][shiftTypeShortName] = false;
                }

                // update employee names tooltip
                if (annualLeave.numberOfAnnualLeave == 0) {
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    var textToReplace = '<br/>' + annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                    var newText = '';
                    var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                    $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                } else {
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    var textToReplace = annualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                    var newText = annualLeave.annualLeaveType + ' - ' + annualLeave.numberOfAnnualLeave + 'дни';
                    var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                    $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                }
            }

            // clear multiple inputs fields
            $('#employeeMultipleAdd').val(0);
            $('#startDate').val('');
            $('#endDate').val('');
            $('#employeeShiftMultipleAdd').val(0);
            $('#numbOfAnnualLeave').val(0);
            changeBooleanToNegative($('#addToHolidayToo'));

            stopAnimationAndEnableButton(buttonEl);
        }
    });
}

function employeeInMultipleAddWasChanged() {
    var employeeId = $(this).val();
    $('#employeeShiftMultipleAdd .removableAnnualLeave').remove();
    if (employeeId != 0) {
        var employee = employeesAnnualLeavesMapById[employeeId];
        var annualLeaves = employee.annualLeaves;
        $.each(annualLeaves, function (key, value) {
            if (value.numberOfAnnualLeave > 0){
                var newDomObj = $('<option class="removableAnnualLeave" shortName="' + key + '" numbOfAnnualLeave="' + value.numberOfAnnualLeave + '" value="' + value.annualLeaveTypeId + '">' + value.annualLeaveType + '</option>');
                $('#employeeShiftMultipleAdd').append(newDomObj);
            }
        });
    }

    employeeShiftInMultipleAddWasChanged();
}

function employeeShiftInMultipleAddWasChanged() {
    var annualLeaveTypeShortName = $("#employeeShiftMultipleAdd").find(':selected').attr('shortName');
    if (annualLeaveTypeShortName != undefined) {
        if ($('#numbOfALT').hasClass('hideText')) {
            $('#numbOfALT').removeClass('hideText');
            $('#numbOfALH').removeClass('hideText');
            $('#isRDH').addClass('hideText');
            $('#isRDT').addClass('hideText');
        }
    } else {
        if ($('#isRDT').hasClass('hideText')) {
            $('#isRDT').removeClass('hideText');
            $('#isRDH').removeClass('hideText');
            $('#numbOfALT').addClass('hideText');
            $('#numbOfALH').addClass('hideText');
        }
    }
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

function firstTimeChanged() {
    var elements = $('.workTimeToCheck');
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        var employeeId = $(element).attr('employeeId');
        var isValidTime = validateTimes(employeeId);
        if (isValidTime) {
            var startTime = $('#startTime_' + employeeId).val();
            var endTime = $('#endTime_' + employeeId).val();
            if (!TimeCalculator.isFirstTimeBeforeSecondTime(startTime, endTime)) {
                $('.secondTimeTime_' + employeeId).css('visibility', 'hidden');
                $('.secondTimeTime_' + employeeId).val('');
            } else {
                $('.secondTimeTime_' + employeeId).css('visibility', 'visible');
            }

            checkTimesAndChangeIsOnNextDay(employeeId);
        } else {
            $('.secondTimeTime_' + employeeId).css('visibility', 'hidden');
            $('#secondStartTime_' + employeeId).val('');
            $('#secondEndTime_' + employeeId).val('');
        }
    }
    var secTimesInputs = $('.secTimeToCheck');
    for (var i = 0; i < secTimesInputs.length; i++) {
        var element = secTimesInputs[i];
        var employeeId = $(element).attr('employeeId');

        var secondStartTime = $('#secondStartTime_' + employeeId).val();
        var secondEndTime = $('#secondEndTime_' + employeeId).val();
        if (secondStartTime != '' && secondEndTime != '') {
            if (TimeCalculator.isFirstTimeBeforeSecondTime(secondStartTime, secondEndTime)) {
                $('.secondTimeTime_' + employeeId).val('');
                alert('Ако втория интервал е изцяло в следващия ден - го добави в следващия ден, а не тук !')
            }
            checkTimesAndChangeIsOnNextDay(employeeId);
        }
    }
}

function validateTimes(employeeId) {
    var inputs = $('.firstTime_' + employeeId);
    var isValid = true;
    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        var value = $(input).val();
        isValid = RegExPatternConstants.TIME.test(value);
        if (!isValid) {
            break;
        }
    }

    return isValid;
}

function checkTimesAndChangeIsOnNextDay(employeeId) {
    var startTime = $('#startTime_' + employeeId).val();
    if (!RegExPatternConstants.TIME.test(startTime)) {
        changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
        return;
    }

    var endTime = $('#endTime_' + employeeId).val();
    if (!RegExPatternConstants.TIME.test(endTime)) {
        changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
        return;
    }

    var secondStartTime = $('#secondStartTime_' + employeeId).val();
    var secondEndTime = $('#secondEndTime_' + employeeId).val();
    if (secondStartTime == '' && secondEndTime == '') {
        var isStartTimeBeforeEnd = TimeCalculator.isFirstTimeBeforeSecondTime(startTime, endTime);
        if (isStartTimeBeforeEnd != null && !isStartTimeBeforeEnd) {
            changeBooleanToPositive($('#isEndOnNextDay_' + employeeId));
            return;
        } else {
            changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
            return;
        }
    } else if (secondStartTime != '' && secondEndTime != ''){
        if (RegExPatternConstants.TIME.test(secondStartTime) && RegExPatternConstants.TIME.test(secondEndTime)) {
            var isSecondStartTimeBeforeEnd = TimeCalculator.isFirstTimeBeforeSecondTime(secondStartTime, secondEndTime);
            if (isSecondStartTimeBeforeEnd != null && !isSecondStartTimeBeforeEnd) {
                changeBooleanToPositive($('#isEndOnNextDay_' + employeeId));
                return;
            } else {
                changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
                return;
            }
        } else {
            changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
            return;
        }
    } else {
        changeBooleanToNegative($('#isEndOnNextDay_' + employeeId));
        return;
    }
}

function changeBooleanToNegative(booleanBox) {
    if ($(booleanBox).hasClass('boolean-value-box')) {
        $(booleanBox).empty();
        var negativeBlock = $('<i class="fa fa-times fa-2x pickable boolean-picker boolean-negative" aria-hidden="true"></i>');
        $(booleanBox).append(negativeBlock);
    }
}

function changeBooleanToPositive(booleanBox) {
    if ($(booleanBox).hasClass('boolean-value-box')) {
        $(booleanBox).empty();
        var positiveBlock = $('<i class="fa fa-check fa-2x pickable boolean-picker boolean-positive" aria-hidden="true"></i>');
        $(booleanBox).append(positiveBlock);
    }
}

function showCustomTime() {
    var employeeId = $(this).attr('employeeId');
    $('#chooseEmployeeShiftBoxTime_' + employeeId).css('display', 'block');
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

            $(tdEmployeeDomCell).attr('isTypicalWorkDay', employeeShift.isTypicalWorkDay);
            $(tdEmployeeDomCell).attr('isTypicalRestDay', employeeShift.isTypicalRestDay);
            $(tdEmployeeDomCell).attr('isLastWorkDayBeforeRestDay', employeeShift.isLastWorkDayBeforeRestDay);
            $(tdEmployeeDomCell).attr('isLastRestDayBeforeWorkDay', employeeShift.isLastRestDayBeforeWorkDay);

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

function inputWasChanged(inputParentDomElement) {
    if ($(inputParentDomElement).attr('row') == undefined) {
        inputParentDomElement = inputParentDomElement.target;
        inputParentDomElement = $(inputParentDomElement).parent().parent();
        rr = inputParentDomElement;
    }

    //validate Input
    var employeeId = $(inputParentDomElement).attr('employeeId');
    var employeeShiftId = $(inputParentDomElement).attr('employeeShiftId');
    var inputDomElement = $(inputParentDomElement).find('input');
    var inputValue = $(inputDomElement).val();
    var inputOldValue = $(inputParentDomElement).attr('inputOldValue');
    if (inputOldValue == undefined) {
        inputOldValue = '';
    }
    var isValid = false;
    var date = $(inputParentDomElement).attr('id').split('_')[1];

    if (inputValue == '') {
        isValid = true;
    } else {
        if (employeePossibleInputValues[employeeId].hasOwnProperty(inputValue)) {
            isValid = employeePossibleInputValues[employeeId][inputValue];
        }

        if (!isValid) {
            var msg = 'Въведената смяна на ' + employeesAnnualLeavesMapById[employeeId].employeeNames + ' за дата ' + date + ' не е валиден!';
            negativeAlertBox(null, msg, null, null);
            $(inputParentDomElement).find('input').val(inputOldValue);
            return;
        }
    }

    var employeeShift = {};
    employeeShift.id = employeeShiftId;
    employeeShift.employeeId = employeeId;
    employeeShift.startTime = '';
    employeeShift.endTime = '';
    employeeShift.secondStartTime = '';
    employeeShift.secondEndTime = '';
    employeeShift.isEndOnNextDay = false;
    var isShiftType = false;
    var isAnnualLeaveType = false;
    var startTime = '';
    var endTime = '';
    var secondStartTime = '';
    var secondEndTime = '';
    var isEndOnNextDay = false;

    if (inputValue == '') {
        employeeShift.shiftTypeId = 0;
    } else {
        for (var i = 0; i < allShiftTypes.length; i++) {
            var shiftType = allShiftTypes[i];
            var shiftTypeShortName = shiftType.shortName;
            if (shiftTypeShortName == inputValue) {
                date = date.replace(/-/g, '.');

                var nextDay = DateCalculator.getNextDayAsStringFromDateAsString(date, '.');
                nextDay = nextDay + ' ';

                date += ' ';
                isShiftType = true;
                employeeShift.shiftTypeId = shiftType.id;

                // check has prev
                if (inputOldValue != '' && inputValue != '' && inputOldValue != inputValue) {
                    isEndOnNextDay = shiftType.isEndOnNextDay;
                    startTime = shiftType.startTime;
                    endTime = shiftType.endTime;
                    secondStartTime = shiftType.secondStartTime;
                    secondEndTime = shiftType.secondEndTime;
                } else {
                    isEndOnNextDay = checkBoolean($('#isEndOnNextDay_' + employeeId));
                    startTime = $('#startTime_' + employeeId).val();
                    endTime = $('#endTime_' + employeeId).val();
                    secondStartTime = $('#secondStartTime_' + employeeId).val();
                    secondEndTime = $('#secondEndTime_' + employeeId).val();
                }

                if (startTime == '' && endTime == '' && secondStartTime == '' && secondEndTime == '') {
                    startTime = shiftType.startTime;
                    endTime = shiftType.endTime;
                    secondStartTime = shiftType.secondStartTime;
                    secondEndTime = shiftType.secondEndTime;
                }

                if (startTime != '' && !RegExPatternConstants.TIME.test(startTime)) {
                    startTime = '';
                }

                if (endTime != '' && !RegExPatternConstants.TIME.test(endTime)) {
                    endTime = '';
                }

                if (secondStartTime != '' && !RegExPatternConstants.TIME.test(secondStartTime)) {
                    secondStartTime = '';
                }

                if (secondEndTime != '' && !RegExPatternConstants.TIME.test(secondEndTime)) {
                    secondEndTime = '';
                }

                if (startTime == '' || endTime == '') {
                    startTime = '';
                    endTime = '';
                    secondStartTime = '';
                    secondEndTime = '';
                } else if (secondStartTime == '' || secondEndTime == '') {
                    secondStartTime = '';
                    secondEndTime = '';
                }

                if (startTime != '') {
                    startTime = date + startTime;
                }

                if (endTime != '') {
                    if (isEndOnNextDay) {
                        if (secondEndTime != '') {
                            endTime = date + endTime;
                        } else {
                            endTime = nextDay + endTime;
                        }
                    } else {
                        endTime = date + endTime;
                    }
                }

                if (secondStartTime != '') {
                    secondStartTime = date + secondStartTime;
                }

                if (secondEndTime != '') {
                    if (isEndOnNextDay) {
                        secondEndTime = nextDay + secondEndTime;
                    } else {
                        secondEndTime = date + secondEndTime;
                    }
                }

                employeeShift.startTime = startTime;
                employeeShift.endTime = endTime;
                employeeShift.secondStartTime = secondStartTime;
                employeeShift.secondEndTime = secondEndTime;
                employeeShift.isEndOnNextDay = isEndOnNextDay;
                break;
            }
        }

        if (!isShiftType) {
            var annualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputValue];
            var isValidAnnualLeave = false;
            var isHoliday = ($(inputParentDomElement).attr('isHoliday') == 'true');

            if (isHoliday) {
                var msg = 'Не може да въведеш отпуск на дата : ' + date + ', защото е почивен ден !';
                negativeAlertBox(null, msg, null, null);
                $(inputParentDomElement).find('input').val(inputOldValue);
                return;
            }

            if (annualLeave != undefined) {
                var numberOfAnnualLeave = annualLeave.numberOfAnnualLeave;
                if (numberOfAnnualLeave > 0) {
                    isValidAnnualLeave = true;
                    isAnnualLeaveType = true;
                    employeeShift.shiftTypeId = annualLeave.annualLeaveTypeId;
                }
            }

            if (!isValidAnnualLeave) {
                var msg = 'Въведената смяна на ' + employeesAnnualLeavesMapById[employeeId].employeeNames + ' за дата ' + date + ' не е валиден!';
                negativeAlertBox(null, msg, null, null);
                $(inputParentDomElement).find('input').val(inputOldValue);
                return;
            }
        }
    }

    //send to DB
    $.ajax({
        type: 'PUT',
        url: '/boss/updateEmployeeShift',
        data: JSON.stringify(employeeShift),
        contentType: 'application/json',
        error: function (response) {
            $(inputDomElement).val(inputOldValue);
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
        success: function () {
            var employeeShiftToUpdate = employeeShiftMapById[employeeShiftId];
            if (startTime != ''){
                startTime = startTime.split(' ')[1];
            }
            employeeShiftToUpdate.startTime = startTime;
            if (endTime != ''){
                endTime = endTime.split(' ')[1];
            }
            employeeShiftToUpdate.endTime = endTime;
            if (secondStartTime != ''){
                secondStartTime = secondStartTime.split(' ')[1];
            }
            employeeShiftToUpdate.secondStartTime = secondStartTime;
            if (secondEndTime != ''){
                secondEndTime = secondEndTime.split(' ')[1];
            }
            employeeShiftToUpdate.secondEndTime = secondEndTime;
            employeeShiftToUpdate.isEndOnNextDay = isEndOnNextDay;

            //get custom time i dom element
            var customTimeStar = $(inputParentDomElement).find('i');
            $(customTimeStar).css('display', 'none');

            if (inputValue == '') {
                //set tooltip
                $(inputDomElement).attr('title', 'няма въведено');

                //check prev type is Annual leave and increase it
                if (employeesAnnualLeavesMapById.hasOwnProperty(employeeId) && annualLeaveTypes.hasOwnProperty(inputOldValue)) {
                    var employeeAnnualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                    if (employeeAnnualLeave == undefined) {
                        var newEmployeeAnnualLeave = {};
                        newEmployeeAnnualLeave.annualLeaveType = annualLeaveTypes[inputOldValue].fullName;
                        newEmployeeAnnualLeave.annualLeaveTypeId = annualLeaveTypes[inputOldValue].id;
                        newEmployeeAnnualLeave.id = annualLeaveTypes[inputOldValue].employeeAnnualLeaveIds[employeeId];
                        newEmployeeAnnualLeave.numberOfAnnualLeave = 0;
                        employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue] = newEmployeeAnnualLeave;
                        employeeAnnualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                    }
                    var oldNumbOfAnnulLeave = employeeAnnualLeave.numberOfAnnualLeave;
                    employeeAnnualLeave.numberOfAnnualLeave = employeeAnnualLeave.numberOfAnnualLeave + 1;
                    if (employeePossibleInputValues.hasOwnProperty(employeeId)) {
                        employeePossibleInputValues[employeeId][inputOldValue] = true;
                    }

                    // update employee names tooltip text
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    if (employeeAnnualLeave.numberOfAnnualLeave == 1) {
                        var newText = employeeAnnualLeave.annualLeaveType + ' - ' + employeeAnnualLeave.numberOfAnnualLeave + 'дни';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText + '<br/>' + newText;
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    } else {
                        var textToReplace = employeeAnnualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                        var newText = employeeAnnualLeave.annualLeaveType + ' - ' + employeeAnnualLeave.numberOfAnnualLeave + 'дни';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    }
                }

                //set as old value new value
                $(inputParentDomElement).attr('inputOldValue', '');
                $(inputDomElement).attr('value', '');
            } else {
                //check prev type is Annual leave and increase it
                if (employeesAnnualLeavesMapById.hasOwnProperty(employeeId) && annualLeaveTypes.hasOwnProperty(inputOldValue)) {
                    var employeeAnnualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                    if (employeeAnnualLeave == undefined) {
                        var newEmployeeAnnualLeave = {};
                        var annualLeaveType = annualLeaveTypes[inputOldValue];
                        newEmployeeAnnualLeave.annualLeaveType = annualLeaveType.fullName;
                        newEmployeeAnnualLeave.annualLeaveTypeId = annualLeaveType.id;
                        newEmployeeAnnualLeave.id = annualLeaveType.employeeAnnualLeaveIds[employeeId];
                        newEmployeeAnnualLeave.numberOfAnnualLeave = 0;
                        employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue] = newEmployeeAnnualLeave;
                        employeeAnnualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputOldValue];
                    }
                    var oldNumbOfAnnulLeave = employeeAnnualLeave.numberOfAnnualLeave;
                    employeeAnnualLeave.numberOfAnnualLeave = employeeAnnualLeave.numberOfAnnualLeave + 1;
                    if (employeePossibleInputValues.hasOwnProperty(employeeId)) {
                        employeePossibleInputValues[employeeId][inputOldValue] = true;
                    }

                    // update employee names tooltip text
                    var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                    if (employeeAnnualLeave.numberOfAnnualLeave == 1) {
                        var newText = employeeAnnualLeave.annualLeaveType + ' - ' + employeeAnnualLeave.numberOfAnnualLeave + 'дни';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText + '<br/>' + newText;
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    } else {
                        var textToReplace = employeeAnnualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                        var newText = employeeAnnualLeave.annualLeaveType + ' - ' + employeeAnnualLeave.numberOfAnnualLeave + 'дни';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    }
                }
                //

                //set as old value new value
                $(inputParentDomElement).attr('inputOldValue', inputValue);
                $(inputDomElement).attr('value', inputValue);

                //check new type is Annual leave and decrease it
                if (employeesAnnualLeavesMapById.hasOwnProperty(employeeId) &&
                    employeesAnnualLeavesMapById[employeeId].annualLeaves.hasOwnProperty(inputValue)) {
                    var employeeAnnualLeave = employeesAnnualLeavesMapById[employeeId].annualLeaves[inputValue];
                    var oldNumbOfAnnulLeave = employeeAnnualLeave.numberOfAnnualLeave;
                    employeeAnnualLeave.numberOfAnnualLeave = employeeAnnualLeave.numberOfAnnualLeave - 1;
                    if (employeeAnnualLeave.numberOfAnnualLeave == 0 && employeePossibleInputValues.hasOwnProperty(employeeId) &&
                        employeePossibleInputValues[employeeId].hasOwnProperty(inputValue)) {
                        employeePossibleInputValues[employeeId][inputValue] = false;
                        // update employee names tooltip text
                        var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                        var textToReplace = '<br/>' + employeeAnnualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                        var newText = '';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    } else {
                        // update employee names tooltip text
                        var oldEmployeeNamesTooltipText = $('#employeeName_' + employeeId).attr('title');
                        var textToReplace = employeeAnnualLeave.annualLeaveType + ' - ' + oldNumbOfAnnulLeave + 'дни';
                        var newText = employeeAnnualLeave.annualLeaveType + ' - ' + employeeAnnualLeave.numberOfAnnualLeave + 'дни';
                        var newEmployeeNamesTooltipText = oldEmployeeNamesTooltipText.replace(textToReplace, newText);
                        $('#employeeName_' + employeeId).attr('title', newEmployeeNamesTooltipText);
                    }

                    //change tooltip text
                    var tooltipText = employeeAnnualLeave.annualLeaveType;
                    $(inputDomElement).attr('title', tooltipText);

                } else {
                    //change tooltip text
                    var tooltipText;
                    if (startTime != '') {
                        tooltipText = 'смяна:<br/>от: ' + startTime + '<br/>до: ' + endTime;
                        if (secondEndTime != '') {
                            tooltipText = tooltipText + '<br/>от: ' + secondStartTime + '<br/>до: ' + secondEndTime;
                        }
                    } else {
                        tooltipText = notRegularShiftTypesMapObj[inputValue];
                    }
                    $(inputDomElement).attr('title', tooltipText);

                    //check for custom time and set star
                    for (var i = 0; i < regularShiftTypes.length; i++) {
                        var regularShiftType = regularShiftTypes[i];
                        if (regularShiftType.shortName == inputValue) {
                            if (startTime != regularShiftType.startTime || endTime != regularShiftType.endTime ||
                                secondStartTime != regularShiftType.secondStartTime || secondEndTime != regularShiftType.secondEndTime) {
                                $(customTimeStar).css('display', 'inline-block');
                            }
                            break;
                        }
                    }
                }
            }
        }
    });
}

function changeSelectedRowCol() {
    $('.emplShift').removeClass('selectedWorkShiftCellInTable');
    $('.wsh-table-head').removeClass('selectedWorkShiftCellInTable');
    var rowClass = $(this).attr('row');
    var colClass = $(this).attr('col');

    $('.' + rowClass).addClass('selectedWorkShiftCellInTable');
    $('.' + colClass).addClass('selectedWorkShiftCellInTable');

    var employeeShiftBoxId = $(this).attr('employeeShiftBox');
    var inputValue = $(this).find('input').val();
    var employeeId = $(this).attr('employeeId');
    var employeeShiftId = $(this).attr('employeeShiftId');

    //set annualLeaves
    var employee = employeesAnnualLeavesMapById[employeeId];
    var annualLeaves = employee.annualLeaves;
    $('#' + employeeShiftBoxId + ' .removableAnnualLeaveInEmployeeLabel').remove();

    //remove unnecessary shitfs
    var isTypicalWorkDay = ($(this).attr('isTypicalWorkDay') == 'true');
    var isTypicalRestDay = ($(this).attr('isTypicalRestDay') == 'true');
    var isLastWorkDayBeforeRestDay = ($(this).attr('isLastWorkDayBeforeRestDay') == 'true');
    var isLastRestDayBeforeWorkDay = ($(this).attr('isLastRestDayBeforeWorkDay') == 'true');
    var shiftTypeDomElements = $('.shiftTypeInES_' + employeeId); //.css('display', 'block')
    for (var i = 0; i < shiftTypeDomElements.length; i++) {
        var shiftTypeBox = shiftTypeDomElements[i];
        $(shiftTypeBox).css('display', 'block');
        var input = $(shiftTypeBox).find('input');
        var canBeOnWorkDay = ($(input).attr('canBeOnWorkDay') == 'true');
        var canBeOnRestDay = ($(input).attr('canBeOnRestDay') == 'true');
        var canBeOnLastWorkDayBeforeRestDay = ($(input).attr('canBeOnLastWorkDayBeforeRestDay') == 'true');
        var canBeOnLastRestDayBeforeWorkDay = ($(input).attr('canBeOnLastRestDayBeforeWorkDay') == 'true');
        if ((isTypicalWorkDay && !canBeOnWorkDay) || (isTypicalRestDay && !canBeOnRestDay) ||
            (isLastWorkDayBeforeRestDay && !canBeOnLastWorkDayBeforeRestDay) || (isLastRestDayBeforeWorkDay && !canBeOnLastRestDayBeforeWorkDay)){
            $(shiftTypeBox).css('display', 'none');
        }
    }

    var isHoliday = ($(this).attr('isHoliday') == 'true');
    if (!isHoliday) {
        $.each(annualLeaves, function (key, value) {
            if (value.numberOfAnnualLeave > 0) {
                var inputId = 'employeeShiftBox-' + employeeId + '_' + key;
                var newDomObj = $('<div class="form-check removableAnnualLeaveInEmployeeLabel">' +
                    '<label class="form-check-label">' +
                    '<input class="form-check-input" isRegularShift="false" type="radio" name="gridRadios" id="' + inputId + '" value="' + key + '">' +
                    ' ' + key + ' &mdash; ' + value.annualLeaveType +
                    '</label>' +
                    '</div>');

                $(newDomObj).insertBefore('#addCustomTimeButtonContainer_' + employeeId);
                $('#' + inputId).click(setValueToCell);
            }
        });
    }

    $('.secondTimeTime_' + employeeId).css('visibility', 'hidden');
    if (inputValue != '') {
        $('#' + employeeShiftBoxId + ' input').prop('checked', false);
        var inputSelectId = employeeShiftBoxId + '_' + inputValue;
        $('#' + inputSelectId).prop('checked', true);

        //set times
        var startTime = employeeShiftMapById[employeeShiftId].startTime;
        var endTime = employeeShiftMapById[employeeShiftId].endTime;
        var secondStartTime = employeeShiftMapById[employeeShiftId].secondStartTime;
        var secondEndTime = employeeShiftMapById[employeeShiftId].secondEndTime;
        $('#startTime_' + employeeId).val(startTime);
        $('#endTime_' + employeeId).val(endTime);
        $('#secondStartTime_' + employeeId).val(secondStartTime);
        $('#secondEndTime_' + employeeId).val(secondEndTime);
        if (endTime != '') {
            $('.secondTimeTime_' + employeeId).css('visibility', 'visible');
        }

        $('#isEndOnNextDay_' + employeeId).empty();
        if (employeeShiftMapById[employeeShiftId].isEndOnNextDay) {
            $('#isEndOnNextDay_' + employeeId).append(positiveBlock);
        } else {
            $('#isEndOnNextDay_' + employeeId).append(negativeBlock);
        }

    } else {
        $('#' + employeeShiftBoxId + ' input').prop('checked', false);
        $('#startTime_' + employeeId).val('');
        $('#endTime_' + employeeId).val('');
        $('#secondStartTime_' + employeeId).val('');
        $('#secondEndTime_' + employeeId).val('');

        $('#isEndOnNextDay_' + employeeId).empty();
        $('#isEndOnNextDay_' + employeeId).append(negativeBlock);
    }

    $('.chooseEmployeeShiftBox').css('left', "-490px");
    var position = $(this).offset();
    var boxPosition = position.left + 51;
    boxPosition += "px";
    $('#' + employeeShiftBoxId).css('left', boxPosition);

    var cellId = $(this).attr('id');
    $('#' + employeeShiftBoxId).attr('cellId', cellId);

    $('#chooseEmployeeShiftBoxTime_' + employeeId).css('display', 'none');

    // set date
    var date = $(this).attr('id').split('_')[1];
    date = date.replace(/-/g, '.') + ' г.';
    $('#emplShiftLabelLegendRowDate_' + employeeId).text(date);
}

function deselectAllCells(e) {
    if (e.target.nodeName != 'INPUT') {
        $('.emplShift').removeClass('selectedWorkShiftCellInTable');
        $('.wsh-table-head').removeClass('selectedWorkShiftCellInTable');
        $('.chooseEmployeeShiftBox').css('left', "-490px");
    }
}

function setValueToCell() {
    var boxId = $(this).attr('id').split('_')[0];
    var cellId = $('#' + boxId).attr('cellId');
    var newValue = $(this).val();
    var inputParent = $('#' + cellId);
    var inputElement = $('#' + cellId).find('input');
    $(inputElement).val(newValue);

    //hide all choose
    $('.chooseEmployeeShiftBox').css('left', "-490px");
    $('.emplShift').removeClass('selectedWorkShiftCellInTable');
    $('.wsh-table-head').removeClass('selectedWorkShiftCellInTable');

    inputWasChanged(inputParent);
}