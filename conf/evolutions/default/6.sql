# --- !Ups

CREATE TABLE "product" (
                                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                  "name" VARCHAR NOT NULL,
                                  "description" TEXT NOT NULL,
                                  "category" INT NOT NULL,
                                  "price" REAL NOT NULL,
                                  FOREIGN KEY(category) references category(id)
);

# --- !Downs

DROP TABLE "product"
