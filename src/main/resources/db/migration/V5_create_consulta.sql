use btrestia;
create table if not exists consulta(
id bigint auto_increment primary key,
usuario_id bigint not null,
modelo_id bigint not null,
pregunta_id bigint not null,
respuesta_id bigint not null,
constraint fk_usuario foreign key (usuario_id) references usuario(id),
constraint fk_modelo foreign key (modelo_id) references modelo_ia(id),
constraint fk_pregunta foreign key (pregunta_id) references pregunta(id),
constraint fk_respuesta foreign key (respuesta_id) references respuesta(id)
);
insert ignore into db_version(script_name) values( 'V5_create_consulta.sql');


