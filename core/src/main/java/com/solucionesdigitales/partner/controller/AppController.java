package com.solucionesdigitales.partner.controller;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solucionesdigitales.partner.model.entity.App;
import com.solucionesdigitales.partner.service.AppService;

@RestController
@RequestMapping(value="/")
public class AppController {
	private static final Logger LOG = LoggerFactory.getLogger(AppController.class);
	private static final JsonParser jsonParser = new JsonParser();
	
	@Autowired
	private AppService appService;
	
	@PostMapping(value="get-author")
	@ResponseBody
	public App getAuthor(@RequestBody String string, Principal principal) {
		App app = null;
		JsonObject json = jsonParser.parse(string).getAsJsonObject();
		String appName = json.get("frontEnd").getAsString();
		LOG.info("~~~ getAuthor for "+appName+" ~~~");
		List<App> list = appService.getAll();
		for(App a : list) {
			app = a;
			break;
		}
		return(app);
	}
}
