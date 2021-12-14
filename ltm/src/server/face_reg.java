package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import DTO.ImageDTO;
import DTO.UserDTO;
import Model.ImageModel;
import Model.UserModel;

public class face_reg {
	public ImageModel model;
	public UserModel user;
	 public ConnectAPI connectAPI;
	 public ArrayList<ImageDTO> images;
	 public String idUser;
	 public File userFile;
	 public String faceToken;
	 String name;
	 int num;
	 ArrayList<UserDTO> users;
	 public face_reg(String idUser, BufferedImage img) throws IOException {
		 num=0;
		 this.idUser=idUser;
		 System.out.println(this.idUser);
		 model = new ImageModel();
		 user = new UserModel();
		 images = new ArrayList<>();
		 this.name= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		 File f = new File("folder/"+name+".jpg");
		 ImageIO.write(img, "jpg", f);
		 this.userFile = f;
		 System.out.println(this.userFile.getName());
	 }
	 public ArrayList<ImageDTO> getImageUser() {
		 images = model.getAll();
		 ArrayList<ImageDTO> FetchUserImages = new ArrayList<ImageDTO>();
		 ImageDTO dt = new ImageDTO();
		 for(ImageDTO img:images) {
			 if((img.getUser_id().equals(idUser))&&(FetchUserImages.contains(img.getUser_id()))) {
				 
			 }else {
				 if(!img.getUser_id().equals(idUser)) {
					 FetchUserImages.add(img);
				 }		 
			 }
		 }
		 
		 return FetchUserImages;
	 }
	 public String faceCompare(File face1,byte[] face2) {
		 connectAPI = new ConnectAPI();
		  String confidence="";
	    	HashMap<String, String> map = new HashMap<>();
	        HashMap<String, byte[]> byteMap = new HashMap<>();      
	        byte[] buff1 =  connectAPI.getBytesFromFile(face1);
	        System.out.println("face1:"+buff1.toString());
	        System.out.println("face2:"+face2.toString());
	        String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
	        map.put("api_key", "7viAD4eFYAvTta6c9kvLGFkdJoO4yZCX");
	        map.put("api_secret", "DW2P3rftKAdIrU9d-biWBcV4I8YUJ0aJ");
	        byteMap.put("image_file1", buff1);
	        byteMap.put("image_file2", face2);
	        try{
	            byte[] bacd = connectAPI.post(url, map, byteMap);
	            System.out.println(bacd.toString());
	            String str = new String(bacd);
	            System.out.println(str);
	            JSONParser parser = new JSONParser();
	            JSONObject  obj = (JSONObject)parser.parse(str);
	            JSONArray array =  (JSONArray)obj.get("faces1");
	            JSONObject obj2 = (JSONObject) array.get(0);
	            this.faceToken = (String)obj2.get("face_token");
	            if(obj.containsKey("error_message")) {
	            	JSONObject obj3 = new JSONObject();
	            	return null;
	            }
	            confidence = (Double)obj.get("confidence")+"";
	            return confidence;
	        }catch(Exception e) {
	        	System.out.println("Không so sánh được Face");
	        }
	        return confidence;
	    
	 }
	 public ArrayList<JSONObject> getResultFromAPI() throws Exception{
		 ArrayList<JSONObject> list_obj= new ArrayList<>();
		 ArrayList<ImageDTO> userImages = getImageUser();
		 System.out.println(new File("").getAbsolutePath());
		 System.out.print(userImages.size());
		 users = new ArrayList<>();
		 for(int i=0;i<userImages.size();i++) {
			 JSONObject obj = new JSONObject();
			 File  tmp =new File("folder/"+userImages.get(i).getImage_name());
			 byte[] bytes = new byte[(int) tmp.length()];
			  FileInputStream fis = null;
			  fis= new FileInputStream(tmp);
			  fis.read(bytes);
			 String con = faceCompare(userFile,bytes)+"";
			 obj.put("confidence",con);
			 users = null;
			 System.out.println(userImages.get(i).getUser_id());
			 users = user.getUser(userImages.get(i).getUser_id());
			 System.out.println(users.size()+"error:"+users.get(i).getNameUser());
			 obj.put("image", userImages.get(i).getImage_name());
			 obj.put("name", users.get(i+1).getNameUser());
			 list_obj.add(obj);
		 }
		 ImageDTO dto = new ImageDTO(idUser, name+".jpg");
		 model.Insert(dto);
		 if(AddFaceAPI(faceToken)) {
			 setUpUserIdAPI(faceToken, idUser);
		 }
		 return list_obj;
	 }
	 
	 
	 public boolean AddFaceAPI(String facetok) {
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
	        	System.out.println("Lỗi không AddFaceAPI được");                                                                       
	        }
	        return true;
	    }
	    
	    public  boolean setUpUserIdAPI(String facetok,String user_id) {
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
	        	System.out.println("Lỗi không setUpUderIDAPI được");
	        }
	        return true;
	    }
}
