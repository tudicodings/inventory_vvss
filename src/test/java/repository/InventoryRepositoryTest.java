package repository;
import inventory.model.InhousePart;
import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class InventoryRepositoryTest {

    private InventoryRepository repo;
    private Inventory inventorySpy;

    @BeforeEach
    void setup() {
        repo = InventoryRepository.getInstance();
        inventorySpy = spy(new Inventory());
        repo.setInventory(inventorySpy);
    }

    @Test
    void testAddPartCallsInventoryAdd() {
        Part part = new InhousePart(1, "Bolt", 2.0, 10, 1, 20, 123);
        repo.addPart(part);
        verify(inventorySpy, atLeastOnce()).addPart(part);
    }

    @Test
    void testDeletePartCallsInventoryDelete() {
        Part part = new InhousePart(2, "Screw", 3.0, 15, 5, 30, 321);
        repo.addPart(part);
        repo.deletePart(part);
        verify(inventorySpy, atLeastOnce()).deletePart(part);
    }
}