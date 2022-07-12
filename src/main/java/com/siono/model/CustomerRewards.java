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

@XmlRootElement(name = "customerRewards")
@Entity
@Table(name = "customer_rewards")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CustomerRewards extends GenericTO<CustomerRewards> implements Serializable {
 
   private static final long serialVersionUID = 1L;
   
   @EqualsAndHashCode.Include
   @Id
   @SequenceGenerator(name="customer_rewards_id_seq", sequenceName="customer_rewards_id_seq",allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="customer_rewards_id_seq")
   @XmlElement
   private Integer id;
   
   @XmlElement(name = "customerId")
   @Column(name = "customer_id")
   private Integer customerId;
   
   @XmlElement(name = "orderId")
   @Column(name = "order_id")
   private Integer orderId;
   
   @XmlElement
   @Column(name = "operation_id")
   private Integer operationId;
   
   @XmlElement(name = "numPoints")
   @Column(name = "num_points")
   private Integer numPoints; 
  
   @XmlElement
   private Timestamp date;    
      
   
	public enum OperationEnum {
		INPUT                (1,"Input"),    //customer earns points
		OUTPUT	             (2,"Output");   //customer spend points		
	    
		private final Integer id;
		private final String description;				
		
		OperationEnum(Integer id,String description){
			this.id = id;
			this.description = description;
		
		} 
		
		public Integer getId() {
			return id;
		}
	
		public String getDescription() {
			return description;
		}
	
	}
   
}
