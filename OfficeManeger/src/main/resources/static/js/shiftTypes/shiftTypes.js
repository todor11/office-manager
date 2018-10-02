
function init() {
    if (document.getElementById('addShiftTypeButton') != null) {
        $('#addShiftTypeButton').click(showAddShiftTypesInputs);
        $('#shiftTypeBtnSave').click(saveShiftType);
        $('#shiftTypeBtnCancel').click(hideAddShiftTypesInputs);
        $('.shiftType-container').on('keyup', keyUpFunctionShiftType);
        setBooleanInputs($('.boolean-input-owner'));
        //$('.timepicker-input').on('change', validateTimeAndSaveShiftTypeIfSet);
        //set time picker
        var elements = $('.timepicker-input');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            setTimePickerToElement(element, validateTimeAndSaveShiftTypeIfSet);
        }
    }
}

//Show Add shiftTypes
function showAddShiftTypesInputs() {
    $('#addShiftTypeInputs').show();
}

//Hide Add shiftTypes
function hideAddShiftTypesInputs() {
    $('#addShiftTypeInputs').hide();
}

//Add shiftType to DOM
function addShiftTypeDOM(shiftType) {
    var id = shiftType.id;
    var fullName = shiftType.fullName;
    var shortName = shiftType.shortName;
    var startTime = shiftType.startTime;
    var endTime = shiftType.endTime;
    var secondStartTime = shiftType.secondStartTime;
    var secondEndTime = shiftType.secondEndTime;
    var shiftDuration = shiftType.shiftDuration;
    var breakDuration = shiftType.breakDuration;
    var isRegularShift = shiftType.isRegularShift;
    var isEndOnNextDay = shiftType.isEndOnNextDay;

    var canBeOnWorkDay = shiftType.canBeOnWorkDay;
    var canBeOnRestDay = shiftType.canBeOnRestDay;
    var canBeOnDoubleShiftRegime = shiftType.canBeOnDoubleShiftRegime;
    var canBeOnTripleShiftRegime = shiftType.canBeOnTripleShiftRegime;
    //var canBeOnFirstWorkDayAfterRestDay = shiftType.canBeOnFirstWorkDayAfterRestDay;
    //var canBeOnFirstRestDayAfterWorkDay = shiftType.canBeOnFirstRestDayAfterWorkDay;
    var canBeOnLastWorkDayBeforeRestDay = shiftType.canBeOnLastWorkDayBeforeRestDay;
    var canBeOnLastRestDayBeforeWorkDay = shiftType.canBeOnLastRestDayBeforeWorkDay;

    var newRow = $('<div class="shiftTypeFullCont" shiftType_id="' + id + '">' +
        '                    <div class="row">' +
        '                        <label class="form-control col-sm-3 shift-type-label">Пълно описание</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Кр. изп.</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Начало</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Край</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Второ начало</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Втори край</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Продълж.</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Почивка</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Ежедневна</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">Следв. ден</label>' +
        '                    </div>' +
        '                    <div class="row shiftType-container" shiftType_id="' + id + '">' +
        '                        <input type="text" class="updateFullNameClass form-control col-sm-3" value="' + fullName + '" oldData="' + fullName + '"/>' +
        '                        <input type="text" class="updateShortNameClass form-control col-sm-1" value="' + shortName + '" oldData="' + shortName + '"/>' +
        '                        <input type="text" class="updateStartTime timepicker-input form-control col-sm-1 center-text" value="' + startTime + '" oldData="' + startTime + '" id="startTime_' + startTime + '"/>' +
        '                        <input type="text" class="updateEndTime timepicker-input form-control col-sm-1 center-text" value="' + endTime + '" oldData="' + endTime + '" id="endTime_' + endTime + '"/>' +
        '                        <input type="text" class="updateSecondStartTime timepicker-input form-control col-sm-1 center-text" value="' + secondStartTime + '" oldData="' + secondStartTime + '" id="secondStartTime_' + secondStartTime + '"/>' +
        '                        <input type="text" class="updateSecondEndTime timepicker-input form-control col-sm-1 center-text" value="' + secondEndTime + '" oldData="' + secondEndTime + '" id="secondEndTime_' + secondEndTime + '"/>' +
        '                        <input type="text" class="updateShiftDuration timepicker-input form-control col-sm-1 center-text" value="' + shiftDuration + '" oldData="' + shiftDuration + '" id="shiftDuration_' + shiftDuration + '"/>' +
        '                        <input type="text" class="updateBreakDuration timepicker-input form-control col-sm-1 center-text" value="' + breakDuration + '" oldData="' + breakDuration + '" id="breakDuration_' + breakDuration + '"/>' +
        '                        <div class="updateIsRegularShift form-control col-sm-1 center-text boolean-input-owner" data="' + isRegularShift + '"><i class="fa pickable ' + (isRegularShift ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="updateIsEndOnNextDay form-control col-sm-1 center-text boolean-input-owner" data="' + isEndOnNextDay + '"><i class="fa pickable ' + (isEndOnNextDay ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                    </div>' +
        '                    <div class="row">' +
        '                        <label class="form-control col-sm-3 shift-type-label" style="visibility: hidden"></label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">работни дни</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">почивни дни</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">двусменен режим</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">трисменен режим</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">последен работен</label>' +
        '                        <label class="form-control col-sm-1 shift-type-label">последен почивен</label>' +
        '                    </div>' +
        '                    <div class="row shiftType-container" shiftType_id="' + id + '">' +
        '                        <input style="visibility: hidden" type="text" class="form-control col-sm-3" disabled/>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnWorkDay_' + id + '" data="' + canBeOnWorkDay + '"><i class="fa pickable ' + (canBeOnWorkDay ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnRestDay_' + id + '" data="' + canBeOnRestDay + '"><i class="fa pickable ' + (canBeOnRestDay ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnDoubleShiftRegime_' + id + '" data="' + canBeOnDoubleShiftRegime + '"><i class="fa pickable ' + (canBeOnDoubleShiftRegime ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnTripleShiftRegime_' + id + '" data="' + canBeOnTripleShiftRegime + '"><i class="fa pickable ' + (canBeOnTripleShiftRegime ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnLastWorkDayBeforeRestDay_' + id + '"  data="' + canBeOnLastWorkDayBeforeRestDay + '"><i class="fa pickable ' + (canBeOnLastWorkDayBeforeRestDay ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                        <div class="form-control col-sm-1 center-text boolean-input-owner" id="updateCanBeOnLastRestDayBeforeWorkDay_' + id + '"  data="' + canBeOnLastRestDayBeforeWorkDay + '"><i class="fa pickable ' + (canBeOnLastRestDayBeforeWorkDay ? 'fa-check' : 'fa-times') + '" aria-hidden="true"></i></div>' +
        '                    </div>' +
        '                    <hr>' +
        '                </div>')
        .on('keyup', keyUpFunctionShiftType);

    $('#shiftTypeList').append(newRow);

    setBooleanInput($(newRow).find('.updateIsRegularShift'));
    setBooleanInput($(newRow).find('.updateIsEndOnNextDay'));

    setBooleanInput($('#updateCanBeOnWorkDay_' + id));
    setBooleanInput($('#updateCanBeOnRestDay_' + id));
    setBooleanInput($('#updateCanBeOnDoubleShiftRegime_' + id));
    setBooleanInput($('#updateCanBeOnTripleShiftRegime_' + id));
    //setBooleanInput($('#updateCanBeOnFirstWorkDayAfterRestDay_' + id));
    //setBooleanInput($('#updateCanBeOnFirstRestDayAfterWorkDay_' + id));
    setBooleanInput($('#updateCanBeOnLastWorkDayBeforeRestDay_' + id));
    setBooleanInput($('#updateCanBeOnLastRestDayBeforeWorkDay_' + id));

    hideAddShiftTypesInputs();
}

function updateShiftType(shiftType, currentDOMShiftType) {
    var shiftTypeId = shiftType.id;
    $.ajax({
        type: 'PUT',
        url: '/shiftTypes/' + shiftTypeId,
        data: JSON.stringify(shiftType),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                    restoreInputShiftTypeData(currentDOMShiftType);
                }
            }
        },
        success: function () {
            var message = 'Успешно ъпдейтва ' + shiftType.fullName;
            positiveAlertBox(null, message, null, null);
            updateInputShiftTypeAttr(currentDOMShiftType);
        }
    })
}

function updateInputShiftTypeAttr(currentDOMShiftType) {
    var fullNameInput = currentDOMShiftType.find('.updateFullNameClass');
    fullNameInput.attr('oldData', fullNameInput.val());
    var shortNameInput = currentDOMShiftType.find('.updateShortNameClass');
    shortNameInput.attr('oldData', shortNameInput.val());
}

function restoreInputShiftTypeData(currentDOMShiftType) {
    var fullNameInput = currentDOMShiftType.find('.updateFullNameClass');
    fullNameInput.val(fullNameInput.attr('oldData'));
    var shortNameInput = currentDOMShiftType.find('.updateShortNameClass');
    shortNameInput.val(shortNameInput.attr('oldData'));
}

function deleteShiftType(shiftTypeId) {
    $.ajax({
        type: 'DELETE',
        url: '/shiftTypes/delete/' + shiftTypeId
    })
}

//Save the shiftType
function saveShiftType() {
    var shiftType = {};
    shiftType.fullName = $('#newShiftTypeInputFullName').val();
    shiftType.shortName = $('#newShiftTypeInputShortName').val();
    shiftType.startTime = $('#newShiftTypeInputStartTime').val();
    shiftType.endTime =   $('#newShiftTypeInputEndTime').val();
    shiftType.secondStartTime = $('#newShiftTypeInputSecondStartTime').val();
    shiftType.secondEndTime =   $('#newShiftTypeInputSecondEndTime').val();
    shiftType.shiftDuration = $('#newShiftTypeInputShiftDuration').val();
    shiftType.breakDuration = $('#newShiftTypeInputBreakDuration').val();
    var isRegularShiftI = $('#newShiftTypeIsRegularShift').find('.fa');
    shiftType.isRegularShift = $(isRegularShiftI).hasClass('fa-check');
    var isEndOnNextDayI = $('#newShiftTypeIsEndOnNextDay').find('.fa');
    shiftType.isEndOnNextDay = $(isEndOnNextDayI).hasClass('fa-check');
    shiftType.isActive = true;


    var canBeOnWorkDay = $('#canBeOnWorkDay').find('.fa').hasClass('fa-check');
    var canBeOnRestDay = $('#canBeOnRestDay').find('.fa').hasClass('fa-check');
    var canBeOnDoubleShiftRegime = $('#canBeOnDoubleShiftRegime').find('.fa').hasClass('fa-check');
    var canBeOnTripleShiftRegime = $('#canBeOnTripleShiftRegime').find('.fa').hasClass('fa-check');
    //var canBeOnFirstWorkDayAfterRestDay = $('#canBeOnFirstWorkDayAfterRestDay').find('.fa').hasClass('fa-check');
    //var canBeOnFirstRestDayAfterWorkDay = $('#canBeOnFirstRestDayAfterWorkDay').find('.fa').hasClass('fa-check');
    var canBeOnLastWorkDayBeforeRestDay = $('#canBeOnLastWorkDayBeforeRestDay').find('.fa').hasClass('fa-check');
    var canBeOnLastRestDayBeforeWorkDay = $('#canBeOnLastRestDayBeforeWorkDay').find('.fa').hasClass('fa-check');
    shiftType.canBeOnWorkDay = canBeOnWorkDay;
    shiftType.canBeOnRestDay = canBeOnRestDay;
    shiftType.canBeOnDoubleShiftRegime = canBeOnDoubleShiftRegime;
    shiftType.canBeOnTripleShiftRegime = canBeOnTripleShiftRegime;
    //shiftType.canBeOnFirstWorkDayAfterRestDay = canBeOnFirstWorkDayAfterRestDay;
    //shiftType.canBeOnFirstRestDayAfterWorkDay = canBeOnFirstRestDayAfterWorkDay;
    shiftType.canBeOnLastWorkDayBeforeRestDay = canBeOnLastWorkDayBeforeRestDay;
    shiftType.canBeOnLastRestDayBeforeWorkDay = canBeOnLastRestDayBeforeWorkDay;

    shiftType.shiftTypeGroupId = $('#shiftTypeGroup').val();

    $.ajax({
        type: 'POST',
        url: '/shiftTypes/add',
        data: JSON.stringify(shiftType),
        dataType: 'json',
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
        success: function (shiftType) {
            var message = 'Успешно добави ' + shiftType.fullName;
            positiveAlertBox(null, message, null, null);
            shiftType.startTime = TimeCalculator.convertTimeToString(shiftType.startTime);
            shiftType.endTime = TimeCalculator.convertTimeToString(shiftType.endTime);
            shiftType.secondStartTime = TimeCalculator.convertTimeToString(shiftType.secondStartTime);
            shiftType.secondEndTime = TimeCalculator.convertTimeToString(shiftType.secondEndTime);
            shiftType.shiftDuration = TimeCalculator.convertTimeToString(shiftType.shiftDuration);
            shiftType.breakDuration = TimeCalculator.convertTimeToString(shiftType.breakDuration);
            addShiftTypeDOM(shiftType);
        }
    });
}

function changeAndUpdateShiftType(currentDOMShiftType) {
    var shiftType = {};
    shiftType.id = currentDOMShiftType.attr('shiftType_id');
    shiftType.fullName = currentDOMShiftType.find('.updateFullNameClass').val();
    shiftType.shortName = currentDOMShiftType.find('.updateShortNameClass').val();
    shiftType.startTime = currentDOMShiftType.find('.updateStartTime').val();
    shiftType.endTime = currentDOMShiftType.find('.updateEndTime').val();
    shiftType.secondStartTime = currentDOMShiftType.find('.updateSecondStartTime').val();
    shiftType.secondEndTime = currentDOMShiftType.find('.updateSecondEndTime').val();
    shiftType.shiftDuration = currentDOMShiftType.find('.updateShiftDuration').val();
    shiftType.breakDuration = currentDOMShiftType.find('.updateBreakDuration').val();
    shiftType.isRegularShift = currentDOMShiftType.find('.updateIsRegularShift').attr('data');
    shiftType.isEndOnNextDay = currentDOMShiftType.find('.updateIsEndOnNextDay').attr('data');

    var id = shiftType.id;
    shiftType.canBeOnWorkDay = $('#updateCanBeOnWorkDay_' + id).attr('data');
    shiftType.canBeOnRestDay = $('#updateCanBeOnRestDay_' + id).attr('data');
    shiftType.canBeOnDoubleShiftRegime = $('#updateCanBeOnDoubleShiftRegime_' + id).attr('data');
    shiftType.canBeOnTripleShiftRegime = $('#updateCanBeOnTripleShiftRegime_' + id).attr('data');
    //shiftType.canBeOnFirstWorkDayAfterRestDay = $('#updateCanBeOnFirstWorkDayAfterRestDay_' + id).attr('data');
    //shiftType.canBeOnFirstRestDayAfterWorkDay = $('#updateCanBeOnFirstRestDayAfterWorkDay_' + id).attr('data');
    shiftType.canBeOnLastWorkDayBeforeRestDay = $('#updateCanBeOnLastWorkDayBeforeRestDay_' + id).attr('data');
    shiftType.canBeOnLastRestDayBeforeWorkDay = $('#updateCanBeOnLastRestDayBeforeWorkDay_' + id).attr('data');

    shiftType.shiftTypeGroupId = $('#shiftTypeGroup_' + id).val();

    shiftType.isActive = true;
    updateShiftType(shiftType, currentDOMShiftType);
}

function keyUpFunctionShiftType(e) {
    var currentDOMShiftType = $(this);
    if (e.which == 27) {
        var question = 'Сигурен ли си, че искаш да изтриеш ' + currentDOMShiftType.find('.updateFullNameClass').val() + ' ?';
        confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteShiftTypeFunction , currentDOMShiftType);
    } else if (e.which == 13) {
        changeAndUpdateShiftType(currentDOMShiftType);
    }
}

function confirmedDeleteShiftTypeFunction(currentDOMShiftType) {
    var shiftTypeId = currentDOMShiftType.attr('shiftType_id');
    //Delete DOM shiftType
    $('#shiftTypeFullCont_' + shiftTypeId).remove();

    //Delete in MySQL
    deleteShiftType(shiftTypeId);
}

function validateTimeAndSaveShiftTypeIfSet() {
    var element = $(this);
    var parent = $(this).parent();
    if ($(element).hasClass('timepicker-input') && !($(parent).attr('oldData') == null)) {
        var value = $(element).val();
        if (value.length > 4 && value.indexOf(' ') == -1) {
            changeAndUpdateShiftType(parent);
        }
    }
}

