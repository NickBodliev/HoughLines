package houghlines;

import java.awt.image.BufferedImage;

public class EdgeDetector {
    
    BufferedImage img;
    
    public EdgeDetector(BufferedImage img_){
        img = toGrayScale(img_);
    }
    
    private BufferedImage toGrayScale(BufferedImage img_){
        BufferedImage gray_img = new BufferedImage(img_.getWidth(), img_.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y=0; y<img_.getHeight(); y++){
            for (int x=0; x<img_.getWidth(); x++){
                gray_img.setRGB(x, y, img_.getRGB(x, y));
            }
        }
        return gray_img;
    }
    
    public BufferedImage detectEdges(){
        return img;
    }
    
}