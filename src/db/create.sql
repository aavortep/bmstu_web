drop table account, reh_base, room, rehearsal cascade;

CREATE TABLE Account (
    ID serial NOT NULL PRIMARY KEY,
    fio text NOT NULL,
    phone text,
    mail text,
	password text
);

CREATE TABLE Reh_base (
    ID serial NOT NULL PRIMARY KEY,
    ownerID int NOT NULL REFERENCES Account(ID),
    name text NOT NULL,
    address text NOT NULL,
    phone text,
	mail text
);

CREATE TABLE Room (
    ID serial NOT NULL PRIMARY KEY,
    baseID int NOT NULL REFERENCES Reh_base(ID),
    name text NOT NULL,
	type text,
	area int,
    cost int
);

CREATE TABLE Rehearsal (
    ID serial NOT NULL PRIMARY KEY,
    musicianID int NOT NULL REFERENCES Account(ID),
	roomID int NOT NULL REFERENCES Room(ID),
    rehTime time
);