create table activity (id bigint not null generated always as identity, active smallint not null, finished smallint not null, name varchar(255), primary key (id));
create table time_entry (id bigint not null generated always as identity, time_end bigint, time_start bigint, activity_id bigint, primary key (id));
alter table time_entry add constraint FK42EDB080EA8BCFEB foreign key (activity_id) references activity;
