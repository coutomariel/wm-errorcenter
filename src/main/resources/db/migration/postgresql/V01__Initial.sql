create table users(
	id serial,
	password varchar,
	email varchar(100),
	primary key (id)
);

create table errors(
	id serial,
	users integer,
	description varchar(500),
	ambient varchar(50),
	level varchar(50),
	status bit,
	primary key (id),
	foreign key (users) references users(id)
);
