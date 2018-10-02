var computerId;

function init() {
    var splitedHref = window.location.href.split('/');
    computerId = splitedHref[splitedHref.length - 1];
    getComputer();
    $('#saveComputerBtn').click(saveComputer);
}

function getComputer() {
    $.ajax({
        type: 'GET',
        url: '/getEditComputer/' + computerId,
        data: 'json',
        success: function (computer) {
            var name = computer.name;
            var bussinessUnitId = computer.businessUnitId;
            var computerIp= computer.ip;
            $('#inputComputerName').val(name);
            $('#bussinessUnit').val(bussinessUnitId);
            $('#computerIP').val(computerIp);
            $('#role').val(computer.roleId);
        }
    });
}

function saveComputer() {
    var computer = {};
    computer.name = $('#inputComputerName').val();
    computer.businessUnitId = $('#bussinessUnit').val();
    computer.id = computerId;
    computer.roleId = $('#role').val();

    $.ajax({
        type: 'PUT',
        url: '/computer/update',
        data: JSON.stringify(computer),
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
            var message = 'Успешно ъпдейтва ' + computer.name;
            positiveAlertBox(null, message, null, null);
        }
    });
}