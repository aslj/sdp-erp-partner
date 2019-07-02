package com.solucionesdigitales.partner.service;

import java.util.List;
import com.solucionesdigitales.partner.model.entity.App;

public interface AppService {
	public List<App> getAll();
	public List<App> getByAuthor(String author);
	public Long countByAuthor(String author);
	public App getOne(Long idApp);
	public App post(App app);
	public App put(App app);
	public boolean delete(App app);
}
