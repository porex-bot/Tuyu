package com.qiu.qiupicturebackend.collection.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CollectionItemView implements Serializable {

    private Long itemId;
    private Long collectionId;
    private Long assetId;
    private Long assetVersionId;
    private Long sectionId;
    private Long sortOrder;
    private String note;
    private Long addedBy;
    private Date addedAt;
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
