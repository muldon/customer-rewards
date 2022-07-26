package com.siono.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@XmlRootElement
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Statement implements Serializable {

	private static final long serialVersionUID = -123291921465937662L;

	@XmlElement
	private String customerName;
	
	@XmlElement
	private List<StatementPeriod> statementPeriods; 
	
	@XmlElement
	private Integer totalPoints; 
	 
}
 