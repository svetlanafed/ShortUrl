create sequence shorter_id_seq start 1 increment 1;

create table if not exists shorter
(
    id           int8      not null default nextval('shorter_id_seq'),
    hash         varchar(20) not null unique,
    original_url varchar(255) not null unique ,
    created_at   timestamp not null
);