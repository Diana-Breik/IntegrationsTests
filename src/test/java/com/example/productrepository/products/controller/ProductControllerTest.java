package com.example.productrepository.products.controller;

import com.example.productrepository.products.ProductRepository;
import com.example.productrepository.products.ProductService;
import com.example.productrepository.products.models.NewProduct;
import com.example.productrepository.products.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DirtiesContext
    void getAllProducts() throws Exception {
        //GIVEN
        Product product = new Product("1","product",11);
        productRepository.save(product);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))


                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                       [
                       {
                       "id": "1",
                       "title": "product",
                       "price" : 11
                       }
                       ]
                       """));



    }

    @Test
    @DirtiesContext
    void addProduct() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                 {
                 "title": "test-title",
                 "price": 40
                 }
                  """)

           )

        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                   {
                   "title": "test-title",
                   "price": 40
                   }
                   """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void getProductById() throws Exception {
        //GIVEN
        String id = "11" ;
        Product newProduct = new Product(id,"test",22);
        productRepository.save(newProduct);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + id))


                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                       {
                       "id" : "11",
                       "title": "test",
                       "price": 22
                       }
                       """));



    }

    @Test
    @DirtiesContext //die Tests sind so unabhängig voneinander
    void putProduct() throws Exception {
        //GIVEN
        String id = "11" ;
        Product newProduct = new Product(id,"test",22);
        productRepository.save(newProduct);
        NewProduct newProduct1 = new NewProduct("test2",40);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                 {
                 "title" :"test2",
                 "price" : 40
                 }
                   """)
        )

        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                       {
                       "id" : "11",
                       "title" :"test2",
                       "price" : 40
                       }
                       """
                ));
    }

    @Test
    @DirtiesContext
    void deleteProduct() throws Exception {
        //GIVEN
        String id = "11" ;
        Product newProduct = new Product(id,"test",22);
        productRepository.save(newProduct);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + id))

        //THEN
                .andExpect(status().isOk());

        Optional<Product> producttest = productRepository.findById(id);
        assertTrue(producttest.isEmpty());
    }

}