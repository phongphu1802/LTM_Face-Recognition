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

public class ImageModel {
    static ArrayList<ImageDTO> images=new ArrayList<ImageDTO>();
    MyConnectUnit connect;
    ResultSet rsImage;
    public ImageModel(){
        try {
            this.connect=MyConnect.getDAO();
        } catch (Exception ex) {
            Logger.getLogger(ImageModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<ImageDTO> getAll(){
    	try {
            images.clear();
            rsImage=this.connect.Select("Hinhanh");
            //Lấy thông tin table tai khoản
            while(rsImage.next()){
                ImageDTO image=new ImageDTO();
                image.setUser_id(rsImage.getString(1));
                image.setImage_name(rsImage.getString(2));
                images.add(image);
            }
            return images;
        } catch (Exception ex) {
            Logger.getLogger(ImageModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return images;
    }
    //Select image from user_id
    public ArrayList<ImageDTO> Select(String user_id){
        try {
            String condition="ID_user ='"+user_id+"'";
            rsImage=this.connect.Select("Hinhanh", condition);
            //Lấy thông tin table tai khoản
            while(rsImage.next()){
                ImageDTO image=new ImageDTO();
                image.setUser_id(rsImage.getString(1));
                image.setImage_name(rsImage.getString(2));
                images.add(image);
            }
            return images;
        } catch (Exception ex) {
            Logger.getLogger(ImageModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return images;
    }
    
    //Insert image
    public void Insert(ImageDTO image){
        try {
            HashMap<String, Object>map=new HashMap<String,Object>();
            map.put("ID_user",image.getUser_id());
            map.put("photo",image.getImage_name());
            this.connect.Insert("Hinhanh", map);
        } catch (Exception ex) {
            Logger.getLogger(ImageModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
