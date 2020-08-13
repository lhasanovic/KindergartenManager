BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `teachers` (
                                          `id`	INTEGER NOT NULL UNIQUE,
                                          `first_name`	TEXT,
                                          `last_name`	TEXT,
                                          `phone_number`  TEXT,
                                          `special_needs`  TEXT,
                                          PRIMARY KEY(`id`)
);
INSERT INTO `teachers` VALUES (10004, 'Adnan', 'Adnanovic', '38761889825', 'Yes');
INSERT INTO `teachers` VALUES (10005, 'Luka', 'Lukic', '123123123', 'No');
CREATE TABLE IF NOT EXISTS `children` (
                                          `id`	INTEGER NOT NULL UNIQUE,
                                          `first_name`	TEXT,
                                          `last_name`	TEXT,
                                          `birth_date`	TEXT,
                                          `parent_name`	TEXT,
                                          `phone_number`	TEXT,
                                          `special_need`	TEXT,
                                          `teacher`	INTEGER,
                                          PRIMARY KEY(`id`),
                                          FOREIGN KEY(`teacher`) REFERENCES `teachers`
);
INSERT INTO `children` VALUES (10001, 'Niko', 'Nikić', '01.01.2017', 'Ivan', '38761123456', 'None', 10004);
INSERT INTO `children` VALUES (10002, 'Mujo', 'Mujić', '02.02.2017', 'Ahmed', '38761111222', 'None', 10004);
INSERT INTO `children` VALUES (10003, 'Ana', 'Anić', '03.03.2017.', 'Marija', '38761112233', 'Autizam', 10004);
CREATE TABLE IF NOT EXISTS `diary` (
                         `child`	INTEGER NOT NULL,
                         `year`	INTEGER,
                         `month`	INTEGER,
                         `day`	INTEGER,
                         `time`	TEXT,
                         `activity`	TEXT,
                         `description`	TEXT
);
INSERT INTO `diary` VALUES (10001, 2020, 8, 12, '11:10', 'Crying', 'Niko was crying because he missed his parents');
INSERT INTO `diary` VALUES (10001, 2020, 8, 11, '12:20', 'Sleeping', 'Niko was sleeping for two hours, setting his new record');
COMMIT;