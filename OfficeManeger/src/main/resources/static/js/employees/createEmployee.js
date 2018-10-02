var activePhoneTypes;
function init() {
    if (document.getElementById('empl-main-button') != null) {
        $('.empl-phone-number').on('keyup', updatePhoneContactLabel);
        $('.empl-phone-number').on('change', createNewPhoneContactBlock);
        $('#empl-main-button').click(createNewEmployee);
        getActivePhoneTypes();
    }
}

function getActivePhoneTypes() {
    $.ajax({
        type: 'GET',
        url: '/phoneContactTypes',
        data: 'json',
        success: function (phoneTypes) {
            activePhoneTypes = phoneTypes;
        }
    });
}

function updatePhoneContactLabel() {
    var inputElement = $(this);
    var inputID = $(inputElement).attr('id');
    var inputText = $(inputElement).val();
    var labelID = inputID.replace('input', 'label');
    var labelElement = document.getElementById(labelID);
    if (labelElement != undefined) {
        labelElement.innerText = 'Тел. номер : ' + inputText;
    }
}

function createNewPhoneContactBlock() {
    var phoneContainer = $('#phone-container');
    var phoneContainerChilds = $(phoneContainer).children();
    var lastPhoneBlock = phoneContainerChilds[phoneContainerChilds.length - 1];
    var lastPhoneBlockId = $(lastPhoneBlock).attr('id');
    var blockCounter = lastPhoneBlockId.split('-')[lastPhoneBlockId.split('-').length - 1];
    try {
        blockCounter = parseInt(blockCounter);
        blockCounter++;

        var newBlock = $('<div class="card empl-phone-card" id="empl-card-block-' + blockCounter + '">' +
                            '<div class="card-header empl-phone-header" role="tab" id="heading-' + blockCounter + '">' +
                                '<p class="mb-0">' +
                                    '<a class="empl-cont-a" id="empl-phone-number-label-' + blockCounter + '" data-toggle="collapse" data-parent="#accordion" href="#collapse-' + blockCounter + '" aria-expanded="true" aria-controls="collapse-' + blockCounter + '">' +
                                        'Тел. номер :' +
                                    '</a>' +
                                '</p>' +
                            '</div>' +
                            '<div id="collapse-' + blockCounter + '" class="collapse" role="tabpanel" aria-labelledby="heading-' + blockCounter + '">' +
                                '<div class="card-block empl-card-block">' +
                                    '<label class="form-control-label">Телефонен номер</label>' +
                                    '<input type="text" class="form-control form-control-danger empl-phone-number" id="empl-phone-number-input-' + blockCounter + '"/>' +
                                    '<label class="form-control-label">Служебен/личен</label>' +
                                    '<select class="form-control form-control-danger" id="empl-phone-type-' + blockCounter + '">' +
                                    '</select>' +
                                    '<label class="form-control-label">Допълн. инфо</label>' +
                                    '<input type="text" id="phone-description-' + blockCounter + '" class="form-control form-control-danger"/>' +
                                '</div>' +
                            '</div>' +
                        '</div>');
        $(newBlock).find('.empl-phone-number').on('keyup', updatePhoneContactLabel);
        $(newBlock).find('.empl-phone-number').on('change', createNewPhoneContactBlock);
        $(newBlock).insertAfter(lastPhoneBlock);

        $('#empl-phone-type-' + blockCounter).append($('<option selected="true" value="">Избери от падащото меню</option>'));
        $.each(activePhoneTypes, function (i, phoneType) {
            $('#empl-phone-type-' + blockCounter).append($('<option value="' + phoneType +'">' + phoneType +'</option>'));
        });
    }
    catch(err) {
    }
}

function createNewEmployee() {
    //set animation to button and disable it
    var buttonEl = $('#empl-main-button');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    var employee = {};
    var firstName = $('#firstName').val();
    if (firstName == '' || !RegExPatternConstants.PERSON_SINGLE_NAME.test(firstName)) {
        EPPZScrollTo.scrollVerticalToElementById('firstName');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Името не е валидно !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.firstName = firstName;

    var middleName = $('#middleName').val();
    if (!RegExPatternConstants.PERSON_SINGLE_NAME_OR_EMPTY.test(middleName)) {
        EPPZScrollTo.scrollVerticalToElementById('middleName');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Презимето не е валидно !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.middleName = middleName;

    var lastName = $('#lastName').val();
    if (lastName == '' || !RegExPatternConstants.PERSON_SINGLE_NAME.test(lastName)) {
        EPPZScrollTo.scrollVerticalToElementById('lastName');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Фамилията не е валидна !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.lastName = lastName;

    var egn = $('#EGN').val();
    if (egn == '' || !RegExPatternConstants.PERSON_EGN.test(egn)) {
        EPPZScrollTo.scrollVerticalToElementById('EGN');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'ЕГН-то не е въведено или не е валидно !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.egn = egn;

    var officiaryID = $('#officiaryID').val();
    if (officiaryID == '' || !RegExPatternConstants.BASIC_TEXT.test(officiaryID)) {
        EPPZScrollTo.scrollVerticalToElementById('officiaryID');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Служебния номер не е въденен или не е валиден !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.officiaryID = officiaryID;

    var address = $('#address').val();
    employee.address = address;

    var rankId = $('#rank').val();
    if (!RegExPatternConstants.DROPDOWN.test(rankId)) {
        EPPZScrollTo.scrollVerticalToElementById('rank');
        stopAnimationAndEnableButton(buttonEl);
        var msg = 'Званието не е избрано !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    employee.rankId = rankId;

    var phoneNumbers = [];
    var phoneContainerChilds = $('#phone-container').children();
    for (var i = 0; i < phoneContainerChilds.length; i++) {
        var phoneContainer = phoneContainerChilds[i];
        var blockIndexElements = $(phoneContainer).attr('id').split('-');
        var blockIndex = blockIndexElements[blockIndexElements.length - 1];
        var phoneNumber = $('#empl-phone-number-input-' + blockIndex).val();
        if (RegExPatternConstants.PHONE_NUMBER.test(phoneNumber)) {
            var phoneType = $('#empl-phone-type-' + blockIndex).val();
            if (phoneType == '') {
                EPPZScrollTo.scrollVerticalToElementById('empl-phone-type-' + blockIndex);
                stopAnimationAndEnableButton(buttonEl);
                var msg = 'Типа на телефонния номер е задължителен !';
                negativeAlertBox(null, msg, null, null);
                return;
            }

            var description = $('#phone-description-' + blockIndex).val();
            var phone = {};
            phone.phoneNumber = phoneNumber;
            phone.phoneType = phoneType;
            phone.description = description;
            phoneNumbers.push(phone);
        }
    }
    employee.phoneNumbers = phoneNumbers;

    $.ajax({
        type: 'POST',
        url: '/employees/create',
        data: JSON.stringify(employee),
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

            alert('dobaven e');
        }
    });
}
