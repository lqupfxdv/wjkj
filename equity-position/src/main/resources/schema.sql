drop table if exists Transaction;
drop table if exists Trade;

create table Transaction (
	--id integer PRIMARY KEY,
	TransactionID identity,
	TradeID integer,
	Version integer,
	SecurityCode varchar(10),
	Quantity integer,
	TradeType varchar(10),
	Operation varchar(10)
);

create table Trade (
	TradeID identity,
	Version integer,
	SecurityCode varchar(10),
	Quantity integer,
	TradeType varchar(10),
	Operation varchar(10)
);
