package br.com.valorcerto.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    @NonNull
    private String productCode;    // Código de barras

    private String name;
    private double price;
    private String imageUrl;

    // Construtor (pode ser sem parâmetros)
    public Product() { }

    // Getters e Setters
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
