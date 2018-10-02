function init() {
    $('.pickableWorkScheduleCell').on('click', changeSelectedCell);

}

function changeSelectedCell() {
    $('.pickableWorkScheduleCell').removeClass('activeWorkScheduleCell');
    if ($(this).hasClass('disableShiftCell')) {
        var msg = 'В този ден няма смяна която да е подходяща!';
        negativeAlertBox(null, msg, null, null);
        return;
    }

    $(this).addClass('activeWorkScheduleCell');

    //show possible employees
    $('.workScheduleChooseEmployeeShiftBox').css('left', "-1490px");
    var possibleEmployeesBoxId = $(this).attr('possibleEmployeesBoxId');
    //var position = $(this).offset();
    //var boxPosition = position.left - 20;
    //boxPosition += "px";
    $('#' + possibleEmployeesBoxId).css('left', '300px');

    var shiftGroup
}