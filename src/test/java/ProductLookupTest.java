package whiteblack;

import static org.junit.jupiter.api.Assertions.*;

import inventory.model.Product;
import inventory.repository.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



class ProductLookupTest {
    private Path tempFile;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file to store the product data
        String fileName = "example.txt";

        // Define the directory where you want to create the file
        //String directoryPath = "/Users/tudorr/Desktop/laborator vvss/gith/data";
        String directoryPath = "data";

        // Create a File object with the specified directory and file name
        File file = new File(directoryPath, fileName);
        System.out.println("Filename: " + file.getAbsolutePath());


        // Define the product data as a string
        String productData = String.join(System.lineSeparator(),
                "P,1,SuperWidget,999,50,5,100,1:2",
                "P,2,SuperGadget,1999,0,5,100,1:2",
                "P,3,SuperThingamabob,499,20,5,100,1:2",
                "P,4,SuperThingamabob2,499,0,5,100,1:2"
                // Add more product data as needed
        );

        // Write the product data to the temporary file
        Files.writeString(file.toPath().toAbsolutePath(), productData);
        inventoryRepository = new InventoryRepository();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary file
//        Files.deleteIfExists(tempFile);
        inventoryRepository = null;
    }


    @Test
    void testNoProductFoundNoIdMatch() {
        Product p = new Product(0,null, 0.0, 0,0, 0, null);
        Product nonext = inventoryRepository.lookupProduct("nonexistent");
        assertEquals(p.getProductId(), nonext.getProductId());
    }

    @Test
    void testProductFoundButZeroStock() throws Exception {
        Product result = inventoryRepository.lookupProduct("SuperGadget");
        assertEquals(0, result.getInStock());
    }

    @Test
    void testProductFoundWithStock() throws Exception {
        Product result = inventoryRepository.lookupProduct("SuperWidget");
        assertTrue(result.getInStock() > 0);
    }

    @Test
    void testFirstProductNameContainsSearchItemAndHasStock() throws Exception {
        Product result = inventoryRepository.lookupProduct("SuperWidget");
        assertTrue(result.getName().contains("SuperWidget") && result.getInStock() > 0);
    }

    @Test
    void testFirstProductIdMatchesSearchItem() throws Exception {
        Product result = inventoryRepository.lookupProduct("1");
        assertEquals("1", String.valueOf(result.getProductId()));
    }

    @Test
    void testNonFirstProductNameContainsSearchItemButFirstProductIdDoesNotMatch() {
        Exception exception = assertThrows(Exception.class, () -> {
            inventoryRepository.lookupProduct(null);
        });
        assertEquals("Couldn't find the product!", exception.getMessage());
    }

    @Test
    void testFirstProductNameContainsSearchItemButZeroStockAndNextProductIdMatches() throws Exception {
        Product result = inventoryRepository.lookupProduct("4");
        assertEquals("SuperThingamabob2", String.valueOf(result.getName()));
    }
}