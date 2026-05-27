package com.qiu.qiupicturebackend.collection.representation;

import lombok.Data;

import java.util.List;

@Data
public class CollectionBoardResponse {

    private Long collectionId;
    private Long workspaceId;
    private String name;
    private String description;
    private String purpose;
    private String layout;
    private String status;
    private List<SectionGroup> sections;
    private SectionGroup unsorted;

    @Data
    public static class SectionGroup {
        private Long sectionId;
        private String name;
        private Long sortOrder;
        private List<CollectionItemResponse> items;
    }
}
