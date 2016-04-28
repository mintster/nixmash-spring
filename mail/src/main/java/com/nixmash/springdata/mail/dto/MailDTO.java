package com.nixmash.springdata.mail.dto;


/**
 * Created by daveburke on 4/28/16.
 */
public class MailDTO {

    private String from;
    private String fromName;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private String templateName;

    public static enum Type {
        PLAIN,
        HTML
    };

    private Type type = Type.PLAIN;

    public MailDTO() {
    }

    private MailDTO(MailDTO m) {
        this.type = m.type;
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

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @Override
    public String toString() {

        return "MailDTO{" +
                "from=" + from +
                ", fromName=" + fromName +
                ", to=" + to +
                ", cc='" + cc + '\'' +
                ", bcc='" + bcc + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", templateName='" + templateName + '\'' +
                ", type=" + type +
                '}';
    }

}
