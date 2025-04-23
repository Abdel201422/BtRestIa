use btrestia;
Create table if not exists respuesta(
id bigint auto_increment primary key,
token varchar(255),
texto text,
fecha timestamp default current_timestamp
);
insert ignore into db_version(script_name) values( 'V4_create_respuesta.sql');
