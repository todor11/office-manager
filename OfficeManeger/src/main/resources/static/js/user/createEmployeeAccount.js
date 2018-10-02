function init() {
    $('#createAccountButton').click(saveNewEmployeeAccount);
}


function saveNewEmployeeAccount() {
    var account = {};

    var employeeId = $('#employee').val();
    if (employeeId == '0') {
        var msg = 'Не е избран служител !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    account.employeeId = employeeId;

    var roleId = $('#role').val();
    if (roleId == '0') {
        var msg = 'Не е избрана роля !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    account.roleId = roleId;

    var username = $('#inputUserName').val();
    if (!RegExPatternConstants.USERNAME.test(username)) {
        var msg = 'Потребителското име не е валидно. Валидни са само латински букви(малки и големи), цифри и _ , от 5 до 30 символа !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    account.username = username;

    var password = $('#inputPassword').val();
    if (!RegExPatternConstants.PASSWORD.test(password)) {
        var msg = 'Паролата не е валидна !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    account.password = password;

    var confirmPassword = $('#inputConfirmPassword').val();
    if (!RegExPatternConstants.PASSWORD.test(confirmPassword)) {
        var msg = 'Повторената парола не е валидна !';
        negativeAlertBox(null, msg, null, null);
        return;
    } else if (confirmPassword != password) {
        var msg = 'Паролите не съвпадат !';
        negativeAlertBox(null, msg, null, null);
        return;
    }
    account.confirmPassword = confirmPassword;

    registerAccount(account);
}

function registerAccount(account) {
    //set animation to button and disable it
    var buttonEl = $('#createAccountButton');
    if ($(buttonEl).attr('isActive') === 'false') {
        return;
    }
    startAnimationAndDisableButton(buttonEl);

    $.ajax({
        type: 'POST',
        url: '/createEmployeeAccount',
        data: JSON.stringify(account),
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
            alert('syzdaden');
        }
    });
}
