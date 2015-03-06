create table if not exists `hml` (
  `hml_id` bigint not null auto_increment,
  `id` varchar(512) not null,
  `hml` longtext not null,
  primary key (`hml_id`),
  index (`id`),
) engine=innodb charset=utf8 auto_increment=1;
