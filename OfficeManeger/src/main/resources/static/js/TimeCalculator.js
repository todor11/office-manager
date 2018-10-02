var TimeCalculator = (function (){

    function TimeCalculator() {
        this.DEFAULT_TIME_SEPARATOR = ':';
        this.REGEX_TIME = new RegExp('^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$');
    }

    TimeCalculator.prototype.validateTime = function(timeAsString) {
        return this.REGEX_TIME.test(timeAsString);
    };

    TimeCalculator.prototype.isFirstTimeBeforeSecondTime = function(firstTimeAsString, secondTimeAsString) {
        if (this.validateTime(firstTimeAsString) && this.validateTime(secondTimeAsString)) {
            var firstMatch = this.REGEX_TIME.exec(firstTimeAsString);
            var secondMatch = this.REGEX_TIME.exec(secondTimeAsString);
            var firstHour = firstMatch[1];
            firstHour = parseInt(firstHour);
            var secondHour = secondMatch[1];
            secondHour = parseInt(secondHour);

            if (firstHour < secondHour) {
                return true;
            } else if (firstHour > secondHour) {
                return false;
            } else {
                var firstMinute = firstMatch[2];
                firstMinute = parseInt(firstMinute);
                var secondMinute = secondMatch[2];
                secondMinute = parseInt(secondMinute);

                if (firstMinute < secondMinute) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return null;
        }
    };

    TimeCalculator.prototype.convertTimeToString = function(time) {
        if (time instanceof Date) {
            var hour = time.getHours();
            if (hour < 10) {
                hour = "0" + hour;
            }

            var minute = time.getMinutes();
            if (minute < 10) {
                minute = "0" + minute;
            }

            return hour + this.DEFAULT_TIME_SEPARATOR + minute;
        } else if (time instanceof Array) {
            if (time.length == 2 && !isNaN(parseInt(time[0])) && !isNaN(parseInt(time[1]))) {
                var hour = parseInt(time[0]);
                if (hour < 10) {
                    hour = "0" + hour;
                }

                var minute = parseInt(time[1]);
                if (minute < 10) {
                    minute = "0" + minute;
                }

                return hour + this.DEFAULT_TIME_SEPARATOR + minute;
            }
        }

        return "";
    };


    var timeCalculator;

    return {
        isFirstTimeBeforeSecondTime: function (firstTimeAsString, secondTimeAsString) {
            if (!timeCalculator) {
                timeCalculator = new TimeCalculator();
            }

            return timeCalculator.isFirstTimeBeforeSecondTime(firstTimeAsString, secondTimeAsString);
        },
        convertTimeToString: function (time) {
            if (!timeCalculator) {
                timeCalculator = new TimeCalculator();
            }

            return timeCalculator.convertTimeToString(time);
        }

    };
})();
