/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.MyConnectUnit;
import DTO.TaiKhoanDTO;
import DTO.UserDTO;
import Model.TaiKhoanModel;
import Model.UserModel;
import java.sql.ResultSet;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;

/**
 *
 * @author LAPTOPTOKYO
 */
public class DangNhapController {
    static ArrayList<TaiKhoanDTO> arTK=new ArrayList<TaiKhoanDTO>();
    static ArrayList<UserDTO> arUser=new ArrayList<UserDTO>();
    TaiKhoanModel TKDangNhap = new TaiKhoanModel();
    UserModel User = new UserModel();
    public String DangNhapController(String strTaiKhoan,String strMatKhau) throws Exception
    {
        //Tìm thông tin tài khoản khi biết tên Tài khoản
        arTK=TKDangNhap.TaiKhoan(strTaiKhoan);
        //Kiểm tra xem Row có rỗng hay không nếu rỗng trả về 0 nghĩa là tài khoản không tồn tại
        if(arTK.size()==0)
        {
            //Tài khoản khong tồn tại
            return "1";
        }
        else{
            //Nếu mật khẩu tài khoản trùng
            if(arTK.get(0).getStrPass().equals(strMatKhau))
            {
                //Đăng nhạp thành công
                return "3";
            }else{
                //Đăng nhập không thành công sai mật khẩu
                return "2";
            } 
        }
    }
    
    public JSONObject Select_User(){
        try {
            //Tìm thông tin nhân viên khi biết mã nhân viên
            arUser=User.User(arTK.get(0).getId_TK());
        } catch (Exception ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject user=new JSONObject();
        user.put("LastName", arUser.get(0).getLastName());
        user.put("NameUser", arUser.get(0).getNameUser());
        //user.put("Date_of_birth",arUser.get(0).getDate_of_birth());
        return user;    
    }
}
