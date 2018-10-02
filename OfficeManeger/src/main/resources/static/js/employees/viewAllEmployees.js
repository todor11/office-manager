
function init() {
    $('.delete-employee-button').click(deleteEmployee);
}

function deleteEmployee() {
    var employeeId = $(this).attr('employeeId');
    var employeeNames = $(this).attr('employeeNames');
    var question = 'Сигурен ли си, че искаш да изтриеш ' + employeeNames + ' ?';
    confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteEmployee , employeeId);

}

function confirmedDeleteEmployee(employeeId) {
    $.ajax({
        type: 'DELETE',
        url: '/employees/delete/' + employeeId,
        error: function () {
        },
        success: function () {
            //remove from DOM
            $('#employee_' + employeeId).remove();
        }
    });
}
