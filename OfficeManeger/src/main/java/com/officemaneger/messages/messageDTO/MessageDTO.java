package com.officemaneger.messages.messageDTO;

import com.officemaneger.enums.MessageType;

public class MessageDTO {

    private String message;

    private MessageType type;

    private String fieldName;

    public MessageDTO() {
        super();
    }

    public MessageDTO(MessageType type, String message, String fieldName) {
        super();
        this.message = message;
        this.type = type;
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
