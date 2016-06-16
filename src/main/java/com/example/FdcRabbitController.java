package com.example;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FdcRabbitController {
	
	private static Logger logger = Logger.getLogger(FdcRabbitController.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public FdcRabbitController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/publish", method=RequestMethod.POST )
	public String publish(@RequestParam(value="message", defaultValue="FDC and Pivotal are BFFs")String message){
		rabbitTemplate.convertAndSend(message);
		logger.info("Sent: " + message);
		return message;
	}

	@RequestMapping(value="/receive", method=RequestMethod.GET)
	public String receive(){
		String message = (String)rabbitTemplate.receiveAndConvert(FdcRabbitApplication.queueName);
		if(message == null){
			message = "Oh, snap! No more messages in queue";
			logger.info(message);
		}
		else{
			logger.info("Received: " + message);
		}
		return message;
	}
}
