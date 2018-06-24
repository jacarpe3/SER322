package database;

/**
 * Contains all SQL Queries for database
 * @author Wyatt Draggoo
 * @version 1.0
 */
public class SQL {
        
    public static class Create {
        public static final String db = "CREATE DATABASE ser322comics;";
        public static final String extension = "CREATE EXTENSION ISN;";
        
        // Create Tables Queries
        public static final String tablePublisher =
                "CREATE TABLE Publisher (" +
                    "pubID SERIAL PRIMARY KEY," +
                    "name VARCHAR (255)" +
                    ");";
        public static final String tableContributor = 
                "CREATE TABLE Contributor (" +
                    "contribID SERIAL PRIMARY KEY," +
                    "fName VARCHAR (255)," +
                    "lName VARCHAR (255)" +
                    ");";
        public static final String tableCovers =
                "CREATE TABLE Covers (" +
                    "coverID SERIAL PRIMARY KEY," +
                    "value NUMERIC," +
                    "artist INTEGER REFERENCES Contributor(contribID)," +
                    "coverImage bytea," +
                    "thumbnailImage bytea" +
                    ");";
        public static final String tableSeries =
                "CREATE TABLE Series (" +
                    "seriesUPC UPC PRIMARY KEY," +
                    "seriesName VARCHAR (255)" +
                    ");";
        public static final String tableComics =
                "CREATE TABLE Comics (" +
                    "comicID SERIAL PRIMARY KEY," +
                    "comicSerial ISSN," +
                    "seriesUPC UPC REFERENCES Series(seriesUPC)," +
                    "issueNum INTEGER," +
                    "issueTitle VARCHAR (255)," +
                    "pub INTEGER REFERENCES Publisher(pubID)," +
                    "pubDate DATE," +
                    "UNIQUE (comicSerial, seriesUPC)" +
                    ");";
        public static final String tableComicWriters =
                "CREATE TABLE ComicWriters (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "writer INTEGER REFERENCES Contributor(contribID)," +
                    "PRIMARY KEY (comic, writer)" +
                    ");";
        public static final String tableArtistRoles =
                "CREATE TABLE artistRoles (" +
                    "roleID SERIAL PRIMARY KEY," +
                    "roleName VARCHAR (24)" +
                    ");";
        public static final String tableComicArtists =
                "CREATE TABLE ComicArtists (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "artist INTEGER REFERENCES Contributor(contribID)," +
                    "role INTEGER REFERENCES artistRoles(roleID)," +
                    "PRIMARY KEY (comic, artist, role)" +
                    ");";
        public static final String tableComicCovers =
                "CREATE TABLE ComicCovers (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "cover INTEGER REFERENCES Covers(coverID)," +
                    "PRIMARY KEY (comic, cover)" +
                    ");";
    }
    
    public static class Insert {
        public static final String publisher =
                "INSERT INTO Publisher (name) VALUES" +
                    "('Dark Horse Comics')," +
                    "('Titan Comics')," +
                    "('Marvel Comics')," +
                    "('Image Comics')," +
                    "('DC Entertainment');";
        public static final String contributor =
                "INSERT INTO Contributor (fName, lName) VALUES" +
                    "('Scott', 'Shaw')," +
                    "('Peter', 'Kuper')," +
                    "('Jimmy', 'Pulmiotti')," +
                    "('Chris', 'Roberson')," +
                    "('Georges', 'Jeanty')," +
                    "('Karl', 'Story')," +
                    "('Wes', 'Dzioba')," +
                    "('Dan', 'Dos Santos')," +
                    "('Adam', 'Hughes')," +
                    "('Cavan', 'Scott')," +
                    "('Rachael', 'Scott')," +
                    "('Nick', 'Abadzis')," +
                    "('Mariano', 'Laclaustra')," +
                    "('Arianna', 'Florean')," +
                    "('Giorgia', 'Sposito')," +
                    "('Alex', 'Paknadel')," +
                    "('I.N.J.', 'Culbard')," +
                    "('Triona', 'Farrell')," +
                    "('Emma', 'Beeby')," +
                    "('Gordon', 'Rennie')," +
                    "('Katy', 'Rex')," +
                    "('Ivan', 'Rodriguez')," +
                    "('Wellington', 'Diaz')," +
                    "('Lolanda', 'Zanfardino')," +
                    "('Rodrigo', 'Fernandes')," +
                    "('George', 'Mann');";
        public static final String artistRoles = 
                "INSERT INTO artistRoles (roleName) VALUES" +
                    "('Artist')," +
                    "('Penciller')," +
                    "('Inker')," +
                    "('Colorist');";
        public static final String series =
                "INSERT INTO Series (seriesUPC, seriesName) VALUES" +
                    "('761568000849', 'Serenity')," +
                    "('074470618263', 'Doctor Who');";
    }

    public static class Drop {
        public static final String db = "DROP DATABASE ser322comics;";
    }
    
}
