package com.example.sop_finallab.view;

import com.example.sop_finallab.pojo.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Route(value = "view")
public class ProductView extends VerticalLayout {
    Product prod;
    private ComboBox productList;
    private TextField productName;
    private NumberField productCost, productProfit, productPrice;
    private Button addProduct, updateProduct, delProduct, clearProduct;
    private List<Product> products;
    private Product selectProduct = new Product();
    private int index;
    public ProductView(){
        HorizontalLayout buttonBox = new HorizontalLayout();
        productList = new ComboBox<>();
        productName = new TextField();
        productPrice = new NumberField();
        productProfit = new NumberField();
        productCost = new NumberField();
        productList.setLabel("Product List");
        productList.setWidth("600px");
        productName.setLabel("Product Name");
        productName.setWidth("600px");
        productName.setValue("");
        productCost.setLabel("Product Cost");
        productCost.setValue(0.0);
        productCost.setWidth("600px");
        productProfit.setLabel("Product Profit");
        productProfit.setValue(0.0);
        productProfit.setWidth("600px");
        productPrice.setLabel("Product Price");
        productPrice.setValue(0.0);
        productPrice.setWidth("600px");
        productPrice.setEnabled(false);
        addProduct = new Button("Add Product");
        updateProduct = new Button("Update Product");
        delProduct = new Button("Delete Product");
        clearProduct = new Button("Clear Product");
        buttonBox.add(addProduct, updateProduct, delProduct, clearProduct);
        add(productList, productName, productCost, productProfit, productPrice, buttonBox);

        productList.addFocusListener(event ->{
            List<Product> p = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getAllProduct/")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Product>>() {})
                    .block();
//            ObjectMapper mapper = new ObjectMapper();
//            Product product = mapper.convertValue(p.get(0), Product.class);
            this.products = new ArrayList<>(p);
            ArrayList name = new ArrayList<>();
            for (Product product: this.products
                 ) {
                name.add(product.getProductName());
            }
            productList.setItems(name);
        });

        productList.addValueChangeListener(event -> {
            System.out.println(event.getValue());
            if(event.getValue() != null){
                List<Product> p = WebClient.create().get().uri("http://localhost:8080/getAllProduct/").retrieve().bodyToMono(new ParameterizedTypeReference<List<Product>>() {}).block();
                this.products = p;
                for (int i = 0 ; i < this.products.size() ; i++) {
                    if(productList.getValue().equals(this.products.get(i).getProductName())){
                        this.index =  i;
                        productCost.setValue(this.products.get(i).getProductCost());
                        productProfit.setValue(this.products.get(i).getProductProfit());
                        productPrice.setValue(this.products.get(i).getProductPrice());
                        productName.setValue(this.products.get(i).getProductName());
                        this.selectProduct = this.products.get(i);
                    }
                }
            }
            System.out.println(this.index);

        });

        productCost.addValueChangeListener(event -> {
            double price = WebClient.create().get().uri("http://localhost:8080/getPrice/" + productCost.getValue() + "/" + productProfit.getValue()).retrieve().bodyToMono(double.class).block();
            productPrice.setValue(price);
        });

        productProfit.addValueChangeListener(event -> {
            double price = WebClient.create().get().uri("http://localhost:8080/getPrice/" + productCost.getValue() + "/" + productProfit.getValue()).retrieve().bodyToMono(double.class).block();
            productPrice.setValue(price);
        });

        addProduct.addClickListener(event ->{
            double price = WebClient.create().get().uri("http://localhost:8080/getPrice/" + productCost.getValue() + "/" + productProfit.getValue()).retrieve().bodyToMono(double.class).block();
            productPrice.setValue(price);
            boolean response = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addProduct/")
                    .body(Mono.just(new Product(null, productName.getValue(), productCost.getValue(), productProfit.getValue(), productPrice.getValue())), Product.class)
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();
            List<Product> p = WebClient.create().get().uri("http://localhost:8080/getAllProduct/").retrieve().bodyToMono(new ParameterizedTypeReference<List<Product>>() {}).block();
            this.products = new ArrayList<>(p);
            ArrayList name = new ArrayList<>();
            for (Product product: this.products
            ) {
                name.add(product.getProductName());
            }
            productList.setItems(name);
            this.productList.setValue(this.products.get(this.products.size()-1).getProductName());
            System.out.println(response);
//            System.out.println(productService.getAllProduct().get(0).getProductName());
        });

        updateProduct.addClickListener(event->{
            int index = 0;
            double price = WebClient.create().get().uri("http://localhost:8080/getPrice/" + productCost.getValue() + "/" + productProfit.getValue()).retrieve().bodyToMono(double.class).block();
            productPrice.setValue(price);
            boolean response = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateProduct/")
                    .body(Mono.just(new Product(this.selectProduct.get_id(), productName.getValue(), productCost.getValue(), productProfit.getValue(), price)), Product.class)
                    .retrieve()
                    .bodyToMono(boolean.class).block();
            System.out.println(response);
            List<Product> p = WebClient.create().get().uri("http://localhost:8080/getAllProduct/").retrieve().bodyToMono(new ParameterizedTypeReference<List<Product>>() {}).block();
            this.products = new ArrayList<>(p);
            System.out.println(p);
            ArrayList name = new ArrayList<>();
            for (Product product: this.products
            ) {
                name.add(product.getProductName());

            }
            productList.setItems(name);
            this.productList.setValue(this.products.get(name.indexOf(productName.getValue())).getProductName());
        });
        delProduct.addClickListener(event ->{
           boolean response = WebClient.create()
                   .post()
                   .uri("http://localhost:8080/deleteProduct/")
                   .body(Mono.just(this.selectProduct.get_id()), String.class)
                   .retrieve()
                   .bodyToMono(boolean.class)
                   .block();
            System.out.println(this.index);
            this.selectProduct = this.products.get(this.index-1);
            this.index = 0;
            System.out.println(this.selectProduct.getProductName());
            productList.setValue(this.selectProduct.getProductName());
            productName.setValue(this.selectProduct.getProductName());
            productCost.setValue(this.selectProduct.getProductCost());
            productProfit.setValue(this.selectProduct.getProductProfit());
            productPrice.setValue(this.selectProduct.getProductPrice());
        });
        clearProduct.addClickListener(event ->{
            productCost.setValue(0.0);
            productProfit.setValue(0.0);
            productPrice.setValue(0.0);
            productName.setValue("");
        });

    }

}
