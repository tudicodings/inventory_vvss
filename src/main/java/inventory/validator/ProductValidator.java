package inventory.validator;

import inventory.model.Product;

public class ProductValidator implements Validator<Product> {
    @Override
    public void validate(Product product) throws ValidatorException {
        String errors = "";
        if (product.getName().equals("")) {
            errors += "Name cannot be empty!\n";
        }
        if (product.getInStock() < 0) {
            errors += "In stock cannot be negative!\n";
        }
        if (product.getPrice() < 0) {
            errors += "Price cannot be negative!\n";
        }
        if (product.getMin() < 0) {
            errors += "Min cannot be negative!\n";
        }
        if (product.getMax() < 0) {
            errors += "Max cannot be negative!\n";
        }
        if (product.getMin() > product.getMax()) {
            errors += "Min cannot be greater than max!\n";
        }
        if (product.getInStock() < product.getMin() || product.getInStock() > product.getMax()) {
            errors += "In stock must be between min and max!\n";
        }
        if (product.getAssociatedParts().size() == 0) {
            errors += "Product must have at least one part!\n";
        }
        if (errors.length() > 0) {
            throw new ValidatorException(errors);
        }
    }
}