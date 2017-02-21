package Funciones;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @since 06/05/2012
 * @version 1 @ultima revision 06/05/2012
 */
public class Funciones_Image {

    /**
     * Trasforma un bufferedImage en una imagen
     *
     * @param buffer buffer de entrada que contiene una imagen
     * @param nombreImagen nombre de la imagen de salida
     * @param formato formato para la imagen de salida
     */
    public static void ImagenAFormato(BufferedImage buffer, String nombreImagen, String formato) {
        File outputFile = new File(nombreImagen);
        try {
            ImageIO.write(buffer, formato, outputFile);
        } catch (IOException ex) {
        }
    }

    /**
     * Trasforma un bufferedImage en un vector
     *
     * @param buffer buffer de entrada que contiene una imagen
     */
    public static BufferedImage VetorBuffer(int alto, int ancho, int[] axu) {
        BufferedImage image2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_INDEXED);
        int color;
        int pod = 0;
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                color = axu[pod];
                image2.setRGB(i, j, color);
                pod++;
            }
        }
        BufferedImage bg = new BufferedImage(ancho, alto, BufferedImage.OPAQUE);
        ((Graphics2D) bg.getGraphics()).drawImage(image2, 0, 0, null);
        return bg;
    }

    public static float[] BufferAVector(BufferedImage d) {
        int pos = 4;
        float vector[] = new float[d.getWidth() * d.getHeight() + 4];
        vector[0] = 256;
        vector[1] = 0;
        vector[2] = d.getWidth();
        vector[3] = d.getHeight();
        for (int a = 0; a < d.getWidth(); a++) {
            for (int gg = 0; gg < d.getHeight(); gg++) {
                vector[pos] = d.getRGB(a, gg);
                pos++;

            }
        }
        return vector;
    }

    public static int[] flotanteInt(float[] r) {
        int[] axu = new int[r.length - 4];
        for (int i = 4, x = 0; i < r[2] * r[3]; i++, x++) {
            int valor;
            valor = (int) r[i];
            axu[x] = valor;
            System.out.print(" " + r[x] + " ");
        }
        return axu;
    }

    public static String VerificaFormato(String NAMEIS) {
        String formato = null;
        if (NAMEIS.endsWith(".jpg") || NAMEIS.endsWith(".JPG")) {
            formato = "jpg";
        } else if (NAMEIS.endsWith(".tiff") || NAMEIS.endsWith(".TIFF")) {
            formato = "tiff";
        } else if (NAMEIS.endsWith(".gif") || NAMEIS.endsWith(".GIF")) {
            formato = "gif";
        } else if (NAMEIS.endsWith(".bmp") || NAMEIS.endsWith(".BMP")) {
            formato = "bmp";
        } else if (NAMEIS.endsWith(".png") || NAMEIS.endsWith(".PNG")) {
            formato = "png";
        } else if (NAMEIS.endsWith(".ico") || NAMEIS.endsWith(".ICO")) {
            formato = "ico";
        } else if (NAMEIS.endsWith(".wbmp") || NAMEIS.endsWith(".WBMP")) {
            formato = "wbmp";
        } else if (NAMEIS.endsWith(".img") || NAMEIS.endsWith(".IMG")) {
            formato = "img";
        }
        return formato;
    }
}
