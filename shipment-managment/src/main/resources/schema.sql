drop table if exists Shipment;
drop table if exists ShipmentParentChild;

create table Shipment (
	--id integer PRIMARY KEY,
	id identity,
	quantity integer,
	name varchar(100)
);

create table ShipmentParentChild (
	id identity,
	parentId integer,
	childId integer
);