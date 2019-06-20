package com.example.demo;

import com.example.demo.handler.UdpDecoderHandler;
import com.example.demo.handler.UdpEncoderHandler;
import com.example.demo.handler.UdpHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.netty.udp.UdpServer;

import java.time.Duration;


@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

    }

	@Bean
	CommandLineRunner serverRunner(UdpDecoderHandler udpDecoderHandler,UdpEncoderHandler udpEncoderHandler,UdpHandler udpHandler){
		return strings ->{
			createUdpServer(udpDecoderHandler,udpEncoderHandler,udpHandler);

		};
	}

	private void createUdpServer(UdpDecoderHandler udpDecoderHandler,UdpEncoderHandler udpEncoderHandler,UdpHandler udpHandler){
		UdpServer.create()
				.handle((in,out)->{
					in.receive()
							.asByteArray()
							.subscribe();
					return Flux.never();
				})
				.port(8888) //UDP Server 端口
		        .doOnBound(conn -> conn
							.addHandler("decoder",udpDecoderHandler)
							.addHandler("encorder",udpEncoderHandler)
							.addHandler("handler",udpHandler)
				)
				.bindNow(Duration.ofSeconds(30));

	}
}
