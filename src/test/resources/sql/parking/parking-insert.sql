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

insert into `tb_vagas` (id,code,status)
values (10,'A-01', 'LIVRE');
insert into `tb_vagas` (id,code,status)
values (20,'A-02', 'LIVRE');
insert into `tb_vagas` (id,code,status)
values (30,'A-03', 'OCUPADA');
insert into `tb_vagas` (id,code,status)
values (40,'A-04', 'LIVRE');

insert into tb_clients_vagas (receipt_number, plate, brand,
                                model, color, entry_date, client_id, vaga_id)
values ('20230313-101500', 'JBH0J05','HONDA', 'CIVIC', 'BRANCO', '2023-03-13 10:15:00', 12, 10);

insert into tb_clients_vagas (receipt_number, plate, brand,
                                model, color, entry_date, client_id, vaga_id)
values ('20230314-101500', 'IJX0I61','CHEVROLET', 'CELTA', 'PRATA', '2023-03-14 10:15:00', 11, 20);

insert into tb_clients_vagas (receipt_number, plate, brand,
                                model, color, entry_date, client_id, vaga_id)
values ('20230315-101500', 'JBH0J05','HONDA', 'CIVIC', 'BRANCO', '2023-03-15 10:15:00', 12, 30);