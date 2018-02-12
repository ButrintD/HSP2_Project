package com.hsp.realtimechatapplication;

/**
 * Created by Butrint on 12/20/2017.
 */

public class InstantMessage {

    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    // no argument constructor required from Firebase
    public InstantMessage() {
    }

    // also public getters required from Firebase
    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
