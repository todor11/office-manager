

function init() {
    $('.boolean-value-box').click(changeBoolean);
    $('.boolean-value-box').click(updateIsEnabled);
    $('.accountUsername').on('change', updateUsername);
    $('.accountUsername').on('keyup', customValidateUsername);
    $('.accountRoleSelect').on('change', updateRole);
    $('.delete-account-button').on('click', deleteAccount);
    $('.resetPassword-button').on('click', resetPassword);
}

function updateUsername() {
    var newUsername = $(this).val();
    if (newUsername == '' || !RegExPatternConstants.USERNAME.test(newUsername)) {
        var msg = 'Новото потребителско име не е валидно. Валидни са само латински букви(малки и големи), цифри и _ , от 5 до 30 символа !';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var accountId = $(this).attr('accountId');
    if (isNaN(parseInt(accountId)) || parseInt(accountId) < 1) {
        var msg = 'Собственика на акаунта не е валиден';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var userUpdateUsernameModel = {};
    userUpdateUsernameModel.newUsername = newUsername;
    userUpdateUsernameModel.accountId = accountId;

    $.ajax({
        type: 'PUT',
        url: '/changeUsernameToAccount',
        data: JSON.stringify(userUpdateUsernameModel),
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
        success: function () {
            var message = 'Успешно редактира потребителското име.';
            positiveAlertBox(null, message, null, null);
        }
    });
}

function updateRole() {
    var roleId = $(this).val();
    if (isNaN(parseInt(roleId)) || parseInt(roleId) < 1) {
        var msg = 'Ролята на акаунта не е валидна';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var accountId = $(this).attr('accountId');
    if (isNaN(parseInt(accountId)) || parseInt(accountId) < 1) {
        var msg = 'Собственика на акаунта не е валиден';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    var account = {};
    account.accountId = accountId;
    account.roleId = roleId;

    $.ajax({
        type: 'PUT',
        url: '/changeRoleToAccount',
        data: JSON.stringify(account),
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
        success: function () {
            var message = 'Успешно редактира ролята.';
            positiveAlertBox(null, message, null, null);
        }
    });

}

function updateIsEnabled() {
    var accountId = $(this).attr('accountId');
    var isActive = checkBoolean(this);

    $.ajax({
        type: 'PUT',
        url: '/company/changeIsActiveAccount/' + accountId + '/' + isActive,
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
        success: function () {
            var message = '';
            if (isActive) {
                message = 'Успешно активира профила.';
            } else {
                message = 'Успешно деактивира профила.';
            }

            positiveAlertBox(null, message, null, null);
        }
    });
}

function deleteAccount() {
    var accountId = $(this).attr('accountId');
    var username = $(this).attr('username');
    var question = 'Сигурен ли си, че искаш да изтриеш акаунт с username ' + username + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteAccount , accountId);
}

function confirmedDeleteAccount(accountId) {
    $.ajax({
        type: 'DELETE',
        url: '/deleteAccount/' + accountId,
        error: function () {
        },
        success: function () {
            //remove from DOM
            $('#account_' + accountId).remove();
        }
    });
}

function resetPassword() {
    var accountId = $(this).attr('accountId');
    var username = $(this).attr('username');
    var question = 'Сигурен ли си, че искаш да нулираш паролата на акаунт с username ' + username + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Reset!', 'btn-danger', confirmedResetPassword , accountId);
}

function confirmedResetPassword(accountId) {
    $.ajax({
        type: 'POST',
        url: '/resetAccountPassword/' + accountId,
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
        success: function () {
            var message = 'Успешно ресетна паролата на акаунта. Новата парола е 123123';
            positiveAlertBox(null, message, null, null);
        }
    });
}

function customValidateUsername() {
    var username = $(this).val();
    var isValid = RegExPatternConstants.USERNAME.test(username);
    if (isValid) {
        realTimeValidateUsername($(this));
    } else {
        inputValidate(RegExPatternConstants.USERNAME, $(this));
    }
}

function realTimeValidateUsername(usernameInput) {
    var username = $(usernameInput).val();
    var accountId = (usernameInput).attr('accountId');

    $.ajax({
        type: 'GET',
        url: '/checkUsername/' + username + '/' + accountId,
        data: 'json',
        error: function () {
            $(usernameInput).addClass('invalid-input');
        },
        success: function () {
            $(usernameInput).removeClass('invalid-input');
        }
    });
}
