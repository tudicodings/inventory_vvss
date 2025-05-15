package inventory.service;

import inventory.model.InhousePart;
import inventory.repository.InventoryRepository;
import inventory.validator.Validator;
import inventory.validator.ValidatorException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceTest {

    private InventoryService service;
    private InventoryRepository repoMock;
    private Validator partValidator;

    @BeforeEach
    void init() {
        repoMock = mock(InventoryRepository.class);
        partValidator = mock(Validator.class);
        service = new InventoryService(repoMock);

        try {
            var field = InventoryService.class.getDeclaredField("partValidator");
            field.setAccessible(true);
            field.set(service, partValidator);
        } catch (Exception e) {
            fail("Nu s-a putut seta validatorul.");
        }

        when(repoMock.getAutoPartId()).thenReturn(1);
    }

    // ------------------------- ECP TESTING -------------------------

    @Test
    @Order(1)
    @DisplayName("ECP Valid: price pozitiv, inStock între min și max")
    void testAddInhousePart_ECP_Valid() throws ValidatorException {
        // Arrange
        double price = 10.0;
        int inStock = 5;

        // Act
        service.addInhousePart("Bolt", price, inStock, 1, 10, 123);

        // Assert
        verify(partValidator).validate(any(InhousePart.class));
        verify(repoMock).addPart(any(InhousePart.class));
    }

    @Test
    @Order(2)
    @DisplayName("ECP Invalid: price negativ")
    void testAddInhousePart_ECP_InvalidPrice() throws ValidatorException {
        // Arrange
        double price = -5.0;
        int inStock = 5;

        doThrow(new ValidatorException("Invalid price")).when(partValidator).validate(any());

        // Act & Assert
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", price, inStock, 1, 10, 123));
    }

    @Test
    @Order(3)
    @DisplayName("ECP Invalid: inStock sub minim")
    void testAddInhousePart_ECP_InvalidInStockLow() throws ValidatorException {
        // Arrange
        double price = 8.0;
        int inStock = 0;

        doThrow(new ValidatorException("Invalid stock")).when(partValidator).validate(any());

        // Act & Assert
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", price, inStock, 1, 10, 123));
    }

    @Test
    @Order(4)
    @DisplayName("ECP Invalid: inStock peste maxim")
    void testAddInhousePart_ECP_InvalidInStockHigh() throws ValidatorException {
        // Arrange
        double price = 8.0;
        int inStock = 15;

        doThrow(new ValidatorException("Invalid stock")).when(partValidator).validate(any());

        // Act & Assert
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", price, inStock, 1, 10, 123));
    }

    // ------------------------- BVA TESTING -------------------------

    @ParameterizedTest
    @Order(5)
    @DisplayName("BVA Valid: price la limită inferioară și inStock = min")
    @ValueSource(doubles = {0.01, 1.0})
    void testAddInhousePart_BVA_ValidPrice_MinStock(double price) throws ValidatorException {
        // Arrange
        int inStock = 1;

        // Act
        service.addInhousePart("Bolt", price, inStock, 1, 10, 123);

        // Assert
        verify(repoMock).addPart(any(InhousePart.class));
    }

    @Test
    @Order(6)
    @DisplayName("BVA Invalid: price = 0 (limita inferioară invalidă)")
    void testAddInhousePart_BVA_InvalidPriceZero() throws ValidatorException {
        // Arrange
        double price = 0.0;
        int inStock = 5;

        doThrow(new ValidatorException("Invalid price")).when(partValidator).validate(any());

        // Act & Assert
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", price, inStock, 1, 10, 123));
    }

    @Test
    @Order(7)
    @DisplayName("BVA Invalid: inStock = max+1")
    void testAddInhousePart_BVA_InvalidStockAboveMax() throws ValidatorException {
        // Arrange
        double price = 5.0;
        int inStock = 11;

        doThrow(new ValidatorException("Invalid stock")).when(partValidator).validate(any());

        // Act & Assert
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", price, inStock, 1, 10, 123));
    }

    @AfterAll
    static void afterAllTests() {
        System.out.println("All tests completed.");
    }

}
