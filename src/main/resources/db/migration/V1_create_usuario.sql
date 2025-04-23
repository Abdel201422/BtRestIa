use btrestia;
Create table if not exists usuario(
id bigint auto_increment primary key,
token varchar(255),
nombre varchar(255),
email varchar(255),
fecha_creacion timestamp default current_timestamp
);

insert ignore into db_version(script_name) values( 'V1_create_usuario.sql');