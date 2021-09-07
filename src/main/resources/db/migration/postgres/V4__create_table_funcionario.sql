create table funcionario (
    id bigint not null,
    cpf varchar(255) not null,
    data_atualizacao date not null,
    data_criacao date not null,
    email varchar(255) not null,
    nome varchar(255) not null,
    perfil varchar(255) not null,
    qtd_horas_almoco float,
    qtd_horas_trabalho_dia float,
    senha varchar(255) not null,
    valor_hora decimal(19,2),
    empresa_id bigint,
    primary key (id)
)