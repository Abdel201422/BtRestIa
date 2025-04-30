use btrestia;
insert into modelo_ia values (1, "Mistral", "modelo_1", true)
insert into modelo_ia values (2, "Ollama3", "modelo_2", true)
insert ignore into db_version(script_name) values( 'V6_insertar_datos.sql');


