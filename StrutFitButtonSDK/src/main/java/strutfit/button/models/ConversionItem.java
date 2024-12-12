package strutfit.button.models;

public class ConversionItem {

    public ConversionItem(String productIdentifier, double price, int quantity, String size) {
        this.productIdentifier = productIdentifier;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.sizeUnit = null;
        this.sku = null;
    }

    public ConversionItem(String productIdentifier, double price, int quantity, String size, String sizeUnit) {
        this.productIdentifier = productIdentifier;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.sizeUnit = sizeUnit;
        this.sku = null;
    }

    // Constructor
    public ConversionItem(String productIdentifier, double price, int quantity, String size, String sizeUnit, String sku) {
        this.productIdentifier = productIdentifier;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.sizeUnit = sizeUnit;
        this.sku = sku;
    }

    public String productIdentifier;

    public double price;

    public int quantity;

    public String size;

    public String sizeUnit;

    public String sku;
}
