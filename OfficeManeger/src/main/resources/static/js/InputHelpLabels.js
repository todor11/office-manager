var InputHelpLabels = (function (){

    function InputLabels() {
        this.PERSON_SINGLE_NAME_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Разрешени символи: букви(кирилица и латиница,малки и големи). Поне 2 символа.' +
            '</small>';
        this.PERSON_SINGLE_NAME_OR_EMPTY_VALIDATE = '<small class="form-text text-muted">' +
            '<span defaultClass="recommendable-text"></span>Разрешени символи: букви(кирилица и латиница,малки и големи). Поне 2 символа.' +
            '</small>';
        this.PERSON_EGN_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Разрешени символи: цифри, 10 символа.' +
            '</small>';
        this.BASIC_TEXT_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Разрешени символи: букви(кирилица и латиница,малки и големи). Поне 3 символа.' +
            '</small>';
        this.BASIC_TEXT_EMPTY_VALIDATE = '<small class="form-text text-muted">' +
            '<span defaultClass="recommendable-text"></span>Разрешени символи: букви(кирилица и латиница,малки и големи).' +
            '</small>';
        this.DROPDOWN_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Избери от падащото меню.' +
            '</small>';
        this.USERNAME_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Разрешени символи: малки и големи на латиница от a до z,от A до Z, символ _ , цифри от 0 до 9 . От 5 до 30 символа.' +
            '</small>';
        this.PASSWORD_VALIDATE = '<small class="form-text text-muted">' +
            '<span class="required-text" defaultClass="required-text">Задължително поле. </span>Минимум 5 символа.' +
            '</small>';
    }

    InputLabels.prototype.getLabel = function(inputClassName) {
        return $(this[inputClassName]);
    };

    var inputLabels;

    return {
        getHelpLabel: function (inputClassName) {
            if (!inputLabels) {
                inputLabels = new InputLabels();
            }

            return inputLabels.getLabel(inputClassName);
        }
    };
})();


