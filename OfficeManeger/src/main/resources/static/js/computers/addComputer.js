function init() {
    $('#addComputerBtn').click(addComputer);
}

function addComputer() {
    var computer = {};
    computer.name = $('#inputComputerName').val();

    $.ajax({
        type: 'POST',
        url: '/addComputer',
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
        success: function (savedComputerId) {
            window.location.href = '/computer/edit/' + savedComputerId;
        }
    });
}