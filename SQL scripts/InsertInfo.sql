-- Inserting data into categories table
INSERT INTO `onlinevintageshop`.`categories` (`name`) VALUES 
('Vintage Clothing'),
('Antique Furniture'),
('Retro Electronics'),
('Collectible Toys');

-- Inserting data into products table
INSERT INTO `onlinevintageshop`.`products` (`name`, `price`, `manufacturing_year`, `quality`, `quantity`, `description`, `category_id`, `discount`) VALUES 
('1950s Men\'s Leather Jacket', 350.00, 1950, 'Excellent', 5, 'Authentic leather jacket from the 1950s.', 1, NULL),
('Victorian Oak Dining Table', 1200.00, 1901, 'Good', 1, 'Beautifully preserved Victorian dining table made of solid oak.', 2, NULL),
('1970s Sony Walkman', 150.00, 1978, 'Used', 3, 'Vintage Sony Walkman in working condition.', 3, 5),
('1980s Nintendo Game Boy', 200.00, 1989, 'Fair', 2, 'Classic Nintendo Game Boy with some signs of wear.', 4, 20);

-- Inserting data into users table
INSERT INTO `onlinevintageshop`.`users` (`username`, `role`, `email`, `address`, `password`) VALUES 
('aliceJ', 'productsCustomer', 'alice@example.com', '123 Main St, Anytown, USA', '0123456789'),
('admin', 'Admin', 'admin@example.com', '456 Elm St, Othertown, USA', 'admin12345');

-- Inserting data into shopping_carts table
INSERT INTO `onlinevintageshop`.`shopping_carts` (`total_sum`, `user_id`) VALUES 
(0, 1),
(0, 2);

-- Inserting data into shopping_cart_items table
INSERT INTO `onlinevintageshop`.`shopping_cart_items` (`product_id`, `shopping_cart_id`, `quantity`) VALUES 
(1, 1, 2),
(3, 1, 1),
(2, 2, 1);

-- Updating total_sum in shopping_carts table with dynamically calculated total sum
UPDATE `onlinevintageshop`.`shopping_carts` sc
JOIN (
    SELECT sci.shopping_cart_id, SUM(p.price * sci.quantity) AS total
    FROM `shopping_cart_items` sci
    JOIN `products` p ON sci.product_id = p.id
    GROUP BY sci.shopping_cart_id
) AS sums ON sc.id = sums.shopping_cart_id
SET sc.total_sum = sums.total;

