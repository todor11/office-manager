
function init() {
    //rank
    if (document.getElementById('addRankButton') != null) {
        $('#addRankButton').click(showAddRanksInputs);
        $('#rankBtnSave').click(saveRank);
        $('#rankBtnCancel').click(hideAddRanksInputs);
        $('.rank-container').on('keyup', keyUpFunctionRank);
    }
}

//Show Add Ranks
function showAddRanksInputs() {
    $('#addRankInputs').show();
}

//Hide Add Ranks
function hideAddRanksInputs() {
    $('#addRankInputs').hide();
}

//Add Rank to DOM
function addRankDOM(rank) {
    var id = rank.id;
    var name = rank.name;
    var rankRate = rank.rankRate;
    $('#rankList').append(
        $('<div></div>')
            .addClass('row rank-container')
            .attr('rank_id', id)
            .append(
                $('<input/>')
                    .addClass('updateNameClass form-control col-sm-6')
                    .attr('oldData', name)
                    .val(name))
            .append(
                $('<input/>')
                    .addClass('updateRankRateClass form-control col-sm-4')
                    .attr('type', 'number')
                    .val(rankRate))
            .on('keyup', keyUpFunctionRank)
    );

    hideAddRanksInputs();
}

function updateRank(rank, currentDOMRank) {
    var rankId = rank.id;
    $.ajax({
        type: 'PUT',
        url: '/ranks/' + rankId,
        data: JSON.stringify(rank),
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                    restoreInputRankData(currentDOMRank);
                }
            }
        },
        success: function () {
            var message = 'Успешно ъпдейтва ' + rank.name;
            positiveAlertBox(null, message, null, null);
            updateInputRankAttr(currentDOMRank);
        }
    })
}

function updateInputRankAttr(currentDOMRank) {
    var nameInput = currentDOMRank.find('.updateNameClass');
    nameInput.attr('oldData', nameInput.val());
}

function restoreInputRankData(currentDOMRank) {
    var nameInput = currentDOMRank.find('.updateNameClass');
    nameInput.val(nameInput.attr('oldData'));
}

function deleteRank(rankId) {
    $.ajax({
        type: 'DELETE',
        url: '/ranks/delete/' + rankId
    })
}

//Save the rank
function saveRank() {
    var rank = {};
    rank.name = $('#newRankInputName').val();
    rank.rankRate = $('#newRankInputRankRate').val();

    $.ajax({
        type: 'POST',
        url: '/ranks/add',
        data: JSON.stringify(rank),
        dataType: 'json',
        contentType: 'application/json',
        error: function (response) {
            var errorJSON = response.responseJSON;
            if (errorJSON != undefined) {
                if (errorJSON.hasOwnProperty('message') && errorJSON.hasOwnProperty('type') && errorJSON.hasOwnProperty('fieldName')
                    && errorJSON.type == 'ERROR') {
                    negativeAlertBox(null, errorJSON.message, null, null);
                }
            }
        },
        success: function (rank) {
            var message = 'Успешно добави ' + rank.name;
            positiveAlertBox(null, message, null, null);
            addRankDOM(rank);
        }
    });
}

function changeAndUpdateRank(currentDOMRank) {
    var rank = {};
    rank.id = currentDOMRank.attr('rank_id');
    rank.name = currentDOMRank.find('.updateNameClass').val();
    rank.rankRate = currentDOMRank.find('.updateRankRateClass').val();
    updateRank(rank, currentDOMRank);
}

function keyUpFunctionRank(e) {
    var currentDOMRank = $(this);
    if (e.which == 27) {
        var question = 'Сигурен ли си, че искаш да изтриеш ' + currentDOMRank.find('.updateNameClass').val() + ' ?';
        confirmBox('Внимание !', question, 'Отмени', null, 'Изтрий!', 'btn-danger', confirmedDeleteRankFunction , currentDOMRank);
    } else if (e.which == 13) {
        changeAndUpdateRank(currentDOMRank);
    }
}

function confirmedDeleteRankFunction(currentDOMRank) {
    var rank_id = currentDOMRank.attr('rank_id');
    //Delete DOM Rank
    currentDOMRank.remove();
    //Delete in MySQL
    deleteRank(rank_id);
}

