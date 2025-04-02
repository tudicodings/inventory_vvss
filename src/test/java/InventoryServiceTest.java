package whiteblack;

import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.PartValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Inventory Service Tests")
class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;
    private PartValidator partValidator;

    @BeforeEach
    void setUp() {
        inventoryRepository = new InventoryRepository();
        partValidator = new PartValidator();
        inventoryService = new InventoryService(inventoryRepository);
    }

    @AfterEach
    void tearDown() {
        inventoryRepository = null;
        inventoryService = null;
        partValidator = null;
    }

    @Test
    @DisplayName("Add In-House Part - TC1_ECP Successful Case")
    @Tag("ECP")
    void testAddInhousePartTC1_ECP() {
        // Arrange
        String name = "Part1";
        double price = 100;
        int inStock = 15;
        int min = 5;
        int max = 30;
        int partDynamicValue = 5;
        int size = inventoryRepository.getAllParts().size();

        // Act
        assertDoesNotThrow(() -> inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue));

        // Assert
        assertEquals(inventoryRepository.getAllParts().size(), size + 1);
    }

    @Test
    @DisplayName("Add In-House Part - TC2_ECP Error Case")
    @Tag("Error")
    void testAddInhousePartTC2_ECP() {
        // Arrange
        String name = "festar";
        double price = -15;
        int inStock = -15;
        int min = -5;
        int max = -30;
        int partDynamicValue = -15;

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        });
        assertEquals("In stock cannot be negative!\nPrice cannot be negative!\nMin cannot be negative!\nMax cannot be negative!\nMin cannot be greater than max!\nIn stock must be between min and max!\n", exception.getMessage());
    }

    @Test
    @DisplayName("Add In-House Part - TC3_ECP Error Case")
    @Tag("Error")
    void testAddInhousePartTC3_ECP() {
        // Arrange
        String name = "";
        double price = 20;
        int inStock = 15;
        int min = 10;
        int max = 25;
        int partDynamicValue = -2;

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        });
        assertEquals("Name cannot be empty!\n", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "M", "N", "P" })
    @DisplayName("Add In-House Part - TC1_BVA Successful Case")
    @Tag("BVA")
        //@Test
    void testAddInhousePartTC1_BVA(String name) {
        // Arrange
        //String name = "M";
        double price = 1;
        int inStock = 2;
        int min = 1;
        int max = 10;
        int partDynamicValue = 100;
        int sizeBefore = inventoryRepository.getAllParts().size();

        // Act
        assertDoesNotThrow(() -> inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue));

        // Assert
        assertEquals(sizeBefore + 1, inventoryRepository.getAllParts().size());
    }

    @Test
    @DisplayName("Add In-House Part - TC2_BVA Error Case")
    @Tag("CompilerError")
    void testAddInhousePartTC2_BVA() {
        // Arrange
        String name = "festar";
        double price = 50;
        int inStock = 1;
        int min = 2;
        int max = 3;
        int partDynamicValue = 2;

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        });
        assertEquals("In stock must be between min and max!\n", exception.getMessage());
    }

    @RepeatedTest(3)
    @DisplayName("Add In-House Part - TC3_BVA Successful Case")
    @Tag("BVA")
    void testAddInhousePartTC3_BVA() {
        // Arrange
        String name = "M";
        double price = 2;
        int inStock = 50;
        int min = 45;
        int max = 99;
        int partDynamicValue = 45;
        int sizeBefore = inventoryRepository.getAllParts().size();

        // Act
        assertDoesNotThrow(() -> inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue));

        // Assert
        assertEquals(sizeBefore + 1, inventoryRepository.getAllParts().size());
    }

    @Test
    @DisplayName("Add In-House Part - TC4_BVA Error Case")
    @Tag("Error")
    @Timeout(1)
    void testAddInhousePartTC4_BVA() {
        // Arrange
        String name = "M…1234";
        double price = 2;
        int inStock = 101;
        int min = 1;
        int max = 100;
        int partDynamicValue = 1;

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> inventoryService.addInhousePart(name, price, inStock, min, max, partDynamicValue));
        assertEquals("In stock must be between min and max!\n", exception.getMessage());
    }
}
