/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Cipher.AES;
import Cipher.RSA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
import ltm_face_recognition.Face_Recognition;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LAPTOPTOKYO
 */
public class DangKyController {

    BufferedWriter out = null;
    BufferedReader in = null;
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private RSA rsa;
    private AES aes;
    private Socket socket;
    private SecretKey secretKey;

    public Socket ConnectServer(String host, int port) {
        try {
            setSocket(new Socket(host, port));
            rsa = new RSA();
            aes = new AES();
            return getSocket();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            JOptionPane.showMessageDialog(null, "Server is busy right now");
        }
        return null;
    }

    public void disconnectServer() throws IOException {
        getSocket().close();
    }

    public DangKyController() {

    }

    public String DangKyController(String host, int port, String strTaiKhoan, String strMatKhau, String strNhapLaiMatKhau, String strHo, String strTen, String strNgaySinh) {
        String Result = "";
        String line = "";
        //Ki???m tra d??? li???u r???ng
        if (!strTaiKhoan.equals("") && !strMatKhau.equals("") && !strNhapLaiMatKhau.equals("") && !strHo.equals("") && !strTen.equals("") && !strNgaySinh.equals("")) {
            //Ki???m tra m???t kh???u nh???p l???i
            if (strMatKhau.equals(strNhapLaiMatKhau)) {
                //Ki???m tra h??? kh??ng ???????c c?? s???
                if (Pattern.matches("^\\D+$", strHo)) {
                    //Ki???m tra t??n kh??ng ???????c c?? s???
                    if (Pattern.matches("^\\D+$", strTen)) {
                        if (calculateAge(strNgaySinh) > 18 && calculateAge(strNgaySinh) < 100) {
                            try {
                                setSocket(ConnectServer(host, port));
                                if (getSocket() != null && getSocket().isConnected()) {
                                    out = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream()));
                                    in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
                                    //encrypt
                                    setSecretKey(aes.generatorKey());
                                    String cipherText1 = Base64.getEncoder().encodeToString(getSecretKey().getEncoded());
                                    String RSAEncrypt = Base64.getEncoder().encodeToString(rsa.encrypt(cipherText1, publicKey));
                                    //send secret key
                                    out.write(RSAEncrypt);
                                    out.newLine();
                                    out.flush();
                                    //G???i d??? li???u ??i
                                    out.write(aes.encrypt("register"));
                                    out.newLine();
                                    out.flush();
                                    JSONObject object = new JSONObject();
                                    object.put("user", strTaiKhoan);
                                    object.put("pass", strMatKhau);
                                    object.put("lastname", strHo);
                                    object.put("firstname", strTen);
                                    object.put("date_of_birth", strNgaySinh + "T08:51:10.1225942+07:00");
                                    System.out.println(object);
                                    out.write(aes.encrypt(object.toString()));
                                    out.newLine();
                                    out.flush();
                                    //B???t d??? li???u v???
                                    line = aes.decrypt(in.readLine(), getSecretKey());
                                    System.out.println("Server sent: " + line);
                                    switch (line) {
                                        case "0": {
                                            JOptionPane.showMessageDialog(null, "T??i kho???n " + strTaiKhoan + " ????ng k?? th??nh c??ng.");
                                            Result = "T??i kho???n ????ng k?? th??nh c??ng.";
                                            out.write(aes.encrypt("DEAD"));
                                            out.newLine();
                                            out.flush();
                                            break;
                                        }
                                        case "1": {
                                            JOptionPane.showMessageDialog(null, "T??i kho???n " + strTaiKhoan + " ????ng k?? ???? c?? ng?????i d??ng vui l??ng ch???n t??n t??i kho???n kh??c.");
                                            Result = "T??i kho???n " + strTaiKhoan + " ????ng k?? ???? c?? ng?????i d??ng vui l??ng ch???n t??n t??i kho???n kh??c.";
                                            break;
                                        }
                                    }
                                }
                                //????ng k???t n???i
                                disconnectServer();
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Tu???i ph???i l???n h??n 18 v?? b?? h??n 100!!!");
                            Result = "Tu???i ph???i l???n h??n 18 v?? b?? h??n 100.";
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "T??n kh??ng ???????c c?? s???!!!");
                        Result = "T??n kh??ng ???????c c?? s???.";
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "H??? kh??ng ???????c c?? s???!!!");
                    Result = "H??? kh??ng ???????c c?? s???.";
                }
            } else {
                JOptionPane.showMessageDialog(null, "M???t kh???u nh???p l???i kh??ng gi???ng nhau!!!");
                Result = "M???t kh???u nh???p l???i kh??ng gi???ng nhau.";
            }
        } else {
            JOptionPane.showMessageDialog(null, "D??? li???u kh??ng ???????c r???ng!!!");
            Result = "D??? li???u kh??ng ???????c r???ng.";
        }
        return Result;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the secretKey
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    //Th???c thi g???i request v??? client
    public void Reply(String stResult) {
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(stResult);
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String Encrypt(String stResult) {
        return aes.encrypt(stResult);
    }

    public static long calculateAge(String birthDate) {
        LocalDate localDate1 = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate localDate2 = LocalDate.now();
        long years = java.time.temporal.ChronoUnit.YEARS.between(localDate1, localDate2);
        return years;
    }
}
