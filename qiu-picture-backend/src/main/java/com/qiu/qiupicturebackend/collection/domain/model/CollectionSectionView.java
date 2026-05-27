package com.qiu.qiupicturebackend.collection.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CollectionSectionView implements Serializable {

    private Long sectionId;
    private Long collectionId;
    private String name;
    private Long sortOrder;
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
