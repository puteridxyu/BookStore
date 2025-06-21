-- === Drop tables (FK safe order) ===
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
