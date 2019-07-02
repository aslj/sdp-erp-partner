package com.solucionesdigitales.partner.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solucionesdigitales.partner.model.entity.App;
import com.solucionesdigitales.partner.repository.AppRepository;
import com.solucionesdigitales.partner.service.AppService;

@Service("appService")
public class AppServiceImpl implements AppService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImpl.class);
	
	@Autowired
	private AppRepository repository;

	@Override
	public List<App> getAll() {
		return((List<App>)repository.findAll());
	}
	
	@Override
	public List<App> getByAuthor(String author) {
		return((List<App>)repository.findByAuthor(author));
	}

	@Override
	public Long countByAuthor(String author) {
		return(repository.countByAuthor(author));
	}

	@Override
	public App getOne(Long idApp) {
		return(repository.findOne(idApp));
	}

	@Override
	public App post(App app) {
		boolean exists = app.getIdApp()==null?false:repository.exists(app.getIdApp());
		if(!exists) {
			repository.save(app);
			LOGGER.info("...record inserted: "+app);
		} else {
			LOGGER.warn("*ERROR* while insert: "+app+", the record already exists");
		}
		return(exists?null:app);
	}

	@Override
	public App put(App app) {
		boolean exists = repository.exists(app.getIdApp());
		if(exists) {
			repository.save(app);
			LOGGER.info("...record updated: "+app);
		} else {
			LOGGER.warn("*ERROR* while update: "+app+", the record doesn't exist");
		}
		return(exists?app:null);
	}

	@Override
	public boolean delete(App app) {
		boolean exists = repository.exists(app.getIdApp());
		if(exists) {
			repository.delete(app);
			LOGGER.info("Record deleted: "+app);
		} else {
			LOGGER.warn("*ERROR* while delete: "+app+", the record doesn't exist");
		}
		return(exists);
	}
}
