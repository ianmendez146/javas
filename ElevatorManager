import java.util.concurrent.*;
import java.util.ArrayList;

public class ElevatorManager { 

    static abstract class Elevador { 
        protected int posicionActual; 
        protected int direccionActual; // 1: Arriba, -1: Abajo, 0: Parado
        ArrayList<Integer> pool = new ArrayList<Integer>(); 
        
        public Elevador(int posicionInicial) {
            this.posicionActual = posicionInicial;
            this.direccionActual = 0; 
        }
    }

    public void eficiencia() {
        /*
            Elevador parado
                 - piso de origen - piso del elevado
            Elevador en movimiento
                 - distancia a destino actual + distancia de Destino a Origen de Peticion
            Elevador muy lejos (solo si la opcion 1 y 2 estan ocupadas)
                 - destino actual -(piso final + piso origen)

            Priorizar el tiempo. Mientras más tiempo menos es prioridad
        */
    } 

    public boolean EstadoElevador (){
       // true --> se está moviendo
       // false --> está detenido
       
       return false; 
    }
    
    public String direccionElevador (boolean estaMovimiento){ 
        int movimiento = 1; // Variable de ejemplo 
        
        if(estaMovimiento == true){
            if (movimiento == 1){
                return "se mueve para arriba";
            } else if (movimiento == 2){
                return "se mueve para abajo";
            }
        }
        return "parado";
    }
}
