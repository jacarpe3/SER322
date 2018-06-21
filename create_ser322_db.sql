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
	coverImage bytea
);
CREATE TABLE Series (
	seriesID SERIAL PRIMARY KEY,
	seriesName VARCHAR (255)
);
CREATE TABLE Comics (
	comicISBN ISBN PRIMARY KEY,
	seriesID INTEGER REFERENCES Series(seriesID),
	issueNum INTEGER,
	issueTitle VARCHAR (255),
	pubID INTEGER REFERENCES Publisher(pubID),
	pubDate DATE
);
CREATE TABLE ComicWriters (
	comicISBN ISBN REFERENCES Comics(comicISBN),
	writerID INTEGER REFERENCES Contributor(contribID),
	PRIMARY KEY (comicISBN, writerID)
);
CREATE TABLE ComicArtists (
	comicISBN ISBN REFERENCES Comics(comicISBN),
	artistID INTEGER REFERENCES Contributor(contribID),
	PRIMARY KEY (comicISBN, artistID)
);
CREATE TABLE ComicCovers (
	comicISBN ISBN REFERENCES Comics(comicISBN),
	coverID INTEGER REFERENCES Covers(coverID),
	PRIMARY KEY (comicISBN, coverID)
);
