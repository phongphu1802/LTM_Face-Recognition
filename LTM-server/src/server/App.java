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
        /*final String FACE_DETECT_API = "a";
    	connectAPI = new ConnectAPI();
		File file = new File("C:\\Users\\LENOVO\\Desktop\\face_regconize\\258767165_423087576039271_6129665173535320895_n.jpg");
		byte[] buff = connectAPI.getBytesFromFile(file);
		String url = "https://api-us.faceplusplus.com/facepp/v3/search";
        HashMap<String, String> map = new HashMap<>(); 
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "7viAD4eFYAvTta6c9kvLGFkdJoO4yZCX");
        map.put("api_secret", "DW2P3rftKAdIrU9d-biWBcV4I8YUJ0aJ");
        map.put("faceset_token", "3d16c073176a8e4a1d0cc9d62a1d7953");
        byteMap.put("image_file", buff);
        try{
            byte[] bacd = connectAPI.post(url, map, byteMap);
            String str = new String(bacd);
            JSONParser parser = new JSONParser();
            JSONObject  obj = (JSONObject)parser.parse(str);
            JSONArray array =  (JSONArray)obj.get("results");
            JSONArray array2 =  (JSONArray)obj.get("faces");
           // System.out.println(array.toJSONString());
            String user_id="";
            for(int i=0;i<array.size();i++) {
            	JSONObject  obj2 = (JSONObject)array.get(i);
            	if((double)obj2.get("confidence")>85 &&(String)obj2.get("user_id")!=null) {
            		user_id = (String)obj2.get("user_id");
            		break;
            	}else {
            		user_id = "Us005";
            	}
            }
            System.out.println(str);
            JSONObject  obj2=(JSONObject)array2.get(0);
            String tokenCurrentImage = (String)obj2.get("face_token");
            String image_id = (String)obj.get("image_id");
            //String s2 = setUpAPI(tokenCurrentImage);
            /*if(AddFaceAPI(tokenCurrentImage)==true) {
            	System.out.println("add sucess");
            	File test = new File("./folder/"+tokenCurrentImage+".jpg");
            	BufferedImage im = ImageIO.read(file);
            	ImageIO.write(im, "JPG", test);
            	if(setUpUserIdAPI(tokenCurrentImage,user_id)) {
            System.out.println("add success");
            	}else {
            		System.out.println("set id fail");	
            	}
            }else {
            	System.out.println("set to faceset fail");
            }
            */
            //System.out.print(s2);
            // }catch (Exception e) {
             //	e.printStackTrace();
                         // TODO: handle exception
            //}*/
    }
            


   
    
    public static boolean AddFaceAPI(String facetok) {
    	connectAPI = new ConnectAPI();
    	HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        String url = "https://api-us.faceplusplus.com/facepp/v3/faceset/addface";
        map.put("api_key", "7viAD4eFYAvTta6c9kvLGFkdJoO4yZCX");
        map.put("api_secret", "DW2P3rftKAdIrU9d-biWBcV4I8YUJ0aJ");
        map.put("faceset_token", "3d16c073176a8e4a1d0cc9d62a1d7953");
        map.put("face_tokens", facetok);
        try{
            byte[] bacd = connectAPI.post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println(str);
            JSONParser parser = new JSONParser();
            JSONObject  obj = (JSONObject)parser.parse(str);
            if(obj.containsKey("error_message")) {
            	return false;
            }
            return true;
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return true;
    }
    
    public static boolean setUpUserIdAPI(String facetok,String user_id) {
    	connectAPI = new ConnectAPI();
    	HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        String url = "https://api-us.faceplusplus.com/facepp/v3/face/setuserid";
        map.put("api_key", "7viAD4eFYAvTta6c9kvLGFkdJoO4yZCX");
        map.put("api_secret", "DW2P3rftKAdIrU9d-biWBcV4I8YUJ0aJ");
        map.put("user_id", user_id);
        map.put("face_token", facetok);
        try{
            byte[] bacd = connectAPI.post(url, map, byteMap);
            String str = new String(bacd);
            JSONParser parser = new JSONParser();
            JSONObject  obj = (JSONObject)parser.parse(str);
            System.out.println(str);
            if(obj.containsKey("error_message")) {
            	return false;
            }
            return true;
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return true;
    }
}
