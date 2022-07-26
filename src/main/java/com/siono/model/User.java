package com.siono.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "user")
@Entity
@Table(name = "cr_user")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class User extends GenericTO<User> implements Serializable {
 
   private static final long serialVersionUID = 1L;
   
   @EqualsAndHashCode.Include
   @Id
   @XmlElement
   private Integer id;
   
   @XmlElement(name = "roleId")
   @Column(name = "role_id")
   private Integer roleId;
   
   @XmlElement
   @Column(name = "status_id")
   private Integer statusId;
   
   @XmlElement
   private String name; 
 
   @XmlElement
   private String email;
   
 
   @XmlAttribute(name="phone")
   private String phone;
    
   @XmlElement
   private Timestamp date;    
         
   
	public enum UserRoleEnum {
		ADMIN				 (1,"Administrador"),
		CUSTOMER		     (2,"Customer");		
	    
		private final Integer id;
		private final String description;
				
		
		UserRoleEnum(Integer id,String descricao){
			this.id = id;
			this.description = descricao;
		
		}
				
	    public static UserRoleEnum getRoleByRoleId(Integer id){
	    	switch(id){
	    		case(1): return ADMIN;
	    		case(2): return CUSTOMER;	    		
	    	  	}
	    	return null;
	    }
	
		public Integer getId() {
			return id;
		}
	
		public String getDescription() {
			return description;
		}
	
	}
	

	public enum UserStatusEnum {
		ACTIVE	 	         (1,"Active"),
		INACTIVE			 (2,"Inactive");		
	
	    
		private final Integer id;
		private final String description;
				
		
		UserStatusEnum(Integer id,String description){
			this.id = id;
			this.description = description;		
		}
		
	    public static UserStatusEnum getStatusById(Integer id){
	    	switch(id){
	    		case(1): return ACTIVE;
	    		case(2): return INACTIVE;	    			    		
	    	  	}
	    	return null;
	    }
	
		public Integer getId() {
			return id;
		}
	
		public String getDescription() {
			return description;
		}
	
	}
	
   
}
