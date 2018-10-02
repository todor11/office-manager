
var RestPaginationManager =(function (){

    var tableBodyEndId = '-table-body';
    var MAX_NUMB_OF_PAGES_TO_SHOW = 7;

    function PageContainer() {
        this.SEPARATOR = '?page=';
        this.END_OF_LEFT_BTN_ID = '-left-btn';
        this.END_OF_RIGHT_BTN_ID = '-right-btn';
    }

    PageContainer.prototype.addNewPaginationTable = function(url, callbackToSendTableElements) {
        var shortUrl = getShortUrlFromFullUrl(url);
        var urlWithoutPageNumber = url + this.SEPARATOR;
        var leftButtonDom = document.getElementById(shortUrl + this.END_OF_LEFT_BTN_ID);
        var rightButtonDom = document.getElementById(shortUrl + this.END_OF_RIGHT_BTN_ID);
        var newPageInfoBox = new PageInfoBox(shortUrl, urlWithoutPageNumber, leftButtonDom, rightButtonDom, callbackToSendTableElements);
        this[shortUrl] = newPageInfoBox;

        sendAjaxRequest(url, setupPage);
    };

    PageContainer.prototype.getPageInfoObj = function(shortUrl) {
        return this[shortUrl];
    };

    function updatePageInfo(resultPage, url) {
        var shortUrl = getShortUrlFromFullUrl(url);
        var pageInfoObj = pageInfoContainer.getPageInfoObj(shortUrl);
        var callback = pageInfoObj.getCallbackToSendTableElements();
        if (callback instanceof Function) {
            $('#' + shortUrl + '-anim').addClass('dont-display');
            EPPZScrollTo.scrollVerticalToElementById(shortUrl);
            var firstElementCountNumb = 1 + (pageInfoObj.getCurrentPageNumber()) * pageInfoObj.getDefaultNumbOfElementsInPage();
            callback(resultPage.content, firstElementCountNumb);
        }
    }

    function setupPage(resultPage, url) {
        var endOfContainerId = '-container';
        var shortUrl = getShortUrlFromFullUrl(url);
        $('#' + shortUrl + '-anim').addClass('dont-display');

        //set buttons
        var pageBtnContainer = document.getElementById(shortUrl + endOfContainerId);
        var pages = resultPage.totalPages;
        var paginationBlock = document.getElementById(shortUrl);
        var pageInfoObj = pageInfoContainer.getPageInfoObj(shortUrl);
        if (pages < 2) {
            $(paginationBlock).addClass('dont-display');
            var callback = pageInfoObj.getCallbackToSendTableElements();
            if (callback instanceof Function) {
                callback(resultPage.content, 1);
            }
            return;
        } else {
            $(paginationBlock).removeClass('dont-display');
        }

        if (pageBtnContainer != null) {
            var leftBtn = pageInfoObj.getLeftButtonDom();
            var rightBtn = pageInfoObj.getRightButtonDom();

            //set left arrow
            $(leftBtn).addClass('disabled');
            $(leftBtn).click(leftButtonClicked);

            //set right arrow
            $(rightBtn).removeClass('disabled');
            $(rightBtn).click(rightButtonClicked);

            //set pages buttons
            var pagesButtonsArr = [];

            if (pages > MAX_NUMB_OF_PAGES_TO_SHOW) {
                pageInfoObj.setHasMorePagesThenShowing(true);
            }

            var numbOfButtons = pages >= MAX_NUMB_OF_PAGES_TO_SHOW ? MAX_NUMB_OF_PAGES_TO_SHOW : pages;
            for (var i = 0; i < numbOfButtons; i++) {
                var digitToDisplay = i + 1;
                var newPageBtn = $('<li class="page-item" shortUrl="' + pageInfoObj.getShortUrl() + '" pageToCall="' + i + '" id="' + pageInfoObj.getShortUrl() + '-page-btn_' + i + '">' +
                    '<a class="page-link">' + digitToDisplay + '</a>' +
                    '</li>');
                if (i == 0) {
                    $(newPageBtn).addClass('active');
                }
                $(newPageBtn).click(buttonClicked);
                pagesButtonsArr.push(newPageBtn);
                $(newPageBtn).insertBefore($(rightBtn));
            }

            pageInfoObj.setPagesButtonsArr(pagesButtonsArr);
            pageInfoObj.setMaxPageNumber(pages - 1);
            pageInfoObj.setDefaultNumbOfElementsInPage(resultPage.content.length);
            var callback = pageInfoObj.getCallbackToSendTableElements();
            if (callback instanceof Function) {
                callback(resultPage.content, 1);
            }
        }
    }

    function leftButtonClicked() {
        if ($(this).hasClass('disabled')) {
            return;
        }

        var shortUrl = $(this).attr('shortUrl');

        //empty table
        var tableBody = document.getElementById(shortUrl + tableBodyEndId);
        $(tableBody).empty();
        $('#' + shortUrl + '-anim').removeClass('dont-display');

        var pageInfoBox = pageInfoContainer.getPageInfoObj(shortUrl);

        $('#' + pageInfoBox.getShortUrl() + '-container .page-item').removeClass('active');
        if (pageInfoBox.getHasMorePagesThenShowing()) {
            if (pageInfoBox.getPagesButtonsArr()[0].attr('pageToCall') != 0) {
                var btnsArr = pageInfoBox.getPagesButtonsArr();
                for (var i = 0; i < btnsArr.length; i++) {
                    var btn = btnsArr[i];
                    $(btn).attr('id', pageInfoBox.getShortUrl() + '-page-btn_' + i);
                    $(btn).attr('pageToCall', i);
                    $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + i + ' a').text(i + 1);
                }
            }
        }

        var newPageNumber = 0;
        $(this).addClass('disabled');

        var rightBttn = pageInfoBox.getRightButtonDom();
        if ($(rightBttn).hasClass('disabled')) {
            $(rightBttn).removeClass('disabled');
        }

        var newActiveButtn = $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + newPageNumber);
        $(newActiveButtn).addClass('active');

        var fullUrl = pageInfoBox.getUrlWithoutPageNumber(shortUrl) + newPageNumber;

        pageInfoBox.setCurrentPageNumber(newPageNumber);
        sendAjaxRequest(fullUrl, updatePageInfo);
    }

    function rightButtonClicked() {
        if ($(this).hasClass('disabled')) {
            return;
        }

        var shortUrl = $(this).attr('shortUrl');

        //empty table
        var tableBody = document.getElementById(shortUrl + tableBodyEndId);
        $(tableBody).empty();
        $('#' + shortUrl + '-anim').removeClass('dont-display');

        var pageInfoBox = pageInfoContainer.getPageInfoObj(shortUrl);

        $('#' + pageInfoBox.getShortUrl() + '-container .page-item').removeClass('active');
        if (pageInfoBox.getHasMorePagesThenShowing()) {
            if (pageInfoBox.getPagesButtonsArr()[MAX_NUMB_OF_PAGES_TO_SHOW - 1].attr('pageToCall') != pageInfoBox.getMaxPageNumber()) {
                var btnsArr = pageInfoBox.getPagesButtonsArr();
                var pageNumb = pageInfoBox.getMaxPageNumber();
                for (var i = btnsArr.length - 1; i >= 0; i--) {
                    var btn = btnsArr[i];
                    $(btn).attr('id', pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb);
                    $(btn).attr('pageToCall', pageNumb);
                    $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb + ' a').text(pageNumb + 1);
                    pageNumb--;
                }
            }
        }

        var newPageNumber = pageInfoBox.getMaxPageNumber();
        $(this).addClass('disabled');

        var leftBttn = pageInfoBox.getLeftButtonDom();
        if ($(leftBttn).hasClass('disabled')) {
            $(leftBttn).removeClass('disabled');
        }

        var newActiveButtn = $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + newPageNumber);
        $(newActiveButtn).addClass('active');

        var fullUrl = pageInfoBox.getUrlWithoutPageNumber(shortUrl) + newPageNumber;

        pageInfoBox.setCurrentPageNumber(newPageNumber);
        sendAjaxRequest(fullUrl, updatePageInfo);
    }

    function buttonClicked() {
        if ($(this).hasClass('active')) {
            return;
        }

        var shortUrl = $(this).attr('shortUrl');

        //empty table
        var tableBody = document.getElementById(shortUrl + tableBodyEndId);
        $(tableBody).empty();
        $('#' + shortUrl + '-anim').removeClass('dont-display');

        var pageInfoBox = pageInfoContainer.getPageInfoObj(shortUrl);
        var newPageIndex = $(this).attr('pageToCall');
        if (isNaN(parseInt(newPageIndex))) {
            return;
        } else {
            newPageIndex = parseInt(newPageIndex);
        }

        $('#' + pageInfoBox.getShortUrl() + '-container .page-item').removeClass('active');
        if (pageInfoBox.getHasMorePagesThenShowing()) {
            if (newPageIndex < (MAX_NUMB_OF_PAGES_TO_SHOW / 2)) {
                if (pageInfoBox.getPagesButtonsArr()[0].attr('pageToCall') != 0) {
                    var btnsArr = pageInfoBox.getPagesButtonsArr();
                    for (var i = 0; i < btnsArr.length; i++) {
                        var btn = btnsArr[i];
                        $(btn).attr('id', pageInfoBox.getShortUrl() + '-page-btn_' + i);
                        $(btn).attr('pageToCall', i);
                        $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + i + ' a').text(i + 1);
                    }
                }
            } else if (newPageIndex > (pageInfoBox.getMaxPageNumber() - (MAX_NUMB_OF_PAGES_TO_SHOW / 2))) {
                if (pageInfoBox.getPagesButtonsArr()[MAX_NUMB_OF_PAGES_TO_SHOW - 1].attr('pageToCall') != pageInfoBox.getMaxPageNumber()) {
                    var btnsArr = pageInfoBox.getPagesButtonsArr();
                    var pageNumb = pageInfoBox.getMaxPageNumber();
                    for (var i = btnsArr.length - 1; i >= 0; i--) {
                        var btn = btnsArr[i];
                        $(btn).attr('id', pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb);
                        $(btn).attr('pageToCall', pageNumb);
                        $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb + ' a').text(pageNumb + 1);
                        pageNumb--;
                    }
                }
            } else {
                var btnsArr = pageInfoBox.getPagesButtonsArr();
                var pageNumb = parseInt(newPageIndex + 1 - (MAX_NUMB_OF_PAGES_TO_SHOW / 2));
                for (var i = 0; i < btnsArr.length; i++) {
                    var btn = btnsArr[i];
                    $(btn).attr('id', pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb);
                    $(btn).attr('pageToCall', pageNumb);
                    $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + pageNumb + ' a').text(pageNumb + 1);
                    pageNumb++;
                }
            }
        }

        $('#' + pageInfoBox.getShortUrl() + '-page-btn_' + newPageIndex).addClass('active');
        pageInfoBox.setCurrentPageNumber(newPageIndex);

        var leftBttn = pageInfoBox.getLeftButtonDom();
        if (newPageIndex > 0) {
            $(leftBttn).removeClass('disabled');
        } else {
            $(leftBttn).addClass('disabled');
        }

        var rightBttn = pageInfoBox.getRightButtonDom();
        if (newPageIndex < pageInfoBox.getMaxPageNumber()) {
            $(rightBttn).removeClass('disabled');
        } else {
            $(rightBttn).addClass('disabled');
        }

        var fullUrl = pageInfoBox.getUrlWithoutPageNumber(shortUrl) + newPageIndex;
        sendAjaxRequest(fullUrl, updatePageInfo);
    }

    function getShortUrlFromFullUrl(url) {
        var splittedUrl = url.split('/');
        var shortUrl = splittedUrl[splittedUrl.length - 1];
        if (shortUrl.indexOf('?') != -1) {
            var index = shortUrl.indexOf('?');
            shortUrl = shortUrl.substring(0, index);
        }
        return shortUrl;
    }

    var pageInfoContainer;

    return {
        addNewPaginationTable: function (shortUrl, callbackToSendTableElements) {
            if (!pageInfoContainer) {
                pageInfoContainer = new PageContainer();
            }

            pageInfoContainer.addNewPaginationTable(shortUrl, callbackToSendTableElements);
        }
    };
})();

function sendAjaxRequest (url, callback){
    $.ajax({
        type: 'GET',
        url: url,
        data: 'json',
        success: function (resultPage) {
            if (callback instanceof Function) {
                callback(resultPage, url);
            }
        }
    });
}

//page obj{
// content: Array[1],
// last: false,
// totalPages: 4,
// totalElements: 4,
// size: 1,
// number: 0,
// sort: null,
// numberOfElements: 1,
// first: true
//}