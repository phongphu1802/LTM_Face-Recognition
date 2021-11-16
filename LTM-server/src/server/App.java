package server;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, IOException, SQLException, Exception
    {
        Server t= new Server(4606);
    }
}
