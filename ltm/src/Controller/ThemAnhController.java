/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.DangNhapController.arTK;
import DTO.ImageDTO;
import Model.ImageModel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import static server.App.connectAPI;
import server.ConnectAPI;
import server.Server;
import static server.WorkerThread.bufferedImageToMat;
import server.face_detect;
import server.face_reg;


/**
 *
 * @author LAPTOPTOKYO
 */
public class ThemAnhController {
    static ArrayList<ImageDTO> arIG;
    ImageModel add_image= new ImageModel();
    public ThemAnhController(){
        
    }
    
    public int themAnh(String iduser,BufferedImage bimg, byte[] temp1){
        arIG = add_image.Select(iduser);
        //Kiểm tra dữ liệu của thành viên đã có trong database chưa
        //Nếu có thì kiểm tra tỉ lệ giống nhau
        //Nếu không thì cho add ảnh mới vào.
        if(arIG.size()==0)
        {
            if(bimg!=null){
            try {
                String name_image = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                File f1 = new File("folder/"+name_image+".jpg");
                ImageIO.write(bimg, "jpg", f1);
                //Tài khoản khong tồn tại
                ImageDTO temp = new ImageDTO();
                temp.setUser_id(iduser);
                temp.setImage_name(name_image+".jpg");
                    add_image.Insert(temp);
                    return 1;
                } catch (IOException ex) {
                    Logger.getLogger(ThemAnhController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            //Hàm kiểm tra ti le khop nhau của ảnh mới vào so với ảnh cũ
            //Nếu tỉ lệ lớn hơn 80% cho phep thêm ảnh
            //Nếu tỉ lệ thấp hơn trả về ảnh không trùng khớp
            File f2 = new File("folder/"+arIG.get(arIG.size()-1).getImage_name());
               
            if(faceCompare(f2, temp1)>80.0){
                try {
                    String name_image = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    File f1 = new File("folder/"+name_image+".jpg");
                    ImageIO.write(bimg, "jpg", f1);
                    //Tài khoản khong tồn tại
                    ImageDTO temp = new ImageDTO();
                    temp.setUser_id(iduser);
                    temp.setImage_name(name_image+".jpg");
                    add_image.Insert(temp);
                    return 1;
                } catch (IOException ex) {
                    Logger.getLogger(ThemAnhController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                return 2;
            }
        }
        return 0;
    }
     public double faceCompare(File face1,byte[] face2) {
	connectAPI = new ConnectAPI();
        double confidence=0.0;
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
	            
	            if(obj.containsKey("error_message")) {
	            	JSONObject obj3 = new JSONObject();
	            	return confidence;
	            }
	            confidence = (Double)obj.get("confidence");
	            return confidence;
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return confidence; 
	 }
}
