package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

public class Evento {
    private String Nombre = "";
    private String Lugar = "";
    private String Fecha = "";
    private boolean Agotado = false;

    public Evento(String nombre, String lugar, String fecha, Boolean agotado){
        setNombre(nombre);
        setLugar(lugar);
        setFecha(fecha);
        setAgotado(agotado);

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
