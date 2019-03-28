//CachedRowSet wird rekonstruiert (deserialisiert) und die Änderungen in der Datenbank eingespielt

package local.wagenhuber.guenther;

import javax.sql.rowset.CachedRowSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RowSetTest3 {

    public static void main(String[] args) {
        FileInputStream in;

        try {
            in = new FileInputStream("dbconnect.properties");
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            String url = prop.getProperty("url");
            String user = prop.getProperty("user", "");
            String password = prop.getProperty("password", "");

            RowSetUtility util = new RowSetUtility(url, user, password);

            //CachedRowSet aus Datei erstellen
            CachedRowSet crs = RowSetUtility.deserialize("buch.dat");
            //CachedRowSet an Util-Klasse übergeben
            util.setRowSet(crs);
            //Datenbank aktualisieren
            if (util.propagate()) {
                System.out.println("DB update erfolgreich");
            } else {
                System.out.println("DB update fehlgeschlagen");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
