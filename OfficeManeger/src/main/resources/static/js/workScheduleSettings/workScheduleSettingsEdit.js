function init() {
    $('.boolean-value-box').click(changeBoolean);
    $('#saveButton').click(saveChanges);
}

function saveChanges() {
    //set animation to button and disable it
    var buttonEl = $('#saveButton');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    var settings = {};
    var id = $(buttonEl).attr('settingsId');
    if (isNaN(parseInt(id)) || parseInt(id) < 1) {
        EPPZScrollTo.scrollVerticalToElementById('numbOfEmployeesInShift');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Id на настройките не е валидно !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    settings.id = id;

    var numbOfEmployeesInShift = $('#numbOfEmployeesInShift').val();
    if (isNaN(parseInt(numbOfEmployeesInShift)) || parseInt(numbOfEmployeesInShift) < 1) {
        EPPZScrollTo.scrollVerticalToElementById('numbOfEmployeesInShift');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Броя на служителите в смяна не е валиден !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    settings.numbOfEmployeesInShift = numbOfEmployeesInShift;

    var booleanBoxes = $('.groupBox');
    var shiftTypeGroupsToObserveIds = [];
    for (var i = 0; i < booleanBoxes.length; i++) {
        var booleanBoxeEl = booleanBoxes[i];
        var isActiveShiftTypeGroup = checkBoolean($(booleanBoxeEl));
        if (isActiveShiftTypeGroup) {
            var shiftTypeGroupId = $(booleanBoxeEl).attr('shiftTypeGroupId');
            shiftTypeGroupsToObserveIds.push(shiftTypeGroupId);
        }
    }

    settings.shiftTypeGroupsToObserveIds = shiftTypeGroupsToObserveIds;

    settings.isOnDoubleShiftRegime = $('#isOnDoubleShiftRegime').find('.fa').hasClass('boolean-positive');
    settings.isOnTripleShiftRegime = $('#isOnTripleShiftRegime').find('.fa').hasClass('boolean-positive');

    $.ajax({
        type: 'POST',
        url: '/boss/workScheduleSettingsUpdate',
        data: JSON.stringify(settings),
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
            alert('redaktiran e');
        }
    });
}