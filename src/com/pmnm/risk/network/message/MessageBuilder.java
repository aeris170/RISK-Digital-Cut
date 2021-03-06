package com.pmnm.risk.network.message;

import java.io.Serializable;

public class MessageBuilder {

	private String sender = "";
	private Object data = new Object();
	private MessageType type = MessageType.DEFAULT;

	public Message build() {
		return new Message(sender, data, type);
	}

	public MessageBuilder setSender(String sender) {
		this.sender = sender;
		return this;
	}

	public MessageBuilder setData(Object data) {
		this.data = data;
		return this;
	}

	public MessageBuilder setType(MessageType type) {
		this.type = type;
		return this;
	}

	public static class Message implements Serializable {

		private static final long serialVersionUID = -2276330445489776124L;

		private final String sender;
		private final Object data;
		private final MessageType type;

		Message(String sender, Object data, MessageType type) {
			this.sender = sender;
			this.data = data;
			this.type = type;
		}

		public String getSender() {
			return sender;
		}

		public Object getData() {
			return data;
		}

		public MessageType getType() {
			return type;
		}
	}
	
}
