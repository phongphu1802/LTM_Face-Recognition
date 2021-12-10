/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DTO.TaiKhoanDTO;
import DTO.UserDTO;
import Model.TaiKhoanModel;
import Model.UserModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author LAPTOPTOKYO
 */
public class DangKyController {
    static ArrayList<TaiKhoanDTO> arTK=new ArrayList<TaiKhoanDTO>();
    static ArrayList<UserDTO> arUser=new ArrayList<UserDTO>();
    TaiKhoanModel TKDangKy = new TaiKhoanModel();
    UserModel User = new UserModel();
    
    public String DangKyController(String strUser,String strPass, String lastName, String userName,String date_Of_Birth) throws Exception
    {
        //Tìm thông tin tài khoản khi biết tên Tài khoản
        arTK=TKDangKy.taiKhoan(strUser);
        //Kiểm tra xem Row có rỗng hay không nếu rỗng trả về 0 nghĩa là tài khoản không tồn tại
        if(arTK.size()==0)
        {
            //Insert tài khoản vào
            TaiKhoanDTO tk = new TaiKhoanDTO();
            int id_tk = TKDangKy.select_Count()+1;
            String Id_TK="TK"+id_tk;
            tk.setId_TK(Id_TK);
            tk.setStrUser(strUser);
            tk.setStrPass(doHashing(strPass));
            tk.setStrStatus("1");
            TKDangKy.insert(tk);
            
            //Insert user
            int id_us = User.select_Count()+1;
            String Id_US="Us"+id_us;
            UserDTO us = new UserDTO();
            us.setIdTK(Id_TK);
            us.setIdUser(Id_US);
            us.setLastName(lastName);
            us.setNameUser(userName);
            us.setDate_of_birth(date_Of_Birth);
            us.setStatus("1");
            User.insert(us);
            return "0";
        }else{
           //Tài khoản đã tồn tại
           return "1";
        }
    }
       
    public static String doHashing(String pass){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pass.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            for(byte b: resultByteArray){
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
