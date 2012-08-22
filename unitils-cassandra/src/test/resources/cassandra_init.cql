create keyspace unitils WITH strategy_class = SimpleStrategy AND strategy_options:replication_factor = 1;
user unitils;

--fruit
create table fruits(
 id ascii primary key,
 name text,
 price double
);


