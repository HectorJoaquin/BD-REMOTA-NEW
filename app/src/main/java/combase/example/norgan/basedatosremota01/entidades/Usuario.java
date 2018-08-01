package combase.example.norgan.basedatosremota01.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Usuario {


    private Integer documento;
    private String nombre;
    private String profesion;
    private  String dato;
    private Bitmap imagenId;


    public Usuario() {
    }

    public Usuario(Integer documento, String nombre, String profesion,String dato,Bitmap imagenId) {
        this.documento = documento;
        this.nombre = nombre;
        this.profesion = profesion;
        this.dato=dato;
        this.imagenId=imagenId;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {


            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            this.imagenId= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);


        }catch (Exception e){

            e.printStackTrace();
        }


    }

    public Bitmap getImagenId() {
        return imagenId;
    }

    public void setImagenId(Bitmap imagenId) {
        this.imagenId = imagenId;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }
}
