# --- !Ups

CREATE TABLE "rating" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "grade" REAL NOT NULL,
                           "description" TEXT NOT NULL,
                           "order" INT NOT NULL,
                           FOREIGN KEY("order") references "order"(id)
);

# --- !Downs

DROP TABLE "rating"