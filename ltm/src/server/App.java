package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Controller.DangNhapController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static ConnectAPI connectAPI;
    public static void main( String[] args ) throws ClassNotFoundException, IOException, SQLException, Exception
    {
        Server t= new Server(4606);
    }
}
