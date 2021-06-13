# --- !Ups

CREATE TABLE "orderedProduct" (
                                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                  "order" INT NOT NULL,
                                  "product" INT NOT NULL,
                                  FOREIGN KEY("order") references "order"(id),
                                  FOREIGN KEY("product") references "product"(id)
);

# --- !Downs

DROP TABLE "orderedProduct"
