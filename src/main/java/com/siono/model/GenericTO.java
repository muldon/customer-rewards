package com.siono.model;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class GenericTO<T> {
  
	@XmlElement
	Integer id;

	@XmlElement
    @Transient
    List<T> data;  

	@XmlElement
	Timestamp date;
	
	@XmlElement
	@Transient	
	Integer total;  
	
	//other common properties could be added here

}
