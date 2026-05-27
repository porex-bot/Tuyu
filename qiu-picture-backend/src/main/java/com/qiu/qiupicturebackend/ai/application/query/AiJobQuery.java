package com.qiu.qiupicturebackend.ai.application.query;

public class AiJobQuery {

    private int offset = 0;
    private int limit = 20;
    private String status;

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
