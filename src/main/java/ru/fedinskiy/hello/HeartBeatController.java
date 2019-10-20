package ru.fedinskiy.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/is_alive")
public class HeartBeatController {
	@Get("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getHello() {
		return "yes";
	}
}
