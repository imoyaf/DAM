package model;

public enum TipoIncidencia {
    URGENTE("U"),
    NORMAL("N");

    private final String codigo;

    TipoIncidencia(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoIncidencia fromCodigo(String codigo) {
        for (TipoIncidencia tipo : TipoIncidencia.values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoIncidencia: " + codigo);
    }


}
