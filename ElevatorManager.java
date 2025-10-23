import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

public class ElevatorManager {
    private static final int TIEMPO_POR_PISO = 2; // Segundos por piso
    private static final int TIEMPO_DE_PARADA = 5; // Segundos por parada (puertas)
    
    // Lista de todos los elevadores que gestiona el Manager
    private final List<Elevador> elevadores = new ArrayList<>(); 

    // Constructor para inicializar los elevadores
    public ElevatorManager(int numElevadores, int pisoInicial) {
        for (int i = 0; i < numElevadores; i++) {
            elevadores.add(new Elevador(pisoInicial) {}); 
        }
    }

    static abstract class Elevador {
        protected int posicionActual;
        protected int direccionActual; // 1: Arriba, -1: Abajo, 0: Parado
        protected final ArrayList<Integer> pool = new ArrayList<>(); // Cola de destinos
        
        // El lock es la herramienta para la sincronización
        private final Lock lock = new ReentrantLock(); 

        public Elevador(int posicionInicial) {
            this.posicionActual = posicionInicial;
            this.direccionActual = 0;
        }

        /* Método para añadir una solicitud de forma segura */
        public void agregarSolicitud(int pisoDestino) {
            lock.lock(); //Adquirir el bloqueo
            try {
                if (!pool.contains(pisoDestino)) {
                    pool.add(pisoDestino);
                    //ordenar el pool para un movimiento eficiente
                    pool.sort(null); 
                }
            } finally {
                lock.unlock(); 
            }
        }
        
        // Getter de Posición (Método seguro)
        public int getPosicionActual() {
             lock.lock();
             try {
                 return posicionActual;
             } finally {
                 lock.unlock();
             }
         }
    }
    //---------------------------------------------------------------------------------------------------//   
    static class Peticion {
        int pisoOrigen; 
        int direccionDeseada; 
        long tiempoSolicitud; 

        public Peticion(int origen, int direccion) {
            this.pisoOrigen = origen;
            this.direccionDeseada = direccion;
            this.tiempoSolicitud = System.currentTimeMillis(); 
        }
    }

    /*Indica si un elevador se está moviendo o no.*/
    public boolean EstadoElevador (Elevador e){
        e.lock.lock(); 
        try {
            return e.direccionActual != 0; 
        } finally {
            e.lock.unlock(); 
        }
    }

    /*Describe la dirección actual de un elevador.*/
    public String direccionElevador (Elevador e){ 
        e.lock.lock();
        try {
            int direccion = e.direccionActual;
            
            if (direccion == 1){
                return "se mueve para arriba";
            } else if (direccion == -1){ 
                return "se mueve para abajo";
            } else { 
                return "parado";
            }
        } finally {
            e.lock.unlock(); 
        }
    }
    
    //---------------------------------------------------------------------------------------------------//

    /*Calcula y retorna el elevador más eficiente (con menor TEL) para una solicitud.*/
    public Elevador obtenerElevadorMasEficiente(Peticion solicitud) {
        Elevador mejorElevador = null;
        double minTEL = Double.MAX_VALUE;

        for (Elevador elevador : elevadores) {
            double tel = calcularTEL(elevador, solicitud);

            if (tel < minTEL) {
                minTEL = tel;
                mejorElevador = elevador;
            }
            // NOTA: Aquí iría la lógica de desempate (prioridad por tiempo) si TEL es igual.
        }
        return mejorElevador;
    }


    /*Calcula el Tiempo Estimado de Llegada (TEL) para un elevador dado.*/
    private double calcularTEL(Elevador e, Peticion s) {
        
        e.lock.lock(); 
        try {
            // Copia de estado segura
            int posicion = e.posicionActual;
            int direccion = e.direccionActual;
            // Se hace una copia del pool para liberar el lock rápidamente
            ArrayList<Integer> currentPool = new ArrayList<>(e.pool); 
            
            int distanciaPisos = 0;
            int paradasIntermedias;
            int numPool = currentPool.size(); 

            if (direccion == 0) {// Elevador parado
                distanciaPisos = Math.abs(posicion - s.pisoOrigen);
                paradasIntermedias = numPool;
                
            } else { // Elevador en movimiento
                
                boolean mismaDireccion = (direccion == s.direccionDeseada);
                boolean enCamino = false;

                // Determinar si la solicitud está en la ruta actual
                if (mismaDireccion) {
                    if (direccion == 1) { // Moviéndose hacia arriba
                        enCamino = (s.pisoOrigen >= posicion);
                    } else { // Moviéndose hacia abajo (-1)
                        enCamino = (s.pisoOrigen <= posicion);
                    }
                }
                
                if (mismaDireccion && enCamino) {
                    // En camino y compatible
                    distanciaPisos = Math.abs(posicion - s.pisoOrigen);
                    
                    // Contar solo las paradas que están antes del piso de la solicitud
                    paradasIntermedias = (int) currentPool.stream()
                        .filter(piso -> (direccion == 1) ? (piso < s.pisoOrigen) : (piso > s.pisoOrigen))
                        .count();
                } 
                else { 
                    //Incompatible, dirección opuesta o fuera de camino
                    
                    // 1. Encontrar el último destino (punto de retorno)
                    int ultimoDestino = (numPool > 0) ? currentPool.get(currentPool.size() - 1) : posicion;

                    // 2. Distancia restante para llegar al último destino actual
                    distanciaPisos += Math.abs(posicion - ultimoDestino);
                    
                    // 3. Sumar la distancia del viaje de vuelta desde el punto de retorno al origen de la solicitud
                    distanciaPisos += Math.abs(ultimoDestino - s.pisoOrigen);
                    
                    // Paradas
                    paradasIntermedias = numPool;
                }
            }

            // FÓRMULA FINAL DE TEL
            return (distanciaPisos * TIEMPO_POR_PISO) + (paradasIntermedias * TIEMPO_DE_PARADA);
            
        } finally {
            e.lock.unlock(); 
        }
    }
}
