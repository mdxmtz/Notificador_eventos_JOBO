package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

public class Tree {

    Node root;

    public Tree(){
        root = null;
    }

    public void insert(Evento evento){
        Node newNode = new Node(evento);
        if(root==null)root = newNode;
        else insert(newNode,root);
    }

    private void insert(Node newNode, Node node ){
        int key = newNode.key;
        if(key==node.key){
            System.out.println("Evento duplicado");
            node.getEvento().showEvento();
            return;
        }
        if(key<node.key){
            if(node.left==null){
                node.left = newNode;
                newNode.parent = node;
            }else insert(newNode, node.left);
        }else{
            if(node.right==null){
                node.right = newNode;
                newNode.parent = node;
            }else insert(newNode, node.right);
        }
    }

    public boolean exist(int key){
        return exist(key,root);
    }

    private boolean exist(int key, Node node){
        if(node == null)return false;
        if(key==node.key)return true;
        if(key<node.key)return exist(key,node.left);
        return exist(key,node.right);
    }
}
