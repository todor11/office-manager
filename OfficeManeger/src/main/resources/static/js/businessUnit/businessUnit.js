
var freeEmployees = null;
var tempEmployeeList = null;

function init() {
    if (document.getElementById('addBusinessUnitButton') != null) {
        $('.delete-business-unit-button').click(deleteBusinessUnit);
    }
    if (document.getElementById('bu-main-button') != null) {
        var command = $('#bu-main-button').attr('data');
        if (command == 'saveNew') {
            $('#bu-main-button').click(saveOrUpdateNewBusinessUnit);
            $('#add-new-employee-button').click(addNewEmployeeFields);
            getFreeEmployeesNames();
            $('.bu-select').on('change', eventSelectChanged);
            $('#inputUnitName').on('change', validateUnitName);
        } else if (command == 'update') {
            //getFreeEmployeesNames(true);
            //addNewEmployeeButton();
            //updateMainUnitValue();
            //$('.bu-select').on('change', eventSelectChanged);
            //$('#inputUnitName').on('change', validateUnitName);
            //$('#inputUnitName').on('keyup', changePageName);
            //$('#bu-main-button').click(saveOrUpdateNewBusinessUnit);
        }
    }
}

//event handler business unit delete
function deleteBusinessUnit() {
    var button = $(this);
    var businessUnitId = $(button).attr('businessUnitID');
    var businessUnitName = $(button).attr('businessUnitName');
    var businessUnitDomElement = document.getElementById(businessUnitId);
    var question = 'Сигурен ли си, че искаш да изтриеш ' + businessUnitName + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteBusinessUnitFunction , businessUnitDomElement);

}

function confirmedDeleteBusinessUnitFunction(businessUnitDomElement) {
    //delete MySQL
    var businessUnitId = $(businessUnitDomElement).attr('id');
    $.ajax({
        type: 'DELETE',
        url: '/businessUnit/delete/' + businessUnitId,
        error: function () {
            var msg = 'Структорното звено не можа да се изтрие';
            negativeAlertBox(null, msg, null, null);
        },
        success: function () {
            //remove from DOM
            businessUnitDomElement.remove();
        }
    });
}

function saveOrUpdateNewBusinessUnit() {
    var businessUnit = {};
    //validateUnitName
    var newUnitName = $('#inputUnitName').val();
    if (newUnitName == '') {
        var msg = 'Името на структорното звено не може да е празно поле !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    businessUnit.unitName = newUnitName;

    //main unit
    businessUnit.mainUnit = $('#bu-main-unit').val();
    if (businessUnit.mainUnit == 'no') {
        businessUnit.mainUnit = '';
    }

    //validate boss position and names
    var bossPosition = $('#businessUnitNewBossPosition').val();
    var bossId = $('#businessUnitNewBossNameAndID').val();
    if (bossId != '-1' && bossPosition == '') {
        var msg = 'Не си създал длъжност на началника, а си сложил началник! За да има началник - трябва да има и длъжност !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    businessUnit.bossPosition = bossPosition;
    businessUnit.bossId = bossId;

    //validate employees
    var employeesPositions = [];
    var employeesIds = [];

    var emplInputs = $('.bu-empl-inputs');
    var emplSelect = $('.bu-empl-select');

    for (var i = 0; i < emplInputs.length; i++) {
        var positionName = $(emplInputs[i]).val();
        var emplId = $(emplSelect[i]).val();
        if (positionName == '' && emplId != -1) {
            var emplNames = emplSelect[i].selectedOptions[0].text;
            var msg = 'Не си въвел длъжност за ' + emplNames + '. Има ли служител - трябва да има и длъжност !';
            negativeAlertBox(null, msg, null, null);
            return;
        } else if (positionName == '') {
            continue;
        }

        employeesPositions.push(positionName);
        employeesIds.push(emplId);
    }

    businessUnit.employeesPositions = employeesPositions;
    businessUnit.employeesIds = employeesIds;

    var buttonEvent = $('#bu-main-button').attr('data');
    //send
    if (buttonEvent == 'saveNew') {
        sendToDatabaseNewBusinessUnit(businessUnit);
    } else if(buttonEvent == 'update') {
        var unitID = $('#bu-main-button').attr('unitID');
        businessUnit.id = unitID;
        sendToDatabaseUpdatedBusinessUnit(businessUnit, unitID);
    }

}

function addNewEmployeeFields() {
    // check last
    var buttonContainer = $('#add-new-employee-button').parent();
    var lastEmployeePosition = $(buttonContainer).find('input');
    var lastInputValue = lastEmployeePosition.val();
    if (lastInputValue == null || lastInputValue == '') {
        var msg = 'Не си въвел длъжност, нямащ нужда от ново поле !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var container = $('#ub-employee-box');
    var button = $('#add-new-employee-button');
    //button.remove();
    var newContainer = $('<div class="row bu-employee-row">' +
                            '<label class="form-control-label bu-short-label">Длъжност :</label>' +
                            '<input type="text" class="form-control form-control-danger bu-short bu-empl-inputs"/>' +
                            '<label class="form-control-label bu-short-label">Служител :</label>' +
                        '</div>');

    var select = $('<select class="form-control form-control-danger bu-short bu-select bu-empl-select" name="businessUnitNewBossPosition" oldData="-1"></select>');
    $(select).on('change', eventSelectChanged);
    setOptionsToSelect(select);
    $(newContainer).append(select);
    //$(newContainer).append(button);
    $(newContainer).insertBefore('#add-new-employee-button');
    //$('#add-new-employee-button').click(addNewEmployeeFields);
}

function getFreeEmployeesNames(isEditPage) {
    $.ajax({
        type: 'GET',
        url: '/employees/getNoPositionEmployees',
        data: 'json',
        success: function (employees) {
            freeEmployees = employees;
            tempEmployeeList = [];
            for (var i = 0; i < freeEmployees.length; i++) {
                var obj = freeEmployees[i];
                tempEmployeeList.push(obj);
            }
            if (isEditPage) {
                addOldData();
            } else {
                updateSelectFields();
            }
        }
    });
}

function addOldData() {
    var selectArr = $('.bu-select');
    for (var i = 0; i < selectArr.length; i++) {
        var selectElement = selectArr[i];
        var emplId = $(selectElement).attr('oldData');
        var emplNames = $(selectElement).attr('names');
        var empl = {};
        empl.id = emplId;
        empl.fullName = emplNames;
        freeEmployees.push(empl);
    }

    updateSelectFields();
}

function eventSelectChanged() {
    $(this).attr('isSelected', 'yes');
    updateSelectFields();
}

function updateSelectFields() {
    var selectArr = $('.bu-select');
    var changedEl = null;
    for (var i = 0; i < selectArr.length; i++) {
        var selectElement = selectArr[i];
        if ($(selectElement).attr('isSelected') == 'yes') {
            changedEl = selectElement;
            var oldData = $(selectElement).attr('oldData');
            var newData = $(selectElement).val();
            if (oldData != -1) {
                for (var j = 0; j < freeEmployees.length; j++) {
                    var obj = freeEmployees[j];
                    if (obj.id == oldData) {
                        tempEmployeeList.push(obj);
                        break;
                    }
                }
            }

            if (newData != -1) {
                for (var j = 0; j < tempEmployeeList.length; j++) {
                    var obj = tempEmployeeList[j];
                    if (obj.id == newData) {
                        tempEmployeeList.splice(j, 1);
                        break;
                    }
                }
            }

            $(selectElement).attr('oldData', newData);
        }
        $(selectElement).attr('isSelected', 'no');
        $(selectElement).val(newData);
    }
    for (var i = 0; i < selectArr.length; i++) {
        var selectElement = selectArr[i];
        if (selectElement != changedEl) {
            setOptionsToSelect(selectElement);
        }
        $(selectElement).attr('isSelected', 'no');
    }
}

function setOptionsToSelect(selectElement) {
    var oldData = $(selectElement).attr('oldData');
    $(selectElement).empty();
    var startOption = $('<option value="-1">Не сега</option>');
    $(selectElement).append(startOption);
    for (var i = 0; i < tempEmployeeList.length; i++) {
        var names = tempEmployeeList[i].fullName;
        var option = $('<option></option>').text(names);
        var value = tempEmployeeList[i].id;
        $(option).attr('value', value);
        $(selectElement).append(option);
    }
    if (oldData != -1) {
        for (var j = 0; j < freeEmployees.length; j++) {
            var obj = freeEmployees[j];
            if (obj.id == oldData) {
                var option = $('<option></option>').text(obj.fullName);
                var value = obj.id;
                $(option).attr('value', value);
                $(selectElement).append(option);
                break;
            }
        }
    }
    $(selectElement).val(oldData);
}

function validateUnitName() {
    var newUnitName = $('#inputUnitName').val();
    if (newUnitName == '') {
        return;
    }
    var valueAttr = $('#inputUnitName').attr('value');
    if (valueAttr != undefined && newUnitName == valueAttr) {
        return;
    }

    $.ajax({
        type: 'GET',
        url: '/businessUnit/checkName/' + newUnitName,
        data: 'json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                    return false;
                }
            }

            var msg = 'Структорното звено не можа да се запамети';
            negativeAlertBox(null, msg, null, null);
            return false;
        },
        success: function () {
            return true;
        }
    });
}

function sendToDatabaseNewBusinessUnit(businessUnit) {
    $.ajax({
        type: 'POST',
        url: '/businessUnit/create',
        data: JSON.stringify(businessUnit),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                }
            }
        },
        success: function () {
            var message = 'Успешно добави ' + businessUnit.unitName;
            positiveAlertBox(null, message, null, null);
            setTimeout(function(){ window.location.href = "/businessUnit"; }, 1500);
        }
    });
}

function addNewEmployeeButton() {
    var newEmployeeButton = $('<div id="add-new-employee-button">' +
                                    '<a class="btn btn-info">Добави ново поле</a>' +
                                '</div>');
    $('#ub-employee-box div:last-child').append(newEmployeeButton);

    $('#add-new-employee-button').click(addNewEmployeeFields);
}

function changePageName() {
    var newName = $('#inputUnitName').val();
    $('#bu-main-label').text(newName + ' :');
}


function sendToDatabaseUpdatedBusinessUnit(businessUnit, unitID) {
    $.ajax({
        type: 'PUT',
        url: '/businessUnit/update/' + unitID,
        data: JSON.stringify(businessUnit),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                }
            }
        },
        success: function () {
            var message = 'Успешно редактира ' + businessUnit.unitName;
            positiveAlertBox(null, message, null, null);
            setTimeout(function(){ window.location.href = "/businessUnit"; }, 1500);
        }
    });
}

function updateMainUnitValue() {
    $('#bu-main-unit').val($('#bu-main-unit').attr('value'));
    var currentUnitName = $('#inputUnitName').val();
    var units = $('#bu-main-unit').children();
    for (var i = 0; i < units.length; i++) {
        var option = units[i];
        if (currentUnitName == $(option).val()) {
            option.remove();
        }
    }
}
