package smoothfilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase permite revisar el nivel lexico y sintáctico de gramaticas del tipo,
 * TABICON. En el nivel léxico se revisa si las etiquetas escritas por el usuario
 * están de manera correcta, por úlitmo se revisa si estan escritas de acuerdo con
 * la gramatica para la aplicación.
 * @author Rn >--ö--<
 */
public class MySintaxis {
    
    MyListArgs Param;
    String       Tokens[];
    String       SubTok[];
    String       ReOrder  = ""; 
    String       NewPat;
    ManejoER     ER       = new ManejoER("(-\\w+)(:[\\p{Graph}]+)*");
    ManejoER     ER1      = new ManejoER("-\\w+:([\\p{Graph}]+)");
    
    /**
     * Constructor donde se hace practicamente todo el trabajo, la estrategia seguida es:
     * 1.- En la sintaxis se separan los elementos de asociación y el operador de disyunción, por espacios en blanco
     * 2.- De acuerdo a los elementos léxicos de la sintaxis se revisan cuales utiliza el usuario y su formato
     *      Desde aqui es posible que ocurran errores
     * 3.- Cada etiqueta de los elementos utilizados por el usuario son escritos en una nueva cadena
     * 4.- En la sintaxis original se cambian [ por ( y ] por )?
     * 
     * @param Sintaxis La gramatica tipo tabicon utilizada para la aplicación
     * @param Parametros Objeto con el mapa de los parámetros y sus valores leeidos previamente
     */
    
     MySintaxis( String Sintaxis, MyListArgs Parametros){ //
        
         String Error = "";
         Param        = Parametros;
         Sintaxis     = Sintaxis.replaceAll("(\\(|\\)|\\[|\\]|\\|)", " $1 ");
         Sintaxis     = Sintaxis.replaceAll("\\s+", " ");
         Tokens       = Sintaxis.trim().split(" ");
         NewPat       = Sintaxis.trim().replaceAll("\\[", "(").replaceAll("\\]", ")?");
         NewPat       = NewPat.replaceAll(" ", "\\\\s*" );//"(\\\\s*").replaceAll("\\) ", ")\\\\s*").replaceAll(" \\(", "\\\\s*(").replaceAll(" \\)", "\\\\s*)");    //.replaceAll(" ", "\\\\s*");
         
         //System.out.println("OldPat:" + Sintaxis);
         //System.out.println("Patron:" + NewPat);
         
         for (String Token : Tokens) { //hacerlo para cada uno de los elementos de la sintaxis
            
             if(Token.matches("(\\(|\\)|\\[|\\]|\\|)"))
              ;
             else if (ER.ExistER(Token)){
                  //System.out.println("Token["+Token+"] Grupo(1)["+ ER.Grupo(1) +"]");
                  if (Param.Exists(ER.Grupo(1))){
                      ReOrder = ReOrder + " " + Token.trim(); 
                      if (ER.Grupo(2)!=null){ //revisar el tipo de dato 
                          if(! (Error=RevisarTipoDato(ER.Grupo(1),ER.Grupo(2))).equals(""))
                             System.out.println(Error);         
                         }//del if
                      }//del if
                  //else System.out.println("No está la etiqueta:" + Token);
             }else 
                 System.out.println("Se desconoce el elemento:[" + Token +"]" );
              
         }//del for
         
         ReOrder = ReOrder.trim();
         
         try{
            if (!ReOrder.matches(NewPat)){
                System.out.println("OldPat[" + Sintaxis + "]\r\n"+ "NewPat[" + NewPat +"]\r\nBuscar en[" + ReOrder +"]");
                System.out.println("Error en los parámetros");
                System.exit(0);
                }
            
         } catch(Exception e){
                System.out.println("Error de Sintaxis: el sistema reporta " + e.getMessage());
                System.exit(0);
         }//del catch
         
         //System.out.println("Buscar en["+ ReOrder +"]");     
     }//de la función

     /**
      * Revisa si se especificó un tipo de dato para el parámetro y si el del
      * usuario cumple con dicho formato. También es posible definir un conjunto
      * de valores que solo pueden ocurrir, esto sucede cuando hay dos o más tipos
      * expresados para esa etiqueta
      *
      * @param Token Etiqueta correspondiente al tipo de dato
      * @param Tipo  Tipo de datos o conjunto de datos posibles separados por :
      * @return "" si  no se detectó ningún error, de contrario el error encontrado
      */
     private String RevisarTipoDato (String Token, String Tipo){   
       
       while (Tipo.startsWith(":")) //eliminar todos los : iniciales
              Tipo=Tipo.replaceFirst("^:", "");
         
       SubTok = Tipo.trim().split(":"); //dividirlo en cadenas separadas por :
         
       if ( SubTok.length>1 ) { //entonces hay subtipos determinados para ese parámetro
          Tipo = Param.Args.get(Token) ;
           
          for (String Tok : SubTok) {
              if (Tipo.equalsIgnoreCase(Tok))              
                  return ("");
          
           }//del for
         return ("Para la etiqueta ["+ Token +"] se encontró el valor [" + Tipo + "] y no concuerda con ninguno de los tipo predefinidos "+ Tipo);
       }else 
           
       Tipo = SubTok[0];
           
       if (Tipo.equalsIgnoreCase("str")){
         if (Param.Args.get(Token).equals(""))
            return ("Se esperaba una cadena para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");   
         
       }else if (Tipo.equalsIgnoreCase("int")){
         if (!Param.Args.get(Token).toLowerCase().matches("-?\\d+"))
            return("Se esperaba un entero para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");
         
       }else if (Tipo.equalsIgnoreCase("float")){
         if (Param.ValueArgsAsFloat(Token, Float.NaN)==Float.NaN)
            return("Se esperaba un flotante para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");   
         
       }else if (Tipo.equalsIgnoreCase("bool")){
         if (!Param.Args.get(Token).toLowerCase().matches("(true|false)")) 
            return("Se esperaba un booleano para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");                   
         
       }else if (Tipo.equalsIgnoreCase("date")){
         if (!Param.Args.get(Token).toLowerCase().matches("(\\d{2,4})/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/([01]?[0-9]|[2][0-3])/([0-5]?[0-9])")) 
            return("Se esperaba una fecha para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");                   
         
       }else if (Tipo.equalsIgnoreCase("char")){
         if (Param.Args.get(Token).length()!=1) 
            return("Se esperaba un caracter para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");                    
         
       }else if (Tipo.equalsIgnoreCase("hex")){
         if (!Param.Args.get(Token).toLowerCase().matches("[0-9a-f]+")) 
            return("Se esperaba un hexadecimal para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");                    
         
       }else if (Tipo.equalsIgnoreCase("byte")){
         if (!Param.Args.get(Token).toLowerCase().matches("([0-1]?[0-9]?[0-9])|(2[0-4][0-9])|(25[0-5])")) 
            return("Se esperaba un byte para la etiqueta [" + Token + "] pero se encontró [" + Param.Args.get(Token)+"]");
         
       }else
           return ("Tipo de dato desconocido:" + Tipo); 
       
      return ("");
     }//de la función
    
}
