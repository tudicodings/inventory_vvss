package inventory.validator;

import inventory.model.Part;

public class PartValidator implements Validator<Part> {
    @Override
    public void validate(Part part) throws ValidatorException {
        String errors = "";
        if (part.getName().equals("")) {
            errors += "Name cannot be empty!\n";
        }
        if (part.getInStock() < 0) {
            errors += "In stock cannot be negative!\n";
        }
        if (part.getPrice() < 0) {
            errors += "Price cannot be negative!\n";
        }
        if (part.getMin() < 0) {
            errors += "Min cannot be negative!\n";
        }
        if (part.getMax() < 0) {
            errors += "Max cannot be negative!\n";
        }
        if (part.getMin() > part.getMax()) {
            errors += "Min cannot be greater than max!\n";
        }
        if (part.getInStock() < part.getMin() || part.getInStock() > part.getMax()) {
            errors += "In stock must be between min and max!\n";
        }
        if (errors.length() > 0) {
            throw new ValidatorException(errors);
        }
    }
}