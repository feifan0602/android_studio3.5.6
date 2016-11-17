package com.sun3d.culturalShanghai.object;

/**
 * 
 * @author liningkang 我的消息
 */
public class MessageInfo {
	// 消息标题
	private String messageType;
	// 消息内容
	private String messageContent;

	private String userMessageId;

	public String getUserMessageId() {
		return userMessageId;
	}

	public void setUserMessageId(String userMessageId) {
		this.userMessageId = userMessageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	@Override
	public String toString() {
		return "MessageInfo [messageType=" + messageType + ", messageContent=" + messageContent
				+ "]";
	}

}
