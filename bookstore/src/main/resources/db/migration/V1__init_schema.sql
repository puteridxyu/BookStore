--  DATABASE bookstore;

-- === Drop tables (FK safe order) === 
DROP TABLE IF EXISTS log_entry;
DROP TABLE IF EXISTS family_member;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS userid;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS customer;


-- === customer ===
CREATE TABLE customer (
    customer_id     BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email_office    VARCHAR(150),
    email_personal  VARCHAR(150) UNIQUE NOT NULL,
    phone_number    VARCHAR(20),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- === userid ===
CREATE TABLE userid (
    user_id     BIGSERIAL PRIMARY KEY,
    username    VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(150) UNIQUE,
    role        INTEGER NOT NULL DEFAULT 1,  -- 1 = ADMIN
    is_active   BOOLEAN NOT NULL DEFAULT TRUE
);

-- === family_member ===
CREATE TABLE family_member (
    family_id       BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,
    relationship    VARCHAR(20) NOT NULL CHECK (relationship IN ('Spouse', 'Child', 'Sibling', 'Parent')),
    email           VARCHAR(150),
    phone_number    VARCHAR(20)
);

-- === product ===
CREATE TABLE product (
    product_id      BIGSERIAL PRIMARY KEY,
    book_title      VARCHAR(200) NOT NULL,
    book_price      DECIMAL(10, 2) NOT NULL,
    book_quantity   INTEGER,
    book_category   VARCHAR(50) NOT NULL,
    book_desc       TEXT,
    book_img        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- === Insert customers ===
INSERT INTO customer (first_name, last_name, email_office, email_personal, phone_number) VALUES
('Puteri', 'Zafri', 'puteri.zafri@company.my', 'puteri.z@gmail.com', '0123456789'),
('Daus', 'Rahman', 'afiq.rahman@company.my', 'afiq.r@gmail.com', '0198765432');

-- === Insert users (userid table) ===
INSERT INTO userid (username, password, email, role, is_active) VALUES
('admin1', 'hashed_admin_pw', 'admin@bookstore.my', 1, TRUE),
('staff1', 'hashed_staff_pw', 'staff@bookstore.my', 2, TRUE),
('puteriz', 'hashed_pw_customer', 'puteri.z@gmail.com', 3, TRUE),
('dausrahman', 'hashed_pw_customer', 'afiq.r@gmail.com', 3, TRUE);

-- === Insert family members ===
INSERT INTO family_member (customer_id, name, relationship, email, phone_number) VALUES
(1, 'Zafri Mahamud', 'Parent', 'zafri@gmail.com', '0111000000'),
(1, 'Idrees', 'Child', NULL, '0131234567'),
(1, 'Puteri Intan', 'Sibling', 'puteriintanz@ymail.com', '0147654321'),
(2, 'Nadiah Afiqah', 'Spouse', 'nadiah.afq@gmail.com', '0179990000');

-- === Insert products (books) ===
INSERT INTO product (book_title, book_price, book_quantity, book_category, book_desc, book_img) VALUES
('English Grammar Booster', 29.90, 40, 'English', 'Master grammar with daily practice.', NULL),
('Tunggu Teduh Dulu', 42.00, 15, 'Literature', 'Sebuah novel tentang ketabahan wanita.', NULL),
('Rujukan SPM Matematik Tambahan', 38.50, 60, 'Student Reference', 'Modul latihan Matematik Tambahan SPM.', NULL),
('Asas Sains Komputer Tingkatan 4', 33.00, 50, 'Technology', 'Konsep asas komputer untuk pelajar SPM.', NULL),
('The Power of Habit', 45.00, 20, 'Self Help', 'Bagaimana tabiat terbentuk dan berubah.', NULL);


