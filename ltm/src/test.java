
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.List; 
import java.util.Random; 
import java.util.function.Consumer;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LAPTOPTOKYO
 */
public class test {
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z     
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z     
    private static final String digits = "0123456789"; // 0-9     
    private static final String specials = "~=+%^*/()[]{}/!@#$?|";     
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;     
    private static final String ALL = alpha + alphaUpperCase + digits + specials;     
    private static Random generator = new Random();
     
//    public static void imageIoWrite() {
//         BufferedImage bImage = null;
//         try {
//             File initialImage = new File("D://img/DSCF1867.JPG");
//             bImage = ImageIO.read(initialImage);
// 
//             ImageIO.write(bImage, "gif", new File("D://img/DSCF1867.gif"));
//             ImageIO.write(bImage, "jpg", new File("D://img/DSCF1867.png"));
//             ImageIO.write(bImage, "bmp", new File("D://img/DSCF1867.bmp"));
// 
//         } catch (IOException e) {
//               System.out.println("Exception occured :" + e.getMessage());
//         }
//         System.out.println("Images were written succesfully.");

     /**      * Random string with a-zA-Z0-9, not included special characters      */ 
//    }
    public String randomAlphaNumeric(int numberOfCharactor) {         
        StringBuilder sb = new StringBuilder();         
        for (int i = 0; i < numberOfCharactor; i++) {             
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);             
            char ch = ALPHA_NUMERIC.charAt(number);             
            sb.append(ch);         
        }         return sb.toString();     
    }     /**      * Random string password with at least 1 digit and 1 special character      */     
    
    public String randomPassword(int numberOfCharactor) {         
        List<String> result = new ArrayList<>();         
        Consumer<String> appendChar = s -> {             
            int number = randomNumber(0, s.length() - 1);             
            result.add("" + s.charAt(number));         
        };         
        appendChar.accept(digits);         
        appendChar.accept(specials);         
        while (result.size() < numberOfCharactor) {             
            appendChar.accept(ALL);         
        }         
        Collections.shuffle(result, generator);         
        return String.join("", result);     
    }     
    public static int randomNumber(int min, int max) {         
        return generator.nextInt((max - min) + 1) + min;     
    }     
    public static void main(String a[]){         
        int numberOfCharactor = 8;         
        test rand = new test();             
        System.out.println("randomString1: " + rand.randomAlphaNumeric(numberOfCharactor));         
        System.out.println("randomString1: " + rand.randomAlphaNumeric(numberOfCharactor));        
        System.out.println("randomString1: " + rand.randomAlphaNumeric(numberOfCharactor));         
        System.out.println("randomPassword1: " + rand.randomPassword(numberOfCharactor));         
        System.out.println("randomPassword2: " + rand.randomPassword(numberOfCharactor));     
    }  
}
