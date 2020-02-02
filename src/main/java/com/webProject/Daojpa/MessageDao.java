package com.webProject.Daojpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.webProject.entity.Message;

public interface MessageDao extends CrudRepository<Message,Integer>{
	
	List<Message> findByTag(String filter);
}
