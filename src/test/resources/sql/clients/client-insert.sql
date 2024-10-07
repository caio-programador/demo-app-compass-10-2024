insert into `tb_users` (id, username, password, created_by, created_at, role)
values (100, 'ana1@gmail.com', '$2a$12$FFV9ScOgxMREWLr7IcwPseVMaDPxNZyrJfdEDLUdJS04VUYrWPegG', 'Nobody', '2024-09-24 15:41:05.134548','ROLE_ADMIN')
;
insert into `tb_users` (id, username, password, created_by, created_at, role)
values (101, 'ana2@gmail.com', '$2a$12$FFV9ScOgxMREWLr7IcwPseVMaDPxNZyrJfdEDLUdJS04VUYrWPegG', 'Nobody', '2024-09-24 15:41:05.134548','ROLE_CLIENT')
;
insert into `tb_users` (id, username, password, created_by, created_at, role)
values (102, 'ana3@gmail.com', '$2a$12$FFV9ScOgxMREWLr7IcwPseVMaDPxNZyrJfdEDLUdJS04VUYrWPegG', 'Nobody', '2024-09-24 15:41:05.134548','ROLE_CLIENT');

insert into `tb_users` (id, username, password, created_by, created_at, role)
values (103, 'toby@gmail.com', '$2a$12$FFV9ScOgxMREWLr7IcwPseVMaDPxNZyrJfdEDLUdJS04VUYrWPegG', 'Nobody', '2024-09-24 15:41:05.134548','ROLE_CLIENT');


insert into `tb_clients`(id,name,cpf,user_id)
values (11,'Ana Silva', '51407743058', 101);

insert into `tb_clients`(id,name,cpf,user_id)
values (12,'Ana Silva Santos', '26805583080', 102);