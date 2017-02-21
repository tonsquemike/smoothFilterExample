package smoothfilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author RENE
 */
public class ManejoER {
    
  Pattern p = null;
  Matcher m = null;
  
  ManejoER(String ER){
   p = Pattern.compile(ER);
  }
  
  public boolean ExistER (String line){
    m = p.matcher(line);
    if (m.find()){
       return (true);
       }    
    return (false);    
  }
  
  public String Grupo (int Grupo){
   if (0<=Grupo &&  Grupo <= m.groupCount()){
      return (m.group(Grupo));
    }
    return ("");
  }


}
