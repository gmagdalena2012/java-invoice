package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Collection<Product> products = new ArrayList<>();

	public void addProduct(Product product) {
		addProduct(product, 1); 
	}

	public void addProduct(Product product, Integer quantity) {
		
		if( quantity <= 0)
			throw new IllegalArgumentException();
		
		for (int i = 1; i <= quantity; i++)
			products.add(product);
		
	}

	public BigDecimal getNetValue() {
		
		BigDecimal subtotal = new BigDecimal(0);
		 
		for(Product product : products) {
			subtotal = subtotal.add(product.getPrice());
		}
			 
		return subtotal;
	}

	public BigDecimal getTax() {
		
		BigDecimal total = new BigDecimal(0);
		 
		for(Product product : products) {
			total = total.add(product.getPriceWithTax().subtract(product.getPrice()));
		}
			 
		return total;
	}

	public BigDecimal getGrossValue() {
		
		BigDecimal total = new BigDecimal(0);
		 
		for(Product product : products) {
			total = total.add(product.getPriceWithTax());
		}
		 
		return total;
	}
}
