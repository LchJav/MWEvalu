package com.midd.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.midd.core.Respuestas.Respuestas;
import com.midd.core.administracion.ServiciosAsociados;
import com.midd.core.administracion.ServiciosPerfil;
import com.midd.core.modelo.Asociado;
import com.midd.core.modelo.Perfil;

@RestController
@CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/asociados")
public class RecursosAsociados {
	private final ServiciosAsociados serviciosAsociados;
	private final ServiciosPerfil serviciosPerfil;
	private final PasswordEncoder cifrarClave;
	Logger logger = LoggerFactory.getLogger(RecursosActivos.class);
	private final Respuestas respuestas;
	//Constructor
	public RecursosAsociados(ServiciosAsociados serviciosAsociados, PasswordEncoder cifrarClave, Respuestas respuestas, ServiciosPerfil servicios_perfil) {
		super();
		this.serviciosAsociados = serviciosAsociados;
		this.cifrarClave = cifrarClave;
		this.respuestas = respuestas;
		this.serviciosPerfil = servicios_perfil;
	}
	
	@GetMapping("/buscarAsociados")
	public ResponseEntity<List<Asociado>> obtenerTodosAsociados(){
		List<Asociado> asociados = serviciosAsociados.buscarTodo();
		return new ResponseEntity<>(asociados,HttpStatus.OK);
	}	

	@GetMapping("/buscar/{id}")
	public ResponseEntity<Asociado> obtenerAsociadoPorId(@PathVariable("id") Long id){
		Asociado asociado = serviciosAsociados.buscarAsociadoPorId(id);
		asociado.setClave("");
		return new ResponseEntity<>(asociado,HttpStatus.OK);
	}
	
	@PostMapping("/agregarAsociado")
	public ResponseEntity<?> agregarAsociado(@RequestBody Asociado asociado){
		try {
			serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
			logger.warn("El usuario "+ asociado.getId_numero_Ultimatix() +" ya se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario ya registrado","1001"),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			asociado.setClave(cifrarClave.encode(asociado.getClave()));
			if(serviciosAsociados.validarCorreo(asociado)==false) {
				logger.warn("El correo "+ asociado.getCorreo() +" ya se encuentra registrado");
				return new ResponseEntity<>(respuestas.respuestas("Correo ya registrado","1002"),HttpStatus.BAD_REQUEST);
			}		
			
			if(serviciosAsociados.validarTelefono(asociado)==false) {
				logger.warn("El telefono "+ asociado.getTelefono() +" ya se encuentra registrado");
				return new ResponseEntity<>(respuestas.respuestas("Telefono ya registrado","1003"),HttpStatus.BAD_REQUEST);
			}
			asociado.setEstado(true);
			asociado.setToken(cifrarClave.encode(asociado.getId_numero_Ultimatix().toString()));
			serviciosAsociados.agregarAsociado(asociado);
			asociado.setClave(null);
			logger.info("El asociado "+ asociado.getId_numero_Ultimatix() +" se ha registrado exitosamente");
			return new ResponseEntity<>(asociado,HttpStatus.OK);
		}				
	}
			
	@PostMapping("/actualizarAsociado")
	public ResponseEntity<?> actualizarAsociado(@RequestBody Asociado asociado){
		if (serviciosAsociados.buscarAsociadoId(asociado.getId_numero_Ultimatix())) {
			logger.warn("El asociado "+ asociado.getId_numero_Ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado","1021"),HttpStatus.BAD_REQUEST);
		}
		if(serviciosAsociados.validarCorreoActualizar(asociado)==false) {
			logger.warn("El correo "+ asociado.getCorreo() +" ya se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Correo ya registrado","1021"),HttpStatus.BAD_REQUEST);
		}		
		if(serviciosAsociados.validarTelefonoActualizar(asociado)==false) {
			logger.warn("El telefono "+ asociado.getTelefono() +" ya se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Telefono ya registrado","1023"),HttpStatus.BAD_REQUEST);
		}
		Asociado mio = serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
		mio.setCorreo(asociado.getCorreo());
		mio.setTelefono(asociado.getTelefono());
		mio.setIntentos(0);
		Asociado actualizadoAsociado = serviciosAsociados.actualizarAsociado(mio);
		actualizadoAsociado.setClave("");
		actualizadoAsociado.setToken("");
		return new ResponseEntity<>(actualizadoAsociado,HttpStatus.OK);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Asociado> eliminarAsociadoPorId(@PathVariable("id") Long id){
		serviciosAsociados.eliminarAsociado(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/tcs-login")
	public ResponseEntity<?> iniciarSesion(@RequestBody Asociado asociado){
		Asociado nuevo = null;
		// Guardar fecha del primer intento
		
		try {
			nuevo = serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
			if(nuevo.getIntentos() >= 3 || !nuevo.getEstado()){
				nuevo.setEstado(false);
				Perfil mio = serviciosPerfil.buscarPerfilMio(nuevo.getId_numero_Ultimatix());
				mio.setEstado(false);
				serviciosPerfil.actualizarPerfil(mio);
				serviciosAsociados.actualizarAsociado(nuevo);
				return new ResponseEntity<>(respuestas.respuestas("Usuario bloqueado" ,"1013"),HttpStatus.BAD_REQUEST);
			}
				
			java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
			if(nuevo.getFecha_login() != null){
				if(nuevo.getFecha_login().before(date) && !nuevo.getFecha_login().toString().equals(date.toString())){
					nuevo.setIntentos(0);
					serviciosAsociados.actualizarAsociado(nuevo);
				}
			}
			
		} catch (Exception e) {
			logger.warn("El asociado "+ asociado.getId_numero_Ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario o contraseña incorrecta","1012"),HttpStatus.BAD_REQUEST);
			
		}				
		if (cifrarClave.matches(asociado.getClave(), nuevo.getClave())==false) {
			logger.warn("La contraseña ingresada de "+ asociado.getId_numero_Ultimatix() +" es incorrecta");
			if(nuevo.getIntentos() == 0){
				nuevo = actualizarFecha(nuevo);
				serviciosAsociados.actualizarAsociado(nuevo);
			}
			nuevo.setIntentos(nuevo.getIntentos()+1);
			serviciosAsociados.actualizarAsociado(nuevo);
			return new ResponseEntity<>(respuestas.respuestas("Usuario o contraseña incorrecta , Intento: " + nuevo.getIntentos() + " de " + " 3","1012"),HttpStatus.BAD_REQUEST);
			
		}
		nuevo.setIntentos(0);
		nuevo.setEstado(true);
		serviciosAsociados.actualizarAsociado(nuevo);
		try {
			nuevo = serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
		} catch (Exception e) {
			logger.warn("El asociado "+ asociado.getId_numero_Ultimatix() +" no se encuentra registrado");
			return new ResponseEntity<>(respuestas.respuestas("Usuario o contraseña incorrecta","1012"),HttpStatus.BAD_REQUEST);
		}				
		if (cifrarClave.matches(asociado.getClave(), nuevo.getClave())==false) {
			logger.warn("La contraseña ingresada de "+ asociado.getId_numero_Ultimatix() +" es incorrecta");
			return new ResponseEntity<>(respuestas.respuestas("Usuario o contraseña incorrecta","1012"),HttpStatus.BAD_REQUEST);
		}
		logger.info("El asociado "+ asociado.getId_numero_Ultimatix() +" ha igresado exitosamente");
		nuevo.setClave(null);
		return new ResponseEntity<>(nuevo,HttpStatus.OK);		
	}	

	public Asociado actualizarFecha(Asociado asociado){
		java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
		asociado.setFecha_login(date);
		return asociado;
	}

	@PostMapping("/cambio-password")
	public ResponseEntity<?> cambio_password(@RequestBody Asociado asociado){

		try {
			Asociado nuevo = serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
			if(nuevo.getEstado()){
				if(nuevo.getToken().equals(asociado.getToken())){
					nuevo.setClave(cifrarClave.encode(asociado.getClave()));
					serviciosAsociados.actualizarAsociado(nuevo);
					return new ResponseEntity<>(respuestas.respuestas("Usuario actualizado con exito","1002"),HttpStatus.OK);
				}
				return new ResponseEntity<>(respuestas.respuestas("Token no valido","1002"),HttpStatus.BAD_REQUEST);
				
			}
			return new ResponseEntity<>(respuestas.respuestas("Usuario bloqueado" ,"1013"),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado","1001"),HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/desbloqueo-asociado")
	public ResponseEntity<?> desbloqueo(@RequestBody Asociado asociado){

		try {
			Asociado nuevo = serviciosAsociados.buscarAsociadoPorId(asociado.getId_numero_Ultimatix());
			if(nuevo.getEstado()){
				nuevo.setEstado(false);
				Perfil mio = serviciosPerfil.buscarPerfilMio(nuevo.getId_numero_Ultimatix());
				mio.setEstado(false);
				serviciosPerfil.actualizarPerfil(mio);
				serviciosAsociados.actualizarAsociado(nuevo);
				return new ResponseEntity<>(respuestas.respuestas("Usuario bloqueado con exito","1002"),HttpStatus.OK);
			}else{
				nuevo.setEstado(true);
				Perfil mio = serviciosPerfil.buscarPerfilMio(nuevo.getId_numero_Ultimatix());
				mio.setEstado(true);
				serviciosPerfil.actualizarPerfil(mio);
				nuevo.setIntentos(0);
				serviciosAsociados.actualizarAsociado(nuevo);
				return new ResponseEntity<>(respuestas.respuestas("Usuario desbloqueado con exito","1002"),HttpStatus.OK);
			}
			
			
		} catch (Exception e) {
			return new ResponseEntity<>(respuestas.respuestas("Usuario no registrado","1001"),HttpStatus.BAD_REQUEST);
		}
	}
}