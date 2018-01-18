package com.ibm.ojt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/stocks")
public class InventoryController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GetMapping
	public List<Inventory> findAllInventory() {
		return mongoTemplate.findAll(Inventory.class, "inventory");
	}
	
	@GetMapping("/{prodcode}")
	public List<Inventory> findByProdCode(@PathVariable String prodcode) {
		Query query = new Query().addCriteria(Criteria.where("prodcode").regex(prodcode));
		return mongoTemplate.find(query, Inventory.class, "inventory");
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addProduct(@RequestBody Inventory inventory) {
		mongoTemplate.save(inventory, "inventory");
	}
	
	@PutMapping("/{prodcode}")
	public void updateQuantity(@RequestBody Inventory inventory, @PathVariable String prodcode) {
		Query query = new Query().addCriteria(Criteria.where("prodcode").is(prodcode));
		Update update = new Update().set("qtyAvailable", inventory.getQtyAvailable());
		mongoTemplate.updateFirst(query, update, "inventory");
	}
	
	@DeleteMapping("/{prodcode}")
	public void deleteProduct(@PathVariable String prodcode) {
		Query query = new Query().addCriteria(Criteria.where("prodcode").is(prodcode));
		Inventory _inventory = mongoTemplate.findOne(query, Inventory.class, "inventory");
		if (_inventory != null) {
			mongoTemplate.remove(query, "inventory");
		}
	}
}
