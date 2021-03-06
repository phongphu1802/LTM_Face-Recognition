/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import Cipher.AES;
import Cipher.RSA;
import DTO.HinhanhDTO;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.Visibility;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LAPTOPTOKYO
 */
public class Result_Image extends javax.swing.JFrame {

    private RSA rsa;
    private AES aes;
    static ArrayList<HinhanhDTO> arRI = new ArrayList<HinhanhDTO>();
    public ArrayList<String> result;
    private String userID = "";
    private BufferedImage buff;
    private Socket socket;
    private SecretKey key;
    private String id = "";
    private String LastName = "";
    private String NameUser = "";
    private String Date_of_birth = "";
    
    DefaultTableModel dtm = new DefaultTableModel(new String[]{
        "Tên người nhận điện", "Tỉ lệ"
    }, 0);

    public Result_Image() {
        initComponents();
    }

    public Result_Image(Socket socket, String userID, BufferedImage buff, ArrayList<String> result, SecretKey key) {
        this.socket = socket;
        initComponents();
        this.userID = userID;
        this.buff = buff;
        this.result = result;
        this.key = key;
//    	ImageIcon icon = new ImageIcon(buff); 
//    	Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        jLabel1.setIcon(loadimage_processing(buff, jLabel1.getWidth(), jLabel1.getHeight()));
        try {
            setResult();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Lỗi result_Image");
        }
//        Set_Size_Column_Table();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    aes.setKey(key);
                    writer.write(aes.encrypt("DEAD"));
                    writer.newLine();
                    writer.flush();
                    setVisible(false);
                    System.exit(0);
                } catch (IOException ex) {
//                    Logger.getLogger(Result_Image.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Lỗi kết nối");
                } 
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kết quả sau khi kiểm tra");
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(450, 200, 800, 600));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Kết quả khi nhận điện khuôn mặt");

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên người nhận điện", "Tỉ lệ"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Quay về");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(220, 220, 220)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(235, 235, 235)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 50, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        try {
            Face_Recognition s1 = new Face_Recognition(this.socket, this.key, this.id);
            s1.Start(this.id, this.LastName, this.NameUser, this.Date_of_birth);
            s1.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(Result_Image.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    // can chinh image
    public Icon loadimage_processing(BufferedImage image, int k, int m) {
        /*linkImage là tên icon, k kích thước chiều rộng mình muốn,m chiều dài và hàm này trả về giá trị là 1 icon.*/ int x = k;
        int y = m;
        int ix = image.getWidth();
        int iy = image.getHeight();
        int dx = 0, dy = 0;
        if (x / y > ix / iy) {
            dy = y;
            dx = dy * ix / iy;
        } else {
            dx = x;
            dy = dx * iy / ix;
        }
        return new ImageIcon(image.getScaledInstance(dx, dy,
                image.SCALE_SMOOTH));
    }
    
    public void Set_Size_Column_Table() {
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(450);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(400);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(144);
    }

    public void setResult() throws IOException {
        arRI.clear();
//        if(dtm.getRowCount()>0){
//            for(int i=0; i<dtm.getRowCount(); i++){
//                dtm.removeRow(i);
//            }
//        }
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        Object[] Headers = new Object[]{"Tên người nhận điện", "Tỉ lệ"};
        
        dtm.setColumnIdentifiers(Headers);
        
        JSONObject object1 = null;
        JSONParser parser = new JSONParser();
        for (String s : this.result) {
            try {
                object1 = (JSONObject) parser.parse(s);
                HinhanhDTO hinh = new HinhanhDTO();
                hinh.setsImage((String) object1.get("image"));
                hinh.setsName((String) object1.get("name"));
                hinh.setiConfident(Float.parseFloat((String) object1.get("confidence")));
                arRI.add(hinh);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                System.out.println("Lỗi setResult");
            }            
        }
//        for (int i = 0; i < arRI.size(); i++) {
//            System.out.println(arRI.get(i).getsImage());
//        }
        //Sắp xếp danh sách theo tỉ lệ giảm dần!
        Collections.sort(arRI, new Comparator<HinhanhDTO>() {
            @Override
            public int compare(HinhanhDTO sv1, HinhanhDTO sv2) {
                if (sv1.getiConfident() < sv2.getiConfident()) {
                    return 1;
                } else {
                    if (sv1.getiConfident() == sv2.getiConfident()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        for (int i = 0; i < arRI.size(); i++) {
            Object[] obj = {arRI.get(i).getsName(), arRI.get(i).getiConfident()};
            dtm.addRow(obj);
        }
        System.out.println(dtm.getRowCount());
        dtm.fireTableDataChanged();
    }
    
    class CellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            
            jTable1.setRowHeight(160);
            
            return (Component) value;
        }
        
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * @param LastName the LastName to set
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     * @return the NameUser
     */
    public String getNameUser() {
        return NameUser;
    }

    /**
     * @param NameUser the NameUser to set
     */
    public void setNameUser(String NameUser) {
        this.NameUser = NameUser;
    }

    /**
     * @return the Date_of_birth
     */
    public String getDate_of_birth() {
        return Date_of_birth;
    }

    /**
     * @param Date_of_birth the Date_of_birth to set
     */
    public void setDate_of_birth(String Date_of_birth) {
        this.Date_of_birth = Date_of_birth;
    }
    
    public void Start(String id, String LastName, String NameUser, String Date_of_birth) {
        this.id = id;
        this.LastName = LastName;
        this.NameUser = NameUser;
        this.Date_of_birth = Date_of_birth;
    }

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//                
//            }
//            
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Result_Image().setVisible(true);
//               
//            }
//        });
//      
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
