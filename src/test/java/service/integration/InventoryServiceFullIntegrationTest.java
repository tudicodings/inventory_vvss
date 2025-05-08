package service.integration;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.PartValidator;
import inventory.validator.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceFullIntegrationTest {
    private InventoryService service;
    private InventoryRepository repo;
    private PartValidator validator;

    @BeforeEach
    void setup() {
        repo = InventoryRepository.getInstance();
        validator = new PartValidator(); // validator real
        service = new InventoryService(repo);

        // Inject validator real
        try {
            var field = InventoryService.class.getDeclaredField("partValidator");
            field.setAccessible(true);
            field.set(service, validator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddValidPartWithRealValidatorAndRepo() throws ValidatorException {
        service.addInhousePart("RealPart", 20.0, 3, 1, 5, 321);
        Part part = repo.lookupPart("RealPart");
        assertNotNull(part);
        assertEquals("RealPart", part.getName());
    }

    @Test
    void testAddPartWithInvalidPrice() {
        ValidatorException ex = assertThrows(ValidatorException.class, () ->
                service.addInhousePart("FailPart", -1.0, 3, 1, 5, 999));
        assertTrue(ex.getMessage().toLowerCase().contains("price"));
    }
}