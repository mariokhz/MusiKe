package mario.khz.musike;

/**
 * Clase que representa un instrumento musical.
 * Contiene nombre, sonido y tipo de instrumento.
 */
public class Instrumento {
    private String nombre;
    private String sonido;
    private String tipo;

    /**
     * Constructor principal.
     * @param nombre Nombre del instrumento
     * @param sonido Sonido característico
     * @param tipo Tipo de instrumento (cuerda, viento, percusión, etc.)
     */
    public Instrumento(String nombre, String sonido, String tipo) {
        this.nombre = nombre;
        this.sonido = sonido;
        this.tipo = tipo;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSonido() {
        return sonido;
    }

    public void setSonido(String sonido) {
        this.sonido = sonido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Instrumento{" +
                "nombre='" + nombre + '\'' +
                ", sonido='" + sonido + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
