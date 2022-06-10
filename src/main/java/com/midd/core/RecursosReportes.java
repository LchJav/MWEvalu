package com.midd.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.midd.core.administracion.*;
import com.midd.core.modelo.*;


@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping("/reportes")
public class RecursosReportes {
    
    private final ServicioEquipo servicio_equipos;
    private final ServiciosActivos servicio_activos;
    private final ServiciosPerfil servicio_perfil;
    private final ServicosAsignacionProyecto servicio_asignacion_proyecto;

    
    @Autowired
    public RecursosReportes(ServicioEquipo servicio_equipos, ServiciosActivos servicio_activos,
            ServiciosPerfil servicio_perfil, ServicosAsignacionProyecto servicio_asignacion_proyecto,
            ServicioReportes servicio_reportes) {
        this.servicio_equipos = servicio_equipos;
        this.servicio_activos = servicio_activos;
        this.servicio_perfil = servicio_perfil;
        this.servicio_asignacion_proyecto = servicio_asignacion_proyecto;
    }



    @GetMapping("/reportes-asignaciones")
    public ResponseEntity<?> reportesAsignacionesProyecto(){
        List<Map<String, Object>> lista_respuestas = new ArrayList<>();
        String mensaje = "";
    
        for (AsignacionProyecto asignacion_proyecto : servicio_asignacion_proyecto.buscarAsignaciones()) {
            if (asignacion_proyecto.getEstado()) {
                mensaje = "Activo";
            } else {
                mensaje = "Inactivo";
            }

            Map<String, Object> respuesta = new HashMap<>();
            Perfil perfil = servicio_perfil.perfilUltimatix(asignacion_proyecto.getUltimatix_asi());
            Equipo equipo = servicio_equipos.buscarEquipoId(asignacion_proyecto.getId_equipo_asi());
            respuesta.put("Ultimatix", perfil.getId_ultimatix());
            respuesta.put("Estado", mensaje);
            respuesta.put("Nombre Equipo", equipo.getNombre_equipo_asi());
            respuesta.put("Nombres Completos", perfil.getNombres_completos());
            respuesta.put("Tipo Equipo", equipo.getTipo_equipo_asi());
            respuesta.put("Nombre Líder", equipo.getNombre_lider());
            respuesta.put("Nombre Técnico", equipo.getNombre_tecnico());
            respuesta.put("Fecha Inicio", asignacion_proyecto.getFecha_inicio());
            respuesta.put("Fecha Fin", asignacion_proyecto.getFecha_fin());
            respuesta.put("Asignacion", asignacion_proyecto.getAsignacion());
            
            lista_respuestas.add(respuesta);
        }

        return new ResponseEntity<>(lista_respuestas, HttpStatus.OK);
    }

    @GetMapping("/reportes-activos")
    public ResponseEntity<?> reportesActivos(){
        List<Map<String, Object>> lista_respuestas = new ArrayList<>();
        String mensaje = "";
        for (Activos activos : servicio_activos.buscarTodos()) {
            if (activos.isEstado()) {
                mensaje = "Activo";
            } else {
                mensaje = "Inactivo";
            }
            Map<String, Object> respuesta = new HashMap<>();
            Perfil perfil = servicio_perfil.perfilUltimatix(activos.getId_ultimatix());
            respuesta.put("Ultimatix", activos.getId_ultimatix());
            respuesta.put("Nombres Completos", perfil.getNombres_completos());
            respuesta.put("Usuario Red", perfil.getUsuario_red());
            respuesta.put("Tipo", activos.getTipo());
            respuesta.put("Marca", activos.getMarca());
            respuesta.put("Serie", activos.getSerie());
            respuesta.put("Código Barras", activos.getCodigo_barras());
            respuesta.put("Edificio", activos.getEdificio());
            respuesta.put("Piso", activos.getPiso());
            respuesta.put("Área", activos.getArea());
            respuesta.put("Estado", mensaje);
            respuesta.put("Hostname", activos.getHostname());
            respuesta.put("Dirección Mac", activos.getDireccion_mac());
            respuesta.put("Dirección IP", activos.getDireccion_ip());
            respuesta.put("IP Reservada", activos.isReservada_ip());
            respuesta.put("Fecha Registro", activos.getFecha_registro());
            respuesta.put("fecha_entrega", activos.getFecha_entrega());
            respuesta.put("Fecha Devolución", activos.getFecha_devolucion());
            lista_respuestas.add(respuesta);
        }
        return new ResponseEntity<>(lista_respuestas, HttpStatus.OK);
    }

    @GetMapping("/reportes-equipos")
    public ResponseEntity<?> reporteEquipos(){
        List<Map<String, Object>> lista_respuestas = new ArrayList<>();
        String mensaje = "";
        for (Equipo equipo : servicio_equipos.buscarTodosEquipos()) {
            if (equipo.isEstado_asi()) {
                mensaje = "Activo";
            } else {
                mensaje = "Inactivo";
            }
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("Tipo Equipo", equipo.getTipo_equipo_asi());
            respuesta.put("Nombre Equipo", equipo.getNombre_equipo_asi());
            respuesta.put("Líder Banco", equipo.getNombre_lider());
            respuesta.put("Líder Técnico", equipo.getNombre_tecnico());
            respuesta.put("Descripción", equipo.getDescripcion_asi());
            respuesta.put("Estado", mensaje);
            respuesta.put("Fecha Registro", equipo.getFecha_registro());
            lista_respuestas.add(respuesta);
        }
        return new ResponseEntity<>(lista_respuestas, HttpStatus.OK);
    }

}
