import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class ProductDetailsScraper {
  
  private static final String URL = "https://www.walmart.com/browse/electronics/dell-gaming-laptops/3944_3951_7052607_1849032_4519159";
  private static final String CSV_FILE = "product_details.csv";
  private static final String[] FIELDS = {"Product Name", "Product Price", "Item Number", "Product Category", "Product Description", "Model Number"};
  private static final int NUM_PRODUCTS = 10;
  
  public static void main(String[] args) {
    try {
      
      Document doc = Jsoup.connect(URL).get();
      
      
      Elements products = doc.select(".search-result-gridview-items .search-result-gridview-item");
      int numProducts = Math.min(NUM_PRODUCTS, products.size());
      products = products.subList(0, numProducts);
      
      
      FileWriter csvWriter = new FileWriter(CSV_FILE);
      csvWriter.write(String.join(",", FIELDS) + "\n");
      
      
      for (Element product : products) {
        String name = product.select(".product-title-link").text().replace(",", "");
        String price = product.select(".price-characteristic").text() + product.select(".price-mantissa").text();
        String itemNumber = product.attr("data-item-id");
        String category = product.select(".breadcrumb li:last-child").text();
        String description = product.select(".product-short-description").text().replace(",", "");
        String modelNumber = product.select(".product-short-description").select(":contains(Model #:)").text().replace("Model #:", "").trim();
        
        
        csvWriter.write(String.join(",", name, price, itemNumber, category, description, modelNumber) + "\n");
      }
      
      
      csvWriter.close();
      
      System.out.println("Product details exported to " + CSV_FILE);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}