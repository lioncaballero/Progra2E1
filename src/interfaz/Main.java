package interfaz;

public class Main {
    public static void main(String[] args){
        ConsolaInventario app= new ConsolaInventario();
        if(args.length >0 && args[0].equalsIgnoreCase("--test")) {
            app.ejecutarSuitePruebas();
            return;
        }
        app.iniciar();
    }
}
