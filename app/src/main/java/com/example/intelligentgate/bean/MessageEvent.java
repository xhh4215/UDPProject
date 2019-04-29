package com.example.intelligentgate.bean;

/***
 * EventBus 使用的使用的返回的消息的数据类型
 */
public class MessageEvent {
    private String  message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message){
          this.message = message;
    }

}
