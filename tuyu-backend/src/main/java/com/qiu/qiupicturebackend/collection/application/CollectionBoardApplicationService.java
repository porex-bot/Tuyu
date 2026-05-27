package com.qiu.qiupicturebackend.collection.application;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.activity.application.command.RecordActivityCommand;
import com.qiu.qiupicturebackend.collection.application.command.AddAssetToCollectionCommand;
import com.qiu.qiupicturebackend.collection.application.command.ReorderCollectionItemsCommand;
import com.qiu.qiupicturebackend.collection.domain.model.*;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionItemRepository;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionSectionRepository;
import com.qiu.qiupicturebackend.collection.representation.CollectionBoardResponse;
import com.qiu.qiupicturebackend.collection.representation.CollectionItemResponse;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.exception.ErrorCode;
import com.qiu.qiupicturebackend.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectionBoardApplicationService {

    private static final long SORT_ORDER_GAP = 1000L;

    @Resource
    private CollectionRepository collectionRepository;

    @Resource
    private CollectionItemRepository collectionItemRepository;

    @Resource
    private CollectionSectionRepository collectionSectionRepository;

    @Resource
    private ActivityRecordApplicationService activityRecordApplicationService;

    public CollectionBoardResponse getBoard(Long workspaceId, Long collectionId) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }
        CollectionView collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在"));
        if (!workspaceId.equals(collection.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在");
        }

        List<CollectionSectionView> sections = collectionSectionRepository.findByCollectionId(collectionId);
        List<CollectionItemView> items = collectionItemRepository.findByCollectionId(collectionId);

        Map<Long, List<CollectionItemResponse>> sectionItems = new LinkedHashMap<>();
        // Unsorted section for items without a section
        List<CollectionItemResponse> unsortedItems = new ArrayList<>();

        for (CollectionItemView item : items) {
            CollectionItemResponse itemResponse = toItemResponse(item);
            if (item.getSectionId() != null) {
                sectionItems.computeIfAbsent(item.getSectionId(), k -> new ArrayList<>()).add(itemResponse);
            } else {
                unsortedItems.add(itemResponse);
            }
        }

        CollectionBoardResponse response = new CollectionBoardResponse();
        response.setCollectionId(collection.getCollectionId());
        response.setWorkspaceId(collection.getWorkspaceId());
        response.setName(collection.getName());
        response.setDescription(collection.getDescription());
        response.setPurpose(collection.getPurpose() != null ? collection.getPurpose().name().toLowerCase() : null);
        response.setLayout(collection.getLayout() != null ? collection.getLayout().name().toLowerCase() : null);
        response.setStatus(collection.getStatus() != null ? collection.getStatus().name().toLowerCase() : null);

        List<CollectionBoardResponse.SectionGroup> sectionGroups = new ArrayList<>();
        for (CollectionSectionView section : sections) {
            CollectionBoardResponse.SectionGroup group = new CollectionBoardResponse.SectionGroup();
            group.setSectionId(section.getSectionId());
            group.setName(section.getName());
            group.setSortOrder(section.getSortOrder());
            group.setItems(sectionItems.getOrDefault(section.getSectionId(), Collections.emptyList()));
            sectionGroups.add(group);
        }
        response.setSections(sectionGroups);

        CollectionBoardResponse.SectionGroup unsortedGroup = new CollectionBoardResponse.SectionGroup();
        unsortedGroup.setName("未分组");
        unsortedGroup.setItems(unsortedItems);
        response.setUnsorted(unsortedGroup);

        return response;
    }

    @Transactional
    public CollectionItemResponse addAsset(Long workspaceId, Long collectionId, AddAssetToCollectionCommand command, User loginUser) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }
        CollectionView collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在"));
        if (!workspaceId.equals(collection.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在");
        }

        // Prevent duplicate active item for same collection and asset
        Optional<CollectionItemView> existing = collectionItemRepository
                .findActiveByCollectionAndAsset(collectionId, command.getAssetId());
        if (existing.isPresent()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该资产已在集合中");
        }

        // Compute next sort order
        List<CollectionItemView> items = collectionItemRepository.findByCollectionId(collectionId);
        long maxSortOrder = items.stream()
                .mapToLong(CollectionItemView::getSortOrder)
                .max()
                .orElse(0);

        CollectionItemView item = new CollectionItemView();
        item.setCollectionId(collectionId);
        item.setAssetId(command.getAssetId());
        item.setAssetVersionId(command.getAssetVersionId());
        item.setSectionId(command.getSectionId());
        item.setSortOrder(maxSortOrder + SORT_ORDER_GAP);
        item.setNote(command.getNote());
        item.setAddedBy(loginUser.getId());
        item.setAddedAt(new Date());
        item.setCreatedAt(new Date());

        CollectionItemView saved = collectionItemRepository.save(item);

        activityRecordApplicationService.record(RecordActivityCommand.builder()
                .workspaceId(collection.getWorkspaceId())
                .actorUserId(loginUser.getId())
                .actionType("collection.item.added")
                .targetType("collection")
                .targetId(collectionId)
                .targetName(collection.getName())
                .secondaryTargetType("asset")
                .secondaryTargetId(command.getAssetId())
                .summary("向集合「" + collection.getName() + "」添加了资产")
                .occurredAt(new Date())
                .build());

        return toItemResponse(saved);
    }

    @Transactional
    public void removeAsset(Long workspaceId, Long collectionId, Long itemId) {
        CollectionView collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在"));
        if (!workspaceId.equals(collection.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在");
        }
        CollectionItemView item = collectionItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "条目不存在"));
        if (!item.getCollectionId().equals(collectionId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "条目不属于该集合");
        }
        collectionItemRepository.deleteById(itemId);

        activityRecordApplicationService.record(RecordActivityCommand.builder()
                .workspaceId(collection.getWorkspaceId())
                .actionType("collection.item.removed")
                .targetType("collection")
                .targetId(collectionId)
                .targetName(collection.getName())
                .secondaryTargetType("asset")
                .secondaryTargetId(item.getAssetId())
                .summary("从集合「" + collection.getName() + "」移除了资产")
                .occurredAt(new Date())
                .build());
    }

    @Transactional
    public void reorderItems(Long workspaceId, Long collectionId, ReorderCollectionItemsCommand command) {
        if (workspaceId == null || workspaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid workspace ID");
        }
        CollectionView collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在"));
        if (!workspaceId.equals(collection.getWorkspaceId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "集合不存在");
        }
        if (command.getOrders() == null || command.getOrders().isEmpty()) {
            return;
        }
        for (ReorderCollectionItemsCommand.ItemOrder order : command.getOrders()) {
            collectionItemRepository.updateSortOrder(order.getItemId(), order.getSortOrder());
        }

        activityRecordApplicationService.record(RecordActivityCommand.builder()
                .workspaceId(collection.getWorkspaceId())
                .actionType("collection.items.reordered")
                .targetType("collection")
                .targetId(collectionId)
                .targetName(collection.getName())
                .summary("重新排序了集合「" + collection.getName() + "」")
                .occurredAt(new Date())
                .build());
    }

    private CollectionItemResponse toItemResponse(CollectionItemView item) {
        CollectionItemResponse response = new CollectionItemResponse();
        response.setItemId(item.getItemId());
        response.setCollectionId(item.getCollectionId());
        response.setAssetId(item.getAssetId());
        response.setAssetVersionId(item.getAssetVersionId());
        response.setSectionId(item.getSectionId());
        response.setSortOrder(item.getSortOrder());
        response.setNote(item.getNote());
        response.setAddedBy(item.getAddedBy());
        response.setAddedAt(item.getAddedAt() != null ? item.getAddedAt().toString() : null);
        return response;
    }
}
