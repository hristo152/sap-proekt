ALTER TABLE shopping_cart_items
ADD CONSTRAINT fk_product_id
FOREIGN KEY (product_id)
REFERENCES products(id)
ON DELETE CASCADE;