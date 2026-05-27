package com.qiu.qiupicturebackend.asset.representation;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 视觉资产分页响应。
 */
@Data
public class AssetPageResponse implements Serializable {

    private List<AssetCardResponse> records;
    private long total;
    private long current;
    private long pageSize;

    public static AssetPageResponse empty() {
        AssetPageResponse page = new AssetPageResponse();
        page.setRecords(Collections.emptyList());
        page.setTotal(0);
        page.setCurrent(1);
        page.setPageSize(20);
        return page;
    }

    private static final long serialVersionUID = 1L;
}
