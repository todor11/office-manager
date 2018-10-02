var employeesMapObj= {};

function init() {
    getEmployeesFromDB();
    getAnnualLeaveTypesFromDB();
    $('#addAnnualLeaveButton').click(sendToDb);
}

function getEmployeesFromDB() {
    $.ajax({
        type: 'GET',
        url: '/getAllActiveEmployees',
        data: 'json',
        success: function (employees) {
            $.each(employees, function (i, employee) {
                var employeeId = employee.id;
                var employeeName = employee.name;
                var domEl = $('<option value="' + employeeId + '">' + employeeName + '</option>');
                $('#employeeId').append(domEl);
                employeesMapObj[employeeId] = employeeName;
            });
        }
    });
}

function getAnnualLeaveTypesFromDB() {
    $.ajax({
        type: 'GET',
        url: '/getAllAnnualLeaveTypes',
        data: 'json',
        success: function (annualLeaveTypes) {
            $.each(annualLeaveTypes, function (i, annualLeaveType) {
                var domEl = $('<option value="' + annualLeaveType.id + '">' + annualLeaveType.fullName + '</option>');
                $('#annualLeaveTypeId').append(domEl);
            });
        }
    });
}

function sendToDb() {
    //set animation to button and disable it
    var buttonEl = $('#addAnnualLeaveButton');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    var newAnnualLeave = {};
    var annualLeaveTypeId = $('#annualLeaveTypeId').val();
    if (!(annualLeaveTypeId > 0)) {
        EPPZScrollTo.scrollVerticalToElementById('annualLeaveTypeId');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Не е въведен тип на отпуска !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    newAnnualLeave.annualLeaveTypeId = annualLeaveTypeId;

    var numberOfAnnualLeave = $('#inputNumberOfAnnualLeave').val();
    if (!(numberOfAnnualLeave >= 0)) {
        EPPZScrollTo.scrollVerticalToElementById('inputNumberOfAnnualLeave');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Броя дни отпуск не е валиден !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    newAnnualLeave.numberOfAnnualLeave = numberOfAnnualLeave;

    var employeeId = $('#employeeId').val();
    if (!(employeeId > 0)) {
        EPPZScrollTo.scrollVerticalToElementById('employeeId');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Не е въведен служител !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    newAnnualLeave.employeeId = employeeId;

    $.ajax({
        type: 'POST',
        url: '/annualLeave/create',
        data: JSON.stringify(newAnnualLeave),
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
            stopAnimationAndEnableButton(buttonEl);
            var employeeId = $('#employeeId').val();
            var employeeNames = employeesMapObj[employeeId];
            var message = 'Успешно добави отпуска на ' + employeeNames;
            positiveAlertBox(null, message, null, null);
            $('#employeeId').val(0);
            $('#inputNumberOfAnnualLeave').val(0);
            $('#annualLeaveTypeId').val(0);
        }
    });
}