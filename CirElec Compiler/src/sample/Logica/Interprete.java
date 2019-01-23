package sample.Logica;

import javafx.scene.control.TextArea;
import sample.Constants.TiposToken;

public class Interprete {
    TextArea txtconsola;
    public Interprete (String renglones, TextArea con){
        this.txtconsola=con;
        compilar(renglones);
    }
    private void compilar(String renglones){
        String[] r=renglones.split("\\n");
        for (int x = 0; x < r.length; x++) {
            if (r[x].contains("print")){
                int x1=r[x].indexOf("[");
                int x2=r[x].indexOf("]");
                String texto=r[x].substring(x1+2,x2-1);
                this.txtconsola.setText(txtconsola.getText()+"\n"+texto);
            }
            if (r[x].contains("create")){
                String[] td=r[x].split(" ");
                if (td[2].trim().equals(TiposToken.RESISTANCE)){

                }else  if(td[2].trim().equals(TiposToken.POWERSOURCE)){

                }else{
                    txtconsola.setText(txtconsola.getText()+"\n"+"El tipo de dato "+td[x]+" no existe");
                }
            }
        }//llave for
    }
    public boolean ifExists(Token t){
        boolean exist=false;
        for (int x = 0; x <TiposToken.variables.size() && exist==false ; x++) {
            if (t.getName().equals(TiposToken.variables.get(x).getName())){
                exist=true;
            }

        }
        return exist;
    }
}
