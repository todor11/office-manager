var DateCalculator = (function (){

    var DEFAULT_DATE_SEPARATOR = '.';

    function DateCalculator() {
    }

    DateCalculator.prototype.getNextDayAsJSDateObj = function(date) {
        return this.addDays(date, 1);
    };

    DateCalculator.prototype.getNextDayAsString = function(date, separator) {
        var nextDay = this.getNextDayAsJSDateObj(date);
        return this.convertDateToString(nextDay, separator);
    };

    DateCalculator.prototype.convertDateAsStringToJSDate = function(dateAsString, separator) {
        var pattern = '(\\d{2})' + separator + '(\\d{2})' + separator + '(\\d{4})';
        var regEx = new RegExp(pattern);
        return new Date(dateAsString.replace(regEx,'$3-$2-$1'));
    };

    DateCalculator.prototype.convertDateToString = function(date, separator) {
        var day = date.getDate();
        if (day < 10) {
            day = '0' + day;
        }
        var month = date.getMonth() + 1;
        if (month < 10) {
            month = '0' + month;
        }
        var year = date.getFullYear();

        return day + separator + month + separator + year;
    };

    DateCalculator.prototype.addDays = function(date, amount) {
        var tzOff = date.getTimezoneOffset() * 60 * 1000,
            t = date.getTime(),
            d = new Date(),
            tzOff2;

        t += (1000 * 60 * 60 * 24) * amount;
        d.setTime(t);

        tzOff2 = d.getTimezoneOffset() * 60 * 1000;
        if (tzOff != tzOff2) {
            var diff = tzOff2 - tzOff;
            t += diff;
            d.setTime(t);
        }

        return d;
    };

    DateCalculator.prototype.getNextDayAsStringFromDateAsString = function(dateAsString, separator) {
        var date = this.convertDateAsStringToJSDate(dateAsString, separator);
        if (date instanceof Date){
            return this.getNextDayAsString(date, separator);
        }

        return null;
    };

    DateCalculator.prototype.isStartDateBeforeEndDate = function(startDateAsString, endDateAsString, separator) {
        var startDate = this.convertDateAsStringToJSDate(startDateAsString, separator);
        var endDate = this.convertDateAsStringToJSDate(endDateAsString, separator);
        if (!(startDate instanceof Date) || !(endDate instanceof Date)){
            return false;
        }

        return startDate < endDate;
    };

    DateCalculator.prototype.isStartDateBeforeOrEqualEndDate = function(startDateAsString, endDateAsString, separator) {
        var startDate = this.convertDateAsStringToJSDate(startDateAsString, separator);
        var endDate = this.convertDateAsStringToJSDate(endDateAsString, separator);
        if (!(startDate instanceof Date) || !(endDate instanceof Date)){
            return false;
        }

        return startDate <= endDate;
    };

    var dateCalculator;

    return {
        getNextDayAsJSDateObj: function (date) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (!(date instanceof Date)) {
                return null;
            }

            return dateCalculator.getNextDayAsJSDateObj(date);
        },
        getNextDayAsString: function (date, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            if (!(date instanceof Date)) {
                return null;
            }


            return dateCalculator.getNextDayAsString(date, separator);
        },
        getNextDayAsStringFromDateAsString: function (dateAsString, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            return dateCalculator.getNextDayAsStringFromDateAsString(dateAsString, separator);
        },
        convertDateAsStringToJSDate: function (dateAsString, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            return dateCalculator.convertDateAsStringToJSDate(dateAsString, separator);
        },
        convertDateToString: function (date, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            return dateCalculator.convertDateToString(date, separator);
        },
        getDateMinusDays: function (date, daysToSubtract) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            daysToSubtract = daysToSubtract * (-1);
            return dateCalculator.addDays(date, daysToSubtract);
        },
        getDatePlusDays: function (date, daysToAdd) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            return dateCalculator.addDays(date, daysToAdd);
        },
        isStartDateBeforeEndDate: function (startDate, endDate, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            return dateCalculator.isStartDateBeforeEndDate(startDate, endDate, separator);
        },
        isStartDateBeforeOrEqualEndDate: function (startDate, endDate, separator) {
            if (!dateCalculator) {
                dateCalculator = new DateCalculator();
            }

            if (separator == undefined) {
                separator = DEFAULT_DATE_SEPARATOR;
            }

            return dateCalculator.isStartDateBeforeOrEqualEndDate(startDate, endDate, separator);
        }
    };
})();
