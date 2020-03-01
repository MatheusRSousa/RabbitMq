package conductor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import conductor.DTO.MensagemRetornoDTO;

@Service
public class RabbitService {

	@Autowired
	RabbitServiceConector conector;
	
	public String send(String mensagem) {
		conector.createConection();
		conector.createChannel();
		return conector.publishRabbit(mensagem);
	}
	
	public List<MensagemRetornoDTO> consumer() {
		conector.createConection();
		conector.createChannel();
		return conector.consumerRabbit();
	}
}
