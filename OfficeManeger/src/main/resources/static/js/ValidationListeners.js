
function inputValidate(pattern, inputDomElement, isNoLabelChange) {
    if (pattern instanceof RegExp) {
        var isValid = pattern.test($(inputDomElement).val());
        if (isValid) {
            $(inputDomElement).removeClass('invalid-input');
            if (isNoLabelChange != 'noLabelChange') {
                changeToPositiveHelpLabel($(inputDomElement).parent())
            }
        } else {
            $(inputDomElement).addClass('invalid-input');
            if (isNoLabelChange != 'noLabelChange') {
                changeToNegativeHelpLabel($(inputDomElement).parent());
            }
        }
    }
}

function changeToNegativeHelpLabel(helpLabelParent) {
    var smallEl = $(helpLabelParent).children("small");
    var spanEl = $(smallEl).children("span");
    $(spanEl).text('НЕВАЛИДЕН ! ');
    $(spanEl).removeClass();
    $(spanEl).addClass($(spanEl).attr('defaultClass'));
}

function changeToPositiveHelpLabel(helpLabelParent) {
    var smallEl = $(helpLabelParent).children("small");
    var spanEl = $(smallEl).children("span");
    $(spanEl).text('Валиден ! ');
    $(spanEl).removeClass();
    $(spanEl).addClass('valid-text');
}

function personSingleNameInputValidate() {
    inputValidate(RegExPatternConstants.PERSON_SINGLE_NAME, $(this));
}

function personSingleNameOrEmptyInputValidate() {
    inputValidate(RegExPatternConstants.PERSON_SINGLE_NAME_OR_EMPTY, $(this));
}

function personEGNValidate() {
    var egn = $(this).val();

    if (!RegExPatternConstants.PERSON_EGN.test(egn)) {
        $(this).addClass('invalid-input');
        changeToNegativeHelpLabel($(this).parent());
    } else {
        if (EGNManager.getIsValidEGN(egn)) {
            $(this).removeClass('invalid-input');
            changeToPositiveHelpLabel($(this).parent());
        } else {
            $(this).addClass('invalid-input');
            changeToNegativeHelpLabel($(this).parent());
            $(this).parent().find('span').text('ЕГН-ето е грешно !')
        }
    }
}

function basicTextInputValidate() {
    inputValidate(RegExPatternConstants.BASIC_TEXT, $(this));
}

function basicTextEmptyInputValidate() {
    inputValidate(RegExPatternConstants.BASIC_TEXT_EMPTY, $(this));
}

function personFullNameInputValidate() {
    inputValidate(RegExPatternConstants.PERSON_FULL_NAME, $(this));
}

function textInputValidate() {
    inputValidate(RegExPatternConstants.TEXT, $(this));
}

function numberInputValidate() {
    inputValidate(RegExPatternConstants.NUMBER, $(this));
}

function dateInputValidate() {
    inputValidate(RegExPatternConstants.DATE, $(this));
}

function dateTimeInputValidate() {
    inputValidate(RegExPatternConstants.DATE_TIME, $(this));
}

function timeInputValidate() {
    inputValidate(RegExPatternConstants.TIME, $(this));
}

function dropdownInputValidate() {
    inputValidate(RegExPatternConstants.DROPDOWN, $(this));
}

function emailInputValidate() {
    inputValidate(RegExPatternConstants.EMAIL, $(this));
}

function usernameInputValidate() {
    inputValidate(RegExPatternConstants.USERNAME, $(this));
}

function passwordInputValidate() {
    inputValidate(RegExPatternConstants.PASSWORD, $(this));
}