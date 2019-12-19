package com.sazke.repuve.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import java.net.URL;

@RestController
@RequestMapping("/img")
public class ImageAnalizer {

    private static final String imageUrl = "http://www2.repuve.gob.mx:8080/ciudadania/jcaptcha";
    private static final String path = "/home/sazke/Documents/repuve/img.jpg";
    private int r,g,b;
    private Color color;
    int umbral = 127;//


    @GetMapping()
    public void getImage() throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(path);
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
        File pathToFile = new File(path);
       BufferedImage img = set_Blanco_y_Negro_con_Umbral(ImageIO.read(pathToFile));
       ImageIO.write(img,"jpg",pathToFile);
    }

    public BufferedImage set_Blanco_y_Negro_con_Umbral(BufferedImage f){
        BufferedImage bn = new BufferedImage(f.getWidth(),f.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        //se traspasan los colores Pixel a Pixel
        for(int i=0;i<f.getWidth();i++){
            for(int j=0;j<f.getHeight();j++){
                color = new Color(f.getRGB(i, j));
                //se extraen los valores RGB
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                //dependiendo del valor del umbral, se van separando los
                // valores RGB a 0 y 255
                r =(r>umbral)? 255: 0;
                g =(g>umbral)? 255: 0;
                b =(b>umbral)? 255: 0;
                bn.setRGB(i, j, new Color(r,g,b).getRGB());
            }
        }
        return bn;
    }


}
