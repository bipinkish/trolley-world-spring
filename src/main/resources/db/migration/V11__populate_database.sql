-- Insert categories
INSERT INTO categories (name) VALUES
('Fruits & Vegetables'),
('Dairy & Bakery'),
('Snacks & Beverages'),
('Meat & Seafood'),
('Frozen Foods'),
('Personal Care'),
('Household Supplies'),
('Baby Care'),
('Breakfast & Cereals'),
('Packaged Foods');

-- Insert products
INSERT INTO products (name, price, description, category_id) VALUES
-- Fruits & Vegetables
('Banana (1 dozen)', 60.00, 'Fresh ripe bananas sourced locally.', 1),
('Tomatoes (1 kg)', 45.00, 'Farm-fresh red tomatoes.', 1),
('Potatoes (1 kg)', 35.00, 'Premium quality potatoes.', 1),
('Onions (1 kg)', 40.00, 'Fresh onions perfect for everyday cooking.', 1),
('Apples (1 kg)', 160.00, 'Imported crisp red apples.', 1),

-- Dairy & Bakery
('Amul Milk (1L)', 68.00, 'Toned milk from Amul dairy.', 2),
('Britannia Whole Wheat Bread (400g)', 45.00, 'Soft and fresh wheat bread.', 2),
('Amul Butter (100g)', 60.00, 'Creamy salted butter.', 2),
('Mother Dairy Curd (500g)', 45.00, 'Thick and creamy curd.', 2),

-- Snacks & Beverages
('Lay’s Classic Salted Chips (90g)', 30.00, 'Crispy potato chips with salt flavor.', 3),
('Coca-Cola (1.25L)', 55.00, 'Refreshing soft drink.', 3),
('Frooti Mango Drink (600ml)', 40.00, 'Real mango juice refreshment.', 3),
('Parle-G Biscuits (800g)', 50.00, 'Classic glucose biscuits.', 3),

-- Meat & Seafood
('Chicken Breast (500g)', 180.00, 'Fresh boneless chicken breast.', 4),
('Mutton Curry Cut (1kg)', 650.00, 'Tender goat meat pieces for curry.', 4),
('Rohu Fish (1kg)', 300.00, 'Freshwater fish rich in protein.', 4),

-- Frozen Foods
('McCain French Fries (750g)', 180.00, 'Ready-to-fry crispy fries.', 5),
('Veg Momos (10 pieces)', 120.00, 'Delicious steamed vegetable momos.', 5),

-- Personal Care
('Colgate Toothpaste (150g)', 75.00, 'Strong teeth and fresh breath.', 6),
('Dove Soap (100g)', 70.00, 'Moisturizing beauty bathing bar.', 6),
('Head & Shoulders Shampoo (180ml)', 135.00, 'Anti-dandruff shampoo.', 6),

-- Household Supplies
('Vim Dishwash Liquid (500ml)', 110.00, 'Removes grease and stains easily.', 7),
('Surf Excel Matic (1kg)', 250.00, 'Powerful front-load detergent.', 7),
('Lizol Floor Cleaner (1L)', 190.00, 'Kills 99.9% of germs on floors.', 7),

-- Baby Care
('Pampers Baby Diapers (Pack of 20)', 320.00, 'Comfortable and absorbent diapers.', 8),
('Johnson’s Baby Lotion (200ml)', 150.00, 'Gentle care for baby’s soft skin.', 8),

-- Breakfast & Cereals
('Kellogg’s Corn Flakes (475g)', 180.00, 'Crispy flakes made from real corn.', 9),
('Nestle Everyday Milk Powder (400g)', 210.00, 'Instant milk powder for tea and coffee.', 9),
('Quaker Oats (1kg)', 220.00, 'High fiber oats for a healthy start.', 9),

-- Packaged Foods
('Maggie Noodles (Pack of 4)', 60.00, 'Instant noodles with masala flavor.', 10),
('Tata Salt (1kg)', 25.00, 'Iodized salt for daily use.', 10),
('Aashirvaad Atta (5kg)', 280.00, 'High-quality whole wheat flour.', 10),
('Saffola Gold Oil (1L)', 210.00, 'Blended oil for healthy heart.', 10);
