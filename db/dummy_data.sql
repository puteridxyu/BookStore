-- === Insert customers === 
INSERT INTO customer (customer_id, first_name, last_name, email_office, email_personal, phone_number)
VALUES 
(1, 'Puteri', 'Zafri', 'puteri.zafri@company.my', 'puteri.z@gmail.com', '0123456789'),
(2, 'Afiq', 'Rahman', 'afiq.rahman@company.my', 'afiq.r@gmail.com', '0198765432');

-- === Insert users === 
INSERT INTO "user" (username, password, email, customer_id, is_active)
VALUES 
  ('admin1', 'hashed_admin_pw', 'admin@bookstore.my', NULL, TRUE),
  ('staff1', 'hashed_staff_pw', 'staff@bookstore.my', NULL, TRUE),
  ('puteriz', 'hashed_customer_pw', 'puteri.z@gmail.com', 1, TRUE),
  ('staff2', 'hashed_staff_pw2', 'staff2@bookstore.my', NULL, TRUE);

-- === Insert family members ===
INSERT INTO family_member (customer_id, name, relationship, email, phone_number)
VALUES 
(1, 'Zafri Mahamud', 'Parent', 'zafri@gmail.com', '0111000000'),
(1, 'Idrees', 'Child', NULL, '0131234567'),
(1, 'Puteri Intan', 'Sibling', 'puteriintanz@ymail.com', '0147654321'),
(2, 'Nadiah Afiqah', 'Spouse', 'nadiah.afq@gmail.com', '0179990000');

-- === Insert products (books) ===
INSERT INTO product (book_title, book_price, book_quantity, book_category, book_desc, book_img) VALUES
('English Grammar Booster', 29.90, 40, 'English', 'A practical guide to mastering English grammar with daily exercises.', NULL),
('Tunggu Teduh Dulu', 42.00, 15, 'Literature', 'Sebuah novel yang menyentuh tema kekuatan wanita dalam menghadapi takdir.', NULL),
('Rujukan SPM Matematik Tambahan', 38.50, 60, 'Student Reference', 'Modul latihan dan soalan peperiksaan SPM untuk subjek Matematik Tambahan.', NULL),
('Asas Sains Komputer Tingkatan 4', 33.00, 50, 'Technology', 'Panduan lengkap bagi pelajar SPM tentang konsep asas komputer dan kod.', NULL),
('The Power of Habit', 45.00, 20, 'Self Help', 'Buku popular tentang bagaimana tabiat terbentuk dan bagaimana kita boleh mengubahnya.', NULL);
