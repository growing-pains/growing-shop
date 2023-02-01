insert into company(name, business_registration_number, status, grade, created_at, modified_at)
values ('성장하는 상점', '0000000000', 'NORMAL', 'DIAMOND', now(), now());

insert into "user"(name, mobile, email, login_id, password, company, status, type, grade)
values ('관리자', '01000000000', 'admin@growing.shop', 'admin',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', null, 'NORMAL', 'ADMIN', 'NORMAL'),
       ('판매자', '01000000000', 'seller@growing.shop', 'seller',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', 1, 'NORMAL', 'SELLER', 'NORMAL'),
       ('일반 유저', '01000000000', 'normal@growing.shop', 'normal',
        '$2a$10$JerbZtT.2QH4NtLillE4ueutqYwYUVMHuhpuqr1ZbWT1GBSGQBzVC', null, 'NORMAL', 'NORMAL', 'NORMAL');
