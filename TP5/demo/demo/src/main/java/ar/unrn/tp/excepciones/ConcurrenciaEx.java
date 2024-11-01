package ar.unrn.tp.excepciones;

public class ConcurrenciaEx extends RuntimeException {
    public ConcurrenciaEx(String mensaje) {
        super(mensaje);
    }
}