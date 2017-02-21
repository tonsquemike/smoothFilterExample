package smoothfilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.*;

/**
 * Proporciona métodos para el manejo de archivos de texto tanto para lectura como
 * para escritura, también se proporcionan métodos para el manejo de carpetas y de
 * sus archivos.
 * @author RENE
 */
public class ManejoArchivos {
/**
 * Es un buffer de escritura de archivo que permite crear un archivo de escritura
 * al cual se le puede ir agregando lineas mediante otra función, cuando ya no
 * se quieran mas lineas es necesario cerrar el bufer de salida
 * @see     #Open_Write_File(java.lang.String)
 * @see     #Write_in_File(java.lang.String)
 * @see     #Close_Write_File() 
 */
BufferedWriter BR = null;
FileWriter     FR = null;

/** Crea un archivo de texto en el cual se escribe un arreglo de cadenas al final del
 * archivo, si el archivo no existe lo crea
 * @param FileName Nombre del archivo
 * @param  Contenido arreglo de lineas a escribir
 * @return TRUE si se puedo crear el archivo junto con su contenido
*/
  public boolean Write_File_Add (String FileName, String []Contenido){
   File ar           = null;
   FileWriter fr     = null;
   BufferedWriter br = null;

     try{
       ar = new File          (FileName);
       fr = new FileWriter    (ar,true);
       br = new BufferedWriter(fr);

       for (int i=0; i < Contenido.length-1 ;i++){
         br.write(Contenido[i]+"\r\n");
         }
       if (Contenido.length >= 1)
       br.write(Contenido[Contenido.length-1]);

       br.close();
       fr.close();
       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);

   }
/** Crea un archivo de texto en el cual se escribe una solo cadena al final del mismo,
 * si el archivo no existe lo crea
 * @param FileName Nombre del archivo
 * @param  Contenido arreglo de lineas a escribir
 * @return TRUE si se puedo crear el archivo junto con su contenido
*/
  public boolean Write_String_File_Add (String FileName, String Contenido){
   File ar           = null;
   FileWriter fr     = null;
   BufferedWriter br = null;

     try{
       ar = new File          (FileName);
       fr = new FileWriter    (ar,true);
       br = new BufferedWriter(fr);
       br.write(Contenido);
       br.close();
       fr.close();
       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);
   }


/**
 * Crea una carpeta y no avanza hasta que crea la carpeta
 * @param Path
 */
public void UntilCrearCarpetas(String Path){
 File Dir = new File(Path);

 if (!Dir.exists())

 while (!Dir.mkdirs())
      ;

}

public boolean CrearCarpetas(String Path){
  File Dir = new File(Path);

 if (!Dir.exists())
   if (!Dir.mkdirs()){
       System.out.println("No se pudo crear la subcarpeta:"+Path);
       return(false);
     }
 return(true);
}

/**
 * Permite saber si existe ya sea un archivo o una ruta especificada
 * @param Path  Ruta o archivo del que se desea saber si existe
 * @return TRUE si existe el Path como ruta o como archivo
 */
public boolean ExisteCarpetaArchivo (String Path){
 File Dir = new File(Path);
    if (!Dir.exists())        //si no existe regresa false
         return(false);

return (true);
}

/**
 * Renombra una carpeta con un nuevo nombre
 * @param OldName Nombre con la carpeta origen
 * @param NewName Nombre que se le va asignar a la carpeta origen
 * @return TRUE si se pudo llevar con exito la operación
 */
public boolean RenamePath(String OldName, String NewName){
    File Dir = new File(OldName);
    if (!Dir.exists()){
         System.out.println("RenamePath: no existe la carpeta fuente: "         + OldName);
         return(false);
     }
    if (!Dir.isDirectory()){
         System.out.println("RenamePath: la ruta especificada no es una carpeta: " + OldName);
         return(false);
     }

    File NewDir = new File(NewName);
    if (NewDir.exists()){
         System.out.println("RenamePath: no se pudo renombrar la carpeta origen :\n" + OldName
        + "\ncon el nuevo nombre:\n" + NewName + "\nporque ya existe una con el nuevo nombre");
         return(false);
     }

    if (!Dir.renameTo(NewDir)){
         System.out.println("RenamePath: no se pudo renombrar la carpeta:" + OldName + " a \n" + NewName);
         return (false);
    }
return (true);
}

/**
 * Agrega un nombre de una carpeta o de un archivo a una ruta
 * @param Root
 * @param FilePath
 * @return La ruta Root a la que se le agregó el archivo o ruta FilePath
 */
  public String AddToPath (String Root, String FilePath){

  return ( (Root.endsWith(File.separator)) ? Root+FilePath : Root+File.separator+FilePath );

}
/**
 * Se encarga de copiar un archivo a una ruta especificada
 * @param fromFileName Archivo fuente a copiar
 * @param toPathName Ruta donde se va copiar el archivo
 * @return TRUE si se copió el archivo
 */
  public boolean CopyFile(String fromFileName, String toPathName) {
    File fromFile = new File(fromFileName);
    if (!fromFile.exists()){
         System.out.println("Copy: no existe el archivo fuente: "            + fromFileName);
         return(false);
     }
    if (fromFile.isDirectory()){
         System.out.println("Copy: el archivo especificado es una carpeta: " + fromFileName);
         return(false);
     }
    if (!fromFile.canRead()) {
         System.out.println("Copy: el archivo fuente no se puede leer: "     + fromFileName);
         return(false);
     }

    File toPath = new File(toPathName);

    if (!toPath.exists())
       if (!toPath.mkdirs()) { //crea las subcarpetas necesarias
           System.out.println("Copy: no se pudieron crear la(s) carpeta(s) de salida:" + toPath);
           return (false);
           }

    String toFileName    = AddToPath(toPathName, fromFile.getName());
    File toFile          = new File(toFileName);
    FileInputStream from = null;
    FileOutputStream to  = null;
    try {
      from = new FileInputStream (fromFile);
      to   = new FileOutputStream(toFile);
      byte[] buffer = new byte[4096];
      int bytesRead;

      while ((bytesRead = from.read(buffer)) != -1)
        to.write(buffer, 0, bytesRead); // write

     } catch (IOException e){//Catch exception if any
         System.err.println("Error: " + e.getMessage());
         }
      finally {
         if (from != null)
          try {
              from.close();
             } catch (IOException e) { ; }
         if (to != null)
          try {
             to.close();
             } catch (IOException e) { ; }
         }
      return (true);
  }

  /**
   * Copia la lista de arhivos (especificados con su ruta absoluta) a la ruta toPathName
   * @param fromFileName Arreglo con la lista de archivos para copiar (especificados con su ruta absoluta)
   * @param toPathName Ruta destino donde se van a copiar los archivos
   * @return TRUe si se copiaron todos los archivos
   */
  public boolean CopyFiles(String []fromFileName, String toPathName){
    for (int i = 0; i < fromFileName.length; i++) {
       if (!CopyFile(fromFileName[i],toPathName))
           return false;
      }
  return (true);
  }
/**
 * Cuando se llama esta función el sistema no avanza hasta que
 * se borra la carpeta
 * @param Dir carpeta a borrar
 */
  public void UntilDelCarpeta(String Dir){

   while (!DelCarpeta(Dir))
          ;

  }

/** Esta función borra un directorio y todos sus archivos y subcarpetas, sin confirmar.
 * @param Dir Directorio que se quiere borrar.
 * @return Si TRUE si se borró la carpeta Dir, en caso contrario FALSE
 */
  public boolean DelCarpeta (String Dir){
   File Ruta = new File(Dir);
   //no se revisa si el directorio existe pues el objetivo es que no exista y sí así es entonces se cumple el objetivo
   if (!Ruta.isDirectory()){
          System.out.println("ERROR: No es un directorio <"+ Dir +">");
       return (false);
      }

   File []SubCarpetas = Ruta.listFiles();

   for (int i = 0 ; i < SubCarpetas.length ; i++){
      if (SubCarpetas[i].isDirectory()) {
           if (!DelCarpeta(SubCarpetas[i].getAbsolutePath()))
              return (false);
         }//del if
      else{
           if (!SubCarpetas[i].delete()){ //borra los archivos
              //System.out.println("no se puede borrar el archivo :"+SubCarpetas[i].getName() );
              return (false);
              }
         }//del lese
     }

   return (Ruta.delete()); //borra la subcarpeta
}
/**
 * Lee un archivo y regresa una arreglo con las lineas de ese archivo
 * @param FileName Este parámetro se refiere el nombre del archivo que se desea abrir para lectura
 * @return regresa un arreglo con las lineas del archivo leido
*/
  public String [] Read_Text_File(String FileName){
   File archivo      = null; //@var esta variable se utiliza para la descripcion del archivo
   FileReader fr     = null; //@var archivo de lectura
   FileReader fn     = null;
   BufferedReader br = null;
   BufferedReader bn = null;

     try{
       archivo = new File  (FileName);
       fr = new FileReader (archivo);
       br = new BufferedReader(fr);
       String linea;
       int i=0;
       while((linea=br.readLine())!=null)
            i++;

       fr.close();
       br.close();

       fn = new FileReader (archivo);
       bn = new BufferedReader(fn);
       String []lineas   = new String[i];
       i=0;
       while((linea=bn.readLine())!=null)
        lineas[i++]=linea;
       fn.close();
       bn.close();
       return (lineas);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(null);
   }//fin de la funcion read_file
/**
 * Lee un archivo de texto y regresa su salida en una sola línea
 * @param FileName Este parámetro se refiere el nombre del archivo que se desea abrir para lectura
 * @return regresa un arreglo con las lineas del archivo leido
*/
  public String  Reads_Text_File (String FileName){
   File archivo      = null; //@var esta variable se utiliza para la descripcion del archivo
   FileReader     fr = null; //@var archivo de lectura
   BufferedReader br = null;

     try{
       archivo        = new File          (FileName);
       fr             = new FileReader    (archivo);
       br             = new BufferedReader(fr);
       String linea   = "";
       int    i       = 0;
       char []MiBufer = new char [1024];

       while((i=br.read(MiBufer)) != -1)
          linea = linea + String.copyValueOf(MiBufer, 0, i);
            

       fr.close();
       br.close();
       return (linea);
     }catch(Exception e){
         e.printStackTrace();
      }
   return("");
   }//fin de funcion
/** Crea un archivo de texto en el cual se escribe un arreglo de cadenas
 * @param FileName Nombre del archivo
 * @param  Contenido arreglo de lineas a escribir
 * @return TRUE si se puedo crear el archivo junto con su contenido
*/
  public boolean Write_File (String FileName, String []Contenido){
   File ar           = null;
   FileWriter fr     = null;
   BufferedWriter br = null;

     try{
       ar = new File          (FileName);
       fr = new FileWriter    (ar);
       br = new BufferedWriter(fr);

       for (int i=0; i < Contenido.length-1 ;i++){
         br.write(Contenido[i]+"\r\n");
         }
       if (Contenido.length >= 1)
       br.write(Contenido[Contenido.length-1]);

       br.close();
       fr.close();
       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);

   }

  /** Crea un archivo de texto en el cual se escribe un arreglo de cadenas
 * @param FileName Nombre del archivo
 * @param  Contenido arreglo de lineas a escribir
 * @return TRUE si se puedo crear el archivo junto con su contenido
*/
  public boolean Write_String_File (String FileName, String Contenido){
   File ar           = null;
   FileWriter fr     = null;
   BufferedWriter br = null;

     try{
       ar = new File          (FileName);
       fr = new FileWriter    (ar);
       br = new BufferedWriter(fr);
       br.write(Contenido);
       br.close();
       fr.close();
       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);
   }

  /**
   * Abre un archivo para escritura pero no escribe nada, solo deja abierto el buffer de salida
   * @param FileName Nombre del archivo
   * @return TRUE: si se pudo crear el archivo
   * @see    #Write_in_File(java.lang.String)
   * @see    #Close_Write_File()
   */
  public boolean Open_Write_File (String FileName){
   File ar           = null;

   if (FR!=null && BR!=null)
       return(false);

     try{
       ar = new File          (FileName);
       FR = new FileWriter    (ar);
       BR = new BufferedWriter(FR);

       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);
}
  /**
   * Una vez abierto un archivo para escritura con Open_Write_File se puede agregar
   * lineas de texto al archivo.
   * @param linea texto que se va agregando al archivo
   * @return TRUE si se pudo agregar la línea de texto, FALSO en caso contrario
   * @see    #Open_Write_File(java.lang.String)
   * @see    #Close_Write_File()
   */

  public boolean Write_in_File   (String linea){
  
       if (BR != null){
           try{
             BR.write(linea+"\r\n");
             return (true);

           }catch(Exception e){
            e.printStackTrace();
           }
           
           }//del if
       else {
           System.out.println(" Error Write_in_File: se está intentando escribir en un arcvivo que no se ha abierto");
           System.exit(0);
           return(false);
           }
       
   return(false);
   }

  /**
   * Cierra el archivo que se abrió previamente con Open_Write_File
   * @return TRUE: si se pudo cerrar el archivo, FALSE en otro caso
   * @see    #Open_Write_File(java.lang.String)
   * @see    #Write_in_File(java.lang.String)
   */
  public boolean Close_Write_File (){

  try{
      if (BR != null)
          { BR.close();
            BR = null;
          }
      if (FR != null)
          { FR.close();
            FR = null;
          }

       return (true);
     }catch(Exception e){
         e.printStackTrace();
      }
   return(false);
}

///**
// * Extrae todos los archivos con su ruta absoluta que cumplen con el filtro Ext
// * @param Dir Carpeta donde se empleará la busqueda
// * @return Arreglo con todos los archivos que cumplen con el filtro Ext
// */
// public String [] List_all_Files (String Dir, String Ext){
//
//  }

}
/**
 * @see no se para que sirve
 * @param String FileName Este parámetro se refiere el nombre del archivo que se desea abrir para lectura
 * @return regresa un arreglo con las lineas del archivo leido
*/
