package conductor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import conductor.DTO.MensagemRetornoDTO;
import conductor.service.RabbitService;

@Controller
@RequestMapping("/rabbit")
public class RabbitController {
	
	@Autowired
	RabbitService service;
	
	@PostMapping(value="/send")
	public ResponseEntity<String> send(String mensagem) {
		return new ResponseEntity<String>(service.send(mensagem), HttpStatus.OK);
	}
	
	@GetMapping(value="/consumer")
	public ResponseEntity<List<MensagemRetornoDTO>> consumer(){
		return new ResponseEntity<List<MensagemRetornoDTO>>(service.consumer(), HttpStatus.OK);
	}
}
