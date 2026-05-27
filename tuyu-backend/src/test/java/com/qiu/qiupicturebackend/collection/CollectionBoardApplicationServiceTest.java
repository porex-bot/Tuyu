package com.qiu.qiupicturebackend.collection;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.collection.application.CollectionBoardApplicationService;
import com.qiu.qiupicturebackend.collection.application.command.AddAssetToCollectionCommand;
import com.qiu.qiupicturebackend.collection.application.command.ReorderCollectionItemsCommand;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionItemView;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionItemRepository;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionSectionRepository;
import com.qiu.qiupicturebackend.collection.representation.CollectionBoardResponse;
import com.qiu.qiupicturebackend.collection.representation.CollectionItemResponse;
import com.qiu.qiupicturebackend.exception.BusinessException;
import com.qiu.qiupicturebackend.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionBoardApplicationServiceTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CollectionItemRepository collectionItemRepository;

    @Mock
    private CollectionSectionRepository collectionSectionRepository;

    @Mock
    private ActivityRecordApplicationService activityRecordApplicationService;

    @InjectMocks
    private CollectionBoardApplicationService service;

    @Test
    void shouldAddAssetToCollection() {
        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(1L);

        AddAssetToCollectionCommand command = new AddAssetToCollectionCommand();
        command.setAssetId(100L);

        User user = new User();
        user.setId(1L);

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(collectionItemRepository.findActiveByCollectionAndAsset(1L, 100L)).thenReturn(Optional.empty());
        when(collectionItemRepository.findByCollectionId(1L)).thenReturn(Collections.emptyList());
        when(collectionItemRepository.save(any(CollectionItemView.class))).thenAnswer(inv -> {
            CollectionItemView v = inv.getArgument(0);
            v.setItemId(10L);
            return v;
        });

        CollectionItemResponse result = service.addAsset(1L, 1L, command, user);

        assertNotNull(result);
        assertEquals(100L, result.getAssetId());
        verify(collectionItemRepository).save(any(CollectionItemView.class));
    }

    @Test
    void shouldPreventDuplicateActiveAssetItem() {
        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(1L);

        AddAssetToCollectionCommand command = new AddAssetToCollectionCommand();
        command.setAssetId(100L);

        User user = new User();
        user.setId(1L);

        CollectionItemView existing = new CollectionItemView();
        existing.setItemId(5L);

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(collectionItemRepository.findActiveByCollectionAndAsset(1L, 100L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class, () -> service.addAsset(1L, 1L, command, user));
        verify(collectionItemRepository, never()).save(any(CollectionItemView.class));
    }

    @Test
    void shouldRemoveCollectionItem() {
        CollectionItemView item = new CollectionItemView();
        item.setItemId(10L);
        item.setCollectionId(1L);

        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(1L);
        collection.setName("测试");

        when(collectionItemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertDoesNotThrow(() -> service.removeAsset(1L, 1L, 10L));
        verify(collectionItemRepository).deleteById(10L);
    }

    @Test
    void shouldNotRemoveItemWhenWrongWorkspace() {
        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(2L);

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertThrows(BusinessException.class, () -> service.removeAsset(1L, 1L, 10L));
        verify(collectionItemRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldReorderItems() {
        ReorderCollectionItemsCommand command = new ReorderCollectionItemsCommand();
        ReorderCollectionItemsCommand.ItemOrder order1 = new ReorderCollectionItemsCommand.ItemOrder();
        order1.setItemId(1L);
        order1.setSortOrder(1000L);
        ReorderCollectionItemsCommand.ItemOrder order2 = new ReorderCollectionItemsCommand.ItemOrder();
        order2.setItemId(2L);
        order2.setSortOrder(2000L);
        command.setOrders(java.util.List.of(order1, order2));

        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(1L);
        collection.setName("测试");
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertDoesNotThrow(() -> service.reorderItems(1L, 1L, command));
        verify(collectionItemRepository).updateSortOrder(1L, 1000L);
        verify(collectionItemRepository).updateSortOrder(2L, 2000L);
    }

    @Test
    void shouldGetBoardWithSections() {
        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(1L);
        collection.setName("看板测试");

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(collectionSectionRepository.findByCollectionId(1L)).thenReturn(Collections.emptyList());
        when(collectionItemRepository.findByCollectionId(1L)).thenReturn(Collections.emptyList());

        CollectionBoardResponse result = service.getBoard(1L, 1L);

        assertNotNull(result);
        assertEquals("看板测试", result.getName());
        assertNotNull(result.getSections());
        assertNotNull(result.getUnsorted());
    }

    @Test
    void shouldRejectGetBoardWhenWorkspaceMismatch() {
        CollectionView collection = new CollectionView();
        collection.setCollectionId(1L);
        collection.setWorkspaceId(2L);

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertThrows(BusinessException.class, () -> service.getBoard(1L, 1L));
    }

    @Test
    void shouldRejectAddToNonExistentCollection() {
        AddAssetToCollectionCommand command = new AddAssetToCollectionCommand();
        command.setAssetId(100L);
        User user = new User();
        user.setId(1L);

        when(collectionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.addAsset(1L, 99L, command, user));
    }
}
