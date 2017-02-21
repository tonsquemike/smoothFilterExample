package smoothfilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.*;

/**
 * Permite la lectura de argumentos pasados por linea de comandos o por lectura de
 * archivo. Además permite buscar y recuperar cadenas. 
 * @author RENE
 */

public class MyListArgs {
 
    Map  <String , String> Args     = null;
    List <String>          OLineas  = null; //para hacer el doble mapeo de Líneas a argumentos linea=>Key=>Valor
    ManejoER               ER_Keys  = null;
    ManejoER               ER_Comen = null;
    
    
/**
 * Construye un Lista de cadenas a partir de un arreglo de cadenas
 * @param Strings Arreglo de cadenas
 */
         MyListArgs(String []Strings){
 
 Args     = new HashMap <String , String>();
 OLineas  = new LinkedList <String>();
 ER_Keys  = new ManejoER("^\\s*(-\\w+[\\p{Print}&&[^\\s]]*)\\s+(\"(.+)\"\\s*$|(.+))");
 ER_Comen = new ManejoER("^\\s*#(.*)");
 
 for (int i=1; i<Strings.length ;i+=2){
     Args.put(Strings[i-1].toUpperCase(), Strings[i]);
   }//del for
 
}//del constructor
 
/**
 * El objetivo de esta función es hacer una reconstrucción del archivo de parámetros
 * de acuerdo con la estructura del archivo orignal, pero considerando los valores
 * actuales del mapa, además que es posible que se agreguen nuevas claves.
 * @param FileName Nuevo nombre del archivo
 * @return true  si se pudo hacer la operación, en otro caso, false
 */         
         
boolean  SaveToFile(String FileName){
 ManejoArchivos IO = new ManejoArchivos();
 IO.Open_Write_File(FileName);
 String Key="";
 List <String> Tempo = new LinkedList<String>();
 
 for (String Linea : OLineas) { //hacerlo para todas las líneas del archivo original
 
     if (ER_Keys.ExistER(Linea)){//si la linea es una key
         Key = ER_Keys.Grupo(1); 
         if (Args.containsKey(Key)){ //y si el mapa contiene esa key, entonces escribirla en el archivo con su valor respectivo
             IO.Write_in_File( Key + ((Key.length()<8)? "\t\t" : "\t") + Args.get(Key));
             Tempo.add(Key); //guardarla para despues verificar si guardaron todas
             }//del if
     }else if (ER_Comen.ExistER(Linea))//de lo contrario, si es un comentario
         IO.Write_in_File(Linea);    
     
     //ahora revisar cuales se van a agregar
     
     Set <String> Claves = Args.keySet();
     for (String Clave : Claves) {
         if (!Tempo.contains(Clave)) //si existe una clave no agregada previamente
             IO.Write_in_File( Clave + ((Clave.length()<8)? "\t\t" : "\t") + Args.get(Clave));
         }//del for
          
    }//del if
IO.Close_Write_File();
return (true);
}//de la función
 
 
        
         
         
/**
 * Se agrega al arreglo el par Argumento Valor, si ya esta el argumento entonces
 * no se agrega nada y se regresa el Valor que estaba
 *
 * @param Arg    Parámetro a agregar
 * @param Value  Valor a agregar
 * @return       Valor que corresponde a ese parámetro
 */
String   AddIFNOTArgValue (String Arg, String Value){    
  if (!Args.containsKey(Arg)) //si no contiene esa clave
      Args.put(Arg, Value);   //agrega el par key-valor
  
  return (Args.get(Arg)); //regresa su valor
}

/**
 * Si el argumento Arg no está se agrega al arreglo el par Argumento Valor,
 * de lo contrario se cambia el valor que tenía por Value
 * @param Arg   Parámetro a cambiar o agregar
 * @param Value Valor a cambiar o agregar
 */
void     ChangeOrAdd(String Arg, String Value){
  
  Args.put(Arg, Value);
   
}

/**
 * Lee los parámetrso que se pasaron mediante un archivo
 * @param FileName Nombre del archivo con los parámetros
 * @return TRUE: si se pudo cargar el archivo
 */
boolean  AddArgsFromFile (String FileName ){
 ManejoArchivos IO = new ManejoArchivos();
 String    Texto   = IO.Reads_Text_File(FileName);
 
 String [] Lineas  = IO.Read_Text_File(FileName);
 
 
 for (int i = 0; i <  Lineas.length; i++) {
     OLineas.add(Lineas[i]);
     if (ER_Keys.ExistER(Lineas[i])) //entonces se agregaría
         Args.put(ER_Keys.Grupo(1), ER_Keys.Grupo(3)!=null? ER_Keys.Grupo(3) : ER_Keys.Grupo(4).trim());        
     
     }//del for

   /* Set <String> ok= Args.keySet();
    for (String string : ok) {
        System.out.println("="+string);   
    }*/
    
return (true);
}

/**
 * Revisa si existe la clave
 * @param Label cadena que se va a buscar si existe en el arreglo
 * @return true si existe la cadena Label en el arreglo, falso en otro caso
 */
boolean  Exists( String Label){
   return( Args.containsKey(Label) );
}
/**
 * Se utiliza para leer parámetros de tipo String, primero se busca la cadena en la lista de cadenas y se regresa la siguiente cadena como STRING
 * @param Label párametro a leer
 * @param Default Valor por default que se regresa si no se encuentra Label en el arreglo
 * @return el valor del parámetro como una cadena
 */
String   ValueArgsAsString( String Label , String Default){  
   return( Args.containsKey(Label) ? Args.get(Label) : Default );
}
/**
 * Se utiliza para leer parámetros de tipo int, primero se busca la cadena
 * en la lista de cadenas y se regresa la siguiente cadena como int
 * @param Label párametro a leer
 * @param Default Valor por default que se regresa si no se encuentra Label en el arreglo
 * @return el valor del parámetro como un entero (int)
 */
int      ValueArgsAsInteger( String Label, int Default ){
   return( Args.containsKey(Label) ? Integer.valueOf(Args.get(Label)) : Default );
}
/**
 * Se utiliza para leer parámetros de tipo float, primero se busca la cadena
 * en la lista de cadenas y se regresa la siguiente cadena como float
 * @param Label párametro a leer
 * @param Default Valor por default que se regresa si no se encuentra Label en el arreglo
 * @return el valor del parámetro como un real (float)
 */
float    ValueArgsAsFloat( String Label, float Default ){
   return( Args.containsKey(Label) ? Float.valueOf(Args.get(Label)) : Default );
}
/**
 * Se utiliza para leer parámetros de tipo boolean, primero se busca la cadena
 * en la lista de cadenas y se regresa la siguiente cadena como boolean
 * @param Label párametro a leer
 * @param Default Valor por default que se regresa si no se encuentra Label en el arreglo
 * @return el valor del parámetro como un boolean
 */
boolean  ValueArgsAsBoolean( String Label, boolean Default ){
   return( Args.containsKey(Label) ? Args.get(Label).equalsIgnoreCase("true") : Default );
}

/**
 * Se utiliza para leer parámetros de tipo Calendar, primero se busca la cadena
 * en la lista de cadenas y se regresa la siguiente cadena como Calendar. Puesto
 * que en nuestro sistema el mes 1 corresponde a ENERO entonces a nivel de
 * codificación se le resta 1 al mes para coincidir con la fecha de Calendar.
 * @param Label parámetro a leer
 * @param Default Valor por defaul en caso de no encontrarse la fecha
 * @return la fecha en formato Calendar, en caso de no hacer regresa null
 */
Calendar ValueArgsAsDate ( String Label, String Default ){
Calendar Fecha  = null;
ManejoER Format = new ManejoER("(\\d{2,4})/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/([01]?[0-9]|[2][0-3])/([0-5][0-9])");//(\\d{2,4})/(\\d\\d)/(\\d\\d)/(\\d\\d)/(\\d\\d)");
String Date = Args.containsKey(Label) ? Args.get(Label) : Default ;

if ( Format.ExistER(Date) ){
     Fecha = Calendar.getInstance();
     Fecha.set(Integer.valueOf(Format.Grupo(1)), Integer.valueOf(Format.Grupo(2))-1,
               Integer.valueOf(Format.Grupo(3)), Integer.valueOf(Format.Grupo(4)),
               Integer.valueOf(Format.Grupo(5)));
    }

// System.out.println( Fecha.get(Calendar.YEAR)+"/"+ Fecha.get(Calendar.MONTH)+"/"+
//                     Fecha.get(Calendar.DAY_OF_MONTH)+"/"+Fecha.get(Calendar.HOUR_OF_DAY)+"/"+
//                     Fecha.get(Calendar.MINUTE));
return(Fecha);
}



}