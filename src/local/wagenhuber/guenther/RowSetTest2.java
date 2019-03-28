//CachedRowSet wird rekonstruiert (deserialisiert) und der erste Datensatz wird geändert. Die Instanz wird wiederum serialisiert

package local.wagenhuber.guenther;

import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.sql.SQLException;

public class RowSetTest2 {

    public static void main(String[] args) {

        try {
            CachedRowSet crs = RowSetUtility.deserialize("buch.dat");

            //Erste Zeile auswählen
            crs.absolute(1);
            //Wert lesen und erhöhen (+10)
            double verkaufspreis = crs.getDouble(3) + 10;
            System.out.println(verkaufspreis);
            crs.updateDouble(3, verkaufspreis);
            //Datensatz wird geändert!
            crs.updateRow();

            RowSetUtility.serialize(crs, "buch.dat");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
