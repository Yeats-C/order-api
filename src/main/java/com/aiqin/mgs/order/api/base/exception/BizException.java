package com.aiqin.mgs.order.api.base.exception;


import com.aiqin.ground.util.protocol.MessageId;
import org.springframework.http.HttpStatus;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = -2269720460856900019L;

    private String msg;

    private int httpCode=400;

    private int code=0;

    private MessageId messageId;

    public BizException(String msg){
        super(msg);
        this.msg = msg;
    }

    public BizException(String msg, Throwable e){
        super(msg, e);
        this.msg = msg;
    }

    public BizException(HttpStatus httpStatus){
        super(httpStatus.getReasonPhrase());
        this.msg = httpStatus.getReasonPhrase();
        this.httpCode = httpStatus.value();
    }

    public BizException(MessageId messageId){
        super(messageId.getMessage());
        this.messageId=messageId;
        this.msg =messageId.getMessage();
        this.code = Integer.parseInt(messageId.getCode());
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getHttpCode() {
        return httpCode;
    }

    public MessageId getMessageId() {
        return messageId;
    }
}
