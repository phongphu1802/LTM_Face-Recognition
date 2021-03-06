/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import Cipher.RSA;
import Cipher.AES;
import Controller.ThemAnhController;
import java.awt.event.WindowAdapter;
import javax.swing.Icon;

/**
 *
 * @author LAPTOPTOKYO
 */
public class Face_Recognition extends javax.swing.JFrame {

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private String id = "";
    private String LastName = "";
    private String NameUser = "";
    private String Date_of_birth = "";
    private RSA rsa;
    private AES aes;
    Camera camera;
    private Socket socket;
    private SecretKey secretKey;
    private BufferedImage staticImg;

    /**
     * Creates new form Face_Recognition
     */
    public Face_Recognition(Socket socket, SecretKey secretKey, String id) throws IOException {
        this.socket = socket;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        rsa = new RSA();
        aes = new AES();
        aes.setKey(secretKey);
        this.id = id;
        System.out.println(this.id);
        this.secretKey = secretKey;
        System.out.println("key nhan dc " + this.secretKey);
        initComponents();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(aes.encrypt("DEAD"));
                    writer.newLine();
                    writer.flush();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(Face_Recognition.class.getName()).log(Level.SEVERE, null, ex);
                } finally {

                }
            }
        });
    }

    public Face_Recognition() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        initComponents();
    }

    public void disconnectServer() throws IOException {
        socket.close();
    }

    public void Start(String id, String LastName, String NameUser, String Date_of_birth) {
        this.id = id;
        this.LastName = LastName;
        this.NameUser = NameUser;
        this.Date_of_birth = Date_of_birth;
        jLabel2.setText("T??n ng?????i d??ng: " + this.LastName + " " + this.NameUser);
        jLabel4.setText("Ng??y sinh: " + this.Date_of_birth);
    }

    // can chinh image
    public Icon loadimage_processing(String linkImage, int k, int m) {/*linkImage l?? t??n icon, k k??ch th?????c chi???u r???ng m??nh mu???n,m chi???u d??i v?? h??m n??y tr??? v??? gi?? tr??? l?? 1 icon.*/
        try {
            BufferedImage image = ImageIO.read(new File(linkImage));//?????c ???nh d??ng BufferedImage
            this.staticImg = image;
            int x = k;
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
            return new ImageIcon(image.getScaledInstance(dx, dy, image.SCALE_SMOOTH));
        } catch (IOException e) {
            System.out.println("L???i loadimage_processing");
        }
        return null;
    }

    // can chinh image
    public Icon loadimage_processing2(BufferedImage linkImage, int k, int m) {/*linkImage l?? t??n icon, k k??ch th?????c chi???u r???ng m??nh mu???n,m chi???u d??i v?? h??m n??y tr??? v??? gi?? tr??? l?? 1 icon.*/
        BufferedImage image = linkImage;//?????c ???nh d??ng BufferedImage
        int x = k;
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
        return new ImageIcon(image.getScaledInstance(dx, dy, image.SCALE_SMOOTH));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(500, 100, 0, 0));
        setSize(getMaximumSize());

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("L???y ???nh c?? \ntrong m??y"); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setText("M??? m??y ???nh");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setText("Th??m nh???n d???ng m???i");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Ch????ng tr??nh nh???n d???ng khu??n m???t");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("T??n ng?????i d??ng: Nguy???n Phong Ph??");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setText("Ng??y sinh: 18/02/2000");

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton4.setText("Nh???n di???n ???nh");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton5.setText("Nh???n di???n v???t");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //btn L???y ???nh c?? trong m??y
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        try {
            GetImage gI = new GetImage();
            int x = jLabel3.getWidth();
            int y = jLabel3.getHeight();
            jLabel3.setIcon(loadimage_processing(gI.getImage(), x, y));
        } catch (NullPointerException e) {
            System.out.println("L???i ko ch???n d??? li???u");
        }
    }//GEN-LAST:event_jButton1MouseClicked

    //btn M??? m??y ???nh
    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        setVisible(false);
        //dispose();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                camera = new Camera(socket, secretKey, id);
                camera.Start(id, LastName, NameUser, Date_of_birth);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            camera.startCamera();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Face_Recognition.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh ParseException");
                        } catch (InvalidKeyException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh InvalidKeyException");
                        } catch (BadPaddingException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh BadPaddingException");
                        } catch (IllegalBlockSizeException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh IllegalBlockSizeException");
                        } catch (NoSuchPaddingException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh NoSuchPaddingException");
                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            System.out.println("L???i n??t m??? m??y ???nh NoSuchAlgorithmException");
                        }
                    }
                }).start();
            }
        });
    }//GEN-LAST:event_jButton2MouseClicked

    public static BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    //btn Th??m nh???n d???ng m???i
    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        if (jLabel3.getIcon() == null) {
            JOptionPane.showMessageDialog(null, "B???n c???n ch???p ???nh ho???c th??m ???nh t??? m??y t??nh ????? c?? th??? th??m ???nh.");
        } else {
            ThemAnhController temp = new ThemAnhController(socket, secretKey);
            temp.ThemAnhController(staticImg, this.id);
        }
    }//GEN-LAST:event_jButton3MouseClicked

    //btn Nh???n d???ng khu??n m???t
    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        if (jLabel3.getIcon() == null) {
            JOptionPane.showMessageDialog(null, "B???n c???n ch???p ???nh ho???c th??m ???nh t??? m??y t??nh ????? ki???m tra.");
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer.write(aes.encrypt1("face regconize", this.secretKey));
                writer.newLine();
                writer.flush();

                writer.write(aes.encrypt1(id, this.secretKey));
                writer.newLine();
                writer.flush();

                System.out.println("Da gui id" + id);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(this.staticImg, "jpg", byteArrayOutputStream);//image to byte[]
                String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());//encode image to string
                String cipherText2 = aes.encrypt1(byteImage, this.secretKey);
                //send image 
                writer.write(cipherText2);
                writer.newLine();
                writer.flush();
                System.out.println("Da gui anh");
                ArrayList<String> result = new ArrayList<>();
                while (true) {
                    String response = reader.readLine();
                    if (aes.decrypt(response, this.secretKey).equalsIgnoreCase("END")) {
                        break;
                    } else {
                        result.add(aes.decrypt(response, secretKey));
                    }
                }
                Result_Image rs = new Result_Image(socket, id, staticImg, result, secretKey);
                rs.Start(this.id, this.LastName, this.NameUser, this.Date_of_birth);
                rs.setVisible(true);
                this.setVisible(false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("L???i n??t nh???n d???ng khu??n m???t");
            }
        }
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        if (jLabel3.getIcon() == null) {
            JOptionPane.showMessageDialog(null, "B???n c???n ch???p ???nh ho???c th??m ???nh t??? m??y t??nh ????? ki???m tra.");
        } else {
            try {
                if (socket != null && socket.isConnected()) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //encrypt

                    //send func keyword by secretKey
                    System.out.println("key load" + this.secretKey);
                    writer.write(aes.encrypt("Object"));
                    writer.newLine();
                    writer.flush();
                    byte[] imageData;
                    ImageIcon icon;
                    //get Image from camera
                    if (this.staticImg != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(staticImg, "jpg", byteArrayOutputStream);//image to byte[]
                        String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());//encode image to string
                        String cipherText2 = aes.encrypt1(byteImage, this.secretKey);
                        System.out.println(cipherText2);
                        //send image 
                        writer.write(cipherText2);
                        writer.newLine();
                        writer.flush();
                        BufferedImage bf;
                        boolean c = true;
                        while (c) {
                            System.out.println("v??o ??hile");
                            String line = reader.readLine();
                            //add image receive to bufferimage
                            String plainText = aes.decrypt(line, secretKey);
                            bf = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(plainText)));
//             	                System.out.println(bf.toString());
                            if (bf != null) {
                                Mat m = bufferedImageToMat(bf);
                                final MatOfByte buf2 = new MatOfByte();
                                Imgcodecs.imencode(".jpg", m, buf2);
                                imageData = buf2.toArray();
                                Image image = bf;
//                                Image image2 = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                                icon = new ImageIcon(image);

                                jLabel3.setIcon(icon);
                                setVisible(true);
                                c = false;
                            }
                        }
                    }
                } else {
                    this.setVisible(false);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton5MouseClicked

    public void loadAnh(Mat image) throws IOException, InterruptedException, ParseException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        //connect to server 
        if (socket != null && socket.isConnected()) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //encrypt

            //send func keyword by secretKey
            System.out.println("key load" + this.secretKey);
            writer.write(aes.encrypt("Camera"));
            writer.newLine();
            writer.flush();
            byte[] imageData;
            ImageIcon icon;
            //get Image from camera
            BufferedImage bImage = MatToBufferedImage(image);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", byteArrayOutputStream);//image to byte[]
            String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());//encode image to string
            String cipherText2 = aes.encrypt1(byteImage, this.secretKey);
            //send image 
            writer.write(cipherText2);
            writer.newLine();
            writer.flush();
            BufferedImage bf;
            boolean c = true;
            while (c) {
                String line = reader.readLine();
                //add image receive to bufferimage
                String plainText = aes.decrypt(line, secretKey);
                bf = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(plainText)));
//                System.out.println(bf.toString());
                if (bf != null) {
                    this.staticImg = bf;

                    jLabel3.setIcon(loadimage_processing2(bf, jLabel3.getWidth(), jLabel3.getHeight()));

                    setVisible(true);
                    c = false;
                }
            }
        } else {
            this.setVisible(false);
        }
    }

    /**
     * @param args the command line arguments
     */
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
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Face_Recognition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Face_Recognition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Face_Recognition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Face_Recognition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new Face_Recognition().setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(Face_Recognition.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
