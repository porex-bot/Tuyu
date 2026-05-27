package com.qiu.qiupicturebackend.collection.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CollectionView implements Serializable {

    private Long collectionId;
    private Long workspaceId;
    private String name;
    private String description;
    private CollectionPurpose purpose;
    private CollectionLayout layout;
    private CollectionStatus status;
    private Long coverAssetId;
    private Integer itemCount;
    private Long createdBy;
    private Long updatedBy;
    private Date createdAt;
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}
