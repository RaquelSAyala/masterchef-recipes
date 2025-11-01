package dows.masterchef.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    private Counter counter;

    @BeforeEach
    void setUp() {
        counter = new Counter();
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyCounter() {
        // Act & Assert
        assertNotNull(counter);
        assertNull(counter.getId());
        assertEquals(0L, counter.getSeq());
    }

    @Test
    void parameterizedConstructor_ShouldInitializeFields() {
        // Arrange
        String expectedId = "recipe_seq";
        long expectedSeq = 100L;

        // Act
        Counter counter = new Counter(expectedId, expectedSeq);

        // Assert
        assertNotNull(counter);
        assertEquals(expectedId, counter.getId());
        assertEquals(expectedSeq, counter.getSeq());
    }

    @Test
    void setId_ShouldSetIdCorrectly() {
        // Arrange
        String expectedId = "user_seq";

        // Act
        counter.setId(expectedId);

        // Assert
        assertEquals(expectedId, counter.getId());
    }

    @Test
    void setId_WithNull_ShouldSetNullId() {
        // Act
        counter.setId(null);

        // Assert
        assertNull(counter.getId());
    }

    @Test
    void setSeq_ShouldSetSeqCorrectly() {
        // Arrange
        long expectedSeq = 999L;

        // Act
        counter.setSeq(expectedSeq);

        // Assert
        assertEquals(expectedSeq, counter.getSeq());
    }

    @Test
    void setSeq_WithNegativeValue_ShouldSetNegativeSeq() {
        // Arrange
        long negativeSeq = -1L;

        // Act
        counter.setSeq(negativeSeq);

        // Assert
        assertEquals(negativeSeq, counter.getSeq());
    }

    @Test
    void setSeq_WithZero_ShouldSetZero() {
        // Act
        counter.setSeq(0L);

        // Assert
        assertEquals(0L, counter.getSeq());
    }

    @Test
    void setSeq_WithMaxLongValue_ShouldSetMaxValue() {
        // Arrange
        long maxLong = Long.MAX_VALUE;

        // Act
        counter.setSeq(maxLong);

        // Assert
        assertEquals(maxLong, counter.getSeq());
    }

    @Test
    void setSeq_WithMinLongValue_ShouldSetMinValue() {
        // Arrange
        long minLong = Long.MIN_VALUE;

        // Act
        counter.setSeq(minLong);

        // Assert
        assertEquals(minLong, counter.getSeq());
    }

    @Test
    void getId_WhenIdNotSet_ShouldReturnNull() {
        // Act & Assert
        assertNull(counter.getId());
    }

    @Test
    void getSeq_WhenSeqNotSet_ShouldReturnZero() {
        // Act & Assert
        assertEquals(0L, counter.getSeq());
    }

    @Test
    void class_ShouldHaveDocumentAnnotation() {
        // Act & Assert
        Document documentAnnotation = Counter.class.getAnnotation(Document.class);
        assertNotNull(documentAnnotation);
        assertEquals("counters", documentAnnotation.collection());
    }

    @Test
    void idField_ShouldHaveIdAnnotation() throws NoSuchFieldException {
        // Arrange
        Field idField = Counter.class.getDeclaredField("id");

        // Act & Assert
        assertNotNull(idField.getAnnotation(Id.class));
    }

    @Test
    void equals_WithSameObject_ShouldReturnTrue() {
        // Act & Assert
        assertEquals(counter, counter);
    }

    @Test
    void equals_WithDifferentType_ShouldReturnFalse() {
        // Arrange
        String differentObject = "not a counter";

        // Act & Assert
        assertNotEquals(counter, differentObject);
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        // Act & Assert
        assertNotEquals(counter, null);
    }

    @Test
    void hashCode_Consistency_ShouldReturnSameHashCode() {
        // Arrange
        int firstHashCode = counter.hashCode();
        int secondHashCode = counter.hashCode();

        // Act & Assert
        assertEquals(firstHashCode, secondHashCode);
    }

    @Test
    void toString_ShouldReturnNonEmptyString() {
        // Act
        String toStringResult = counter.toString();

        // Assert
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }


    @Test
    void counter_ShouldBeMutable() {
        // Arrange
        String initialId = "initial_id";
        long initialSeq = 10L;
        counter.setId(initialId);
        counter.setSeq(initialSeq);

        // Act - Modify values
        String newId = "new_id";
        long newSeq = 20L;
        counter.setId(newId);
        counter.setSeq(newSeq);

        // Assert
        assertEquals(newId, counter.getId());
        assertEquals(newSeq, counter.getSeq());
        assertNotEquals(initialId, counter.getId());
        assertNotEquals(initialSeq, counter.getSeq());
    }

    @Test
    void counter_WithSameFieldValues_ShouldBeEqual() {
        // Arrange
        Counter counter1 = new Counter("same_id", 100L);
        Counter counter2 = new Counter("same_id", 100L);

        // Act & Assert
        assertEquals(counter1.getId(), counter2.getId());
        assertEquals(counter1.getSeq(), counter2.getSeq());
    }

    @Test
    void counter_WithDifferentFieldValues_ShouldNotBeEqual() {
        // Arrange
        Counter counter1 = new Counter("id1", 100L);
        Counter counter2 = new Counter("id2", 200L);

        // Act & Assert
        assertNotEquals(counter1.getId(), counter2.getId());
        assertNotEquals(counter1.getSeq(), counter2.getSeq());
    }

    @Test
    void fieldNames_ShouldMatchExpected() throws NoSuchFieldException {
        // Arrange
        Field idField = Counter.class.getDeclaredField("id");
        Field seqField = Counter.class.getDeclaredField("seq");

        // Act & Assert
        assertEquals("id", idField.getName());
        assertEquals("seq", seqField.getName());
        assertEquals(String.class, idField.getType());
        assertEquals(long.class, seqField.getType());
    }

    @Test
    void sequentialOperations_ShouldWorkCorrectly() {
        // Arrange
        Counter counter = new Counter();

        // Act - Perform sequential operations
        counter.setId("operation_test");
        counter.setSeq(1L);
        counter.setSeq(counter.getSeq() + 1); // Increment
        counter.setId(counter.getId() + "_updated");

        // Assert
        assertEquals("operation_test_updated", counter.getId());
        assertEquals(2L, counter.getSeq());
    }

    @Test
    void boundaryValues_ShouldBeHandledCorrectly() {
        // Test boundary values for sequence
        long[] testValues = {0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE};

        for (long testValue : testValues) {
            // Arrange & Act
            counter.setSeq(testValue);

            // Assert
            assertEquals(testValue, counter.getSeq(),
                    "Should handle value: " + testValue);
        }
    }

    @Test
    void id_WithEmptyString_ShouldBeAllowed() {
        // Arrange
        String emptyId = "";

        // Act
        counter.setId(emptyId);

        // Assert
        assertEquals(emptyId, counter.getId());
    }

    @Test
    void id_WithSpecialCharacters_ShouldBeAllowed() {
        // Arrange
        String specialId = "id-with-special-chars_123!#";

        // Act
        counter.setId(specialId);

        // Assert
        assertEquals(specialId, counter.getId());
    }
}