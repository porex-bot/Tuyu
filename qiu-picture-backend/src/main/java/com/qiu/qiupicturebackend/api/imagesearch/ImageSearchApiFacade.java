package com.qiu.qiupicturebackend.api.imagesearch;

import com.qiu.qiupicturebackend.api.imagesearch.model.ImageSearchResult;
import com.qiu.qiupicturebackend.api.imagesearch.sub.GetImageFirstUrlApi;
import com.qiu.qiupicturebackend.api.imagesearch.sub.GetImageListApi;
import com.qiu.qiupicturebackend.api.imagesearch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ImageSearchApiFacade {
    /**
     * 搜索图片
     *
     * @param imageUrl 图片的url
     * @return 图片的搜索结果
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;

    }
}
