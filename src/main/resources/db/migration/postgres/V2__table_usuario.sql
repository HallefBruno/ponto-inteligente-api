create table usuario(
    id serial not null primary key,
    email varchar(90) not null unique,
    perfil varchar(50) not null,
    senha varchar(100) not null
);