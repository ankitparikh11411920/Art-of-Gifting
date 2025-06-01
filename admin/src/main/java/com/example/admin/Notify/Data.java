package com.example.admin.Notify;

public class Data {
    private String Title;
    private String Message;
    private String Extra;

    public Data(String title, String message, String extra) {
        Title = title;
        Message = message;
        Extra = extra;
    }


    public Data() {
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
