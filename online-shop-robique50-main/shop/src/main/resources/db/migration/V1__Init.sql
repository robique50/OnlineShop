CREATE SCHEMA IF NOT EXISTS "online-shop";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS product_category (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  name VARCHAR(255) NOT NULL,
                                  description TEXT
);

CREATE TABLE IF NOT EXISTS product (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL NOT NULL,
                         weight DOUBLE PRECISION,
                         category_id UUID REFERENCES product_category(id),
                         supplier VARCHAR(255),
                         image_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS location (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          name VARCHAR(255) NOT NULL,
                          country VARCHAR(255),
                          city VARCHAR(255),
                          street_address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS stock (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       product_id UUID REFERENCES product(id),
                       location_id UUID REFERENCES location(id),
                       quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS "user" (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email_address VARCHAR(255),
                        user_role VARCHAR(50) NOT NULL CHECK (user_role IN ('ADMIN', 'CUSTOMER'))
);

CREATE TABLE IF NOT EXISTS "placed_order" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         customer_id UUID REFERENCES "user"(id),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         country VARCHAR(255),
                         city VARCHAR(255),
                         street_address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_detail (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              order_id UUID REFERENCES "placed_order"(id),
                              product_id UUID REFERENCES product(id),
                              shipped_from UUID REFERENCES location(id),
                              quantity INTEGER
);