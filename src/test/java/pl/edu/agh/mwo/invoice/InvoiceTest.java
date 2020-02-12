package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private Invoice invoice;

	@Before
	public void createEmptyInvoiceForTheTest() {
		invoice = new Invoice();
	}

	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetValue()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTax()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossValue()));
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
		invoice.addProduct(taxFreeProduct);
		Assert.assertThat(invoice.getGrossValue(), Matchers.comparesEqualTo(invoice.getNetValue()));
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProducts() {
		invoice.addProduct(new TaxFreeProduct("Ebook", new BigDecimal("30")));
		invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("50")));
		invoice.addProduct(new OtherProduct("Liker", new BigDecimal("40")));
		Assert.assertThat(new BigDecimal("120"), Matchers.comparesEqualTo(invoice.getNetValue()));
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		// tax: 0
		invoice.addProduct(new TaxFreeProduct("Perfumy", new BigDecimal("250")));
		// tax: 0.48
		invoice.addProduct(new DairyProduct("Mleko Bio", new BigDecimal("6")));
		// tax: 57.5
		invoice.addProduct(new OtherProduct("Lampka nocna", new BigDecimal("250")));
		Assert.assertThat(new BigDecimal("57.98"), Matchers.comparesEqualTo(invoice.getTax()));
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		// price with tax: 120
		invoice.addProduct(new TaxFreeProduct("Krem", new BigDecimal("120")));
		// price with tax: 27
		invoice.addProduct(new DairyProduct("Maslo klarowane", new BigDecimal("25")));
		// price with tax: 18.45
		invoice.addProduct(new OtherProduct("Śrubokręt", new BigDecimal("15")));
		Assert.assertThat(new BigDecimal("165.45"), Matchers.comparesEqualTo(invoice.getGrossValue()));
	}

	@Test
	public void testInvoiceHasProperSubtotalWithQuantityMoreThanOne() {
		// 2 x szampon - price: 110
		invoice.addProduct(new TaxFreeProduct("Szampon", new BigDecimal("55")), 2);
		// 3 x ser Brie  - price: 24
		invoice.addProduct(new DairyProduct("ser Brie", new BigDecimal("8")), 3);
		// 1000 x śruba - price: 400
		invoice.addProduct(new OtherProduct("Śruba", new BigDecimal("0.40")), 1000);
		Assert.assertThat(new BigDecimal("534"), Matchers.comparesEqualTo(invoice.getNetValue()));
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		// 2x bateria - price with tax: 24
		invoice.addProduct(new TaxFreeProduct("Bateria", new BigDecimal("12")), 2);
		// 3x jogurt - price with tax: 16.2
		invoice.addProduct(new DairyProduct("jogurt", new BigDecimal("5")), 3);
		// 1000 x kołek drewniany - price with tax: 246
		invoice.addProduct(new OtherProduct("Kołek drewniany", new BigDecimal("0.20")), 1000);
		Assert.assertThat(new BigDecimal("286.20"), Matchers.comparesEqualTo(invoice.getGrossValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		invoice.addProduct(new TaxFreeProduct("Smartwatch", new BigDecimal("999")), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		invoice.addProduct(new DairyProduct("Twaróg", new BigDecimal("6.5")), -1);
	}
}
