package Controller;

import javax.sound.midi.Receiver;

public class Message2 {
    private String sender;
    private String receiver;
    private String content;
    private String sendTime; // 添加发送时间字段

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    // 必须提供默认构造方法，以便 Firebase 数据库进行反序列化
    public Message2() {
    }

    public Message2(String sender,String receiver,String content,String sendTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
    }

    // Getter 和 Setter 方法
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

