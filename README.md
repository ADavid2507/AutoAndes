# AutoAndes API

Backend del Examen Parcial II, versión A. Java 21, Spring Boot 4.1.2, Maven y Oracle.
El proyecto ejecutable está en esta carpeta. `AutoAndes/` es una copia inicial preexistente y no forma parte del build.
PharmaBackend se consultó únicamente como referencia y no se modificó.

## Ejecutar

Con Oracle iniciado en `localhost:1522/FREEPDB1` y el esquema propio `AuntoAndes`:

```powershell
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=dev'
```

La conexión local existente se conserva en `config/application-local.yaml`, ignorado por Git.
Para otra máquina se pueden definir `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` (Base64 de al menos 32 bytes aleatorios).
Con esas variables también se puede ejecutar sin perfil: `.\mvnw.cmd spring-boot:run`.
Se requiere Oracle disponible, Java 21 y acceso a Maven Central en la primera ejecución.

- API: http://localhost:8080/api/v1
- Swagger: http://localhost:8080/swagger-ui.html
- Contrato OpenAPI: http://localhost:8080/v3/api-docs

Flyway aplica `V1__esquema_y_semilla.sql` a un esquema vacío. Después valida sus checksums.
Hibernate usa `ddl-auto: validate`; no modifica silenciosamente el DDL. No activar baseline en versión 1 para omitir esta migración.

## Usuarios de prueba del examen

| Usuario | Contraseña de prueba | Rol | Activo |
|---|---|---|---|
| admin | Auto2026* | ADMIN | Sí |
| asesor | Auto2026* | ASESOR | Sí |
| jefe | Auto2026* | JEFE_TALLER | Sí |
| inactivo | Auto2026* | ASESOR | No |

Las contraseñas se insertan como hashes BCrypt; la API nunca devuelve los hashes.
POST `/auth/login` recibe `username` y `password`. Devuelve `token`, `tipo: Bearer`, `expiraEnSegundos: 3600`, `username`, `nombreCompleto`, `roles` y `permisos`.
En Swagger pega el token en **Authorize**. En otras herramientas usa `Authorization: Bearer <token>`.

## Tres partes del backend

1. **P1: base y CRUD.** Arquitectura por capas; entidades JPA; migración y semillas; consulta de clientes; CRUD de servicios y vehículos; DTO Request/Response como clases Lombok; mappers manuales; Bean Validation en Request; errores comunes; logs; Swagger. RN-01 y RN-02.
2. **P2: órdenes y reporte.** Registro cabecera-detalle transaccional; precios y totales calculados en servidor; número OS; control del kilometraje; anulación; filtros combinables; reporte JPQL agregado; CORS. RN-03, RN-04 y RN-05.
3. **P3: seguridad.** BCrypt, JWT firmado de una hora, filtro de firma/expiración, API sin sesiones, unión de permisos por roles, `/auth/me`, 401 y 403 en JSON.

La trazabilidad detallada y explicaciones para la defensa están en `docs/BACKEND.md`.

## Contrato

Todas las rutas siguientes usan el prefijo `/api/v1`. Solo el login, Swagger/OpenAPI y OPTIONS son públicos.

| Método | Ruta | Permiso |
|---|---|---|
| POST | /auth/login | Público |
| GET | /auth/me | Autenticado |
| GET | /clientes | VEHICULO_LEER |
| GET | /servicios y /servicios/{id} | SERVICIO_LEER |
| POST, PUT, DELETE | /servicios, /servicios/{id} | SERVICIO_ESCRIBIR |
| PATCH | /servicios/{id}/estado | SERVICIO_ESCRIBIR |
| GET | /vehiculos?clienteId y /vehiculos/{id} | VEHICULO_LEER |
| POST, PUT, DELETE | /vehiculos, /vehiculos/{id} | VEHICULO_ESCRIBIR |
| POST | /ordenes | ORDEN_REGISTRAR |
| GET | /ordenes?placa&estado&desde&hasta y /ordenes/{id} | ORDEN_LEER |
| PATCH | /ordenes/{id}/anular | ORDEN_ANULAR |
| GET | /reportes/ingresos-por-servicio?desde&hasta | REPORTE_VER |

POST crea sobre la colección (201). PUT/DELETE usan `{id}` (200/204).
Servicios: `codigo`, `nombre`, `precio`, `duracionMin`, `activo`.
Vehículos: `placa`, `marca`, `modelo`, `anio`, `clienteId`.
Estado de servicio: `{ "activo": false }`. Anulación: `{ "motivo": "Solicitud del cliente" }`.

```json
{
  "vehiculoId": 1,
  "kilometraje": 45000,
  "detalles": [{ "servicioId": 1, "cantidad": 2 }]
}
```

No se reciben precios ni total en el DTO de orden. Fechas `yyyy-MM-dd`; `hasta` incluye todo el día.
El reporte exige ambas fechas. Las listas responden arrays, incluido `[]` sin coincidencias, conforme a la guía.
Los errores usan `timestamp`, `status`, `error`, `mensaje`, `ruta`; solo la validación agrega `campos`.

## Verificar

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
python verificacion/verificar_api.py
```

Las pruebas Java usan H2 en modo Oracle y arrancan HTTP real. El dialecto de prueba solo adapta la equivalencia de tipos NUMBER; no reemplaza una prueba contra Oracle.
El script Python usa la biblioteca estándar y requiere una API iniciada; `AUTOANDES_API` cambia la URL.
`verificacion/resultado-api.json` conserva resultados de una ejecución real contra Oracle, sin tokens.
`verificacion/AutoAndes.postman_collection.json` es importable en Postman y contiene login y casos 200/201/400/401/403/404/409.
`verificacion/comprobar_oracle.sql` contiene consultas de solo lectura para demostrar cabecera, detalles, totales y reporte en SQL Developer.

## Alcance de esta entrega

Backend únicamente. La carpeta `autoandes-frontend` contiene trabajo preliminar, no es una entrega terminada y se excluye del repositorio backend.
Los enlaces de repositorio, la rama con apellido, los commits solicitados para la evaluación y la etiqueta final deben cerrarse con los datos del alumno; no se fabrica un historial de trabajo ni se publica automáticamente.
