package conductor.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import conductor.DTO.MensagemRetornoDTO;


@Service
public class RabbitServiceConector {
	
	Connection connection;
	
	Channel channel;
	
	private String nameQueue = "NovaFila";
			
	private String exchangeName = "Lab";
	
	private String uri = "amqp://ptlgkwzz:ZCfooWdHANnzJG4CPtYvwVAJLgpJmtEw@prawn.rmq.cloudamqp.com/ptlgkwzz"; 
	
	private String routingKey = "Lab.Routing";

	
	@Autowired
	Properties properties;
	
	public void createChannel() {
		try {
			
			channel = connection.createChannel();
			
			channel.queueDeclare(nameQueue,false, false,false, null);
			
			channel.exchangeDeclare(exchangeName, "fanout");
			channel.queueBind(nameQueue, exchangeName, routingKey);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void createConection() {
		try {
			ConnectionFactory factory;
			
			if (connection == null) {
				factory = new ConnectionFactory();
				factory.setUri(uri);
				connection = factory.newConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String publishRabbit(String mensagem) {
		try {
			channel.basicPublish(exchangeName, routingKey,null, mensagem.getBytes("UTF-8"));
			channel.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return mensagem;
	}
	
	public List<MensagemRetornoDTO> consumerRabbit() {
		try {
			
			List<MensagemRetornoDTO> lista = new ArrayList<MensagemRetornoDTO>();
			Consumer consumer  = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String mensagem = new String (body, "UTF-8");
					MensagemRetornoDTO mensagemDTO = new MensagemRetornoDTO();
					mensagemDTO.setMensagem(mensagem);
					lista.add(mensagemDTO);
					System.out.println("Mensagem recebida :" + mensagem);
					
				}
			};
			channel.basicConsume(nameQueue, true, consumer);
			
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
}
