package com.ecommerce.spring.common.event;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties("ecommerce.rabbit")
@Validated
public class EcommerceRabbitProperties {
    @NotBlank
    private String publishX;

    @NotBlank
    private String publishDlx;

    @NotBlank
    private String publishDlq;

    @NotBlank
    private String receiveQ;

    @NotBlank
    private String receiveDlx;

    @NotBlank
    private String receiveDlq;

    @NotBlank
    private String receiveRecoverX;

    public String getPublishX() {
        return publishX;
    }

    public void setPublishX(String publishX) {
        this.publishX = publishX;
    }

    public String getPublishDlx() {
        return publishDlx;
    }

    public void setPublishDlx(String publishDlx) {
        this.publishDlx = publishDlx;
    }

    public String getPublishDlq() {
        return publishDlq;
    }

    public void setPublishDlq(String publishDlq) {
        this.publishDlq = publishDlq;
    }

    public String getReceiveQ() {
        return receiveQ;
    }

    public void setReceiveQ(String receiveQ) {
        this.receiveQ = receiveQ;
    }

    public String getReceiveDlx() {
        return receiveDlx;
    }

    public void setReceiveDlx(String receiveDlx) {
        this.receiveDlx = receiveDlx;
    }

    public String getReceiveDlq() {
        return receiveDlq;
    }

    public void setReceiveDlq(String receiveDlq) {
        this.receiveDlq = receiveDlq;
    }

    public String getReceiveRecoverX() {
        return receiveRecoverX;
    }

    public void setReceiveRecoverX(String receiveRecoverX) {
        this.receiveRecoverX = receiveRecoverX;
    }
}
