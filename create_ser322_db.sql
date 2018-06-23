CREATE DATABASE ser322comics;
CREATE USER comicadmin WITH PASSWORD 'comicadmin';
GRANT ALL PRIVILEGES ON DATABASE ser322comics TO comicadmin;
\connect ser322comics
CREATE EXTENSION ISN;
CREATE TABLE Publisher (
	pubID SERIAL PRIMARY KEY,
	name VARCHAR (255)
);
CREATE TABLE Contributor (
	contribID SERIAL PRIMARY KEY,
	fName VARCHAR (255),
	lName VARCHAR (255)
);
CREATE TABLE Covers (
	coverID SERIAL PRIMARY KEY,
	value NUMERIC,
	artist INTEGER REFERENCES Contributor(contribID),
	coverImage bytea,
	thumbnailImage bytea
);
CREATE TABLE Series (
	seriesUPC UPC PRIMARY KEY,
	seriesName VARCHAR (255)
);
CREATE TABLE Comics (
	comicID SERIAL PRIMARY KEY,
	comicSerial ISSN,
	seriesUPC UPC REFERENCES Series(seriesUPC),
	issueNum INTEGER,
	issueTitle VARCHAR (255),
	pub INTEGER REFERENCES Publisher(pubID),
	pubDate DATE,
	UNIQUE (comicSerial, seriesUPC)
);
CREATE TABLE ComicWriters (
	comic INTEGER REFERENCES Comics(comicID),
	writer INTEGER REFERENCES Contributor(contribID),
	PRIMARY KEY (comicID, writerID)
);
CREATE TABLE ComicArtists (
	comic INTEGER REFERENCES Comics(comicID),
	artist INTEGER REFERENCES Contributor(contribID),
	role INTEGER REFERENCES artistRoles(roleID),
	PRIMARY KEY (comicID, artistID, role)
);
CREATE TABLE ComicCovers (
	comicID INTEGER REFERENCES Comics(comicID),
	coverID INTEGER REFERENCES Covers(coverID),
	PRIMARY KEY (comicID, coverID)
);
CREATE TABLE artistRoles (
	roleID SERIAL PRIMARY KEY,
	roleName VARCHAR (24)
);

-- Populate some initial data
INSERT INTO Publisher (name) VALUES
	('Dark Horse Comics'),
	('Titan Comics'),
	('Marvel Comics'),
	('Image Comics'),
	('DC Entertainment');

INSERT INTO Conbributor (fName, lName) VALUES
	('Scott', 'Shaw'),
	('Peter', 'Kuper'),
	('Jimmy', 'Pulmiotti'),
	('Chris', 'Roberson'),
	('Georges', 'Jeanty'),
	('Karl', 'Story'),
	('Wes', 'Dzioba'),
	('Dan', 'Dos Santos'),
	('Adam', 'Hughes'),
	('Cavan', 'Scott'),
	('Rachael', 'Scott'),
	('Nick', 'Abadzis'),
	('Mariano', 'Laclaustra'),
	('Arianna', 'Florean'),
	('Giorgia', 'Sposito'),
	('Alex', 'Paknadel'),
	('I.N.J.', 'Culbard'),
	('Triona', 'Farrell'),
	('Emma', 'Beeby'),
	('Gordon', 'Rennie'),
	('Katy', 'Rex'),
	('Ivan', 'Rodriguez'),
	('Wellington', 'Diaz'),
	('Lolanda', 'Zanfardino'),
	('Rodrigo', 'Fernandes'),
	('George', 'Mann');


INSERT INTO artistRoles (roleName) VALUES
	('Artist'),
	('Penciller'),
	('Inker'),
	('Colorist'),

INSERT INTO Series (seriesUPC, seriesName) VALUES
	('761568000849', 'Serenity'),
	('074470618263', 'Doctor Who');
