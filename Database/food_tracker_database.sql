-- ============================================
-- Smart Food Waste & Donation Tracker Database Schema
-- ============================================

-- Create Database
CREATE DATABASE IF NOT EXISTS food_tracker_db;
USE food_tracker_db;

-- ============================================
-- Table 1: users
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    location VARCHAR(100),
    pincode VARCHAR(10),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_pincode (pincode),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table 2: food_items
-- ============================================
CREATE TABLE IF NOT EXISTS food_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    quantity VARCHAR(50),
    category VARCHAR(50),
    purchase_date DATE NOT NULL,
    expiry_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_category (category),
    INDEX idx_status (status),
    INDEX idx_expiry_date (expiry_date),
    INDEX idx_purchase_date (purchase_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table 3: ngos
-- ============================================
CREATE TABLE IF NOT EXISTS ngos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ngo_name VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address TEXT,
    pincode VARCHAR(10),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_pincode (pincode),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table 4: donations
-- ============================================
CREATE TABLE IF NOT EXISTS donations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    ngo_id BIGINT,
    request_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    pickup_date DATE,
    delivery_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food_items(id) ON DELETE CASCADE,
    FOREIGN KEY (ngo_id) REFERENCES ngos(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_food_id (food_id),
    INDEX idx_ngo_id (ngo_id),
    INDEX idx_status (status),
    INDEX idx_request_date (request_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table 5: recipes
-- ============================================
CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_name VARCHAR(255) NOT NULL,
    description TEXT,
    ingredients TEXT,
    instructions TEXT,
    category VARCHAR(50),
    INDEX idx_category (category),
    INDEX idx_recipe_name (recipe_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data (Optional - for testing)
-- ============================================

-- Insert Sample Users
INSERT INTO users (name, email, password, location, pincode, role) VALUES
('John Doe', 'john@example.com', 'password123', 'New York', '10001', 'USER'),
('Jane Smith', 'jane@example.com', 'password123', 'Los Angeles', '90001', 'USER'),
('Admin User', 'admin@foodtracker.com', 'admin123', 'San Francisco', '94102', 'ADMIN')
ON DUPLICATE KEY UPDATE email=email;

-- Insert Sample NGOs
INSERT INTO ngos (ngo_name, email, password, address, pincode, status) VALUES
('Feed the Hungry Foundation', 'feedhungry@ngo.com', 'password123', '123 Main Street, New York', '10001', 'APPROVED'),
('Community Food Bank', 'foodbank@ngo.com', 'password123', '456 Oak Avenue, Los Angeles', '90001', 'APPROVED'),
('Hunger Relief Organization', 'hungerrelief@ngo.com', 'password123', '789 Pine Road, Chicago', '60601', 'PENDING'),
('Food for All NGO', 'foodforall@ngo.com', 'password123', '321 Elm Street, Houston', '77001', 'APPROVED')
ON DUPLICATE KEY UPDATE email=email;

-- Insert Sample Food Items
INSERT INTO food_items (user_id, item_name, quantity, category, purchase_date, expiry_date, status) VALUES
(1, 'Fresh Tomatoes', '1 kg', 'VEGETABLE', DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ACTIVE'),
(1, 'Bread', '2 loaves', 'BREAD', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ACTIVE'),
(1, 'Milk', '1 liter', 'DAIRY', DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'ACTIVE'),
(2, 'Cooked Rice', '500g', 'COOKED_FOOD', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'ACTIVE'),
(2, 'Packaged Pasta', '500g', 'PACKAGED', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'ACTIVE'),
(2, 'Fresh Apples', '2 kg', 'FRUIT', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ACTIVE'),
(1, 'Yogurt', '500g', 'DAIRY', DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'ACTIVE')
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Insert Sample Recipes
INSERT INTO recipes (recipe_name, description, ingredients, instructions, category) VALUES
('Tomato Soup', 'Delicious tomato soup recipe', 'Fresh tomatoes, onions, garlic, olive oil, salt, pepper', '1. Heat oil, 2. Add onions and garlic, 3. Add tomatoes, 4. Blend and serve', 'VEGETABLE'),
('Fried Rice', 'Quick and easy fried rice', 'Cooked rice, vegetables, soy sauce, oil, eggs', '1. Heat oil, 2. Add vegetables, 3. Add rice, 4. Mix well and serve', 'COOKED_FOOD'),
('Pasta Primavera', 'Fresh vegetable pasta', 'Pasta, fresh vegetables, olive oil, garlic, cheese', '1. Cook pasta, 2. Sauté vegetables, 3. Mix together, 4. Serve with cheese', 'PACKAGED'),
('Apple Smoothie', 'Healthy apple smoothie', 'Fresh apples, yogurt, honey, ice', '1. Blend apples, 2. Add yogurt, 3. Add honey, 4. Serve chilled', 'FRUIT'),
('Yogurt Parfait', 'Layered yogurt dessert', 'Yogurt, fruits, granola, honey', '1. Layer yogurt, 2. Add fruits, 3. Add granola, 4. Drizzle honey', 'DAIRY'),
('Bread Pudding', 'Sweet bread pudding', 'Bread, milk, eggs, sugar, vanilla', '1. Mix ingredients, 2. Soak bread, 3. Bake, 4. Serve warm', 'BREAD'),
('Vegetable Curry', 'Spicy vegetable curry', 'Mixed vegetables, curry spices, coconut milk', '1. Heat oil, 2. Add spices, 3. Add vegetables, 4. Simmer and serve', 'VEGETABLE'),
('Fruit Salad', 'Fresh mixed fruit salad', 'Various fruits, honey, lemon juice', '1. Cut fruits, 2. Mix together, 3. Add honey and lemon, 4. Chill and serve', 'FRUIT')
ON DUPLICATE KEY UPDATE recipe_name=recipe_name;

-- Insert Sample Donations (if you have matching food items)
-- Note: These will only work if the food items above were inserted successfully
-- Uncomment after inserting food items if needed

/*
INSERT INTO donations (user_id, food_id, ngo_id, request_date, status) VALUES
(1, 1, 1, CURDATE(), 'PENDING'),
(2, 4, 2, CURDATE(), 'ACCEPTED'),
(1, 3, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'COMPLETED')
ON DUPLICATE KEY UPDATE user_id=user_id;
*/

-- ============================================
-- Views (Optional - for easier queries)
-- ============================================

-- View: Food items expiring soon (next 3 days)
CREATE OR REPLACE VIEW food_expiring_soon AS
SELECT 
    fi.id,
    fi.item_name,
    fi.quantity,
    fi.category,
    fi.expiry_date,
    fi.status,
    u.name AS user_name,
    u.location,
    u.pincode,
    DATEDIFF(fi.expiry_date, CURDATE()) AS days_until_expiry
FROM food_items fi
JOIN users u ON fi.user_id = u.id
WHERE fi.status = 'ACTIVE'
    AND fi.expiry_date IS NOT NULL
    AND fi.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY)
ORDER BY fi.expiry_date ASC;

-- View: Active donation requests
CREATE OR REPLACE VIEW active_donations AS
SELECT 
    d.id,
    d.request_date,
    d.status,
    fi.item_name,
    fi.quantity,
    fi.category,
    u.name AS donor_name,
    u.location AS donor_location,
    u.pincode AS donor_pincode,
    n.ngo_name,
    n.address AS ngo_address,
    n.pincode AS ngo_pincode
FROM donations d
JOIN food_items fi ON d.food_id = fi.id
JOIN users u ON d.user_id = u.id
LEFT JOIN ngos n ON d.ngo_id = n.id
WHERE d.status IN ('PENDING', 'ACCEPTED')
ORDER BY d.request_date DESC;

-- View: User statistics
CREATE OR REPLACE VIEW user_stats AS
SELECT 
    u.id,
    u.name,
    u.email,
    u.location,
    COUNT(DISTINCT fi.id) AS total_food_items,
    COUNT(DISTINCT CASE WHEN fi.status = 'ACTIVE' THEN fi.id END) AS active_items,
    COUNT(DISTINCT CASE WHEN fi.status = 'DONATED' THEN fi.id END) AS donated_items,
    COUNT(DISTINCT d.id) AS total_donations,
    COUNT(DISTINCT CASE WHEN d.status = 'COMPLETED' THEN d.id END) AS completed_donations
FROM users u
LEFT JOIN food_items fi ON u.id = fi.user_id
LEFT JOIN donations d ON u.id = d.user_id
WHERE u.role = 'USER'
GROUP BY u.id, u.name, u.email, u.location;

-- View: NGO statistics
CREATE OR REPLACE VIEW ngo_stats AS
SELECT 
    n.id,
    n.ngo_name,
    n.email,
    n.location,
    n.status,
    COUNT(DISTINCT d.id) AS total_donations,
    COUNT(DISTINCT CASE WHEN d.status = 'PENDING' THEN d.id END) AS pending_donations,
    COUNT(DISTINCT CASE WHEN d.status = 'ACCEPTED' THEN d.id END) AS accepted_donations,
    COUNT(DISTINCT CASE WHEN d.status = 'COMPLETED' THEN d.id END) AS completed_donations
FROM ngos n
LEFT JOIN donations d ON n.id = d.ngo_id
GROUP BY n.id, n.ngo_name, n.email, n.location, n.status;

-- ============================================
-- Useful Queries
-- ============================================

-- Find food items expiring in next 2 days
-- SELECT * FROM food_items 
-- WHERE status = 'ACTIVE' 
--   AND expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 2 DAY);

-- Find pending donations
-- SELECT * FROM donations WHERE status = 'PENDING';

-- Find approved NGOs by pincode
-- SELECT * FROM ngos WHERE status = 'APPROVED' AND pincode = '10001';

-- Find recipes by category
-- SELECT * FROM recipes WHERE category = 'VEGETABLE';

-- ============================================
-- Stored Procedures (Optional)
-- ============================================

DELIMITER //

-- Procedure: Get food items expiring soon for a user
CREATE PROCEDURE IF NOT EXISTS GetExpiringFoodItems(
    IN p_user_id BIGINT,
    IN p_days INT
)
BEGIN
    SELECT 
        fi.*,
        DATEDIFF(fi.expiry_date, CURDATE()) AS days_until_expiry
    FROM food_items fi
    WHERE fi.user_id = p_user_id
        AND fi.status = 'ACTIVE'
        AND fi.expiry_date IS NOT NULL
        AND fi.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_days DAY)
    ORDER BY fi.expiry_date ASC;
END //

-- Procedure: Get nearby NGOs by pincode
CREATE PROCEDURE IF NOT EXISTS GetNearbyNGOs(
    IN p_pincode VARCHAR(10)
)
BEGIN
    SELECT * 
    FROM ngos 
    WHERE status = 'APPROVED' 
        AND pincode = p_pincode
    ORDER BY created_at DESC;
END //

-- Procedure: Get user donation history
CREATE PROCEDURE IF NOT EXISTS GetUserDonations(
    IN p_user_id BIGINT
)
BEGIN
    SELECT 
        d.*,
        fi.item_name,
        fi.quantity,
        fi.category,
        n.ngo_name,
        n.address AS ngo_address
    FROM donations d
    JOIN food_items fi ON d.food_id = fi.id
    LEFT JOIN ngos n ON d.ngo_id = n.id
    WHERE d.user_id = p_user_id
    ORDER BY d.created_at DESC;
END //

DELIMITER ;

-- ============================================
-- End of Schema
-- ============================================

-- Show tables
SHOW TABLES;

-- Show table structures
-- DESCRIBE users;
-- DESCRIBE food_items;
-- DESCRIBE ngos;
-- DESCRIBE donations;
-- DESCRIBE recipes;

