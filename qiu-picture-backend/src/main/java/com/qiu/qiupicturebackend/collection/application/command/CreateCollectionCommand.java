package com.qiu.qiupicturebackend.collection.application.command;

import lombok.Data;

@Data
public class CreateCollectionCommand {

    private String name;
    private String description;
    private String purpose = "project";
    private String layout = "grid";
}
