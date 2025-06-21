-- === Drop tables  ===
DROP TABLE IF EXISTS log_entry;
DROP TABLE IF EXISTS family_member;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS customer;

-- === customer ===
CREATE TABLE customer (
    customer_id     BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email_office    VARCHAR(150),
    email_personal  VARCHAR(150) UNIQUE,
    phone_number    VARCHAR(20),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- === user ===
CREATE TABLE "user" (
    user_id         BIGSERIAL PRIMARY KEY,
    username        VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    email           VARCHAR(150) UNIQUE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    customer_id     BIGINT REFERENCES customer(customer_id) ON DELETE SET NULL
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

-- === log_entry ===
CREATE TABLE log_entry (
    log_id          BIGSERIAL PRIMARY KEY,
    timestamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method          VARCHAR(10) NOT NULL,
    endpoint        VARCHAR(255) NOT NULL,
    request_body    TEXT,
    response_body   TEXT,
    status_code     INTEGER,
    ip_address      VARCHAR(45),
    user_id         BIGINT REFERENCES "user"(user_id) ON DELETE SET NULL
);

-- === Insert sample data ===
INSERT INTO customer (customer_id, first_name, last_name, email_office, email_personal, phone_number)
VALUES 
(1, 'Puteri', 'Zafri', 'puteri.zafri@company.my', 'puteri.z@gmail.com', '0123456789'),
(2, 'Afiq', 'Rahman', 'afiq.rahman@company.my', 'afiq.r@gmail.com', '0198765432');

INSERT INTO "user" (username, password, email, customer_id, is_active)
VALUES 
  ('admin1', 'hashed_admin_pw', 'admin@bookstore.my', NULL, TRUE),
  ('staff1', 'hashed_staff_pw', 'staff@bookstore.my', NULL, TRUE),
  ('puteriz', 'hashed_customer_pw', 'puteri.z@gmail.com', 1, TRUE),
  ('staff2', 'hashed_staff_pw2', 'staff2@bookstore.my', NULL, TRUE);

INSERT INTO family_member (customer_id, name, relationship, email, phone_number)
VALUES 
(1, 'Zafri Mahamud', 'Parent', 'zafri@gmail.com', '0111000000'),
(1, 'Idrees', 'Child', NULL, '0131234567'),
(1, 'Puteri Intan', 'Sibling', 'puteriintanz@ymail.com', '0147654321'),
(2, 'Nadiah Afiqah', 'Spouse', 'nadiah.afq@gmail.com', '0179990000');

INSERT INTO product (book_title, book_price, book_quantity, book_category, book_desc, book_img) VALUES
('English Grammar Booster', 29.90, 40, 'English', 'A practical guide to mastering English grammar with daily exercises.', NULL),
('Tunggu Teduh Dulu', 42.00, 15, 'Literature', 'Sebuah novel yang menyentuh tema kekuatan wanita dalam menghadapi takdir.', NULL),
('Rujukan SPM Matematik Tambahan', 38.50, 60, 'Student Reference', 'Modul latihan dan soalan peperiksaan SPM untuk subjek Matematik Tambahan.', NULL),
('Asas Sains Komputer Tingkatan 4', 33.00, 50, 'Technology', 'Panduan lengkap bagi pelajar SPM tentang konsep asas komputer dan kod.', NULL),
('The Power of Habit', 45.00, 20, 'Self Help', 'Buku popular tentang bagaimana tabiat terbentuk dan bagaimana kita boleh mengubahnya.', NULL);
