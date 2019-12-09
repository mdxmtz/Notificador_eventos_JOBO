package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

public class Node {
    public Node parent;
    public Node right;
    public Node left;
    private Evento evento;
    public int key;

    public Node(Evento e){
        evento = e;
        key = evento.getID();
        right = null;
        left = null;
    }

    public Evento getEvento(){
        return evento;
    }
}
