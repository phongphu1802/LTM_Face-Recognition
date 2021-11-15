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
import ltm_face_recognition.Face_Recognition;

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
            JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" không tồn tại.");
            return "Tài khoản không tồn tại";
        }
        else{
            //Nếu mật khẩu tài khoản trùng
            if(arTK.get(0).getStrPass().equals(strMatKhau))
            {
                JOptionPane.showMessageDialog(null,"Đăng nhập thành công. Chào bạn "+strTaiKhoan+".");
                //Tìm thông tin nhân viên khi biết mã nhân viên
                arUser=User.User(arTK.get(0).getId_TK());
                Face_Recognition Start= new Face_Recognition();
                Start.Start(arUser.get(0).getLastName(),arUser.get(0).getNameUser(),arUser.get(0).getDate_of_birth());
                Start.setVisible(true);
                return "Đăng nhập thành công";
            }
            JOptionPane.showMessageDialog(null,"Đăng nhập không thành công. vui lòng nhập lại mật khẩu của "+strTaiKhoan+".");
            return "Đăng nhập không thành công";
        }
    }
}
