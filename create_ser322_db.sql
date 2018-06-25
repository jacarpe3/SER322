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
	comicSerial VARCHAR (5) CHECK (comicSerial ~* '^[[:digit:]]{5}$'),
	seriesUPC UPC REFERENCES Series(seriesUPC),
	issueNum VARCHAR (8),
	issueTitle VARCHAR (255),
	publisher INTEGER REFERENCES Publisher(pubID),
	pubDate DATE,
	UNIQUE (comicSerial, seriesUPC)
);
CREATE TABLE ComicWriters (
	comic INTEGER REFERENCES Comics(comicID),
	writer INTEGER REFERENCES Contributor(contribID),
	PRIMARY KEY (comic, writer)
);
CREATE TABLE artistRoles (
	roleID SERIAL PRIMARY KEY,
	roleName VARCHAR (24)
);
CREATE TABLE ComicArtists (
	comic INTEGER REFERENCES Comics(comicID),
	artist INTEGER REFERENCES Contributor(contribID),
	role INTEGER REFERENCES artistRoles(roleID),
	PRIMARY KEY (comic, artist, role)
);
CREATE TABLE ComicCovers (
	comic INTEGER REFERENCES Comics(comicID),
	cover INTEGER REFERENCES Covers(coverID),
	PRIMARY KEY (comic, cover)
);

-- Populate some initial data
INSERT INTO Publisher (pubID, name) VALUES
	(1, 'Dark Horse Comics'),
	(2, 'Titan Comics'),
	(3, 'Marvel Comics'),
	(4, 'Image Comics'),
	(5, 'DC Entertainment');

INSERT INTO Contributor (contribID, fName, lName) VALUES
	(1, 'Scott', 'Shaw'),
	(2, 'Peter', 'Kuper'),
	(3, 'Jimmy', 'Pulmiotti'),
	(4, 'Chris', 'Roberson'),
	(5, 'Georges', 'Jeanty'),
	(6, 'Karl', 'Story'),
	(7, 'Wes', 'Dzioba'),
	(8, 'Dan', 'Dos Santos'),
	(9, 'Adam', 'Hughes'),
	(10, 'Cavan', 'Scott'),
	(11, 'Rachael', 'Scott'),
	(12, 'Nick', 'Abadzis'),
	(13, 'Mariano', 'Laclaustra'),
	(14, 'Arianna', 'Florean'),
	(15, 'Giorgia', 'Sposito'),
	(16, 'Alex', 'Paknadel'),
	(17, 'I.N.J.', 'Culbard'),
	(18, 'Triona', 'Farrell'),
	(19, 'Emma', 'Beeby'),
	(20, 'Gordon', 'Rennie'),
	(21, 'Katy', 'Rex'),
	(22, 'Ivan', 'Rodriguez'),
	(23, 'Wellington', 'Diaz'),
	(24, 'Lolanda', 'Zanfardino'),
	(25, 'Rodrigo', 'Fernandes'),
	(26, 'Thiago', 'Ribeiro'),
	(27, 'George', 'Mann'),
	(28, '', 'Hi-Fi'),
	(29, 'Carlos', 'Cabrera');

INSERT INTO Series (seriesUPC, seriesName) VALUES
	('761568000849', 'Serenity'),
	('074470618263', 'Doctor Who: The Lost Dimension'),
	('074470711117', 'Doctor Who: The Ninth Doctor'),
	('074470682745', 'Doctor Who: The Tenth Doctor'),
	('074470683001', 'Doctor Who: The Eleventh Doctor'),
	('074470618317', 'Doctor Who: The Lost Dimension Special');

INSERT INTO Comics (comicID, comicSerial, seriesUPC, issueNum, issueTitle, publisher, pubDate) VALUES
	(1, '00111', '074470618263', '01A', 'The Lost Dimension Alpha', 2, '2017-09-01'),
	(2, '00121', '074470618263', '01B', 'The Lost Dimension Alpha', 2, '2017-09-01'),
	(3, '00131', '074470618263', '01C', 'The Lost Dimension Alpha', 2, '2017-09-01'),
	(4, '00111', '074470711117', '01A', 'The Lost Dimension Ninth Doctor Special', 2, '2017-10-01'),
	(5, '00121', '074470711117', '01B', 'The Lost Dimension Ninth Doctor Special', 2, '2017-10-01'),
	(6, '00111', '074470682745', '01A', 'The Lost Dimension Tenth Doctor Special', 2, '2017-10-01'),
	(7, '00121', '074470682745', '01B', 'The Lost Dimension Tenth Doctor Special', 2, '2017-10-01'),
	(8, '01011', '074470683001', '01A', 'The Lost Dimension Eleventh Doctor Special', 2, '2017-09-01'),
	(9, '01021', '074470683001', '01B', 'The Lost Dimension Eleventh Doctor Special', 2, '2017-09-01'),
	(10, '00111', '074470618317', '01A', 'The Lost Dimension Special #1', 2, '2017-10-01'),
	(11, '00121', '074470618317', '01B', 'The Lost Dimension Special #1', 2, '2017-10-01'),
	(12, '01511', '074470683001', '01B', 'The Lost Dimension Twelfth Doctor Special', 2, '2017-10-01'),
	(13, '01521', '074470683001', '01B', 'The Lost Dimension Twelfth Doctor Special', 2, '2017-10-01'),
	(14, '00211', '074470618317', '02A', 'The Lost Dimension Special #2', 2, '2017-11-01'),
	(15, '00221', '074470618317', '02B', 'The Lost Dimension Special #2', 2, '2017-11-01'),
	(16, '00211', '074470618263', '02A', 'The Lost Dimension Omega', 2, '2017-11-01'),
	(17, '00221', '074470618263', '02B', 'The Lost Dimension Omega', 2, '2017-11-01'),
	(18, '00111', '761568000849', '01A', NULL, 1, '2016-10-01'),
	(19, '00121', '761568000849', '01B', NULL, 1, '2016-10-01'),
	(20, '00131', '761568000849', '01C', NULL, 1, '2016-10-01'),
	(21, '00211', '761568000849', '02A', NULL, 1, '2016-11-01'),
	(22, '00221', '761568000849', '02B', NULL, 1, '2016-11-01'),
	(23, '00311', '761568000849', '03A', NULL, 1, '2016-12-01'),
	(24, '00321', '761568000849', '03B', NULL, 1, '2016-12-01'),
	(25, '00411', '761568000849', '04A', NULL, 1, '2017-01-01'),
	(26, '00421', '761568000849', '04B', NULL, 1, '2017-01-01'),
	(27, '00511', '761568000849', '05A', NULL, 1, '2017-02-01'),
	(28, '00521', '761568000849', '05B', NULL, 1, '2017-02-01'),
	(29, '00611', '761568000849', '06A', NULL, 1, '2017-03-01'),
	(30, '00621', '761568000849', '06B', NULL, 1, '2017-03-01');

INSERT INTO artistRoles (roleID, roleName) VALUES
	(1, 'Artist'),
	(2, 'Penciller'),
	(3, 'Inker'),
	(4, 'Colorist');

INSERT INTO ComicWriters (comic, writer) VALUES
	(1, 10),
	(2, 10),
	(3, 10),
	(4, 12),
	(5, 12),
	(6, 12),
	(7, 12),
	(8, 16),
	(9, 16),
	(10, 19),
	(10, 20),
	(10, 21),
	(11, 19),
	(11, 20),
	(11, 21),
	(12, 11),
	(13, 11),
	(14, 19),
	(14, 20),
	(14, 21),
	(15, 19),
	(15, 20),
	(15, 21),
	(16, 10),
	(16, 27),
	(16, 12),
	(17, 10),
	(17, 27),
	(17, 12),
	(18, 4),
	(19, 4),
	(20, 4),
	(22, 4),
	(23, 4),
	(24, 4),
	(25, 4),
	(26, 4),
	(27, 4),
	(28, 4),
	(29, 4),
	(30, 4);

INSERT INTO ComicArtists (comic, artist, role) VALUES
	(1, 11, 1),
	(2, 11, 1),
	(3, 11, 1),
	(4, 13, 1),
	(5, 13, 1),
	(6, 14, 1),
	(6, 15, 1),
	(7, 14, 1),
	(7, 15, 1),
	(8, 17, 1),
	(8, 18, 4),
	(9, 17, 1),
	(9, 18, 4),
	(10, 22, 1),
	(10, 23, 1),
	(10, 24, 1),
	(10, 26, 4),
	(11, 22, 1),
	(11, 23, 1),
	(11, 24, 1),
	(11, 26, 4),
	(12, 25, 1),
	(13, 25, 1),
	(14, 22, 1),
	(14, 23, 1),
	(14, 24, 1),
	(14, 28, 4),
	(15, 22, 1),
	(15, 23, 1),
	(15, 24, 1),
	(15, 28, 4),
	(16, 29, 1),
	(16, 13, 1),
	(17, 29, 1),
	(17, 13, 1),
	(18, 5, 1),
	(19, 5, 1),
	(20, 5, 1),
	(22, 5, 1),
	(23, 5, 1),
	(24, 5, 1),
	(25, 5, 1),
	(26, 5, 1),
	(27, 5, 1),
	(28, 5, 1),
	(29, 5, 1),
	(30, 5, 1),
	(18, 6, 1),
	(19, 6, 1),
	(20, 6, 1),
	(22, 6, 1),
	(23, 6, 1),
	(24, 6, 1),
	(25, 6, 1),
	(26, 6, 1),
	(27, 6, 1),
	(28, 6, 1),
	(29, 6, 1),
	(30, 6, 1);
