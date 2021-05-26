# --- !Ups

CREATE TABLE "order" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "customer" INT NOT NULL,
                           "address" INT NOT NULL,
                           FOREIGN KEY(customer) references customer(id),
                           FOREIGN KEY(address) references address(id)
);

# --- !Downs

DROP TABLE "order"
