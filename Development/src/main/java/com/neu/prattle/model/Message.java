package com.neu.prattle.model;

import java.util.Date;

/***
 * A Basic POJO for Message.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class Message {

  /**
   * Id for identifying record within database.
   */
  private int id;

  /**
   * Date recording keeping track of messages. !!CURRENTLY ONLY YEAR-MONTH-DAY!!.
   */
  private Date dateSent;

  /***
   * The name of the user who sent this message.
   */
  private String from;
  /***
   * The name of the user to whom the message is sent.
   */
  private String to;

  /***
   * It represents the type of the message.
   */
  private String type;

  /***
   * It represents the type of the contents of the message.
   */
  private String contentType;

  /***
   * It represents the contents of the message.
   */
  private String content;

  /***
   * It represents the additional information of the message.
   */
  private String additionalInfo;

  public static MessageBuilder messageBuilder() {
    return new MessageBuilder();
  }

  @Override
  public String toString() {
    return new StringBuilder()
            .append("From: ").append(from)
            .append("To: ").append(to)
            .append("Type: ").append(type)
            .append("ContentType: ").append(contentType)
            .append("Content: ").append(content)
            .append("DateSent: ").append(dateSent)
            .append("additionalInfo: ").append(additionalInfo)
            .toString();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDateSent() {
    return dateSent;
  }

  public void setDateSent(Date date) {
    this.dateSent = date;
  }


  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  /***
   * A Builder helper class to create instances of {@link Message}
   */
  public static class MessageBuilder {
    /***
     * Invoking the build method will return this message object.
     */
    Message message;

    public MessageBuilder() {
      message = new Message();
      message.setFrom("Not set");
    }

    public MessageBuilder setFrom(String from) {
      message.setFrom(from);
      return this;
    }

    public MessageBuilder setTo(String to) {
      message.setTo(to);
      return this;
    }

    public MessageBuilder setId(int id) {
      message.setId(id);
      return this;
    }

    public MessageBuilder setDateSent(Date date) {
      message.setDateSent(date);
      return this;
    }

    public MessageBuilder setAdditionalInfo(String addInfo) {
      message.setAdditionalInfo(addInfo);
      return this;
    }

    public MessageBuilder setType(String type) {
      message.setType(type);
      return this;
    }

    public MessageBuilder setContentType(String contentType) {
      message.setContentType(contentType);
      return this;
    }

    public MessageBuilder setMessageContent(String content) {
      message.setContent(content);
      return this;
    }

    public Message build() {
      // Time stamp message if data is not specified
      if (message.getDateSent() == null) {
        message.setDateSent(new Date(System.currentTimeMillis()));
      }
      return message;
    }
  }
}