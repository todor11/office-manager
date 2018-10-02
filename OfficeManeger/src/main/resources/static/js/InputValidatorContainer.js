
var InputValidatorContainer =(function (){

    function InputsContainer() {
    }

    InputsContainer.prototype.addNewInput = function(inputDomElement) {
        var inputId = $(inputDomElement).attr('id');

        //add element in container;
        this[inputId] = inputDomElement;

        if ($(inputDomElement).hasClass('person-single-name-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('PERSON_SINGLE_NAME_VALIDATE'));
            $(inputDomElement).on('keyup change', personSingleNameInputValidate);
        } else if ($(inputDomElement).hasClass('person-single-name-empty-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('PERSON_SINGLE_NAME_OR_EMPTY_VALIDATE'));
            $(inputDomElement).on('keyup change', personSingleNameOrEmptyInputValidate);
        } else if ($(inputDomElement).hasClass('person-EGN-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('PERSON_EGN_VALIDATE'));
            $(inputDomElement).on('keyup change', personEGNValidate);
        } else if ($(inputDomElement).hasClass('basic-text-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('BASIC_TEXT_VALIDATE'));
            $(inputDomElement).on('keyup change', basicTextInputValidate);
        } else if ($(inputDomElement).hasClass('basic-text-empty-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('BASIC_TEXT_EMPTY_VALIDATE'));
            $(inputDomElement).on('keyup change', basicTextEmptyInputValidate);
        } else if ($(inputDomElement).hasClass('dropdown-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('DROPDOWN_VALIDATE'));
            $(inputDomElement).on('keyup change', dropdownInputValidate);
        } else if ($(inputDomElement).hasClass('dropdown-validate-noLabel')) {
            $(inputDomElement).on('keyup change', dropdownInputValidate);
        } else if ($(inputDomElement).hasClass('username-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('USERNAME_VALIDATE'));
            $(inputDomElement).on('keyup change', usernameInputValidate);
        } else if ($(inputDomElement).hasClass('password-validate')) {
            var parent = $(inputDomElement).parent();
            $(parent).append(InputHelpLabels.getHelpLabel('PASSWORD_VALIDATE'));
            $(inputDomElement).on('keyup change', passwordInputValidate);
        }



    };

    var inputsContainer;

    return {
        addNewInput: function (inputDomElement) {
            if (!inputsContainer) {
                inputsContainer = new InputsContainer();
            }

            inputsContainer.addNewInput(inputDomElement);
        }
    };
})();

