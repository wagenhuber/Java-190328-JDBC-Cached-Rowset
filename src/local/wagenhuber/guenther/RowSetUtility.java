//Utility-Klasse liest von Datenbank und schreibt Änderungen in Datenbank

package local.wagenhuber.guenther;

import javax.sql.rowset.CachedRowSet;
//von Hand eingetragen:
import com.sun.rowset.CachedRowSetImpl;

import java.io.*;
import java.sql.*;


public class RowSetUtility {

    private CachedRowSet rowSet;
    private String url;
    private String user;
    private String password;


    public RowSetUtility(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    //Übergabe der Verbindungsparameter und der sql-Abfrage
    public boolean executeQuery(String query) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rowSet = new CachedRowSetImpl();
            rowSet.populate(rs);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Übernahme Änderungen in Datenbank
    public void setRowSet(CachedRowSet rowSet) {
        this.rowSet = rowSet;
    }

    public CachedRowSet getRowSet() {
        return this.rowSet;
    }

    //Zur Erzeugung des Abfrageergebnisses mit Speicherung in einem Cached-RowSet Objekt
    public boolean propagate() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
            rowSet.acceptChanges(con);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Methoden zum serialisieren und deserialisieren
    public static void serialize(CachedRowSet crs, String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(crs);
        out.flush();
        out.close();
    }

    public static CachedRowSet deserialize(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        CachedRowSet crs = ((CachedRowSet) in.readObject());
        in.close();
        return crs;
    }
}
