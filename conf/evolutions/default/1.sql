# --- !Ups

CREATE TABLE "customer" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "nick" VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE "customer"