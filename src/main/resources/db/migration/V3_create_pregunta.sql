use btrestia;
Create table if not exists pregunta(
id bigint auto_increment primary key,
token varchar(255),
texto text,
fecha timestamp default current_timestamp
);
insert ignore into db_version(script_name) values( 'V3_create_pregunta.sql');
