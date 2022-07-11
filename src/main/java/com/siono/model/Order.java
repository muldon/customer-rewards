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

@XmlRootElement(name = "order")
@Entity
@Table(name = "cr_order")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Order extends GenericTO<Order> implements Serializable {
 
   private static final long serialVersionUID = 1L;
   
   @EqualsAndHashCode.Include
   @Id
   @SequenceGenerator(name="order_id_seq", sequenceName="order_id_seq",allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_id_seq")
   @XmlElement
   private Integer id;
   
   @XmlElement(name = "customerId")
   @Column(name = "customer_id")
   private Integer customerId;
   
   @XmlElement
   @Column(name = "status_id")
   private Integer statusId;
   
   @XmlElement(name = "totalValue")
   @Column(name = "total_value")
   private Double totalValue; 
  
   @XmlElement
   private Timestamp date;    
      
   
	public enum OrderStatusEnum {
		REQUESTED            (1,"Requested"), 
		PROCESSING           (2,"Processing"),
		CANCELED_BY_CUSTOMER (3,"Canceled by Customer"),
	    REJECTED   			 (4,"Rejected"),
	    APPROVED             (5,"Approved"),
		PAID	             (6,"Paid"),
		DELIVERED            (7,"Delivered");
	    
		private final Integer id;
		private final String description;				
		
		OrderStatusEnum(Integer id,String descricao){
			this.id = id;
			this.description = descricao;
		
		} 
		
		public Integer getId() {
			return id;
		}
	
		public String getDescription() {
			return description;
		}
	
	}
   
}
