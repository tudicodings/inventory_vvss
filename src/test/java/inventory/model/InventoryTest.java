package inventory.model;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    @Test
    void testAddPart() {
        Inventory inventory = new Inventory();
        Part part = new InhousePart(1, "Bolt", 2.0, 10, 1, 20, 123);
        inventory.addPart(part);
        ObservableList<Part> parts = inventory.getParts();
        assertEquals(1, parts.size());
        assertEquals("Bolt", parts.get(0).getName());
    }

    @Test
    void testAddProductAndLookup() {
        Inventory inventory = new Inventory();
        Product product = new Product(1, "Widget", 10.0, 5, 1, 10, null);
        inventory.addProduct(product);
        Product result = inventory.lookupProduct("Widget");
        assertNotNull(result);
        assertEquals("Widget", result.getName());
    }
}
