# --- !Ups

CREATE TABLE "payment" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "order" INT NOT NULL,
                           "amount" REAL NOT NULL,
                           FOREIGN KEY("order") references "order"(id)
);

# --- !Downs

DROP TABLE "payment"