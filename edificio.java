import java.util.concurrent.*;
public class edificio {
/*
#################################################################################################
###  Esto imprime un edificio como Bienvenida, esto es cuando el usuario ejecute el programa  ###
###        y escoja el elevador.                                                              ###
#################################################################################################*/
    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> cola = new ConcurrentLinkedQueue<String>();
        System.out.println("================================");
        System.out.println("    Condominio los puntitos");
        System.out.println("================================");
        System.out.println("    ");
        System.err.println("    ");
        System.out.println("                    [ ]");
        System.err.println(" =========================");
        System.err.println("    |[   ]  N10  [   ]|");
        System.err.println("    |[   ]  N9   [   ]|");
        System.err.println("    |[   ]  N8   [   ]|");
        System.err.println("    |[   ]  N7   [   ]|");
        System.err.println("    |[   ]  N6   [   ]|");
        System.err.println("    |[   ]  N5   [   ]|");
        System.err.println("    |[   ]  N4   [   ]|");
        System.err.println("    |[   ]  N3   [   ]|");
        System.err.println("    |[   ]  N2   [   ]|");
        System.err.println("    |[   ]  N1   [   ]|");
        System.err.println("================================");
        System.err.println("    |[   ]  -S1  [   ]|");
        System.err.println("    |[   ]  -S2  [   ]|");
        System.err.println("  =========================");
    }
}
