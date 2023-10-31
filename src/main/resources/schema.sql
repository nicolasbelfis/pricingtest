CREATE TABLE pricing_table (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand_id INT,
    product_id INT,
    price_list_id INT,
    priority INT,
    price DECIMAL(10,2),
    currency VARCHAR(3),
    start_date_range TIMESTAMP,
    end_date_range TIMESTAMP
    );
