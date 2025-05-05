use btrestia;
insert into modelo_ia values (1, "Mistral:latest", "modelo_1", true)
insert into modelo_ia values (2, "llama3:8b", "modelo_2", true)
insert ignore into db_version(script_name) values( 'V6_insertar_datos.sql');


