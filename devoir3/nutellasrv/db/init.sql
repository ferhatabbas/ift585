create table "nutella_user" (
id INTEGER PRIMARY KEY AUTOINCREMENT,
nom VARCHAR NOT NULL,
prenom VARCHAR NOT NULL,
pseudo VARCHAR NOT NULL,
img_path VARCHAR DEFAULT "img/default.png",
password VARCHAR NOT NULL,
likedmsg INTEGER DEFAULT 0,
totalmsg INTEGER DEFAULT 0);
