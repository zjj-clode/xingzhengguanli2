/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.Movie;
import com.thinkgem.jeesite.modules.cms.dao.MovieDao;

/**
 * 视频新闻Service
 * @author hhw
 * @version 2017-05-01
 */
@Service
@Transactional(readOnly = true)
public class MovieService extends CrudService<MovieDao, Movie> {

	public Movie get(String id) {
		return super.get(id);
	}
	
	public List<Movie> findList(Movie movie) {
		return super.findList(movie);
	}
	
	public Page<Movie> findPage(Page<Movie> page, Movie movie) {
		return super.findPage(page, movie);
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	
	@Transactional(readOnly = false)
	public void save(Movie movie) {
		super.save(movie);
	}
	
	@Transactional(readOnly = false)
	public void delete(Movie movie) {
		super.delete(movie);
	}
	
}