package org.example.service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public abstract class BaseService<T> {
    protected JpaRepository<T, UUID> repository;
    private static final Logger LOGGER = LogManager.getLogger();
    public List<T> readFromJsonFile(String filePath,
                                    JpaRepository<T,
                                            UUID> repository)
            throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        List<T> products;
        var name=repository.getClass().getName();
        if (file.exists() && file.length() > 0) {
            try {
                products = mapper.readValue(file, new TypeReference<>() {
                });
            } catch (IOException e) {
                throw new RuntimeException("Error reading JSON file: " + e.getMessage(), e);
            }
            if (repository.count() == 0) {
                repository.saveAll(products);
                System.out.println("Products imported into the database.");
            } else {
                System.out.println("Products already exist in the database.");
            }
            return products;
        }else{
            LOGGER.info(name+" data file not found. Creating new file...");
            if(repository.count()>0){
                products=repository.findAll();
                writeItemsToJsonFile(products,filePath);
                LOGGER.info("New "+name+" data file created with existing items from the database.");
            }else {

                LOGGER.info("No "+name+" found in the database.");
            }
        }
        return new ArrayList<>();
    }

    public void addToFile(List<T> entities, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        mapper.writeValue(file, entities);
    }

    @Transactional
    public void addEntity(T entity, String filePath, JpaRepository<T, UUID> repository) throws IOException, ClassNotFoundException {
        repository.save(entity);
        List<T> entities = readFromJsonFile(filePath, repository);
        entities.add(entity);
        addToFile(entities, filePath);
    }

    private void writeItemsToJsonFile(List<T> list,String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    LOGGER.info("A new file for items is created by the path: " + file.getAbsolutePath());
                } else {
                    LOGGER.warn("Couldn't create a new file. The file may already exist.");
                }
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
            LOGGER.info("Items successfully written to the file.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing items to file: " + e.getMessage(), e);
        }
    }

    public List<T> getList() {
        return repository.findAll();
    }

    protected abstract Class<T> getEntityClass();
}
