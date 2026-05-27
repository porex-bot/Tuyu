package com.qiu.qiupicturebackend.collection.representation;

import lombok.Data;

@Data
public class CollectionResponse {

    private Long collectionId;
    private Long workspaceId;
    private String name;
    private String description;
    private String purpose;
    private String layout;
    private String status;
    private Long coverAssetId;
    private Integer itemCount;
    private Long createdBy;
    private String createdAt;
    private String updatedAt;
}
