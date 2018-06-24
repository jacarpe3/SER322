package database;

/**
 * Contains all SQL Queries for database
 * @author Wyatt Draggoo
 * @version 1.0
 */
public class SQL {

    public static class Database {
        static final String PS_URL = "jdbc:postgresql://localhost:5432/";
        static final String DB_URL = "jdbc:postgresql://localhost:5432/ser322comics";
        static final String UN = "postgres";
        static final String PW = "test123";
    }

    public static class Columns {
        public static final String SERIES_UPC = "seriesUPC";
        public static final String CONTRIB_FNAME = "fName";
        public static final String CONTRIB_LNAME = "lName";
        public static final String SERIES_NAME = "seriesName";
        public static final String ISSUE_TITLE = "issueTitle";
        public static final String PUB_NAME = "name";
        public static final String PUB_DATE = "pubDate";
        public static final String ISSUE_NUM = "issueNum";
        public static final String THUMB_IMAGE = "thumbnailImage";
    }
        
    public static class Create {
        public static final String DB = "CREATE DATABASE ser322comics;";
        public static final String EXTENSION = "CREATE EXTENSION ISN;";
        
        // Create Tables Queries
        public static final String TABLE_PUBLISHER =
                "CREATE TABLE Publisher (" +
                    "pubID SERIAL PRIMARY KEY," +
                    "name VARCHAR (255)" +
                    ");";
        public static final String TABLE_CONTRIBUTOR =
                "CREATE TABLE Contributor (" +
                    "contribID SERIAL PRIMARY KEY," +
                    "fName VARCHAR (255)," +
                    "lName VARCHAR (255)" +
                    ");";
        public static final String TABLE_COVERS =
                "CREATE TABLE Covers (" +
                    "coverID SERIAL PRIMARY KEY," +
                    "value NUMERIC," +
                    "artist INTEGER REFERENCES Contributor(contribID)," +
                    "coverImage bytea," +
                    "thumbnailImage bytea" +
                    ");";
        public static final String TABLE_SERIES =
                "CREATE TABLE Series (" +
                    "seriesUPC UPC PRIMARY KEY," +
                    "seriesName VARCHAR (255)" +
                    ");";
        public static final String TABLE_COMICS =
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
        public static final String TABLE_COMIC_WRITERS =
                "CREATE TABLE ComicWriters (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "writer INTEGER REFERENCES Contributor(contribID)," +
                    "PRIMARY KEY (comic, writer)" +
                    ");";
        public static final String TABLE_ARTIST_ROLES =
                "CREATE TABLE artistRoles (" +
                    "roleID SERIAL PRIMARY KEY," +
                    "roleName VARCHAR (24)" +
                    ");";
        public static final String TABLE_COMIC_ARTISTS =
                "CREATE TABLE ComicArtists (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "artist INTEGER REFERENCES Contributor(contribID)," +
                    "role INTEGER REFERENCES artistRoles(roleID)," +
                    "PRIMARY KEY (comic, artist, role)" +
                    ");";
        public static final String TABLE_COMIC_COVERS =
                "CREATE TABLE ComicCovers (" +
                    "comic INTEGER REFERENCES Comics(comicID)," +
                    "cover INTEGER REFERENCES Covers(coverID)," +
                    "PRIMARY KEY (comic, cover)" +
                    ");";
    }
    
    public static class Insert {
        public static final String PUBLISHER =
                "INSERT INTO Publisher (name) VALUES" +
                    "('Dark Horse Comics')," +
                    "('Titan Comics')," +
                    "('Marvel Comics')," +
                    "('Image Comics')," +
                    "('DC Entertainment');";
        public static final String CONTRIBUTOR =
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
        public static final String ARTIST_ROLES =
                "INSERT INTO artistRoles (roleName) VALUES" +
                    "('Artist')," +
                    "('Penciller')," +
                    "('Inker')," +
                    "('Colorist');";
        public static final String SERIES =
                "INSERT INTO Series (seriesUPC, seriesName) VALUES" +
                    "('761568000849', 'Serenity')," +
                    "('074470618263', 'Doctor Who');";
    }

    public static class Update {
        public static final String THUMBNAIL_IMAGE =  "UPDATE covers SET thumbnailImage = ? WHERE coverID = ?";
    }

    public static class Drop {
        public static final String DB = "DROP DATABASE ser322comics;";
    }
    
}
