package com.qiu.qiupicturebackend.collection;

import com.qiu.qiupicturebackend.activity.application.ActivityRecordApplicationService;
import com.qiu.qiupicturebackend.collection.application.CollectionApplicationService;
import com.qiu.qiupicturebackend.collection.application.command.CreateCollectionCommand;
import com.qiu.qiupicturebackend.collection.domain.model.CollectionView;
import com.qiu.qiupicturebackend.collection.domain.repository.CollectionRepository;
import com.qiu.qiupicturebackend.collection.representation.CollectionResponse;
import com.qiu.qiupicturebackend.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionApplicationServiceTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private ActivityRecordApplicationService activityRecordApplicationService;

    @InjectMocks
    private CollectionApplicationService service;

    @Test
    void shouldCreateCollection() {
        CreateCollectionCommand command = new CreateCollectionCommand();
        command.setName("我的集合");
        command.setDescription("测试集合");
        command.setPurpose("project");
        command.setLayout("grid");

        User user = new User();
        user.setId(1L);

        when(collectionRepository.save(any(CollectionView.class))).thenAnswer(inv -> {
            CollectionView v = inv.getArgument(0);
            v.setCollectionId(10L);
            return v;
        });

        CollectionResponse result = service.createCollection(1L, command, user);

        assertNotNull(result);
        assertEquals("我的集合", result.getName());
        assertEquals("active", result.getStatus());
        assertEquals(1L, result.getCreatedBy());
        verify(collectionRepository).save(any(CollectionView.class));
    }

    @Test
    void shouldListCollectionsByWorkspace() {
        CollectionView view = new CollectionView();
        view.setCollectionId(1L);
        view.setWorkspaceId(1L);
        view.setName("测试集合");

        when(collectionRepository.findByWorkspaceId(1L)).thenReturn(List.of(view));

        List<CollectionResponse> result = service.listCollections(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试集合", result.get(0).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoCollections() {
        when(collectionRepository.findByWorkspaceId(1L)).thenReturn(Collections.emptyList());

        List<CollectionResponse> result = service.listCollections(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenWorkspaceIdInvalid() {
        List<CollectionResponse> result = service.listCollections(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(collectionRepository, never()).findByWorkspaceId(any());
    }

    @Test
    void shouldGetCollectionById() {
        CollectionView view = new CollectionView();
        view.setCollectionId(5L);
        view.setWorkspaceId(1L);
        view.setName("详情测试");

        when(collectionRepository.findById(5L)).thenReturn(Optional.of(view));

        CollectionResponse result = service.getCollection(1L, 5L);

        assertNotNull(result);
        assertEquals("详情测试", result.getName());
    }

    @Test
    void shouldReturnNullWhenCollectionNotFound() {
        when(collectionRepository.findById(99L)).thenReturn(Optional.empty());

        CollectionResponse result = service.getCollection(1L, 99L);

        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenWorkspaceIdMismatch() {
        CollectionView view = new CollectionView();
        view.setCollectionId(5L);
        view.setWorkspaceId(2L);

        when(collectionRepository.findById(5L)).thenReturn(Optional.of(view));

        CollectionResponse result = service.getCollection(1L, 5L);

        assertNull(result);
    }
}
