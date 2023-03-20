package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageDTO {
    private String messageNumber;
    private String chatNumber;
    private String messageSender;
    private String messageReceiver;
    private String messageContent;
    private Date messageTime;
    
    public Map<String, String> getChatList() {
		Map<String, String> chat = new HashMap<String, String>();
		chat.put("chatNumber", chatNumber);
		chat.put("messageNumber", messageNumber);
		chat.put("messageSender", messageSender);
		chat.put("messageReceiver", messageReceiver);
		chat.put("messageContent", messageContent);
		chat.put("messageTime", getMessageTime());
		return chat;
	}

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }
    
    public String getChatNumber() {
        return chatNumber;
    }

    public void setChatNumber(String chatNumber) {
        this.chatNumber = chatNumber;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
        String strMessageTime = dateFormat.format(messageTime);
        return strMessageTime;

    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "messageNumber='" + messageNumber + '\'' +
                ", messageSender='" + messageSender + '\'' +
                ", messageReceiver='" + messageReceiver + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageTime=" + getMessageTime() +
                '}';
    }

}
