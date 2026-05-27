package com.qiu.qiupicturebackend.collection.application.command;

import lombok.Data;

import java.util.List;

@Data
public class ReorderCollectionItemsCommand {

    private List<ItemOrder> orders;

    @Data
    public static class ItemOrder {
        private Long itemId;
        private Long sortOrder;
    }
}
