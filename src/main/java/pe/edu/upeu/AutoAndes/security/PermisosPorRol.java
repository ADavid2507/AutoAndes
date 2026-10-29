package pe.edu.upeu.AutoAndes.security;

import pe.edu.upeu.AutoAndes.enums.Permiso;

import java.util.*;

public final class PermisosPorRol {
    private PermisosPorRol() {
    }

    public static Set<Permiso> de(String rol) {
        return switch (rol) {
            case "ADMIN" -> EnumSet.allOf(Permiso.class);
            case "ASESOR" ->
                    EnumSet.of(
                            Permiso.SERVICIO_LEER,
                            Permiso.VEHICULO_LEER,
                            Permiso.VEHICULO_ESCRIBIR,
                            Permiso.ORDEN_LEER,
                            Permiso.ORDEN_REGISTRAR);
            case "JEFE_TALLER" ->
                    EnumSet.of(
                            Permiso.SERVICIO_LEER,
                            Permiso.SERVICIO_ESCRIBIR,
                            Permiso.VEHICULO_LEER,
                            Permiso.ORDEN_LEER,
                            Permiso.ORDEN_ANULAR,
                            Permiso.REPORTE_VER);
            default -> Set.of();
        };
    }
}
