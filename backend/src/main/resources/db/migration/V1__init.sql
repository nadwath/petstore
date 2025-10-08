-- ===========================================
--  PostgreSQL Schema Initialization Script
--  Matches current DB schema (no Flyway)
-- ===========================================

-- ====== ROLES ======
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(64) NOT NULL
    );

-- ====== USERS ======
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    user_status INTEGER
    );

-- ====== USER_ROLES ======
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
    );

-- ====== USER_RIGHTS ======
CREATE TABLE IF NOT EXISTS user_rights (
                                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    right_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id, right_name)
    );

-- ====== CATEGORIES ======
CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL
    );

-- ====== TAGS ======
CREATE TABLE IF NOT EXISTS tags (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL
    );

-- ====== PETS ======
CREATE TABLE IF NOT EXISTS pets (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(128) NOT NULL,
    category VARCHAR(128),
    status VARCHAR(32) NOT NULL,
    tags TEXT,
    category_id BIGINT REFERENCES categories(id)
    );

-- ====== PET_PHOTOS ======
CREATE TABLE IF NOT EXISTS pet_photos (
                                          pet_id BIGINT NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    photo_url VARCHAR(255)
    );

-- ====== PET_TAGS ======
CREATE TABLE IF NOT EXISTS pet_tags (
                                        pet_id BIGINT NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (pet_id, tag_id)
    );

-- ====== ORDERS ======
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      complete BOOLEAN NOT NULL DEFAULT FALSE,
                                      quantity INTEGER NOT NULL,
                                      ship_date TIMESTAMP NOT NULL,
                                      status VARCHAR(255) NOT NULL,
    pet_id BIGINT REFERENCES pets(id)
    );

-- ===========================================
--             SEED DATA
-- ===========================================

-- Roles
INSERT INTO roles(name) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES ('USER') ON CONFLICT DO NOTHING;

-- Admin user
INSERT INTO users(username, password_hash, enabled, email, first_name, last_name, phone, user_status)
SELECT 'admin',
       '$2a$10$Q3s3tq5dwb4d0.1w5lMz.Ov3ZkC9X2w4p5CHvQYqQj6q3tP4K5y5.',  -- bcrypt for "admin"
       TRUE,
       'admin@example.com',
       'System',
       'Administrator',
       '0000000000',
       1
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Assign ADMIN role to admin user
INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- Assign rights
INSERT INTO user_rights(user_id, right_name)
SELECT u.id, v.right_name
FROM users u
         JOIN (VALUES ('PET_READ'), ('PET_WRITE'), ('PET_DELETE')) v(right_name)
              ON TRUE
WHERE u.username = 'admin'
  AND NOT EXISTS (SELECT 1 FROM user_rights ur WHERE ur.user_id = u.id);

-- Sample Categories
INSERT INTO categories(name)
VALUES ('Dog'), ('Cat'), ('Fish')
    ON CONFLICT DO NOTHING;

-- Sample Tags
INSERT INTO tags(name)
VALUES ('Friendly'), ('Playful'), ('Small')
    ON CONFLICT DO NOTHING;

-- Sample Pets
INSERT INTO pets(name, category, status, tags, category_id)
SELECT 'Scooby Doo', 'Dog', 'AVAILABLE', 'friendly,young', c.id FROM categories c WHERE c.name = 'Dog'
UNION ALL
SELECT 'Kitty', 'Cat', 'PENDING', 'playful', c.id FROM categories c WHERE c.name = 'Cat'
UNION ALL
SELECT 'Nemo', 'Fish', 'SOLD', 'small', c.id FROM categories c WHERE c.name = 'Fish'
    ON CONFLICT DO NOTHING;

-- Sample Orders
INSERT INTO orders(complete, quantity, ship_date, status, pet_id)
SELECT FALSE, 1, NOW(), 'PLACED', p.id FROM pets p LIMIT 1;

-- ===========================================
-- End of Script
-- ===========================================