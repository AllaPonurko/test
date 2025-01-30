package org.example.services.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.models.products.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
public abstract class BaseService<T>  {
   protected JpaRepository<T, UUID> repository;

    public List<T> readFromJsonFile(String filePath,
                                    JpaRepository<T,
                                    UUID> repository)
                                    throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        List<T> products;
        if (file.exists() && file.length() > 0) {
            try{
                products=mapper.readValue(file, new TypeReference<>() {
                });
            }catch (IOException e) {
                throw new RuntimeException("Error reading JSON file: " + e.getMessage(), e);
            }
            if(repository.count()==0){
                repository.saveAll(products);
                System.out.println("Products imported into the database.");
            }else {
                System.out.println("Products already exist in the database.");
            }
            return products;
        }
        return new ArrayList<>();
    }

    public void addToFile(List<T> entities,String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        mapper.writeValue(file, entities);
    }
    @Transactional
    public void addEntity(T entity,String filePath,JpaRepository<T, UUID> repository) throws IOException, ClassNotFoundException {
        repository.save(entity);
        List<T> entities = readFromJsonFile(filePath,repository);
        entities.add(entity);
        addToFile(entities,filePath);
    }

    public List<T> getList() {
        return repository.findAll();
    }

    protected abstract Class<T> getEntityClass();
}
