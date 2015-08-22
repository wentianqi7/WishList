create table if not exists WishList (
    id int not null auto_increment primary key,
    title varchar(255) not null,
    image varchar(255),
    url varchar(255) not null
);
