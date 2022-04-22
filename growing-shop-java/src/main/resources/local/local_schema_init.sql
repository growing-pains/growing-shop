CREATE TABLE company (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    business_registration_number VARCHAR(10),
    status VARCHAR(20) NOT NULL COMMENT 'NORMAL|UNDER_REVIEW',
    grade VARCHAR(20) NOT NULL DEFAULT 'BRONZE' COMMENT 'BRONZE|SILVER|GOLD|PLATINUM|DIAMOND',

    PRIMARY KEY (id)
);

CREATE TABLE user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    mobile VARCHAR(11) NOT NULL,
    email VARCHAR(255) NOT NULL,
    login_id VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    company bigint(20),
    status VARCHAR(20) NOT NULL COMMENT 'NORMAL|UNDER_REVIEW|WITHDRAWAL',
    type VARCHAR(20) NOT NULL COMMENT 'NORMAL|SELLER|ADMIN',
    grade VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL|VIP|VVIP|MASTER',

    PRIMARY KEY (id),
    CONSTRAINT user_company FOREIGN KEY (company) REFERENCES company(id)
);

CREATE TABLE product (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    price int NOT NULL,
    company bigint(20) NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT 'NORMAL|UNDER_REVIEW|DELETED',

    PRIMARY KEY (id),
    CONSTRAINT product_company FOREIGN KEY (company) REFERENCES company(id)
);

CREATE TABLE category (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE product_category (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    product bigint(20) NOT NULL,
    category bigint(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT product_category_product FOREIGN KEY (product) REFERENCES product(id),
    CONSTRAINT product_category_category FOREIGN KEY (category) REFERENCES category(id)
);

CREATE TABLE "order" (
    id varchar(255) NOT NULL AUTO_INCREMENT,
    order_at datetime NOT NULL,
    user bigint(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT user_order FOREIGN KEY (user) REFERENCES user(id)
);

CREATE TABLE order_line (
    "order" bigint(20) NOT NULL,
    product bigint(20) NOT NULL,
    price int NOT NULL,
    quantity int NOT NULL,

    CONSTRAINT order_line_order FOREIGN KEY ("order") REFERENCES "order"(id),
    CONSTRAINT order_line_product FOREIGN KEY (product) REFERENCES product(id)
);
