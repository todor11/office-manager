function PageInfoBox(shortUrl, urlWithoutPageNumber, leftButtonDom, rightButtonDom, callbackToSendTableElements) {
    this.shortUrl = shortUrl;
    this.urlWithoutPageNumber = urlWithoutPageNumber;
    this.currentPageNumber = 0;
    this.maxPageNumber = 0;
    this.leftButtonDom = leftButtonDom;
    this.rightButtonDom = rightButtonDom;
    this.callbackToSendTableElements = callbackToSendTableElements;
    this.defaultNumbOfElementsInPage = 0;
    this.hasMorePagesThenShowing = false;
    this.pagesButtonsArr = [];
}

PageInfoBox.prototype.getCurrentPageNumber = function() {
    return this.currentPageNumber;
};

PageInfoBox.prototype.setCurrentPageNumber = function(currentPage) {
    this.currentPageNumber = currentPage;
};

PageInfoBox.prototype.getShortUrl = function() {
    return this.shortUrl;
};

PageInfoBox.prototype.getUrlWithoutPageNumber = function() {
    return this.urlWithoutPageNumber;
};

PageInfoBox.prototype.getLeftButtonDom = function() {
    return this.leftButtonDom;
};

PageInfoBox.prototype.getRightButtonDom = function() {
    return this.rightButtonDom;
};

PageInfoBox.prototype.getCallbackToSendTableElements = function() {
    return this.callbackToSendTableElements;
};

PageInfoBox.prototype.getPagesButtonsArr = function() {
    return this.pagesButtonsArr;
};

PageInfoBox.prototype.setPagesButtonsArr = function(pagesButtonsArr) {
    this.pagesButtonsArr = pagesButtonsArr;
};

PageInfoBox.prototype.getMaxPageNumber = function() {
    return this.maxPageNumber;
};

PageInfoBox.prototype.setMaxPageNumber = function(maxPageNumber) {
    this.maxPageNumber = maxPageNumber;
};

PageInfoBox.prototype.getDefaultNumbOfElementsInPage = function() {
    return this.defaultNumbOfElementsInPage;
};

PageInfoBox.prototype.setDefaultNumbOfElementsInPage = function(defaultNumbOfElementsInPage) {
    this.defaultNumbOfElementsInPage = defaultNumbOfElementsInPage;
};

PageInfoBox.prototype.getHasMorePagesThenShowing = function() {
    return this.hasMorePagesThenShowing;
};

PageInfoBox.prototype.setHasMorePagesThenShowing = function(hasMorePagesThenShowing) {
    this.hasMorePagesThenShowing = hasMorePagesThenShowing;
};
