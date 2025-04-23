CREATE DATABASE BTRESTIA;
USE BTRESTIA;

Create table if not exists db_version (
id int auto_increment primary key,
script_name varchar(255) unique,
appliet_at timestamp default current_timestamp
);

insert ignore into db_version(script_name) values( 'V0_init_version.sql');