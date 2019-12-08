package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

public class Evento{// implements Comparable<Evento>{
    private int ID = 0;
    private String Nombre = "";
    private String Lugar = "";
    private String Fecha = "";
    private int Agotado = 0;

    public Evento(int ID, String nombre, String lugar, String fecha, int agotado){
        setID(ID);
        setNombre(nombre);
        setLugar(lugar);
        setFecha(fecha);
        setAgotado(agotado);
    }

    public void showEvento(){
        System.out.println("ID: " + getID());
        System.out.println("Titulo: " + getNombre());
        System.out.println("Fecha: " + getFecha());
        System.out.println("Lugar: " + getLugar());
        System.out.println("Agotado: " + getAgotado());
    }

    public int getID(){
        return ID;
    }

    public String getNombre(){
        return Nombre;
    }

    public String getLugar(){
        return Lugar;
    }

    public String getFecha(){
        return Fecha;
    }

    public int getAgotado(){
        return Agotado;
    }

    public void setID(int id){
        ID = id;
    }

    public void setNombre(String nombre){
        Nombre = nombre;
    }

    public void setLugar(String lugar){
        Lugar = lugar;
    }

    public void setFecha(String fecha){
        Fecha = fecha;
    }

    public void setAgotado(int agotado){
        Agotado = agotado;
    }

    /*@Override
    public int compareTo(Evento e) {
        if(getID()<=e.getID())return 0;
        return 1;
    }*/

}
