function RegExPatternConstants() {
}

RegExPatternConstants.NAME = new RegExp('^([\\u0410-\\u044Fa-zA-Z0-9\\.&"\'\\- ]){0,50}$');
RegExPatternConstants.DROPDOWN = new RegExp('^[1-9][0-9]{0,18}$');
RegExPatternConstants.DATE = new RegExp('^(0[0-9]|1[0-9]|2[0-9]|3[0-1])\\.(0[0-9]|1[0-2])\\.20[1-9][0-9]$');
RegExPatternConstants.EMAIL = new RegExp('^([a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3})?$');
RegExPatternConstants.USERNAME = new RegExp('^[a-zA-Z0-9_]{5,30}$');
RegExPatternConstants.PERSON_SINGLE_NAME = new RegExp('^[\\u0410-\\u044Fa-zA-Z]{2,50}$');
RegExPatternConstants.PERSON_SINGLE_NAME_OR_EMPTY = new RegExp('^[\\u0410-\\u044Fa-zA-Z]{0,50}$');
RegExPatternConstants.PERSON_FULL_NAME = new RegExp('^[\\u0410-\\u044Fa-zA-Z]+( [\\u0410-\\u044Fa-zA-Z]+){0,2}$');
RegExPatternConstants.PERSON_EGN = new RegExp('^\\d{10,10}$');
RegExPatternConstants.PHONE_NUMBER = new RegExp('^(\\+|00)?\\d{5,13}$');
RegExPatternConstants.PASSWORD = new RegExp('^.{5,30}$');
RegExPatternConstants.DATE_TIME = new RegExp('^(0[0-9]|1[0-9]|2[0-9]|3[0-1])\\.(0[0-9]|1[0-2])\\.20[1-9][0-9] (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$');
RegExPatternConstants.TIME = new RegExp('^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$');
RegExPatternConstants.TEXT = new RegExp('^[\\u0410-\\u044Fa-zA-Z0-9\\., -_;:!\\?]{0,255}$');
RegExPatternConstants.BASIC_TEXT = new RegExp('^[\\u0410-\\u044Fa-zA-Z0-9\\., -_;:!\\?]{3,255}$');
RegExPatternConstants.BASIC_TEXT_EMPTY = new RegExp('^[\\u0410-\\u044Fa-zA-Z0-9\\., -_;:!\\?]{0,255}$');
RegExPatternConstants.BASIC_CYRILLIC_TEXT = new RegExp('^[\\u0410-\\u044F0-9\\., -_;:!\\?]{3,255}$');
RegExPatternConstants.BASIC_CYRILLIC_TEXT_EMPTY = new RegExp('^[\\u0410-\\u044F0-9\\., -_;:!\\?]{0,255}$');
RegExPatternConstants.POSITIVE_NUMBER = new RegExp('^\\d{1,3}$');
RegExPatternConstants.NUMBER = new RegExp('^-?\\d+$');