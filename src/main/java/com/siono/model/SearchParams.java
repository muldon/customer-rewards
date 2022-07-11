package com.siono.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "searchParam")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchParams implements Serializable {

	static final long serialVersionUID = -152197921465937162L;

	@XmlElement
	String searchText;	
	
	@XmlElement
	Integer id; 
	
	@XmlElement
	Integer rowOffset;
	
	@XmlElement
	Integer numRowsPerPage;
	
	@XmlElement
	Integer sortOrder;	

	@XmlElement
	String sortField;
	
	@XmlElement
	Integer idStatus; 
	
	@XmlElement
	Integer idCustomer; 
	
	@XmlElement	
	Timestamp initDate;
	
	@XmlElement	
	Timestamp endDate;
	
}