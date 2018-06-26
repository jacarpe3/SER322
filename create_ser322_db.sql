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
	fName TEXT,
	lName TEXT
);
CREATE TABLE Covers (
	coverID SERIAL PRIMARY KEY,
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
	value NUMERIC,
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

CREATE VIEW fullComicListing AS
WITH
	ArtistList AS (
		SELECT
			comics.comicid AS comicid,
			string_agg(
				contributor.fName || ' ' ||
				contributor.lName || ' (' ||
				artistRoles.roleName || ')',
				', '
				ORDER BY contributor.fname, contributor.lname
			) AS artists FROM contributor
			INNER JOIN comicartists ON contributor.contribID = comicartists.artist
			INNER JOIN artistroles ON comicartists.role = artistroles.roleid
			INNER JOIN comics ON comicartists.comic = comics.comicid GROUP BY comicid
	),
	WriterList AS (
		SELECT
			comics.comicid AS comicid,
			string_agg(
				contributor.fName || ' ' ||
				contributor.lName,
				', '
				ORDER BY contributor.fname, contributor.lname
			) AS writers FROM contributor
			INNER JOIN comicwriters ON contributor.contribid = comicwriters.writer
			INNER JOIN comics ON comicwriters.comic = comics.comicid GROUP BY comicid
	)
	SELECT
		Covers.thumbnailImage,
		Comics.comicSerial,
		Comics.issueNum,
		CASE
			WHEN Comics.issueTitle is NULL THEN
				Series.seriesName || ' ' || Comics.issueNum
			ELSE
				Comics.issueTitle
		END,
		Series.seriesName,
		WriterList.writers,
		ArtistList.artists,
		Publisher.name,
		Comics.pubDate,
		Comics.value
    FROM
		Comics
		INNER JOIN ComicCovers ON Comics.comicID = ComicCovers.comic
		INNER JOIN Covers ON ComicCovers.cover = Covers.coverID
		INNER JOIN Series ON Comics.seriesUPC = Series.seriesUPC
		INNER JOIN WriterList ON WriterList.comicid = Comics.comicid
		INNER JOIN ArtistList ON ArtistList.comicid = Comics.comicid
		INNER JOIN Publisher ON Comics.Publisher = Publisher.pubID;

-- Populate some initial data
INSERT INTO Publisher (pubID, name) VALUES
	(1, 'Dark Horse Comics'),
	(2, 'Titan Comics'),
	(3, 'Marvel Comics'),
	(4, 'Image Comics'),
	(5, 'DC Entertainment');

INSERT INTO Contributor (contribID, fName, lName) VALUES
	(0, '', 'Information Not Available'),
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
	(29, 'Carlos', 'Cabrera'),
	(30, 'Luis', 'Guerrero'),
	(31, 'Tazio', 'Bettin'),
	(32, 'Klebs', 'Jr');

INSERT INTO Series (seriesUPC, seriesName) VALUES
	('761568000849', 'Serenity'),
	('074470618263', 'Doctor Who: The Lost Dimension'),
	('074470711117', 'Doctor Who: The Ninth Doctor'),
	('074470682745', 'Doctor Who: The Tenth Doctor'),
	('074470683001', 'Doctor Who: The Eleventh Doctor'),
	('074470618317', 'Doctor Who: The Lost Dimension Special');

INSERT INTO Comics (comicID, comicSerial, seriesUPC, issueNum, issueTitle, publisher, pubDate, value) VALUES
	(1, '00111', '074470618263', '01A', 'The Lost Dimension Alpha', 2, '2017-09-01', '1.99'),
	(2, '00121', '074470618263', '01B', 'The Lost Dimension Alpha', 2, '2017-09-01', '2.99'),
	(3, '00131', '074470618263', '01C', 'The Lost Dimension Alpha', 2, '2017-09-01', '3.99'),
	(4, '00111', '074470711117', '01A', 'The Lost Dimension Ninth Doctor Special', 2, '2017-10-01', '1.99'),
	(5, '00121', '074470711117', '01B', 'The Lost Dimension Ninth Doctor Special', 2, '2017-10-01', '2.99'),
	(6, '00911', '074470682745', '01A', 'The Lost Dimension Tenth Doctor Special', 2, '2017-10-01', '1.99'),
	(7, '00921', '074470682745', '01B', 'The Lost Dimension Tenth Doctor Special', 2, '2017-10-01', '1.99'),
	(8, '01011', '074470683001', '01A', 'The Lost Dimension Eleventh Doctor Special', 2, '2017-09-01', '3.99'),
	(9, '01021', '074470683001', '01B', 'The Lost Dimension Eleventh Doctor Special', 2, '2017-09-01', '10.99'),
	(10, '00111', '074470618317', '01A', 'The Lost Dimension Special #1', 2, '2017-10-01', '1.99'),
	(11, '00121', '074470618317', '01B', 'The Lost Dimension Special #1', 2, '2017-10-01', '1.99'),
	(12, '01511', '074470683001', '01A', 'The Lost Dimension Twelfth Doctor Special', 2, '2017-10-01', '7.99'),
	(13, '01521', '074470683001', '01B', 'The Lost Dimension Twelfth Doctor Special', 2, '2017-10-01', '1.99'),
	(14, '00211', '074470618317', '02A', 'The Lost Dimension Special #2', 2, '2017-11-01', '1.99'),
	(15, '00221', '074470618317', '02B', 'The Lost Dimension Special #2', 2, '2017-11-01', '1.99'),
	(16, '00211', '074470618263', '02A', 'The Lost Dimension Omega', 2, '2017-11-01', '1.99'),
	(17, '00221', '074470618263', '02B', 'The Lost Dimension Omega', 2, '2017-11-01', '2.99'),
	(18, '00111', '761568000849', '01A', NULL, 1, '2016-10-01', '1.99'),
	(19, '00121', '761568000849', '01B', NULL, 1, '2016-10-01', '6.99'),
	(20, '00131', '761568000849', '01C', NULL, 1, '2016-10-01', '13.99'),
	(21, '00211', '761568000849', '02A', NULL, 1, '2016-11-01', '1.99'),
	(22, '00221', '761568000849', '02B', NULL, 1, '2016-11-01', '1.99'),
	(23, '00311', '761568000849', '03A', NULL, 1, '2016-12-01', '1.99'),
	(24, '00321', '761568000849', '03B', NULL, 1, '2016-12-01', '1.99'),
	(25, '00411', '761568000849', '04A', NULL, 1, '2017-01-01', '1.99'),
	(26, '00421', '761568000849', '04B', NULL, 1, '2017-01-01', '1.99'),
	(27, '00511', '761568000849', '05A', NULL, 1, '2017-02-01', '1.99'),
	(28, '00521', '761568000849', '05B', NULL, 1, '2017-02-01', '1.99'),
	(29, '00611', '761568000849', '06A', NULL, 1, '2017-03-01', '1.99'),
	(30, '00621', '761568000849', '06B', NULL, 1, '2017-03-01', '2.99');

INSERT INTO artistRoles (roleID, roleName) VALUES
	(1, 'Artist'),
	(2, 'Penciller'),
	(3, 'Inker'),
	(4, 'Colorist'),
	(5, 'Cover Artist');

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

INSERT INTO Covers (coverID) VALUES
	(0),
	(1),
	(2),
	(3),
	(4),
	(5),
	(6),
	(7),
	(8),
	(9),
	(10),
	(11),
	(12),
	(13),
	(14),
	(15),
	(16),
	(17),
	(18),
	(19),
	(20),
	(21),
	(22),
	(23),
	(24),
	(25),
	(26),
	(27),
	(28),
	(29),
	(30);

INSERT INTO ComicCovers (comic, cover) VALUES
	(1,1),
	(2,2),
	(3,3),
	(4,4),
	(5,5),
	(6,6),
	(7,7),
	(8,8),
	(9,9),
	(10,10),
	(11,11),
	(12,12),
	(13,13),
	(14,14),
	(15,15),
	(16,16),
	(17,17),
	(18,18),
	(19,19),
	(20,20),
	(21,21),
	(22,22),
	(23,23),
	(24,24),
	(25,25),
	(26,26),
	(27,27),
	(28,28),
	(29,29),
	(30,30);

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
	(30, 6, 1),
	(3, 11, 5),
	(3, 30, 5),
	(6, 30, 5),
	(6, 31, 5),
	(10, 13, 5),
	(14, 32, 5),
	(18, 8, 5),
	(19, 5, 5),
	(20, 9, 5),
	(21, 8, 5),
	(22, 5, 5),
	(23, 8, 5),
	(24, 5, 5),
	(25, 8, 5),
	(26, 5, 5),
	(27, 8, 5),
	(28, 5, 5),
	(29, 8, 5),
	(30, 5, 5);
