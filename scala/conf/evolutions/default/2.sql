# --- !Ups

CREATE TABLE "address" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "name" VARCHAR NOT NULL,
                           "customer" INT NOT NULL,
                           "address_line1" VARCHAR NOT NULL,
                           "address_line2" VARCHAR NOT NULL,
                           FOREIGN KEY(customer) references customer(id)
);

# --- !Downs

DROP TABLE "address"
