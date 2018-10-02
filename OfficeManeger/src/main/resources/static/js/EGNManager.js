var EGNManager = (function (){
    
    var EGN_NOT_VALID = 'ЕГН-то не е валидно';

    function EGNManager() {
        this.PERSON_EGN_REGEX = new RegExp('^\\d{10}$');
        this.indexWeight = [2, 4, 8, 5, 10, 9, 7, 3, 6];
        this.regions = {
            Благоевград: 0,
            Бургас: 44,
            Варна: 94,
            ВеликоТърново: 140,
            Видин: 170,
            Враца: 184,
            Габрово: 218,
            Кърджали: 234,
            Кюстендил: 282,
            Ловеч: 302,
            Монтана: 320,
            Пазарджик: 342,
            Перник: 378,
            Плевен: 396,
            Пловдив: 436,
            Разград: 502,
            Русе: 528,
            Силистра: 556,
            Сливен: 576,
            Смолян: 602,
            Софияград: 624,
            Софияокръг: 722,
            СтараЗагора: 752,
            Добрич: 790,
            Търговище: 822,
            Хасково: 844,
            Шумен: 872,
            Ямбол: 904,
            ДругНеизвестен: 926
        }

    }

    EGNManager.prototype.isValidEGN = function(egn) {
        if (!this.PERSON_EGN_REGEX.test(egn)) {
            return false;
        }

        var sum = 0;
        for (var i = 0; i < 9; i++) {
            var digit = parseInt(egn.charAt(i));
            sum += digit * this.indexWeight[i];
        }

        var lastDigit = egn.charAt(9);
        var resultDigit = sum % 11;
        if (resultDigit > 9) {
            resultDigit = 0;
        }

        if (lastDigit != resultDigit) {
            return false;
        }

        var date = this.getJSDateOfBirth(egn);
        if (date == null) {
            return false;
        }

        return date;
    };

    EGNManager.prototype.getGender = function(egn) {
        var digit = egn.charAt(8);
        if (digit & 1) {
            return 'жена';
        } else {
            return 'мъж';
        }
    };

    EGNManager.prototype.getRegionOfBirth = function(egn) {
        var digit = parseInt(egn.substring(6, 9));
        if (digit > 501) {
            if (digit > 751) {
                if (digit > 843) {
                    if (digit > 903) {
                        if (digit > 925) {
                            return 'Друг/Неизвестен';
                        } else {
                            return 'Ямбол';
                        }
                    } else {
                        if (digit > 871) {
                            return 'Шумен';
                        } else {
                            return 'Хасково';
                        }
                    }
                } else {
                    if (digit > 789) {
                        if (digit > 821) {
                            return 'Търговище';
                        } else {
                            return 'Добрич';
                        }
                    } else {
                        return 'Стара Загора';
                    }
                }
            } else {
                if (digit > 575) {
                    if (digit > 623) {
                        if (digit > 721) {
                            return 'София - окръг';
                        } else {
                            return 'София - град';
                        }
                    } else {
                        if (digit > 601) {
                            return 'Смолян';
                        } else {
                            return 'Сливен';
                        }
                    }
                } else {
                    if (digit > 555) {
                        return 'Силистра';
                    } else {
                        if (digit > 527) {
                            return 'Русе';
                        } else {
                            return 'Разград';
                        }
                    }
                }
            }
        } else {
            if (digit > 233) {
                if (digit > 341) {
                    if (digit > 395) {
                        if (digit > 435) {
                            return 'Пловдив';
                        } else {
                            return 'Плевен';
                        }
                    } else {
                        if (digit > 377) {
                            return 'Перник';
                        } else {
                            return 'Пазарджик';
                        }
                    }
                } else {
                    if (digit > 301) {
                        if (digit > 319) {
                            return 'Монтана';
                        } else {
                            return 'Ловеч';
                        }
                    } else {
                        if (digit > 281) {
                            return 'Кюстендил';
                        } else {
                            return 'Кърджали';
                        }
                    }
                }
            } else {
                if (digit > 169) {
                    if (digit > 183) {
                        if (digit > 217) {
                            return 'Габрово';
                        } else {
                            return 'Враца';
                        }
                    } else {
                        return 'Видин';
                    }
                } else {
                    if (digit > 93) {
                        if (digit > 139) {
                            return 'Велико Търново';
                        } else {
                            return 'Варна';
                        }
                    } else {
                        if (digit > 43) {
                            return 'Бургас';
                        } else {
                            return 'Благоевград';
                        }
                    }
                }
            }
        }

        /* Отделени номера */
        //EGN_REGIONS["Благоевград"]       = 43;  /* от 000 до 043 */
        //EGN_REGIONS["Бургас"]            = 93;  /* от 044 до 093 */
        //EGN_REGIONS["Варна"]             = 139; /* от 094 до 139 */
        //EGN_REGIONS["Велико Търново"]    = 169; /* от 140 до 169 */
        //EGN_REGIONS["Видин"]             = 183; /* от 170 до 183 */
        //EGN_REGIONS["Враца"]             = 217; /* от 184 до 217 */
        //EGN_REGIONS["Габрово"]           = 233; /* от 218 до 233 */
        //EGN_REGIONS["Кърджали"]          = 281; /* от 234 до 281 */
        //EGN_REGIONS["Кюстендил"]         = 301; /* от 282 до 301 */
        //EGN_REGIONS["Ловеч"]             = 319; /* от 302 до 319 */
        //EGN_REGIONS["Монтана"]           = 341; /* от 320 до 341 */
        //EGN_REGIONS["Пазарджик"]         = 377; /* от 342 до 377 */
        //EGN_REGIONS["Перник"]            = 395; /* от 378 до 395 */
        //EGN_REGIONS["Плевен"]            = 435; /* от 396 до 435 */
        //EGN_REGIONS["Пловдив"]           = 501; /* от 436 до 501 */
        //EGN_REGIONS["Разград"]           = 527; /* от 502 до 527 */
        //EGN_REGIONS["Русе"]              = 555; /* от 528 до 555 */
        //EGN_REGIONS["Силистра"]          = 575; /* от 556 до 575 */
        //EGN_REGIONS["Сливен"]            = 601; /* от 576 до 601 */
        //EGN_REGIONS["Смолян"]            = 623; /* от 602 до 623 */
        //EGN_REGIONS["София - град"]      = 721; /* от 624 до 721 */
        //EGN_REGIONS["София - окръг"]     = 751; /* от 722 до 751 */
        //EGN_REGIONS["Стара Загора"]      = 789; /* от 752 до 789 */
        //EGN_REGIONS["Добрич (Толбухин)"] = 821; /* от 790 до 821 */
        //EGN_REGIONS["Търговище"]         = 843; /* от 822 до 843 */
        //EGN_REGIONS["Хасково"]           = 871; /* от 844 до 871 */
        //EGN_REGIONS["Шумен"]             = 903; /* от 872 до 903 */
        //EGN_REGIONS["Ямбол"]             = 925; /* от 904 до 925 */
        //EGN_REGIONS["Друг/Неизвестен"]   = 999; /* от 926 до 999 - Такъв регион понякога се ползва при
        //родени преди 1900, за родени в чужбина
        //или ако в даден регион се родят повече
        //деца от предвиденото. Доколкото ми е
        //известно няма правило при ползването
        //на 926 - 999 */
    };

    EGNManager.prototype.getNumbOfChildrenBeforeHim = function(egn, region) {
        if (!region) {
            region = this.getRegionOfBirth(egn);
        }
        region = region.replace(/[ -//]/g, '');
        var startDigit = this.regions[region];
        var digit = parseInt(egn.substring(6, 9));
        var allNumb = digit - startDigit;

        if (egn.charAt(8) & 1) {
            return 'Преди нея в този ден и регион са се родили ' + parseInt(allNumb / 2) + ' момичета';
        } else {
            return 'Преди него в този ден и регион са се родили ' + parseInt(allNumb / 2) + ' момчета';
        }
    };

    EGNManager.prototype.getJSDateOfBirth = function(egn) {
        var year, month, day;

        var yearAsString = egn.substring(0,2);
        var monthAsString = egn.substring(2,4);
        var dayAsString = egn.substring(4,6);

        if (parseInt(monthAsString) > 40) {
            year = 2000 + parseInt(yearAsString);
            month = parseInt(monthAsString) - 40;
        } else if (parseInt(monthAsString) > 20){
            year = 1800 + parseInt(yearAsString);
            month = parseInt(monthAsString);
        } else {
            year = 1900 + parseInt(yearAsString);
            month = parseInt(monthAsString);
        }

        month = month - 1;
        day = parseInt(dayAsString);
        if (year < 1800 || month < 1 || month > 12 || day < 1 || day > 31) {
            return null;
        }

        return new Date(year, month, day);
    };

    EGNManager.prototype.getDateOfBirthAsString = function(egn, date) {
        if (!date) {
            date = this.getJSDateOfBirth(egn);
        }

        var day = date.getDate();
        if (day < 10) {
            day = '0' + day;
        }

        var month = date.getMonth();
        month += 1;
        if (month < 10) {
            month = '0' + month;
        }

        var year = date.getFullYear();
        return day + '.' + month + '.' + year;
    };

    EGNManager.prototype.getBirthDay = function(egn, date) {
        if (!date) {
            date = this.getJSDateOfBirth(egn);
        }

        var day = date.getDate();
        if (day < 10) {
            day = '0' + day;
        }

        var month = date.getMonth();
        month += 1;
        if (month < 10) {
            month = '0' + month;
        }

        return day + '.' + month;
    };

    EGNManager.prototype.getFullInfo = function(egn, date) {
        var region = this.getRegionOfBirth(egn);
        return new EGNInfo(egn, this.getBirthDay(egn, date), date, this.getDateOfBirthAsString(egn, date), this.getGender(egn), region, this.getNumbOfChildrenBeforeHim(egn, region));
    };

    var manager;

    return {
        getIsValidEGN: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            if (!manager.isValidEGN(egn)) {
                return false;
            }

            return true;
        },
        getGender: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }
            
            if (!manager.isValidEGN(egn)) {
                return EGN_NOT_VALID;
            }

            return manager.getGender(egn);
        },
        getRegionOfBirth: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            if (!manager.isValidEGN(egn)) {
                return EGN_NOT_VALID;
            }

            return manager.getRegionOfBirth(egn);
        },
        getNumbOfChildrenBeforeHim: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            if (!manager.isValidEGN(egn)) {
                return EGN_NOT_VALID;
            }

            return manager.getNumbOfChildrenBeforeHim(egn);
        },
        getJSDateOfBirth: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            var date = manager.isValidEGN(egn);
            if (!date) {
                return EGN_NOT_VALID;
            }

            return date;
        },
        getDateOfBirthAsString: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            var date = manager.isValidEGN(egn);
            if (!date) {
                return EGN_NOT_VALID;
            }

            return manager.getDateOfBirthAsString(egn, date);
        },
        getBirthDay: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            var date = manager.isValidEGN(egn);
            if (!date) {
                return EGN_NOT_VALID;
            }

            return manager.getBirthDay(egn, date);
        },
        getFullInfo: function (egn) {
            if (!manager) {
                manager = new EGNManager();
            }

            var date = manager.isValidEGN(egn);
            if (!date) {
                return EGN_NOT_VALID;
            }

            return manager.getFullInfo(egn, date);
        }
    };
})();

function EGNInfo(egn, birthday, birthDate, birthDateAsString, gender, regionOfBirth, beforeHim) {
    this.egn = egn;
    this.birthday = birthday;
    this.birthDate = birthDate;
    this.birthDateAsString = birthDateAsString;
    this.gender = gender;
    this.regionOfBirth = regionOfBirth;
    this.beforeHim = beforeHim;
}

EGNInfo.prototype.getEGN = function() {
    return this.egn;
};

EGNInfo.prototype.getBirthday = function() {
    return this.birthday;
};

EGNInfo.prototype.getBirthDate = function() {
    return this.birthDate;
};

EGNInfo.prototype.getBirthDateAsString = function() {
    this.birthDateAsString;
};

EGNInfo.prototype.getGender = function() {
    return this.gender;
};

EGNInfo.prototype.getRegionOfBirth = function() {
    return this.regionOfBirth;
};

EGNInfo.prototype.getBeforeHim = function() {
    return this.beforeHim;
};