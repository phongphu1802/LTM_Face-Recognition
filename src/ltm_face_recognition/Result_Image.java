/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import DTO.HinhanhDTO;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    /**
     * Creates new form Result_Image
     */
        static ArrayList<HinhanhDTO> arRI=new ArrayList<HinhanhDTO>();
	public ArrayList<String> result;
	private String userID="";
	private BufferedImage buff;
	private Socket socket;
	private SecretKey key;
	DefaultTableModel dtm=new DefaultTableModel(new String [] {
            "Ảnh cần so sánh", "Tên người nhận điện", "Tỉ lệ"
        },0);
    public Result_Image() {
        initComponents();
        //Set_Size_Column_Table();
    }
    public Result_Image(Socket socket,String userID,BufferedImage buff,ArrayList<String> result,SecretKey key) {
    	this.socket = socket;
    	 initComponents();
    	this.userID = userID;
    	this.buff = buff;
    	this.result = result;
    	this.key = key;
    	
    	 ImageIcon icon = new ImageIcon(buff); 
    	 Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
    	 jLabel1 = new JLabel();
    	 jLabel1.setIcon(icon);
    	 try {
			setResult();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Lỗi result_Image");
		}
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
            	try {
					Face_Recognition face = new Face_Recognition(socket, key, userID);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("Lỗi khởi tạo Components");
				}
                //MainPage m = new MainPage();
                //m.setVisible(true);
                e.getWindow().dispose();
            }
        });
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
               "null"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(396, 396, 396))
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 50, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(334, 334, 334)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
   /* public void Set_Size_Column_Table(){
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(450);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(400);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(144);
    }*/
    public void setResult() throws IOException {
        arRI.clear();
    	DefaultTableModel dtm=(DefaultTableModel)jTable1.getModel();
    	Object[] Headers = new Object[]{ "Ảnh cần so sánh", "Tên người nhận điện", "Tỉ lệ"};
    	
    	dtm.setColumnIdentifiers(Headers);
    	jTable1.getColumn("Ảnh cần so sánh").setCellRenderer(new CellRenderer());
    	
    	JSONObject object1=null;
        JSONParser parser = new JSONParser();
    	for(String s:this.result) {
    		try {
                                object1 = (JSONObject)parser.parse(s);
                                HinhanhDTO hinh= new HinhanhDTO();
                                hinh.setsImage((String)object1.get("image"));
                                hinh.setsName((String)object1.get("name"));
                                hinh.setiConfident(Float.parseFloat((String)object1.get("confidence")));
                                arRI.add(hinh);
                                
//				object1 = (JSONObject)parser.parse(s);
//				String name = (String)object1.get("name");
//				String confidence = (String)object1.get("confidence");
//				String imageName = (String)object1.get("image");
//				File e = new File("C:\\Users\\LENOVO\\eclipse-workspace\\ltm\\folder\\"+imageName);
//				 JLabel imageLabel = new JLabel();
//				ImageIcon icon = new ImageIcon(ImageIO.read(e));
//				Image image = icon.getImage().getScaledInstance(160,160, Image.SCALE_SMOOTH);
//				imageLabel.setIcon(new ImageIcon(image));
//				Object[] obj  = {imageLabel,name,confidence};	
//				dtm.addRow(obj);
    		
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Lỗi setResult");
			}	
    	}
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
            File e = new File("../ltm/folder/"+arRI.get(i).getsImage());
            JLabel imageLabel = new JLabel();
            ImageIcon icon = new ImageIcon(ImageIO.read(e));
            Image image = icon.getImage().getScaledInstance(160,160, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
            Object[] obj  = {imageLabel,arRI.get(i).getsName(),arRI.get(i).getiConfident()};	
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
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                
            }
            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Result_Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Result_Image().setVisible(true);
               
            }
            
           
            
        });
      
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
