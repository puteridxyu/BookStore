-- === Drop existing tables (in correct FK order) ===
DROP TABLE IF EXISTS product_audit;
DROP TABLE IF EXISTS log_entry;
DROP TABLE IF EXISTS family_member;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS customer;

-- === 1. customer Table ===
CREATE TABLE customer (
    customer_id     BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email_office    VARCHAR(150),
    email_personal  VARCHAR(150),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- === 2. user Table (Login) ===
CREATE TABLE "user" (
    user_id         BIGSERIAL PRIMARY KEY,
    username        VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email           VARCHAR(150),
	is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    role            VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' CHECK (role IN ('ADMIN', 'STAFF', 'CUSTOMER')),
    customer_id     BIGINT REFERENCES customer(customer_id) ON DELETE SET NULL
);

-- === 3. family_member Table ===
CREATE TABLE family_member (
    family_id       BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,
    relationship    VARCHAR(20) NOT NULL CHECK (relationship IN ('Spouse', 'Child', 'Sibling', 'Parent')),
    email           VARCHAR(150)
);

-- === 4. product Table ===
CREATE TABLE product (
    product_id      BIGSERIAL PRIMARY KEY,
    book_title      VARCHAR(200) NOT NULL,
    book_price      DECIMAL(10, 2) NOT NULL,
    book_quantity   INTEGER,
    book_category   VARCHAR(50) NOT NULL,
    book_desc       TEXT,
    book_img        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT product_book_category_check CHECK (book_category IN (
        'English',
        'Literature',
        'Student Reference',
        'Science',
        'Technology',
        'Children',
        'Religion',
        'Comics',
        'History',
        'Business',
        'Self Help',
        'Cooking',
        'Health & Wellness',
        'Travel',
        'Motivation',
        'Art & Design'
    ))
);

-- === 5. product_audit Table ===
CREATE TABLE product_audit (
    pro_audit_id    BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    event_type      VARCHAR(20) NOT NULL CHECK (event_type IN ('CREATE', 'UPDATE', 'DELETE')),
    change_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_value       TEXT,
    new_value       TEXT
);

-- === 6. log_entry Table ===
CREATE TABLE log_entry (
    log_id          BIGSERIAL PRIMARY KEY,
    timestamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method          VARCHAR(10) NOT NULL,
    endpoint        VARCHAR(255) NOT NULL,
    request_body    TEXT,
    response_body   TEXT,
    status_code     INTEGER,
    ip_address      VARCHAR(45)
);
