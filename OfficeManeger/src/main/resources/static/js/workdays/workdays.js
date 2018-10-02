
function init() {
    if (document.getElementById('btnAddWorkDay') != null) {
        $('#btnAddWorkDay').click(saveNewWorkDay);
        $('#btnAddHoliday').click(saveNewHoliDay);
        $('.delete-workday-button').click(deleteWorkday);

        $( "#newWorkDayInput" ).datepicker({ dateFormat: 'dd.mm.yy' } );
        $( "#newWorkDayInput" ).change(changeAddWorkdayButton);
        $( "#newHolidayInput" ).datepicker({ dateFormat: 'dd.mm.yy' } );
        $( "#newHolidayInput" ).change(changeAddHolidayButton);
    }
}

//Add new workday
function saveNewWorkDay() {
    var newWorkday = {};
    newWorkday.date = $('#newWorkDayInput').val();
    $.ajax({
        type: 'POST',
        url: '/workdays/addWorkday',
        data: JSON.stringify(newWorkday),
        dataType: 'json',
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    alert(errorJSON.message);
                }
            }
        },
        success: function (newWorkday) {
            addNewWorkdayDOM(newWorkday);
        }
    });
}

function addNewWorkdayDOM(newWorkday) {
    var date = newWorkday.date;
    var year = date[0];
    var month = date[1];
    if (month < 10) {
        month = '0' + month;
    }
    var day = date[2];
    if (day < 10) {
        day = '0' + day;
    }
    var dateToShow = day + '.' + month + '.' + year + ' г.';

    var id = newWorkday.id;
    id = 'workDay_' + id;
    var trId = 'tr_' + newWorkday.id;

    $('#workdays-table-body')
        .append($('<tr></tr>')
            .attr('id', trId)
            .append($('<td></td>')
                .text(dateToShow))
            .append($('<td></td>')
                .append($('<a></a>')
                    .addClass('btn')
                    .addClass('btn-outline-danger')
                    .addClass('btn-sm')
                    .addClass('delete-workday-button')
                    .attr('id', id)
                    .text('Delete')
                    .click(deleteWorkday))));
}

//Add new holiday
function saveNewHoliDay() {
    var newHoliday = {};
    newHoliday.date = $('#newHolidayInput').val();
    $.ajax({
        type: 'POST',
        url: '/workdays/addHoliday',
        data: JSON.stringify(newHoliday),
        dataType: 'json',
        contentType: 'application/json',
        success: function (newHoliday) {
            addNewHolidayDOM(newHoliday);
        }
    });
}

function addNewHolidayDOM(newHoliday) {
    var date = newHoliday.date;
    var year = date[0];
    var month = date[1];
    if (month < 10) {
        month = '0' + month;
    }
    var day = date[2];
    if (day < 10) {
        day = '0' + day;
    }
    var dateToShow = day + '.' + month + '.' + year + ' г.';

    var id = newHoliday.id;
    id = 'workDay_' + id;
    var trId = 'tr_' + newHoliday.id;

    $('#holidays-table-body')
        .append($('<tr></tr>')
            .attr('id', trId)
            .append($('<td></td>')
                .text(dateToShow))
            .append($('<td></td>')
                .append($('<a></a>')
                    .addClass('btn')
                    .addClass('btn-outline-danger')
                    .addClass('btn-sm')
                    .addClass('delete-workday-button')
                    .attr('id', id)
                    .text('Delete')
                    .click(deleteWorkday))));
}

function deleteWorkday(e) {
    var element = e.target;
    var workdayId = element.id.split('_')[1];
    $.ajax({
        type: 'DELETE',
        url: "/workdays/delete/" + workdayId
    })

    var mainElementId = 'tr_' + workdayId;
    var elToDel = document.getElementById(mainElementId);
    elToDel.remove();
}

function changeAddWorkdayButton() {
    var inputValue = $('#newWorkDayInput').val();
    if ((inputValue != undefined) && (inputValue != '')){
        $('#btnAddWorkDay').removeClass('disabled');
    } else {
        $('#btnAddWorkDay').addClass('disabled');
    }
}

function changeAddHolidayButton() {
    var inputValue = $('#newHolidayInput').val();
    if ((inputValue != undefined) && (inputValue != '')){
        $('#btnAddHoliday').removeClass('disabled');
    } else {
        $('#btnAddHoliday').addClass('disabled');
    }
}
