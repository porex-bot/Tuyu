package com.qiu.qiupicturebackend.collection.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.collection.application.command.CreateCollectionCommand;
import com.qiu.qiupicturebackend.collection.domain.model.*;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.collection.representation.CollectionResponse;
import com.qiu.qiupicturebackend.model.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionApplicationService {

    @Resource
    private CollectionRepository collectionRepository;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    public CollectionResponse createCollection(Long workspaceId, CreateCollectionCommand command, User loginUser) {
        CollectionView view = new CollectionView();
        view.setWorkspaceId(workspaceId);
        view.setName(command.getName());
        view.setDescription(command.getDescription());
        view.setPurpose(parsePurpose(command.getPurpose()));
        view.setLayout(parseLayout(command.getLayout()));
        view.setStatus(CollectionStatus.ACTIVE);
        view.setItemCount(0);
        view.setCreatedBy(loginUser.getId());
        view.setUpdatedBy(loginUser.getId());
        Date now = new Date();
        view.setCreatedAt(now);
        view.setUpdatedAt(now);

        CollectionView saved = collectionRepository.save(view);

        activityRecordApplicationService.record(RecordActivityCommand.builder()
                .workspaceId(workspaceId)
                .actorUserId(loginUser.getId())
                .actionType("collection.created")
                .targetType("collection")
                .targetId(saved.getCollectionId())
                .targetName(saved.getName())
                .summary("创建了集合「" + saved.getName() + "」")
                .occurredAt(now)
                .build());

        return toResponse(saved);
    }

    public List<CollectionResponse> listCollections(Long workspaceId) {
        if (workspaceId == null || workspaceId <= 0) {
            return Collections.emptyList();
        }
        List<CollectionView> collections = collectionRepository.findByWorkspaceId(workspaceId);
        return collections.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CollectionResponse getCollection(Long workspaceId, Long collectionId) {
        if (workspaceId == null || collectionId == null || collectionId <= 0) {
            return null;
        }
        CollectionView collection = collectionRepository.findById(collectionId).orElse(null);
        if (collection == null || !workspaceId.equals(collection.getWorkspaceId())) {
            return null;
        }
        return toResponse(collection);
    }

    private CollectionResponse toResponse(CollectionView view) {
        CollectionResponse response = new CollectionResponse();
        response.setCollectionId(view.getCollectionId());
        response.setWorkspaceId(view.getWorkspaceId());
        response.setName(view.getName());
        response.setDescription(view.getDescription());
        response.setPurpose(view.getPurpose() != null ? view.getPurpose().name().toLowerCase() : "project");
        response.setLayout(view.getLayout() != null ? view.getLayout().name().toLowerCase() : "grid");
        response.setStatus(view.getStatus() != null ? view.getStatus().name().toLowerCase() : "draft");
        response.setCoverAssetId(view.getCoverAssetId());
        response.setItemCount(view.getItemCount());
        response.setCreatedBy(view.getCreatedBy());
        response.setCreatedAt(view.getCreatedAt() != null ? view.getCreatedAt().toString() : null);
        response.setUpdatedAt(view.getUpdatedAt() != null ? view.getUpdatedAt().toString() : null);
        return response;
    }

    private CollectionPurpose parsePurpose(String purpose) {
        if (purpose == null) return CollectionPurpose.PROJECT;
        try {
            return CollectionPurpose.valueOf(purpose.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CollectionPurpose.PROJECT;
        }
    }

    private CollectionLayout parseLayout(String layout) {
        if (layout == null) return CollectionLayout.GRID;
        try {
            return CollectionLayout.valueOf(layout.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CollectionLayout.GRID;
        }
    }
}
