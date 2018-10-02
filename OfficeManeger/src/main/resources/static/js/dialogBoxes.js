
function confirmBox(heading, question, cancelButtonTxt, cancelButtonClass, okButtonTxt, okButtonClass, callback, elementCallbackParameter) {
    var confirmModal =
        $('<div class="modal" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModalLabel" aria-hidden="true">'  +
            '<div class="modal-dialog" role="document">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title" id="confirmModalLabel">' + (heading ? heading : 'Внимание !') + '</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
            '<span aria-hidden="true">×</span>' +
            '</button>' +
            '</div>' +
            '<div class="modal-body">' +
            question +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn ' + (cancelButtonClass ? cancelButtonClass : 'btn-secondary') + '" data-dismiss="modal" id="cancelButton">' + (cancelButtonTxt ? cancelButtonTxt : 'Cancel') + '</button>' +
            '<button type="button" class="btn ' + (okButtonClass ? okButtonClass : 'btn-primary') + '" id="okButton">' + (okButtonTxt ? okButtonTxt : 'OK') + '</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>');
    confirmModal.find('#okButton').click(function(event) {
        callback(elementCallbackParameter);
        confirmModal.modal('hide');
    });

    confirmModal.modal('show');
}

function alertBox(heading, message, okButtonTxt, okButtonClass) {
    var alertModal =
        $('<div class="modal" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="alertModalLabel" aria-hidden="true">'  +
            '<div class="modal-dialog" role="document">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title" id="alertModalLabel">' + (heading ? heading : 'Внимание !') + '</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
            '<span aria-hidden="true">×</span>' +
            '</button>' +
            '</div>' +
            '<div class="modal-body">' +
            message +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn ' + (okButtonClass ? okButtonClass : 'btn-primary') + '" id="okButton">' + (okButtonTxt ? okButtonTxt : 'OK !') + '</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>');
    alertModal.find('#okButton').click(function(event) {
        alertModal.modal('hide');
    });

    alertModal.modal('show');
}

function positiveAlertBox(heading, message, okButtonTxt, okButtonClass) {
    var alertModal =
        $('<div class="modal" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="alertModalLabel" aria-hidden="true">'  +
            '<div class="modal-dialog" role="document">' +
            '<div class="modal-content positive-box">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title" id="alertModalLabel">' + (heading ? heading : 'Внимание !') + '</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
            '<span aria-hidden="true">×</span>' +
            '</button>' +
            '</div>' +
            '<div class="modal-body">' +
            message +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn ' + (okButtonClass ? okButtonClass : 'btn-primary') + '" id="okButton">' + (okButtonTxt ? okButtonTxt : 'OK !') + '</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>');
    alertModal.find('#okButton').click(function(event) {
        alertModal.modal('hide');
    });

    alertModal.modal('show');
}

function negativeAlertBox(heading, message, okButtonTxt, okButtonClass) {
    var alertModal =
        $('<div class="modal" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="alertModalLabel" aria-hidden="true">'  +
            '<div class="modal-dialog" role="document">' +
            '<div class="modal-content negative-box">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title" id="alertModalLabel">' + (heading ? heading : 'Внимание !') + '</h5>' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
            '<span aria-hidden="true">×</span>' +
            '</button>' +
            '</div>' +
            '<div class="modal-body">' +
            message +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn ' + (okButtonClass ? okButtonClass : 'btn-primary') + '" id="okButton">' + (okButtonTxt ? okButtonTxt : 'OK !') + '</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>');
    alertModal.find('#okButton').click(function(event) {
        alertModal.modal('hide');
    });

    alertModal.modal('show');
}


//timepicker
/*
function timepickerSetActiveCell() {
    var parent = $(this).parent();
    var value = null;
    var timepicker = $('.cd-popup')[0];
    var ownerId = $(timepicker).attr('owner-id');
    var owner = document.getElementById(ownerId);
    if (owner == undefined) {
        alert('input hasnt id');
    }

    var isTimeSet;
    if (parent.hasClass('hour')) {
        $('.hour .state-default').removeClass('selected-state-default');
        value = $(parent).attr('data-hour');
        isTimeSet = setTimeElementsToOwner(owner, value, true);
    } else {
        $('.minute .state-default').removeClass('selected-state-default');
        value = $(parent).attr('data-minute');
        isTimeSet = setTimeElementsToOwner(owner, value, false);
    }

    if (isTimeSet) {
        $('.cd-popup').removeClass('is-visible');
    } else {
        $(this).addClass('selected-state-default');
    }
}

function setTimeElementsToOwner(owner, value, isHour) {
    var ownerText = $(owner).val();
    if (ownerText.indexOf(':') == -1) {
        ownerText = '  :  ';
    }

    if (value < 10) {
        value = '0' + value;
    }

    var isTimeSet = false;
    if (isHour) {
        ownerText = ownerText.slice(ownerText.indexOf(':'));
        if (ownerText.indexOf(' ') == -1) {
            isTimeSet = true;
        }
        ownerText = value + ownerText;
    } else {
        ownerText = ownerText.slice(0, ownerText.indexOf(':') + 1);
        if (ownerText.indexOf(' ') == -1) {
            isTimeSet = true;
        }
        ownerText = ownerText + value;
    }

    $(owner).val(ownerText);
    return isTimeSet;
}

function convertTimeString(timeAsString) {
    try {
        if (timeAsString == null) {
            return '';
        }
        timeAsString = '' + timeAsString;
        var hour = timeAsString.split(',')[0];
        if (hour < 10) {
            hour = '0' + hour;
        }
        var minute = timeAsString.split(',')[1];
        if (minute < 10) {
            minute = '0' + minute;
        }
        return hour + ':' + minute;
    }
    catch (ex){
        return timeAsString;
    }
}
*/
//end timepicker

//timepicker
function setTimePickerToElement(htmlElement, callbackFunction) {
    if(callbackFunction instanceof Function) {
        $(htmlElement).on('change', callbackFunction);
    }

    $(htmlElement).attr('pattern', '^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$');
    $(htmlElement).on('click', function(event){
        event.preventDefault();

        var position = $(this).offset();
        var timepickerPosTop = position.top + 5 - $(document).scrollTop() + 'px';
        var timepickerPosLeft = position.left + 'px';
        $('.cd-popup-container').css({top: timepickerPosTop, left: timepickerPosLeft, position:'absolute'});
        $('.state-default').removeClass('selected-state-default');
        if ($(this).attr('id') != undefined) {
            var ownerId = $(this).attr('id');
            $('.cd-popup').attr('owner-id', ownerId);
        }
        $('.state-default').click(timepickerSetActiveCell);
        $('.cd-popup').addClass('is-visible');
        $('.cd-popup').draggable();
    });

    //close popup
    $('.cd-popup').on('click', function(event){
        if($(event.target).is('.cd-popup')) {
            event.preventDefault();
            $(this).removeClass('is-visible');
        }
    });

    $(document).keyup(function(event){
        if(event.which=='27'){
            $('.cd-popup').removeClass('is-visible');
            if(callbackFunction instanceof Function) {
                callbackFunction();
            }
        }
    });

    ///////////////////////////////////

    function timepickerSetActiveCell() {
        var parent = $(this).parent();
        var value = null;
        var timepicker = $('.cd-popup')[0];
        var ownerId = $(timepicker).attr('owner-id');
        var owner = document.getElementById(ownerId);
        if (owner == undefined) {
            alert("input hasn't id");
        }

        var isTimeSet;
        if (parent.hasClass('hour')) {
            $('.hour .state-default').removeClass('selected-state-default');
            value = $(parent).attr('data-hour');
            isTimeSet = setTimeElementsToOwner(owner, value, true);
        } else {
            if (parent.hasClass('minute-first')) {
                $('.minute-first .state-default').removeClass('selected-state-default');
                value = $(parent).attr('data-minute');
                isTimeSet = setTimeElementsToOwner(owner, value, false);
            } else {
                $('.minute-second .state-default').removeClass('selected-state-default');
                value = $(parent).attr('data-minute');
                isTimeSet = setTimeElementsToOwner(owner, value, false);
            }
        }

        if (isTimeSet) {
            $('.cd-popup').removeClass('is-visible');
            if(callbackFunction instanceof Function) {
                callbackFunction();
            }
        } else {
            $(this).addClass('selected-state-default');
        }
    }

    function setTimeElementsToOwner(owner, value, isHour) {
        var ownerText = $(owner).val();

        if (ownerText.indexOf(':') == -1) {
            ownerText = '  :  ';
        }

        if (isHour && value < 10) {
            value = '0' + value;
        }

        var isTimeSet = false;
        if (isHour) {
            ownerText = ownerText.slice(ownerText.indexOf(':'));
            if ((ownerText.indexOf(' ') == -1) && (ownerText.indexOf('*') == -1)) {
                isTimeSet = true;
            }
            ownerText = value + ownerText;
        } else {
            var oldMinute = ownerText.slice(ownerText.indexOf(':') + 1);
            if (oldMinute != '  ') {
                var valueIsDec = (value.indexOf('*') == 1);
                var oldDecM = oldMinute.slice(0, 1);
                var oldSM = oldMinute.slice(1);
                value = value.replace('\*', '');
                if (valueIsDec) {
                    value = value + oldSM;
                } else {
                    value = oldDecM + value;
                }
            }

            ownerText = ownerText.slice(0, ownerText.indexOf(':') + 1);

            if (ownerText.indexOf(' ') == -1 && value.indexOf('*') == -1) {
                isTimeSet = true;
            }
            ownerText = ownerText + value;
        }

        //change border color and help label
        $(owner).val(ownerText);
        if ($(owner).hasClass('time-incont-validate')) {
            inputValidate(RegExPatternConstants.TIME, owner, 'noLabelChange');
            var container = $(owner).parent();
            var isValidTime = RegExPatternConstants.TIME.test(ownerText);
            var inputIndex = $(owner).attr('id').split('_')[1];
            var attrValue = $(container).attr('isValidCont');
            var n = 1;
            n = n << inputIndex;
            if (isValidTime) {
                attrValue = attrValue | n;
            } else {
                n = ~n;
                attrValue = attrValue & n;
            }
            $(container).attr('isValidCont', attrValue);
            //inputsContainerValidate(container);
        } else {
            inputValidate(RegExPatternConstants.TIME, owner);
        }

        return isTimeSet;
    }

    function convertTimeString(timeAsString) {
        try {
            if (timeAsString == null) {
                return '';
            }
            timeAsString = '' + timeAsString;
            var hour = timeAsString.split(',')[0];
            if (hour < 10) {
                hour = '0' + hour;
            }
            var minute = timeAsString.split(',')[1];
            if (minute < 10) {
                minute = '0' + minute;
            }
            return hour + ':' + minute;
        }
        catch (ex){
            return timeAsString;
        }
    }
}

//end timepicker

//booleanInput
function setBooleanInput(domElement) {
    var value = $(domElement).attr('data');
    if (value === undefined) {
        alert('dom element hasnt attr data');
        return;
    }

    if (!$(domElement).hasClass('boolean-input-owner')) {
        $(domElement).addClass('boolean-input-owner');
    }

    $(domElement).click(changeInputBooleanValue);
}

function setBooleanInputs(domElementArr) {
    for (var i = 0; i < domElementArr.length; i++) {
        setBooleanInput(domElementArr[i]);
    }
}

function changeInputBooleanValue(e) {
    var element = $(this).find('.fa')[0];
    var oldValue = $(element).hasClass('fa-check');
    var newValue = !oldValue;
    var parent = $(this);
    element.remove();
    $(parent).attr('data', newValue);
    if (newValue) {
        $(parent).append($('<i></i>')
                .addClass('fa fa-check pickable')
                .attr('aria-hidden', 'true'));
    } else {
        $(parent).append($('<i></i>')
            .addClass('fa fa-times pickable')
            .attr('aria-hidden', 'true'));
    }
}