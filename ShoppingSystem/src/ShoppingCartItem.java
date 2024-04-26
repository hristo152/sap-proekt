public class ShoppingCartItem {
    private int productId;
    private int shopingCartId;
    private double price;
    private int quantity;

    public ShoppingCartItem(int productId, int shopingCartId, double price, int quantity){
        this.productId = productId;
        this.shopingCartId = shopingCartId;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getShopingCartId() {
        return shopingCartId;
    }

    public void setShopingCartId(int shopingCartId) {
        this.shopingCartId = shopingCartId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float quantity) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}