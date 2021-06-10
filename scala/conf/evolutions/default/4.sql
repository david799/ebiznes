# --- !Ups

CREATE TABLE "coupon" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "name" VARCHAR NOT NULL,
                           "discount" INT NOT NULL
);

# --- !Downs

DROP TABLE "coupon"