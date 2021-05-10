package controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.Collectible;
import com.group3.beans.Gamer;
import com.group3.controllers.GamerController;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

@ExtendWith(SpringExtension.class)
public class GamerControllerTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		public GamerController getPlayerService(Gamer gg, Collectible c, JWTUtil jwtUtil, GamerService gs, CollectibleTypeService cts, CollectibleService cs) {
			GamerController gc = new GamerController();
			return gc;
		}
	}
}
