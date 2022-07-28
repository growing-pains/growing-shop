insert into role(name) values ('ADMIN'), ('SELLER'), ('NORMAL');

insert into privilege(name, path, description) values
('ROLE', '/roles', 'role 접근 권한'),
('privilege', '/privileges', '경로/기능 접근 권한');

insert into role_privilege(role_id, privilege_id) values
(1, 1), (1, 2);
