package houghlines;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HoughTransform {
    
    final int WHITE = new Color(255, 255, 255).getRGB();
    final int BLACK = new Color(0, 0, 0).getRGB();
    final int RED = new Color(255, 0, 0).getRGB();
    
    int[][] space;
    BufferedImage img;
    int[][] processed_img;
    
    public HoughTransform(String path, int theta_max){
        try{
            BufferedImage img_buff = ImageIO.read(new File(path));
            img = new BufferedImage(img_buff.getWidth(), img_buff.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y=0; y<img.getHeight(); y++){
                for (int x=0; x<img.getWidth(); x++){
                    img.setRGB(x, y, img_buff.getRGB(x, y));
                }
            }
        }catch (Exception ex){
            return;
        }
        int diagonal = (int)Math.sqrt(Math.pow(img.getWidth(), 2) + Math.pow(img.getHeight(), 2)) + 1;
        space = new int[diagonal][theta_max];
        processed_img = new int[img.getHeight()][img.getWidth()];
        BufferedImage edge_img = new EdgeDetector(img).detectEdges();
        for (int i=0; i<edge_img.getHeight(); i++){
            for (int j=0; j<edge_img.getWidth(); j++){
                processed_img[i][j] = edge_img.getRGB(j, i);
            }
        }
        detectLines();
        findMaxes(space);
        drawDetectedLines(processed_img);
    }
    
    private void detectLines(){
        for (int y=0; y<processed_img.length; y++){
            for (int x=0; x<processed_img[0].length; x++){
                if (processed_img[y][x] != BLACK){
                    vote(x, y);
                }
            }
        }
    }
    
    private void vote(int x, int y){
        double r;
        double theta;
        for (theta=0; theta<space[0].length; theta++){
            try{
                r = x * cos(theta) + y * sin(theta);
                space[(int)r][(int)theta]++;
            }catch(Exception ex){
                
            }
        }
    }
    
    private double sin(double n){
        return Math.sin(n);
    }  
    
    private double cos(double n){
        return Math.cos(n);
    }

    private void drawDetectedLines(int[][] m){
        for (int y=0; y<m.length; y++){
            for (int x=0; x<m[0].length; x++){
                if (m[y][x] == RED){
                    try{
                        img.setRGB(x, y, RED);
                    }catch (Exception ex){

                    }
                }
            }
        }
        JFrame f = new JFrame("Hough Transform (theta max: " + space[0].length + ")");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(new JLabel(new ImageIcon(img)));
        f.pack();
        f.setVisible(true);
    }
    
    private void drawLine(int[][] matrix, int[] param){
        double m = -cos(param[0])/sin(param[0]);
        double q = param[1]/sin(param[0]);
        for (double x=0; x<matrix[0].length; x+=0.1){
            try{
                int y = (int)(m*x + q);
                matrix[y][(int)x] = RED;
            }catch (Exception ex){
            
            }
        }
    }
    
    private void findMaxes(int[][] m){
        for (int r=0; r<m.length; r++){
            for (int theta=0; theta<m[0].length; theta++){
                if (isMax(m, theta, r)){
                    drawLine(processed_img, new int[]{theta, r});
                }
            }
        }
    }
    
    private boolean isMax(int[][] m, int theta, int r){
        int k= (int)(space.length/6.5);
        for (int i=r-k; i<r+k; i++){
            for (int j=theta-k; j<theta+k; j++){
                try{
                    if (m[i][j] > m[r][theta]){
                        return false;
                    }
                }catch (Exception ex){
                    
                }
            }
        }
        return true;
    }
    
}