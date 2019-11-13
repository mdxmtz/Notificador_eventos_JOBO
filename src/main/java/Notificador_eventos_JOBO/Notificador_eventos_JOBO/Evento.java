package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

public class Evento {
    private int ID = 0;
    private String Nombre = "";
    private String Lugar = "";
    private String Fecha = "";
    private boolean Agotado = false;

    public Evento(int ID, String nombre, String lugar, String fecha, Boolean agotado){
        setID(ID);
        setNombre(nombre);
        setLugar(lugar);
        setFecha(fecha);
        setAgotado(agotado);
    }

    public void showEvento(){
        System.out.println("ID: " + ID);
        System.out.println("Titulo: " + Nombre);
        System.out.println("Fecha: " + Fecha);
        System.out.println("Lugar: " + Lugar);
        System.out.println("Agotado: " + Agotado);
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

    public Boolean getAgotado(){
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

    public void setAgotado(Boolean agotado){
        Agotado = agotado;
    }
}
