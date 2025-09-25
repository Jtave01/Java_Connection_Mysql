CREATE TABLE eomployees(
     id BIGINT not null auto_increment,
     name VARCHAR(100) not null,
     salary DECIMAL(10,2) not null,
     birthday TIMESTAMP not null,
     PRIMARY KEY(id)

)engine=InnoDB default charset=utf8;