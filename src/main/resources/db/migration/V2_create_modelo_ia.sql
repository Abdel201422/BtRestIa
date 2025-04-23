use btrestia;
create table if not exists modelo_ia(
id bigint auto_increment primary key,
nombre varchar(255),
descripcion text,
activo boolean
);
insert ignore into db_version(script_name) values( 'V2_create_modelo_ia.sql');


