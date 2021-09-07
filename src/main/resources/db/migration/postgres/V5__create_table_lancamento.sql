create table lancamento (
    id bigint not null,
    data timestamp not null,
    data_atualizacao date not null,
    data_criacao date not null,
    descricao varchar(255),
    localizacao varchar(255),
    tipo varchar(255) not null,
    funcionario_id bigint,
    primary key (id)
);