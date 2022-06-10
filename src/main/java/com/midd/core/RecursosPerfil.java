package com.midd.core;


import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.midd.core.Respuestas.Respuestas;
import com.midd.core.administracion.ServiciosPerfil;
import com.midd.core.modelo.Aplicaciones_catalogo;
import com.midd.core.modelo.Habilidades;
import com.midd.core.modelo.Habilidades_funcionales;
import com.midd.core.modelo.Perfil;

@RestController
@CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/perfil/")
public class RecursosPerfil {
	
	private final ServiciosPerfil serviciosPerfil;
	private final Respuestas respuestas;
	Logger logger = LoggerFactory.getLogger(RecursosActivos.class);

	
	@Autowired
	public RecursosPerfil(ServiciosPerfil serviciosPerfil, Respuestas respuestas) {
		this.serviciosPerfil = serviciosPerfil;
		this.respuestas = respuestas;
	}
	
	@PostMapping("actualizarPerfil")
	public ResponseEntity<?> agregarActivo(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		logger.info("El asociado "+ nuevo.getId_ultimatix() +" actualizo el perfil");
		serviciosPerfil.agregarPerfil(nuevo);
		return new ResponseEntity<>(nuevo,HttpStatus.OK);
	}
	
	@PostMapping("habilidadUltimatix")
	public ResponseEntity<?> habilidadUltimatix(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "1011"),HttpStatus.BAD_REQUEST);
		}
		String[] mio = serviciosPerfil.habilidadesUltimatix(nuevo.getId_ultimatix());
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}
	
	@PostMapping("ultimatixHabilidad")
	public ResponseEntity<?> ultimatixHabilidad(@RequestBody String habilidad){
		List<Perfil> mi = serviciosPerfil.buscarPerfiles(habilidad);
		return new ResponseEntity<>(mi,HttpStatus.OK);
	}
	
	@PostMapping("perfil")
	public ResponseEntity<?> perfil(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		logger.info("El asociado "+ nuevo.getId_ultimatix() +" actualizo el perfil");
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());//Errores faltan colocar aun
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}

	@GetMapping("perfiles")
	public ResponseEntity<?> perfilesTodo(){
		List<Perfil> mios = serviciosPerfil.buscarTodos();
		return new ResponseEntity<>(mios,HttpStatus.OK);
	}
	
	@PostMapping("sobreMi")
	public ResponseEntity<?> sobreMi(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());
		mio.setSobreMi(nuevo.getSobreMi());
		serviciosPerfil.agregarPerfil(mio);
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}

	// Permite editar habilidades tecnicas y nivel de conocimiento
	@PostMapping("editarMisHabilidades")
	public ResponseEntity<?> habilidades(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());
		mio.setHabilidades(nuevo.getHabilidades());
		mio.setNivel_habilidad(nuevo.getNivel_habilidad());
		serviciosPerfil.agregarPerfil(mio);
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}
	
	// Permite editar habilidades funcionales y nivel de conocimiento
	@PostMapping("editarMisHabilidades-funcionales")
	public ResponseEntity<?> habilidadesFuncionales(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());
		mio.setHabilidades_funcionales(nuevo.getHabilidades_funcionales());
		mio.setNivel_habilidad_funcional(nuevo.getNivel_habilidad_funcional());
		serviciosPerfil.agregarPerfil(mio);
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}
	
	// Permite editar aplicaciones y nivel de conocimiento
	@PostMapping("editarAplicaciones")
	public ResponseEntity<?> aplicaciones(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());
		mio.setAplicaciones(nuevo.getAplicaciones());
		mio.setNivel_aplicaciones(nuevo.getNivel_aplicaciones());
		serviciosPerfil.agregarPerfil(mio);
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}	
	// Permite el cambio de usuario de Red
	@PostMapping("usuarioRed")
	public ResponseEntity<?> usuarioRed(@RequestBody Perfil nuevo){
		if (serviciosPerfil.buscarPerfilId(nuevo.getId_ultimatix())) {
			logger.warn("El asociado "+ nuevo.getId_ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado", "2031"),HttpStatus.BAD_REQUEST);
		}
		Perfil mio = serviciosPerfil.perfilUltimatix(nuevo.getId_ultimatix());
		mio.setUsuario_red(nuevo.getUsuario_red());
		serviciosPerfil.agregarPerfil(mio);
		return new ResponseEntity<>(mio,HttpStatus.OK);
	}	
	/*
	@PostMapping("agregar-mis-habilidades")
	public ResponseEntity<?> AgregarmisHabilidades(@RequestBody Perfil perfil){
		Object perfil_habilidades = serviciosPerfil.habilidadesDuplicadas(perfil);
		if(perfil_habilidades.equals(false)){
			return new ResponseEntity<>(respuestas.respuestas("Habilidad ya registrada en este perfil", "3001"),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(perfil_habilidades,HttpStatus.OK);
	}
	
	@PostMapping("mis-habilidades")
	public ResponseEntity<?> misHabilidades(@RequestBody Perfil perfil) {
		Map<String, Object> response = new HashMap<>();
		Object perfilObj = serviciosPerfil.buscarHabilidadUltimatix(perfil);
		response.put("habilidades", perfilObj);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	*/
	//Permite actualizar el rol
	@PostMapping("actualizar-rol")
	public ResponseEntity<?> actualizarRol(@RequestBody Perfil perfil) {
		Perfil perfil_bd = serviciosPerfil.perfilUltimatix(perfil.getId_ultimatix());
		if(perfil_bd.getRol().equals("user")){
			perfil_bd.setRol("admin");
		}else{
			perfil_bd.setRol("user");
		}
		serviciosPerfil.agregarPerfil(perfil_bd);
		return new ResponseEntity<>(respuestas.respuestas("El usuario con ultimatix: " + perfil.getId_ultimatix() + " cambio de rol ", "200"),HttpStatus.OK);
	}
	//-----------------------------------------------------------------------------------//
	//Catalogo habilidades tecnicas
	@GetMapping("habilidades")// añadir nivel
	public ResponseEntity<?> habilidades(){
		List<Habilidades> mis = serviciosPerfil.buscarHabilidades();
		return new ResponseEntity<>(mis,HttpStatus.OK);
	}
	
	@PostMapping("agregarHabilidad")
	public ResponseEntity<?> agregarHabilidad(@RequestBody Habilidades habilidad){
		List<Habilidades> mis = serviciosPerfil.buscarHabilidades();
		for (Habilidades iterante : mis) {
				if  (iterante.getNombre().equals(habilidad.getNombre())){
					return new ResponseEntity<>(respuestas.respuestas("Habilidad técnica ya registrada", "400"),
							HttpStatus.BAD_REQUEST);
				}
		}
		serviciosPerfil.agregarHabilidades(habilidad);
		logger.info("La habilidad técnica "+habilidad.getNombre()+" ha sido agregada del catálogo");
		return new ResponseEntity<>(habilidad,HttpStatus.OK);
	}
	
	@PostMapping("eliminarHabilidad")
	public ResponseEntity<?> eliminarHabilidadId(@RequestBody Habilidades habilidad) {
		serviciosPerfil.eliminarHabilidad(habilidad.getId());
		List<Habilidades> habilidades = serviciosPerfil.buscarHabilidades();
		logger.info("La habilidad técnica "+habilidad.getId()+" ha sido eliminada del catálogo");
		return new ResponseEntity<>(habilidades, HttpStatus.OK);
	}
	
	//Catalogo Habilidades funcionales
	
	@GetMapping("habilidadesFuncionales")
	public ResponseEntity<?> habilidadesFuncionales(){
		List<Habilidades_funcionales> mis = serviciosPerfil.buscarHabilidades_funcionales();
		return new ResponseEntity<>(mis,HttpStatus.OK);
	}
	
	@PostMapping("agregarHabilidadFuncional")
	public ResponseEntity<?> agregarHabilidadFuncional(@RequestBody Habilidades_funcionales habilidad){
		List<Habilidades_funcionales> mis = serviciosPerfil.buscarHabilidades_funcionales();
		for (Habilidades_funcionales iterante : mis) {
				if  (iterante.getNombre().equals(habilidad.getNombre())){
					return new ResponseEntity<>(respuestas.respuestas("Habilidad funcional ya registrada", "400"),
							HttpStatus.BAD_REQUEST);
				}
		}
		serviciosPerfil.agregarHabilidades_funcionales(habilidad);
		logger.info("La habilidad funcional "+habilidad.getNombre()+" ha sido agregada del catálogo");
		return new ResponseEntity<>(habilidad,HttpStatus.OK);
	}
	
	@PostMapping("eliminarHabilidadFuncional")
	public ResponseEntity<?> eliminarHabilidadFuncionalId(@RequestBody Habilidades_funcionales habilidad) {
		serviciosPerfil.eliminarHabilidad_funcional(habilidad.getId());
		List<Habilidades_funcionales> habilidades = serviciosPerfil.buscarHabilidades_funcionales();
		logger.info("La habilidad funcional "+habilidad.getId()+" ha sido eliminada del catálogo");
		return new ResponseEntity<>(habilidades, HttpStatus.OK);
	}
	
	//Catalogo Aplicaciones
	
	@GetMapping("aplicaciones")// añadir nivel
	public ResponseEntity<?> Aplicaciones(){
		List<Aplicaciones_catalogo> mis = serviciosPerfil.buscarAplicaciones();
		return new ResponseEntity<>(mis,HttpStatus.OK);
	}
		
	@PostMapping("agregarAplicacion")
	public ResponseEntity<?> agregarAplicacion(@RequestBody Aplicaciones_catalogo aplicacion){
		List<Aplicaciones_catalogo> mis = serviciosPerfil.buscarAplicaciones();
		for (Aplicaciones_catalogo iterante : mis) {
				if  (iterante.getNombre().equals(aplicacion.getNombre())){
					return new ResponseEntity<>(respuestas.respuestas("Aplicación ya registrada", "400"),
							HttpStatus.BAD_REQUEST);
				}
		}
		serviciosPerfil.agregarAplicaciones(aplicacion);
		logger.info("La aplicacion "+aplicacion.getNombre()+" ha sido agregada del catálogo");
		return new ResponseEntity<>(aplicacion,HttpStatus.OK);
	}
		
		@PostMapping("eliminarAplicacion")
		public ResponseEntity<?> eliminarAplicacionId(@RequestBody Aplicaciones_catalogo aplicacion) {
			serviciosPerfil.eliminarAplicaciones(aplicacion.getId());
			List<Aplicaciones_catalogo> aplicaciones = serviciosPerfil.buscarAplicaciones();
			logger.info("La aplicacion "+aplicacion.getId()+" ha sido eliminada del catálogo");
			return new ResponseEntity<>(aplicaciones, HttpStatus.OK);
		}	
}