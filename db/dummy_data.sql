-- === Insert family members (linked to Afiqah) ===
-- Assuming her customer_id is 1 (auto-increment)
INSERT INTO family_member (customer_id, name, relationship, email)
VALUES 
(5, 'Zafri Mahamud', 'Parent', 'zafri@gmail.com'),
(5, 'Idrees', 'Child', NULL),
(5, 'Puteri Intan', 'Sibling', 'puteriintanz@ymail.com');

-- === Insert customer === 
INSERT INTO "user" (username, password, email, role, customer_id, is_active)
VALUES 
('puteriz', 'hashed_customer_pw', 'puteri.z@gmail.com', 'CUSTOMER', 5, TRUE);

-- === Insert products (books) ===
INSERT INTO product (book_title, book_price, book_quantity, book_category, book_desc, book_img) VALUES
('English Grammar Booster', 29.90, 40, 'English', 'A practical guide to mastering English grammar with daily exercises.', NULL),
('Tunggu Teduh Dulu', 42.00, 15, 'Literature', 'Sebuah novel yang menyentuh tema kekuatan wanita dalam menghadapi takdir.', NULL),
('Rujukan SPM Matematik Tambahan', 38.50, 60, 'Student Reference', 'Modul latihan dan soalan peperiksaan SPM untuk subjek Matematik Tambahan.', NULL);

-- === Insert users (admin + staff + cust) ===
INSERT INTO "user" (username, password, email, role, customer_id, is_active)
VALUES 
  ('admin1', 'hashed_admin_pw', 'admin@bookstore.my', 'ADMIN', NULL, TRUE),
  ('staff1', 'hashed_staff_pw', 'staff@bookstore.my', 'STAFF', NULL, TRUE),
  ('puteriz', 'hashed_customer_pw', 'puteri.z@gmail.com', 'CUSTOMER', 1, TRUE);

 