# --- !Ups

CREATE TABLE "orderedProduct" (
                                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                  "customer" INT NOT NULL,
                                  "product" INT NOT NULL,
                                  FOREIGN KEY(customer) references customer(id),
                                  FOREIGN KEY(product) references product(id)
);

# --- !Downs

DROP TABLE "orderedProduct"
