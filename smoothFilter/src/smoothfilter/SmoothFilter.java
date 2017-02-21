/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smoothfilter;

import Funciones.Funciones_Image;
import Funciones.Img;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Miguel
 */
public class SmoothFilter {
    static float[] pixeles;
    /**
     * @param args the command line arguments
     */
    SmoothFilter( String[] Args ) throws IOException{
        String ConfigFile, Archivo, DIR;
        MyListArgs Param;
        BufferedImage image;       
        boolean formato = false;
        Param = new MyListArgs(Args);
        ConfigFile = Param.ValueArgsAsString("-CONFIG", "");
        
        if (!ConfigFile.equals("")) {
            Param.AddArgsFromFile(ConfigFile);
        }//fin if
        String Sintaxis = "-IMG:str -DIR:str";
        MySintaxis Review = new MySintaxis(Sintaxis, Param);
        //PARAMETROS FORZOSOS
        DIR  = Param.ValueArgsAsString("-DIR", "");
    
        Archivo = Param.ValueArgsAsString("-IMG", "");
        if (Archivo.endsWith(".img")|Archivo.endsWith(".IMG"))
            image = Img.abreIMG(Archivo);
        else
            image = ImageIO.read(new File(Archivo));              
        
        BufferedImage salida = Filtro(image);
        if (DIR.endsWith(".img") || DIR.endsWith(".IMG")) {
            formato = true;
        } else if (DIR.endsWith(".jpg") || DIR.endsWith(".JPG") || DIR.endsWith(".TIFF")
                || DIR.endsWith(".tiff") || DIR.endsWith(".gif") || DIR.endsWith(".GIF")
                || DIR.endsWith(".BMP") || DIR.endsWith(".bmp") || DIR.endsWith(".png")
                || DIR.endsWith(".PNG") || DIR.endsWith(".WBMP") || DIR.endsWith(".wbmp")) {
            formato = false;
        }
        if (formato == true) {
            pixeles = Funciones_Image.BufferAVector(salida);
            IOBinFile.WriteBinFloatFileIEEE754(DIR, pixeles);
        } else if (formato == false) {
            String extension = Funciones_Image.VerificaFormato(DIR);
            Funciones_Image.ImagenAFormato(salida, DIR, extension);
        }
    }
    private BufferedImage Filtro( BufferedImage img ){
        Color c;
        int r = 0, g = 0, b = 0;
        boolean bndOrilla = false;        
        
        for( int x = 0; x < img.getWidth(); x++)
        {
            for( int y = 0; y < img.getHeight(); y++) {    
                for(int i = x-1; i<=x+1;i++)
                    for(int j = y-1; j<=y+1; j++){//Aplicación del filtro
                        try{
                            c = new Color(img.getRGB(i, j));
                            r+=c.getRed();
                            g+=c.getGreen();
                            b+=c.getBlue();
                        }catch(Exception e){//en caso de que las coordenadas ubiquen esten en un pixel que esta en la orilla de la imagen
                            i++;            //solo se tomaran 4 pixeles para aplicar el filtro
                            j++;
                            bndOrilla = true;
                        }
                    }
                if( bndOrilla == true )
                {
                    try{img.setRGB(x, y,new Color((int)(r/4),(int)(g/4),(int)(b/4)).getRGB());//aplica el fltro a las orillas de la imagen
                    }catch(Exception e){}
                    
                }
                else
                {
                    try{img.setRGB(x, y,new Color((int)(r/9),(int)(g/9),(int)(b/9)).getRGB());//aplicación del filtro de 3x3
                    }catch(Exception e){}
                  
                }
                
                r   =   0;
                g   =   0;
                b   =   0;
                bndOrilla = false;               
                }
             
        }
        return img;
    }
}
