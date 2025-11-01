package dows.masterchef.service;


import dows.masterchef.model.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SequenceServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    private SequenceService sequenceService;

    @BeforeEach
    void setUp() {
        sequenceService = new SequenceService(mongoOperations);
    }

    @Test
    void getNextSequence_WithExistingCounter_ShouldIncrementAndReturnSequence() {
        // Arrange
        String sequenceName = "recipes";
        Counter existingCounter = new Counter(sequenceName, 100L);
        Counter updatedCounter = new Counter(sequenceName, 101L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(updatedCounter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(101L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_WithNewCounter_ShouldCreateAndReturnInitialSequence() {
        // Arrange
        String sequenceName = "users";
        Counter newCounter = new Counter(sequenceName, 1L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(newCounter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(1L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_WithNullReturn_ShouldThrowException() {
        // Arrange
        String sequenceName = "recipes";

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class,
                () -> sequenceService.getNextSequence(sequenceName));

        assertNotNull(exception);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_WithEmptySequenceName_ShouldWork() {
        // Arrange
        String sequenceName = "";
        Counter counter = new Counter(sequenceName, 1L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(counter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(1L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_WithSpecialCharactersInName_ShouldWork() {
        // Arrange
        String sequenceName = "recipe-seq_123";
        Counter counter = new Counter(sequenceName, 50L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(counter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(50L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }


    @Test
    void getNextSequence_VerifyUpsertOptions() {
        // Arrange
        String sequenceName = "recipes";
        Counter counter = new Counter(sequenceName, 1L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                argThat(options ->
                        options.isUpsert() && options.isReturnNew()
                ),
                eq(Counter.class)
        )).thenReturn(counter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(1L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_MultipleCalls_ShouldIncrementSequence() {
        // Arrange
        String sequenceName = "recipes";
        Counter firstCall = new Counter(sequenceName, 1L);
        Counter secondCall = new Counter(sequenceName, 2L);
        Counter thirdCall = new Counter(sequenceName, 3L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(firstCall, secondCall, thirdCall);

        // Act & Assert
        assertEquals(1L, sequenceService.getNextSequence(sequenceName));
        assertEquals(2L, sequenceService.getNextSequence(sequenceName));
        assertEquals(3L, sequenceService.getNextSequence(sequenceName));

        verify(mongoOperations, times(3)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }


    @Test
    void getNextSequence_WithMaxLongValue_ShouldHandleCorrectly() {
        // Arrange
        String sequenceName = "max_seq";
        Counter maxCounter = new Counter(sequenceName, Long.MAX_VALUE);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(maxCounter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(Long.MAX_VALUE, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }

    @Test
    void getNextSequence_WithZeroValue_ShouldHandleCorrectly() {
        // Arrange
        String sequenceName = "zero_seq";
        Counter zeroCounter = new Counter(sequenceName, 0L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        )).thenReturn(zeroCounter);

        // Act
        long result = sequenceService.getNextSequence(sequenceName);

        // Assert
        assertEquals(0L, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Counter.class)
        );
    }
}