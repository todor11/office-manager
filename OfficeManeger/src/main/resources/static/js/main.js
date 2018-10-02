$(document).ready(function() {
    validateLanguageAndSetClass();
    init();

    // setup inputs
    $.each($('.person-single-name-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.person-single-name-empty-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.person-EGN-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.basic-text-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.basic-text-empty-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.dropdown-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.username-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.password-validate'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });
    $.each($('.dropdown-validate-noLabel'), function (i, input) {
        InputValidatorContainer.addNewInput(input);
    });

    //setup date picker to BG
    $.datepicker.regional['bg'] = {clearText: 'Изтрий', clearStatus: '',
        closeText: 'Затвори', closeStatus: 'Затваряне без промяна',
        prevText: '<Пред', prevStatus: 'Вижте предишния месец',
        nextText: 'Следв>', nextStatus: 'Вижте следващия месец',
        currentText: 'Текущ', currentStatus: 'Вижте текущия месец',
        monthNames: ['Януари','Февруари','Март','Април','Май','Юни',
            'Юли','Август','Септември','Октомври','Ноември','Декември'],
        monthNamesShort: ['Ян','Фев','Март','Апр','Май','Юни',
            'Юли','Авг','Септ','Окт','Ноем','Дек'],
        monthStatus: 'Виж още един месец', yearStatus: 'Виж още една година',
        weekHeader: 'Седм', weekStatus: '',
        dayNames: ['Неделя','Понеделник','Вторник','Сряда','Четвъртък','Петък','Събота'],
        dayNamesShort: ['Нед','Пон','Вт','Ср','Чет','Пет','Съб'],
        dayNamesMin: ['Нед','Пон','Вт','Ср','Чет','Пет','Съб'],
        dayStatus: 'Използвайте Неделя като първия ден от седмицата', dateStatus: 'Изберете дд, мм г',
        dateFormat: 'dd.mm.yy', firstDay: 1,
        initStatus: 'Изберете дата', isRTL: false};
    $.datepicker.setDefaults($.datepicker.regional['bg']);

    //setup tooltips
    $( '.tooltipOwner' ).tooltip({
        content: function() {
            return $(this).attr('title');
        }
    });
});

var token = $("meta[name='_csrf']").attr("content");

$.ajaxSetup({
    beforeSend: function(xhr) {
        xhr.setRequestHeader('X-CSRF-TOKEN', token);
    }
});

window.onscroll = function() {
    scrollFunction();
};

function scrollFunction() {
    //Show scroll button
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("scrollButton").style.display = "block";
    } else {
        document.getElementById("scrollButton").style.display = "none";
    }
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
    EPPZScrollTo.scrollVerticalToElementById('menus-block');

    //old
    //document.body.scrollTop = 0;
    //document.documentElement.scrollTop = 0;
}


//scroll function : EPPZScrollTo.scrollVerticalToElementById('elementId', 110);
var EPPZScrollTo =
    {
        /**
         * Helpers.
         */
        documentVerticalScrollPosition: function()
        {
            if (self.pageYOffset) return self.pageYOffset; // Firefox, Chrome, Opera, Safari.
            if (document.documentElement && document.documentElement.scrollTop) return document.documentElement.scrollTop; // Internet Explorer 6 (standards mode).
            if (document.body.scrollTop) return document.body.scrollTop; // Internet Explorer 6, 7 and 8.
            return 0; // None of the above.
        },

        viewportHeight: function()
        { return (document.compatMode === "CSS1Compat") ? document.documentElement.clientHeight : document.body.clientHeight; },

        documentHeight: function()
        { return (document.height !== undefined) ? document.height : document.body.offsetHeight; },

        documentMaximumScrollPosition: function()
        { return this.documentHeight() - this.viewportHeight(); },

        elementVerticalClientPositionById: function(id)
        {
            var element = document.getElementById(id);
            var rectangle = element.getBoundingClientRect();
            return rectangle.top;
        },

        /**
         * Animation tick.
         */
        scrollVerticalTickToPosition: function(currentPosition, targetPosition)
        {
            var filter = 0.2;
            var fps = 60;
            var difference = parseFloat(targetPosition) - parseFloat(currentPosition);

            // Snap, then stop if arrived.
            var arrived = (Math.abs(difference) <= 0.5);
            if (arrived)
            {
                // Apply target.
                scrollTo(0.0, targetPosition);
                return;
            }

            // Filtered position.
            currentPosition = (parseFloat(currentPosition) * (1.0 - filter)) + (parseFloat(targetPosition) * filter);

            // Apply target.
            scrollTo(0.0, Math.round(currentPosition));

            // Schedule next tick.
            setTimeout("EPPZScrollTo.scrollVerticalTickToPosition("+currentPosition+", "+targetPosition+")", (1000 / fps));
        },

        /**
         * For public use.
         *
         * @param id The id of the element to scroll to.
         * @param padding Top padding to apply above element.
         */
        scrollVerticalToElementById: function(id, padding)
        {
            //set default offset from top
            if (padding == null) {
                padding = 140;
            }

            var element = document.getElementById(id);
            if (element == null)
            {
                console.warn('Cannot find element with id \''+id+'\'.');
                return;
            }

            var targetPosition = this.documentVerticalScrollPosition() + this.elementVerticalClientPositionById(id) - padding;
            var currentPosition = this.documentVerticalScrollPosition();

            // Clamp.
            var maximumScrollPosition = this.documentMaximumScrollPosition();
            if (targetPosition > maximumScrollPosition) targetPosition = maximumScrollPosition;

            // Start animation.
            this.scrollVerticalTickToPosition(currentPosition, targetPosition);
        }
    };

function startAnimationAndDisableButton(buttonEl) {
    if (buttonEl != null && $(buttonEl).attr('id') != undefined) {
        var textBtn = $(buttonEl).text();
        var buttonId = $(buttonEl).attr('id');
        if (document.getElementById(buttonId) == null) {
            return;
        }
        document.getElementById(buttonId).innerHTML = "<i class='fa fa-spinner fa-spin fa-lg'></i>" + " " + textBtn;
        $(buttonEl).attr('isActiveBtn', 'false');
    }
}

function stopAnimationAndEnableButton(buttonEl) {
    if (buttonEl != undefined) {
        var textBtn = $(buttonEl).text();
        textBtn.trim();
        $(buttonEl).attr('isActiveBtn', 'true');
        $(buttonEl).text(textBtn);
    }
}

function changeBoolean(domElement) {
    var positiveBlock = $('<i class="fa fa-check fa-2x pickable boolean-picker boolean-positive" aria-hidden="true"></i>');
    var negativeBlock = $('<i class="fa fa-times fa-2x pickable boolean-picker boolean-negative" aria-hidden="true"></i>');
    var element = $(this);
    if (!$(element).hasClass('boolean-value-box')) {
        element = domElement;
    }
    var boolEl = $(element).children()[0];
    var isPositive = $(boolEl).hasClass('boolean-positive');
    $(element).empty();
    isPositive = !isPositive;
    if (isPositive) {
        $(element).append(positiveBlock);
    } else {
        $(element).append(negativeBlock);
    }

    // boolean validation for help label
    var container = $(element).parent().parent();
    if (isPositive) {
        if ($(container).attr('isValidCont') == undefined) {
            $(container).attr('isValidCont', 1);
        } else {
            var value = $(container).attr('isValidCont');
            value = (value * 1) + 1;
            $(container).attr('isValidCont', value);
        }
    } else {
        if ($(container).attr('isValidCont') == undefined) {
            $(container).attr('isValidCont', 0);
        } else {
            var value = $(container).attr('isValidCont');
            value = value - 1;
            $(container).attr('isValidCont', value);
        }
    }
}

function checkBoolean(boolElement) {
    var element = $(boolElement).children()[0];
    if ($(element).hasClass('boolean-positive')) {
        return true;
    }

    return false;
}