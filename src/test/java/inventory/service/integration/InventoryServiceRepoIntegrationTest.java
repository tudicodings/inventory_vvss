package inventory.service.integration;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.Validator;
import inventory.validator.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class InventoryServiceRepoIntegrationTest {

    private InventoryService service;
    private InventoryRepository realRepo;
    private Validator<Part> partValidatorMock;

    @BeforeEach
    void setup() {
        realRepo = InventoryRepository.getInstance();
        partValidatorMock = mock(Validator.class);
        service = new InventoryService(realRepo);

        // Inject validatorul mock
        try {
            var field = InventoryService.class.getDeclaredField("partValidator");
            field.setAccessible(true);
            field.set(service, partValidatorMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddInhousePartIntegrationWithRepo() throws ValidatorException {
        // Arrange
        doNothing().when(partValidatorMock).validate(any());

        // Act
        service.addInhousePart("TestPart", 10.0, 5, 1, 10, 123);

        // Assert (verificăm că s-a adăugat în repo real)
        assertNotNull(realRepo.lookupPart("TestPart"));
    }

    @Test
    void testAddInvalidPartShouldThrow() throws ValidatorException {
        // Arrange
        doThrow(new ValidatorException("Invalid")).when(partValidatorMock).validate(any());

        // Act & Assert
        try {
            service.addInhousePart("InvalidPart", -10.0, 5, 1, 10, 999);
        } catch (ValidatorException e) {
            assertEquals("Invalid", e.getMessage());
        }
    }
}
