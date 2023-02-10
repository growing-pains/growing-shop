insert into company(name, business_registration_number, status, grade, created_at, modified_at)
values ('성장하는 상점', '0000000000', 'NORMAL', 'DIAMOND', now(), now());

insert into "user"(name, mobile, email, login_id, password, company, status, type, grade)
values ('관리자', '01000000000', 'admin@growing.shop', 'admin',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', null, 'NORMAL', 'ADMIN', 'NORMAL'),
       ('판매자', '01000000000', 'seller@growing.shop', 'seller',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', 1, 'NORMAL', 'SELLER', 'NORMAL'),
       ('일반 유저', '01000000000', 'normal@growing.shop', 'normal',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', null, 'NORMAL', 'NORMAL', 'NORMAL');

insert into product(name, price, status)
values ('상품 1', 1000, 'NORMAL'),
       ('상품 2', 2000, 'NORMAL'),
       ('상품 3', 3000, 'NORMAL'),
       ('상품 4', 4000, 'NORMAL'),
       ('상품 5', 5000, 'NORMAL');

insert into "order"("user_id", status, order_at)
values (3, 'WAITING', now()),
       (3, 'NORMAL', now());

insert into order_line(product_id, price, quantity, "order", is_deleted)
values (1, 3000, 3, 1, false),
       (2, 4000, 2, 1, false),
       (3, 3000, 1, 1, true),
       (4, 12000, 3, 2, false),
       (3, 6000, 2, 2, false),
       (5, 10000, 2, 2, false);
