insert into role(name) values ('ADMIN'), ('SELLER'), ('NORMAL');

insert into policy(name, path, description) values
('ROLE', '/roles', 'role 접근 권한'),
('policy', '/policies', '경로/기능 접근 권한');

insert into role_policy(role_id, policy_id) values
(1, 1), (1, 2);
