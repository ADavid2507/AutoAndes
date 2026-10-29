-- Solo lectura. Ejecutar en SQL Developer en el esquema de AutoAndes.
SELECT "version", "description", "success" FROM "flyway_schema_history" ORDER BY "installed_rank";
SELECT id, numero, fecha, vehiculo_id, kilometraje, estado, total FROM orden_servicio ORDER BY id;
SELECT o.numero, d.servicio_id, d.cantidad, d.precio_unitario, d.subtotal
FROM detalle_orden d JOIN orden_servicio o ON o.id=d.orden_id ORDER BY o.id, d.id;
-- Debe devolver cero filas: ninguna cabecera sin detalle ni total inconsistente.
SELECT o.numero, o.total, SUM(d.subtotal) total_detalle
FROM orden_servicio o LEFT JOIN detalle_orden d ON d.orden_id=o.id
GROUP BY o.numero,o.total HAVING COUNT(d.id)=0 OR SUM(d.subtotal)<>o.total;
-- El reporte excluye ANULADA.
SELECT s.codigo,s.nombre,SUM(d.cantidad) cantidad_total,SUM(d.subtotal) importe_total
FROM detalle_orden d JOIN servicio s ON s.id=d.servicio_id
JOIN orden_servicio o ON o.id=d.orden_id WHERE o.estado='REGISTRADA'
GROUP BY s.codigo,s.nombre ORDER BY importe_total DESC;
