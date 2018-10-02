var tempActiveAnnulLeave;
var waitForResponse = false;

function init() {
    setTempActiveAnnulLeave();

    var newIndex = $('#requestDateIntervals').children().length;
    var newInput = $('<div class="annualLeaveInputsBlock" blockIndex="'+ newIndex + '">' +
        '<select class="annualLeaveRequestInputs invalid-input dropdown-validate-noLabel" id="annualLeaveType_'+ newIndex + '" blockIndex="'+ newIndex + '">' +
        '<option selected value="0">Избери отпуск</option>' +
        '</select>' +
        '<input type="text" class="annualLeaveRequestInputs date-validate invalid-input" placeholder="dd.MM.yyyy" id="startDate_'+ newIndex + '" blockIndex="'+ newIndex + '"/>' +
        '<input type="text" class="annualLeaveRequestInputs date-validate invalid-input" placeholder="dd.MM.yyyy" id="endDate_'+ newIndex + '" blockIndex="'+ newIndex + '"/>' +
        '<input type="text" disabled value="0" class="annualLeaveRequestInputs" id="inputNumberOfAnnualLeave_'+ newIndex + '" blockIndex="'+ newIndex + '"/>' +
        '</div>');
    $('#requestDateIntervals').append(newInput);

    for (var i = 0; i < annualLeaves.length; i++) {
        var currentAnnualLeave = annualLeaves[i];
        var newOptionEl = $('<option annualLeaveTypeId="' + currentAnnualLeave.annualLeaveTypeId + '" value="' + currentAnnualLeave.id + '">' + currentAnnualLeave.annualLeaveType + '</option>');
        $('#annualLeaveType_' + newIndex).append(newOptionEl);
    }

    $( "#startDate_" + newIndex ).datepicker({ dateFormat: 'dd.mm.yy' } );
    $( "#startDate_" + newIndex ).change(dateWasChanged);
    $( "#endDate_" + newIndex ).datepicker({ dateFormat: 'dd.mm.yy' } );
    $( "#endDate_" + newIndex ).change(dateWasChanged);
    $( "#annualLeaveType_" + newIndex ).change(validateInputs);

    $( "#createAnnualLeaveRequestButton" ).click(validateInputsAndSendToDB);
}

function setTempActiveAnnulLeave() {
    tempActiveAnnulLeave = [];
    for (var i = 0; i < annualLeaves.length; i++) {
        tempActiveAnnulLeave.push(annualLeaves[i]);
    }
}

function dateWasChanged() {
    var index = $(this).attr('blockIndex');
    var newValue = $(this).val();
    if (newValue != '' && RegExPatternConstants.DATE.test(newValue)) {
        $(this).removeClass('invalid-input');
    } else {
        $(this).addClass('invalid-input');
    }
    $('#startDate_' + index).attr('hasNewDate', 'true');
    $('#endDate_' + index).attr('hasNewDate', 'true');
    validateInputs(index);
}

function validateInputs(blockIndex) {
    var index = $(this).attr('blockIndex');
    if (index == undefined) {
        index = blockIndex;
    }

    var startDate = $('#startDate_' + index).val();
    var endDate = $('#endDate_' + index).val();
    if (startDate != undefined && RegExPatternConstants.DATE.test(startDate) &&
        endDate != undefined && RegExPatternConstants.DATE.test(endDate)) {
        if ($('#startDate_' + index).attr('hasNewDate') == 'true') {
            if (!DateCalculator.isStartDateBeforeOrEqualEndDate(startDate, endDate, '.')) {
                var msg = 'Крайната дата не може да е преди началната !';
                $('#startDate_' + index).addClass('invalid-input');
                $('#endDate_' + index).addClass('invalid-input');
                negativeAlertBox(null, msg, null, null);
                return;
            }
            getWorkingDaysByDates(startDate, endDate, index);
        }
    } else {
        return;
    }

    validateAndAddNewInputs(index);
}

function getWorkingDaysByDates(startDate, endDate, index) {
    var dateInterval = {};
    dateInterval.startDate = startDate;
    dateInterval.endDate = endDate;
    waitForResponse = true;

    $.ajax({
        type: 'POST',
        url: '/all/getWorkingDaysInDateInterval',
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
        success: function (result) {
            if (isNaN(parseInt(result))) {
                $('#inputNumberOfAnnualLeave_' + index).val('');
            } else {
                $('#inputNumberOfAnnualLeave_' + index).val(result);
            }

            $('#startDate_' + index).attr('hasNewDate', 'false');
            $('#endDate_' + index).attr('hasNewDate', 'false');
            waitForResponse = false;
            validateAndAddNewInputs(index);
        }
    });
}

function validateAndAddNewInputs(index) {
    if (waitForResponse) {
        return;
    }

    var annualLeaveId = $('#annualLeaveType_' + index).val();
    var oldAnnualLeaveId = $('#annualLeaveType_' + index).attr('oldAnnualLeaveId');
    if (oldAnnualLeaveId != undefined && oldAnnualLeaveId != 0) {
        for (var i = 0; i < annualLeaves.length; i++) {
            var annualLeave = annualLeaves[i];
            if (annualLeave.id == oldAnnualLeaveId) {
                tempActiveAnnulLeave.push(annualLeave);
                break;
            }
        }
    }

    if (annualLeaveId == 0 || annualLeaves.length == 0) {
        $('#annualLeaveType_' + index).attr('oldAnnualLeaveId', '0');
        return;
    }

    var startDate = $('#startDate_' + index).val();
    var endDate = $('#endDate_' + index).val();
    if (!DateCalculator.isStartDateBeforeOrEqualEndDate(startDate, endDate, '.')) {
        var msg = 'Крайната дата не може да е преди началната !';
        $('#startDate_' + index).addClass('invalid-input');
        $('#endDate_' + index).addClass('invalid-input');
        negativeAlertBox(null, msg, null, null);
        return;
    }

    for (var i = 0; i < annualLeaves.length; i++) {
        var annualLeave = annualLeaves[i];
        if (annualLeave.id == annualLeaveId) {
            var numbOfDaysInRequest = $('#inputNumberOfAnnualLeave_' + index).val();
            numbOfDaysInRequest = parseInt(numbOfDaysInRequest);
            if (isNaN(numbOfDaysInRequest)) {
                return;
            } else if (numbOfDaysInRequest == 0) {
                var msg = 'Въведените дати не са валидни !';
                $('#startDate_' + index).addClass('invalid-input');
                $('#endDate_' + index).addClass('invalid-input');
                negativeAlertBox(null, msg, null, null);
                return;
            } else if (numbOfDaysInRequest > annualLeave.numberOfAnnualLeave) {
                var msg = 'Нямаш толкова дни от тази отпуска !';
                $('#startDate_' + index).addClass('invalid-input');
                $('#endDate_' + index).addClass('invalid-input');
                negativeAlertBox(null, msg, null, null);
                return;
            }

            $('#startDate_' + index).removeClass('invalid-input');
            $('#endDate_' + index).removeClass('invalid-input');
            break;
        }
    }

    if (annualLeaveId != 0) {
        for (var i = 0; i < tempActiveAnnulLeave.length; i++) {
            var currentActiveAnnulLeave = tempActiveAnnulLeave[i];
            if (currentActiveAnnulLeave.id == annualLeaveId) {
                tempActiveAnnulLeave.splice(i, 1);
                $('#annualLeaveType_' + index).attr('oldAnnualLeaveId', annualLeaveId);
                break;
            }
        }
    }

    var newIndex = $('#requestDateIntervals').children().length;
    var newStartDate = DateCalculator.getNextDayAsStringFromDateAsString(endDate);
    var newInput = $('<div class="annualLeaveInputsBlock" blockIndex="'+ newIndex + '">' +
                        '<select class="annualLeaveRequestInputs invalid-input dropdown-validate-noLabel" id="annualLeaveType_'+ newIndex + '" blockIndex="'+ newIndex + '">' +
                            '<option selected value="0">Избери отпуск</option>' +
                        '</select>' +
                        '<input type="text" disabled class="annualLeaveRequestInputs date-validate" placeholder="dd.MM.yyyy" id="startDate_'+ newIndex + '" blockIndex="'+ newIndex + '" value="' + newStartDate + '"/>' +
                        '<input type="text" class="annualLeaveRequestInputs date-validate invalid-input" placeholder="dd.MM.yyyy" id="endDate_'+ newIndex + '" blockIndex="'+ newIndex + '"/>' +
                        '<input type="text" disabled value="0" class="annualLeaveRequestInputs" id="inputNumberOfAnnualLeave_'+ newIndex + '" blockIndex="'+ newIndex + '"/>' +
                    '</div>');
    $('#requestDateIntervals').append(newInput);

    for (var i = 0; i < tempActiveAnnulLeave.length; i++) {
        var currentAnnualLeave = tempActiveAnnulLeave[i];
        var newOptionEl = $('<option annualLeaveTypeId="' + currentAnnualLeave.annualLeaveTypeId + '" value="' + currentAnnualLeave.id + '">' + currentAnnualLeave.annualLeaveType + '</option>');
        $('#annualLeaveType_' + newIndex).append(newOptionEl);
    }

    InputValidatorContainer.addNewInput($('#annualLeaveType_' + newIndex));
    $( "#startDate_" + newIndex ).datepicker({ dateFormat: 'dd.mm.yy' } );
    $( "#startDate_" + newIndex ).change(dateWasChanged);
    $( "#endDate_" + newIndex ).datepicker({ dateFormat: 'dd.mm.yy' } );
    $( "#endDate_" + newIndex ).change(dateWasChanged);
    $( "#annualLeaveType_" + newIndex ).change(validateInputs);
}

function validateInputsAndSendToDB() {
    var numbOfInputBoxes = $('#requestDateIntervals').children().length;
    var request = {};
    var annualLeaveDateIntervals = [];
    for (var i = 0; i < numbOfInputBoxes; i++) {
        var msg = '';
        var startDate = $('#startDate_' + i).val();
        if (startDate == '' || !RegExPatternConstants.DATE.test(startDate)) {
            resetAllInputsInRow(i);
            continue;
        }

        var endDate = $('#endDate_' + i).val();
        if (endDate == '' || !RegExPatternConstants.DATE.test(endDate)) {
            resetAllInputsInRow(i);
            continue;
        }

        if (!DateCalculator.isStartDateBeforeOrEqualEndDate(startDate, endDate, '.')) {
            resetAllInputsInRow(i);
            continue;
        }

        var annualLeaveTypeId = $('#annualLeaveType_' + i).find(':selected').attr('annualLeaveTypeId');
        if (annualLeaveTypeId == 0) {
            resetAllInputsInRow(i);
            continue;
        }

        var numbOfDays = $('#inputNumberOfAnnualLeave_' + i).val();
        if (numbOfDays == 0) {
            resetAllInputsInRow(i);
            continue;
        }

        var annualLeaveDateInterval = {};
        annualLeaveDateInterval.startDate = startDate;
        annualLeaveDateInterval.endDate = endDate;
        annualLeaveDateInterval.annualLeaveTypeId = annualLeaveTypeId;
        annualLeaveDateIntervals.push(annualLeaveDateInterval);
    }

    if (annualLeaveDateIntervals.length == 0) {
        var msg = 'Нямаш нито един валиден интервал !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    request.annualLeaveDateIntervals = annualLeaveDateIntervals;
    //set animation to button and disable it
    var buttonEl = $('#createAnnualLeaveRequestButton');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    //send to DB
    $.ajax({
        type: 'POST',
        url: '/user/annualLeaveRequest/create',
        data: JSON.stringify(request),
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
            } else {
                var msg = 'Заяваката не е приета !';
                negativeAlertBox(null, msg, null, null);
            }
        },
        success: function () {
            stopAnimationAndEnableButton(buttonEl);
            var msg = 'Заяваката е приета !';
            //positiveAlertBox(null, msg, null, null);
            alert(msg);
            window.location.href = '/';
        }
    });
}

function resetAllInputsInRow(index) {
    $('#startDate_' + index).val('');
    $('#endDate_' + index).val('');
    $('#annualLeaveType_' + index).val(0);
    $('#annualLeaveType_' + index).addClass('invalid-input');
    $('#inputNumberOfAnnualLeave_' + index).val(0);
}