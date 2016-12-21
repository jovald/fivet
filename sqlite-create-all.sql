create table control (
  id                            integer not null,
  fecha                         timestamp,
  prox_control                  timestamp,
  temperatura                   double,
  peso                          double,
  altura                        double,
  diagnostico                   varchar(255),
  nota                          varchar(255),
  veterinario_id                integer,
  version                       integer not null,
  deleted                       integer(1) default 0 not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_control primary key (id),
  foreign key (veterinario_id) references persona (id) on delete restrict on update restrict
);

create table paciente (
  id                            integer not null,
  numero                        integer not null,
  nombre                        varchar(255) not null,
  fecha_nacimiento              timestamp,
  raza                          varchar(255),
  sexo                          varchar(13),
  color                         varchar(255),
  especie                       varchar(255),
  version                       integer not null,
  deleted                       integer(1) default 0 not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint ck_paciente_sexo check ( sexo in ('Hembra','Macho','Indeterminado')),
  constraint pk_paciente primary key (id)
);

create table paciente_control (
  paciente_id                   integer not null,
  control_id                    integer not null,
  constraint pk_paciente_control primary key (paciente_id,control_id),
  foreign key (paciente_id) references paciente (id) on delete restrict on update restrict,
  foreign key (control_id) references control (id) on delete restrict on update restrict
);

create table persona (
  id                            integer not null,
  rut                           varchar(255) not null,
  nombre                        varchar(255) not null,
  password                      varbinary(255) not null,
  direccion                     varchar(255),
  mail                          varchar(255),
  tel_fijo                      varchar(255),
  tel_movil                     varchar(255),
  tipo                          varchar(11) not null,
  version                       integer not null,
  deleted                       integer(1) default 0 not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint ck_persona_tipo check ( tipo in ('Veterinario','Cliente')),
  constraint pk_persona primary key (id)
);

create table persona_paciente (
  persona_id                    integer not null,
  paciente_id                   integer not null,
  constraint pk_persona_paciente primary key (persona_id,paciente_id),
  foreign key (persona_id) references persona (id) on delete restrict on update restrict,
  foreign key (paciente_id) references paciente (id) on delete restrict on update restrict
);

