package Model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controller.DangNhapController;
import DAO.MyConnect;
import DAO.MyConnectUnit;
import DTO.ImageDTO;

public class ImageDAL {


	  static ArrayList<ImageDTO> images=new ArrayList<ImageDTO>();
	    MyConnectUnit connect;
	    ResultSet rsImage;
	    public ImageDAL(){
	        try {
	            this.connect=MyConnect.getDAO();
	        } catch (Exception ex) {
	            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    
	    public ArrayList<ImageDTO> getAllImage(String user_id) throws Exception{
	        String condition="ID_user ='"+user_id+"'";
	        rsImage=this.connect.Select("Hinhanh", condition);
	        //Lấy thông tin table tai khoản
	        while(rsImage.next()){
	            ImageDTO image=new ImageDTO();
	            image.setUser_id(rsImage.getString(1));
	            image.setImage_id(rsImage.getString(2));
	            image.setImage_name(rsImage.getString(3));
	            images.add(image);
	        }
	        return images;
	    }
	    public String addtoImages(String user_id,String image_id,String photo) throws Exception {
	    	ImageDTO image = new ImageDTO();
	    	image.setImage_id(image_id);
	    	image.setUser_id(user_id);
	    	image.setImage_name(image_id);
	    	HashMap<String, Object> hash = new HashMap<>();
	    	hash.put("image", image);
	    	if(connect.Insert("Hinhanh", hash)) {
	    		return "add success";
	    	}
	    	return "add false";
	    }

}
