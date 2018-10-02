
function init() {
    if (document.getElementById('addAnnualLeaveTypeButton') != null) {
        //hideAddАnnualLeaveTypesInputs();
        $('#addAnnualLeaveTypeButton').click(showAddАnnualLeaveTypesInputs);
        $('#annualLeaveTypeBtnSave').click(saveAnnualLeaveType);
        $('#annualLeaveTypeBtnCancel').click(hideAddАnnualLeaveTypesInputs);
        $('.annualLeaveType-container').on('keyup', keyUpFunction);
    }
}


//Show Add annualLeaveTypes
function showAddАnnualLeaveTypesInputs() {
    $('#addAnnualLeaveTypeInputs').show();
}

//Hide Add annualLeaveTypes
function hideAddАnnualLeaveTypesInputs() {
    $('#addAnnualLeaveTypeInputs').hide();
}

/*
function loadAllАnnualLeaveTypes() {
    $.ajax({
        type: 'GET',
        url: '/annualLeaveTypes/all',
        data: 'json',
        success: function (annualLeaveTypes) {
            $.each(annualLeaveTypes, function (i, annualLeaveType) {
                addAnnualLeaveTypeDOM(annualLeaveType);
            });
        }
    });
}
*/

//Add AnnualLeaveTypes to DOM
function addAnnualLeaveTypeDOM(annualLeaveType) {
    var id = annualLeaveType.id;
    var fullName = annualLeaveType.fullName;
    var shortName = annualLeaveType.shortName;
    $('#annualLeaveTypeList').append(
        $('<div></div>')
            .addClass('row annualLeaveType-container')
            .attr('alt_id', id)
            .append(
                $('<input/>')
                    .addClass('updateFullNameClass form-control col-sm-6')
                    .attr('oldData', fullName)
                    .val(fullName))
            .append(
                $('<input/>')
                    .addClass('updateShortNameClass form-control col-sm-4')
                    .attr('oldData', shortName)
                    .val(shortName))
            .on('keyup', keyUpFunction)
    );

    hideAddАnnualLeaveTypesInputs();
}

function updateAnnualLeaveType(annualLeaveType, currentDOMAnnualLeaveType) {
    var annualLeaveTypeId = annualLeaveType.id;
    $.ajax({
        type: 'PUT',
        url: '/annualLeaveTypes/' + annualLeaveTypeId,
        data: JSON.stringify(annualLeaveType),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                    restoreInputData(currentDOMAnnualLeaveType);
                }
            }
        },
        success: function () {
            var message = 'Успешно ъпдейтва ' + annualLeaveType.fullName;
            positiveAlertBox(null, message, null, null);
            updateInputAttr(currentDOMAnnualLeaveType);
        }
    });
}

function updateInputAttr(currentDOMAnnualLeaveType) {
    var fullNameInput = currentDOMAnnualLeaveType.find('.updateFullNameClass');
    fullNameInput.attr('oldData', fullNameInput.val());
    var shortNameInput = currentDOMAnnualLeaveType.find('.updateShortNameClass');
    shortNameInput.attr('oldData', shortNameInput.val());
}

function restoreInputData(currentDOMAnnualLeaveType) {
    var fullNameInput = currentDOMAnnualLeaveType.find('.updateFullNameClass');
    fullNameInput.val(fullNameInput.attr('oldData'));
    var shortNameInput = currentDOMAnnualLeaveType.find('.updateShortNameClass');
    shortNameInput.val(shortNameInput.attr('oldData'));
}

function deleteAnnualLeaveType(annualLeaveTypeId) {
    $.ajax({
        type: 'DELETE',
        url: '/annualLeaveTypes/delete/' + annualLeaveTypeId
    })
}

//Save the annualLeaveType
function saveAnnualLeaveType() {
    var annualLeaveType = {};
    annualLeaveType.fullName = $('#newAnnualLeaveTypeInputFullName').val();
    annualLeaveType.shortName = $('#newAnnualLeaveTypeInputShortName').val();

    $.ajax({
        type: 'POST',
        url: '/annualLeaveTypes/add',
        data: JSON.stringify(annualLeaveType),
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
        success: function (annualLeaveType) {
            var message = 'Успешно добави ' + annualLeaveType.fullName;
            positiveAlertBox(null, message, null, null);
            addAnnualLeaveTypeDOM(annualLeaveType);
        }
    });
}

function changeAndUpdate(currentDOMAnnualLeaveType) {
    var annualLeaveType = {};
    annualLeaveType.id = currentDOMAnnualLeaveType.attr('alt_id');
    annualLeaveType.fullName = currentDOMAnnualLeaveType.find('.updateFullNameClass').val();
    annualLeaveType.shortName = currentDOMAnnualLeaveType.find('.updateShortNameClass').val();
    updateAnnualLeaveType(annualLeaveType, currentDOMAnnualLeaveType);
}

function keyUpFunction(e) {
    var currentDOMAnnualLeaveType = $(this);
    if (e.which == 27) {
        var question = 'Сигурен ли си, че искаш да изтриеш ' + currentDOMAnnualLeaveType.find('.updateFullNameClass').val() + ' ?';
        confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteFunction , currentDOMAnnualLeaveType);
    } else if (e.which == 13) {
        changeAndUpdate(currentDOMAnnualLeaveType);
    }
}

function confirmedDeleteFunction(currentDOMAnnualLeaveType) {
    var alt_id = currentDOMAnnualLeaveType.attr('alt_id');
    //Delete DOM AnnualLeaveType
    currentDOMAnnualLeaveType.remove();
    //Delete in MySQL
    deleteAnnualLeaveType(alt_id);
}

