ALTER TABLE product
    ALTER
        COLUMN description TYPE VARCHAR(255) USING (description::VARCHAR(255));

ALTER TABLE product_category
    ALTER
        COLUMN description TYPE VARCHAR(255) USING (description::VARCHAR(255));

ALTER TABLE location
    ALTER COLUMN name DROP NOT NULL;

ALTER TABLE product
    ALTER COLUMN name DROP NOT NULL;

ALTER TABLE product_category
    ALTER COLUMN name DROP NOT NULL;

ALTER TABLE order_detail
    ALTER COLUMN order_id SET NOT NULL;

ALTER TABLE "user"
    ALTER COLUMN password DROP NOT NULL;

ALTER TABLE product
    ALTER COLUMN price DROP NOT NULL;

ALTER TABLE order_detail
    ALTER COLUMN product_id SET NOT NULL;

ALTER TABLE stock
    ALTER COLUMN quantity DROP NOT NULL;

ALTER TABLE order_detail
    ALTER COLUMN shipped_from SET NOT NULL;

ALTER TABLE "user"
    ALTER
        COLUMN user_role TYPE VARCHAR(255) USING (user_role::VARCHAR(255));

ALTER TABLE "user"
    ALTER COLUMN user_role DROP NOT NULL;

ALTER TABLE "user"
    ALTER COLUMN username DROP NOT NULL;