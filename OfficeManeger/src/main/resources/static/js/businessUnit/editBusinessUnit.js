var unitId;
var freeEmployees = [];

function init() {
    var href = window.location.href;
    unitId = href.split('/')[href.split('/').length - 1];
    userGetAllBusinessUnitsFromDB();
}

function userGetEditBusinessUnitFromDB() {
    $.ajax({
        type: 'GET',
        url: '/businessUnit/getEditBusinessUnit/' + unitId,
        data: 'json',
        success: function (businessUnit) {
            addBusinessUnitToDom(businessUnit);
            getNoPositionEmployees();
        }
    });
}

function userGetAllBusinessUnitsFromDB() {
    $.ajax({
        type: 'GET',
        url: '/businessUnit/getAllBusinessUnits',
        data: 'json',
        success: function (businessUnits) {
            addAllBusinessUnitsToDom(businessUnits);
            userGetEditBusinessUnitFromDB();
        }
    });
}

function getNoPositionEmployees() {
    $.ajax({
        type: 'GET',
        url: '/employees/getNoPositionEmployees',
        data: 'json',
        success: function (employees) {
            freeEmployees = employees;
            //addOldData();
            addEmployeesToDropdown();
        }
    });
}

function addEmployeesToDropdown() {
    var dropdownBoxes = $('.employee-dropdown');
    for (var i = 0; i < freeEmployees.length; i++) {
        var employee = freeEmployees[i];
        for (var j = 0; j < dropdownBoxes.length; j++) {
            var domBox = dropdownBoxes[j];
            var domEl = $('<option value="' + employee.id + '">' + employee.fullName + '</option>');
            $(domBox).append(domEl);
        }
    }
}

function addAllBusinessUnitsToDom(businessUnits) {
    for (var i = 0; i < businessUnits.length; i++) {
        var businessUnit = businessUnits[i];
        var domEl = $('<option value="' + businessUnit.id + '">' + businessUnit.unitName + '</option>');
        $('#bu-main-unit').append(domEl);
    }
}

function addBusinessUnitToDom(businessUnit) {
    var unitName = businessUnit.unitName;
    $('#bu-main-label').text(unitName + ' :');
    $('#inputUnitName').val(unitName);
    
    var mainUnitId = businessUnit.mainUnitId;
    $('#bu-main-unit').val(mainUnitId);
    //remove my unit from main units
    var allUnitsEl = $('#bu-main-unit option');
    for (var i = 0; i < allUnitsEl.length; i++) {
        var unitOption = allUnitsEl[i];
        var value = $(unitOption).val();
        if (value == businessUnit.id) {
            unitOption.remove();
            break;
        }
    }

    var bossPositionName = '';
    var bossPositionId = 0;
    if (businessUnit.bossPosition != null) {
        bossPositionName = businessUnit.bossPosition.name;
        bossPositionId = businessUnit.bossPosition.id;
    }
    $('#businessUnitNewBossPosition').val(bossPositionName);
    $('#businessUnitNewBossPosition').attr('positionId', bossPositionId);

    var bossId = 0;
    var boss = businessUnit.bossPosition.employee;
    var bossNames = '';
    if (boss != null) {
        bossId = boss.id;
        bossNames = boss.fullName;
        var bossDomEl = $('<option value="' + bossId + '">' + bossNames + '</option>');
        $('#businessUnitNewBossNameAndID').append(bossDomEl);
    }
    $('#businessUnitNewBossNameAndID').attr('oldEmployeeId', bossId);
    $('#businessUnitNewBossNameAndID').attr('oldEmployeeNames', bossNames);
    $('#businessUnitNewBossNameAndID').attr('positionId', bossPositionId);
    $('#businessUnitNewBossNameAndID').val(bossId);

    var positions = businessUnit.positions;

    for (var i = 0; i < positions.length; i++) {
        var position = positions[i];
        var positionId = position.id;
        var positionName = position.name;
        var employee = position.employee;
        var hasEmployee = false;
        var emplDom = '';
        var employeeId;
        var employeeNames;
        if (employee != null) {
            hasEmployee = true;
            employeeId = employee.id;
            employeeNames = employee.fullName;
            emplDom = '<option selected value="' + employeeId + '">' + employeeNames + '</option>';
        }

        var positionEmployeeBoxText = '<div class="row bu-employee-row" id="positionBox_' + positionId + '">' +
            '<label class="form-control-label bu-short-label">Длъжност :</label>' +
            '<input type="text" positionId="' + positionId + '" id="inputPositionId_' + positionId + '" class="form-control form-control-danger bu-short bu-empl-inputs" value="' + positionName + '"/>' +
            '<label class="form-control-label bu-short-label">Служител :</label>' +
            '<select id="employeePositionId_' + positionId + '" positionId="' + positionId + '" class="form-control form-control-danger bu-short bu-select bu-empl-select employee-dropdown" name="businessUnitPosition">' +
            '<option ' + (hasEmployee ? '' : 'selected') + ' value="0">Свободно</option>' +
            emplDom +
            '</select>' +
            '<div class="delete-position-btn button-cont" positionId="' + positionId + '">' +
            '<a class="btn btn-danger">Изтрий</a>' +
            '</div>' +
            '</div>';
        var positionEmployeeBox = $(positionEmployeeBoxText);
        $('#ub-employee-box').append(positionEmployeeBox);

        if (hasEmployee) {
            $('#employeePositionId_' + positionId).attr('oldEmployeeId', employeeId);
            $('#employeePositionId_' + positionId).attr('oldEmployeeNames', employeeNames);
        }
    }

    $('#ub-employee-box').append($('<div id="add-new-employee-button" class="button-cont" businessUnitId="' + businessUnit.id + '">' +
        '<a class="btn btn-info">Добави ново поле</a>' +
        '</div>'));
    $('#add-new-employee-button').click(addNewEmployeeFields);
    $('#businessUnitNewBossPosition').on('change', updatePositionName);
    $('#businessUnitNewBossNameAndID').on('change', updatePositionEmployee);
    $('.delete-position-btn').click(deletePosition);
    $('.bu-empl-inputs').on('change', updatePositionName);
    $('.bu-empl-select').on('change', updatePositionEmployee);
    $('#bu-main-unit').on('change', updateMainUnit);
    $('#inputUnitName').on('change', updateBusinessUnitName);
}

function addNewEmployeeFields() {
    //set animation to button and disable it
    var buttonEl = $('#add-new-employee-button');
    var butt = $('#add-new-employee-button').find('a');
    if ($(butt).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(butt);

    // check last
    var buttonContainer = $('#add-new-employee-button').parent();
    var lastEmployeePosition = $(buttonContainer).find('input');
    var lastInputValue = lastEmployeePosition.val();
    if (lastInputValue == null || lastInputValue == '') {
        EPPZScrollTo.scrollVerticalToElementById('cco-companySelect');
        stopAnimationAndEnableButton(butt);
        var msg = 'Не си въвел длъжност, нямащ нужда от ново поле !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var positionAddNewModel = {};
    positionAddNewModel.businessUnitId = $(buttonEl).attr('businessUnitId');

    //send request to db
    $.ajax({
        type: 'POST',
        url: '/positions/addNewPosition',
        data: JSON.stringify(positionAddNewModel),
        contentType: 'application/json',
        error: function (response) {
            stopAnimationAndEnableButton(butt);
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
        success: function (savedPosition) {
            stopAnimationAndEnableButton(butt);
            var positionId = savedPosition.id;
            var newContainer = $('<div class="row bu-employee-row" id="positionBox_' + positionId + '">' +
                '<label class="form-control-label bu-short-label">Длъжност :</label>' +
                '<input type="text" class="form-control form-control-danger bu-short bu-empl-inputs"  positionId="' + positionId + '"/>' +
                '<label class="form-control-label bu-short-label">Служител :</label>' +
                '<select id="employeePositionId_' + positionId + '"  class="form-control form-control-danger bu-short bu-select bu-empl-select employee-dropdown" positionId="' + positionId + '" name="businessUnitNewBossPosition">' +
                '<option selected value="0">Свободно</option>' +
                '</select>' +
                '<div class="delete-position-btn button-cont" positionId="' + positionId + '">' +
                '<a class="btn btn-danger">Изтрий</a>' +
                '</div>' +
                '</div>');

            $(newContainer).insertBefore('#add-new-employee-button');
            var deleteBttn = $(newContainer).find('.delete-position-btn');
            $(deleteBttn).click(deletePosition);
            var nameInput = $(newContainer).find('.bu-empl-inputs');
            $(nameInput).on('change', updatePositionName);
            var emplInput = $(newContainer).find('.bu-empl-select');
            $(emplInput).on('change', updatePositionEmployee);

            //add free employee to select
            for (var i = 0; i < freeEmployees.length; i++) {
                var employee = freeEmployees[i];
                var domEl = $('<option value="' + employee.id + '">' + employee.fullName + '</option>');
                $('#employeePositionId_' + positionId).append(domEl);
            }
        }
    });
}

function deletePosition() {
    var positionId = $(this).attr('positionId');
    var question = 'Сигурен ли си, че искаш да изтриеш ' + '' + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeletePosition , positionId);
}

function confirmedDeletePosition(positionId) {
    $.ajax({
        type: 'DELETE',
        url: '/positions/delete/' + positionId,
        success: function () {
            $('#positionBox_' + positionId).remove();
        }
    })
}

function updatePositionName() {
    var positionId = $(this).attr('positionId');
    var newPositionName = $(this).val();
    if (newPositionName == '') {
        var msg = 'Името на длъжността не може да е празно поле !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    $.ajax({
        type: 'PUT',
        url: '/positions/updateName/' + positionId + '/' + newPositionName,
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
        success: function () {

        }
    });
}

function updatePositionEmployee() {
    var positionId = $(this).attr('positionId');
    var newEmployeeId = $(this).val();
    var newEmployeeNames = $(this).find('option:selected').text();
    var selectDomElement = $(this);
    $.ajax({
        type: 'PUT',
        url: '/positions/updateEmployee/' + positionId + '/' + newEmployeeId,
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
            restoreOldData(selectDomElement);
        },
        success: function () {
            var oldEmployeeId = $(selectDomElement).attr('oldEmployeeId');
            if (oldEmployeeId == undefined || oldEmployeeId == 0) {
                $(selectDomElement).attr('oldEmployeeId', newEmployeeId);
                $(selectDomElement).attr('oldEmployeeNames', newEmployeeNames);
                //change other select
                var allSelects = $('.employee-dropdown');
                var currentSelectId = $(selectDomElement).attr('id');
                for (var i = 0; i < allSelects.length; i++) {
                    var select = allSelects[i];

                    if (newEmployeeId != 0 && $(select).attr('id') != currentSelectId) {
                        var allOptions = $(select).children();
                        for (var j = 0; j < allOptions.length; j++) {
                            var option = allOptions[j];
                            if ($(option).val() == newEmployeeId) {
                                $(option).remove();
                                break;
                            }
                        }
                        for (var j = 0; j < freeEmployees.length; j++) {
                            var freeEmployee = freeEmployees[j];
                            if (freeEmployee.id == newEmployeeId) {
                                freeEmployees.splice(j, 1);
                                break;
                            }
                        }
                    }
                }
            } else {
                var oldEmployeeNames = $(selectDomElement).attr('oldEmployeeNames');
                var oldEmployee = {};
                oldEmployee.id = oldEmployeeId;
                oldEmployee.fullName = oldEmployeeNames;
                freeEmployees.push(oldEmployee);
                $(selectDomElement).attr('oldEmployeeId', newEmployeeId);
                $(selectDomElement).attr('oldEmployeeNames', newEmployeeNames);

                //change other select
                var allSelects = $('.employee-dropdown');
                var currentSelectId = $(selectDomElement).attr('id');
                for (var i = 0; i < allSelects.length; i++) {
                    var select = allSelects[i];
                    if ($(select).attr('id') != currentSelectId) {
                        var domEl = $('<option value="' + oldEmployeeId + '">' + oldEmployeeNames + '</option>');
                        $(select).append(domEl);
                    }

                    if (newEmployeeId != 0 && $(select).attr('id') != currentSelectId) {
                        var allOptions = $(select).children();
                        for (var j = 0; j < allOptions.length; j++) {
                            var option = allOptions[j];
                            if ($(option).val() == newEmployeeId) {
                                $(option).remove();
                                break;
                            }
                        }
                        for (var j = 0; j < freeEmployees.length; j++) {
                            var freeEmployee = freeEmployees[j];
                            if (freeEmployee.id == newEmployeeId) {
                                freeEmployees.splice(j, 1);
                                break;
                            }
                        }
                    }
                }
            }
        }
    });
}

function restoreOldData(oldSelectDomEl) {
    var oldValue = $(oldSelectDomEl).attr('oldEmployeeId');
    if (oldValue == undefined) {
        oldValue = 0;
    }
    $(oldSelectDomEl).val(oldValue);
}

function updateMainUnit() {
    var mainUnitId = $('#bu-main-unit').val();
    if (mainUnitId == unitId) {
        var msg = 'Старшето звено не може да е даденото звено !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    $.ajax({
        type: 'PUT',
        url: '/businessUnit/updateMainUnit/' + unitId + '/' + mainUnitId,
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
        success: function () {
        }
    });
}

function updateBusinessUnitName() {
    var newUnitName = $('#inputUnitName').val();
    if (newUnitName == '') {
        var msg = 'Името не може да е празно поле !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    $.ajax({
        type: 'PUT',
        url: '/businessUnit/updateUnitName/' + unitId + '/' + newUnitName,
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
        success: function () {
        }
    });
}