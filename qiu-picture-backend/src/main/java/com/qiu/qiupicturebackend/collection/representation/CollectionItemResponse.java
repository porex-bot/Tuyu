package com.qiu.qiupicturebackend.collection.representation;

import lombok.Data;

@Data
public class CollectionItemResponse {

    private Long itemId;
    private Long collectionId;
    private Long assetId;
    private Long assetVersionId;
    private Long sectionId;
    private Long sortOrder;
    private String note;
    private Long addedBy;
    private String addedAt;
}
