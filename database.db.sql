CREATE TABLE "teachers" IF NOT EXISTS (
    "id"	INTEGER NOT NULL UNIQUE,
    "first_name"	TEXT,
    "last_name"	TEXT,
    "phone_number"  TEXT,
    "special_needs"  TEXT,
    PRIMARY KEY("id")
);

INSERT INTO "teachers" VALUES (1, 'Adnan', 'Adnanovic', '+38761889825', 'Yes');

CREATE TABLE "children" IF NOT EXISTS (
    "id"	INTEGER NOT NULL UNIQUE,
    "first_name"	TEXT,
    "last_name"	TEXT,
    "birth_date"	TEXT,
    "parent_name"	TEXT,
    "phone_number"	TEXT,
    "special_need"	TEXT,
    "teacher"	INTEGER,
    PRIMARY KEY("id"),
    FOREIGN KEY("teacher") REFERENCES "teachers"("id")
);

INSERT INTO "children" VALUES (1, 'Niko', 'Nikić', '01.01. 2017', 'Ivan', '+38761123456', 'None', 1);
INSERT INTO "children" VALUES (2, 'Mujo', 'Mujić', '02.02.2017', 'Ahmed', '+38761111222', 'None', 1);
INSERT INTO "children" VALUES (3, 'Ana', 'Anić', '03.03.2017.', 'Marija', '+38761112233', 'Autizam', 1);

COMMIT