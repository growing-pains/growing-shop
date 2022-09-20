insert into role(name) values ('ADMIN'), ('SELLER'), ('NORMAL');

insert into policy(name, path, method, description) values
('ROLE', '/roles', 'ALL', 'role 접근 권한'),
('POLICY', '/policies', 'ALL', '경로/기능 접근 권한'),
('TEST', '/tests', 'ALL', '테스트');

insert into role_policy(role_id, policy_id) values
(1, 1), (1, 2), (1, 3), (2, 3), (3, 3);
