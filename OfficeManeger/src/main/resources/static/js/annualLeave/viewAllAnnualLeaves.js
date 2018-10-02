
function init() {
    $('.allAnnualLeaveInput').on('change', updateAnnualLeave);
}

function updateAnnualLeave() {
    var annualLeaveValue = $(this).val();
    annualLeaveValue = parseInt(annualLeaveValue);
    if(!isNaN(annualLeaveValue)) {
        if (annualLeaveValue < 0) {
            var msg = 'Отпуска не може да е отрицателно число. Промени го !!!';
            negativeAlertBox(null, msg, null, null);
            return;
        } else {
            var annualLeaveId = $(this).attr('annualLeaveId');
            $.ajax({
                type: 'PUT',
                url: '/annualLeave/update/' + annualLeaveId + '/' + annualLeaveValue,
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
    } else {
        var msg = 'Въведения брой дни не е валиден. Промени го !!!';
        negativeAlertBox(null, msg, null, null);
        return;
    }
}