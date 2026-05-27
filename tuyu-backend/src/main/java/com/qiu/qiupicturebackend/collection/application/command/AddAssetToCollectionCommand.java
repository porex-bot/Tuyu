package com.qiu.qiupicturebackend.collection.application.command;

import lombok.Data;

@Data
public class AddAssetToCollectionCommand {

    private Long assetId;
    private Long assetVersionId;
    private Long sectionId;
    private String note;
}
