package com.midd.core.administracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midd.core.modelo.*;
import com.midd.core.repositorio.*;

@Service
public class ServicioReportes {
    
    // Repositorios
    private final ActivosRepo activos_repo;
    private final AreaRepo area_repo;
    private final AsignacionesProyectoRepo asignaciones_proyecto_repo;
    private final AsociadosRepo asociados_repo;
    private final EdificioRepo edificio_repo;
    private final EquipoRepo equipo_repo;
    private final HabilidadesRepo habilidaes_repo;
    private final TipoRepo tipo_activo_repo;
    private final TipoProyectoRepo tipo_proyecto_repo;

    // Constructor

    @Autowired
    public ServicioReportes(ActivosRepo activos_repo, AreaRepo area_repo,
            AsignacionesProyectoRepo asignaciones_proyecto_repo, AsociadosRepo asociados_repo,
            EdificioRepo edificio_repo, EquipoRepo equipo_repo, HabilidadesRepo habilidaes_repo,
            TipoRepo tipo_activo_repo, TipoProyectoRepo tipo_proyecto_repo) {
        this.activos_repo = activos_repo;
        this.area_repo = area_repo;
        this.asignaciones_proyecto_repo = asignaciones_proyecto_repo;
        this.asociados_repo = asociados_repo;
        this.edificio_repo = edificio_repo;
        this.equipo_repo = equipo_repo;
        this.habilidaes_repo = habilidaes_repo;
        this.tipo_activo_repo = tipo_activo_repo;
        this.tipo_proyecto_repo = tipo_proyecto_repo;
    }

    // Buscar ultimatix
    public Asociado buscarUltimatixAsociado(Long ultimatix_asociado){
        return asociados_repo.getById(ultimatix_asociado);
    }

    public List<AsignacionProyecto> buscarTodasAsignacionesProyecto() {
        List<AsignacionProyecto> asignaciones_proyectos = asignaciones_proyecto_repo.findAll();
        return asignaciones_proyectos;
    }

}
