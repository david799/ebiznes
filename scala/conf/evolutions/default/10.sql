# --- !Ups

CREATE TABLE "return" (
                          "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                          "description" TEXT NOT NULL,
                          "order" INT NOT NULL,
                          FOREIGN KEY("order") references "order"(id)
);

# --- !Downs

DROP TABLE "return"