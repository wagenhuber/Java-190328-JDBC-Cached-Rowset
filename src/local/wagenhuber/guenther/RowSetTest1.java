//Buchdaten werden abgefragt und in einem CachedRowSet gespeichert. Anschließend wird dieses serialisiert

package local.wagenhuber.guenther;

import javax.sql.rowset.CachedRowSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class RowSetTest1 {

    public static void main(String[] args) {
        FileInputStream in;

        //Übernahme DB-Verbindungsparameter aus Datei
        try {
            in = new FileInputStream("dbconnect.properties");
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            String url = prop.getProperty("url");
            String user = prop.getProperty("user", "");
            String password = prop.getProperty("password", "");

            //Erzeugung RowSetUtility und Übergabe der DB-Verbindungsparameter
            RowSetUtility util = new RowSetUtility(url, user, password);
            //Definieren der sql-Abfrage
            String query = "select * from buecher";
            //Ausführen der SQL-Abfrage und entgegennehmen eines CachedRowSet als Ergebnis, Ausgabe auf Standardausgabe und Abspeichern in Datei
            if (util.executeQuery(query)) {
                CachedRowSet crs = util.getRowSet();

                while (crs.next()) {
                    System.out.println(crs.getString(1) + " " + crs.getString(2));
                }
                RowSetUtility.serialize(crs, "buch.dat");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
