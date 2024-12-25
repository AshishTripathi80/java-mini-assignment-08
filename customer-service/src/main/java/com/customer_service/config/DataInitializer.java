package com.customer_service.config;



import com.customer_service.entity.Customer;
import com.customer_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;



    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) { // Check if the repository is empty
            customerRepository.save(new Customer("Ashish Tripathi", "ashish.tripathi@example.com"));
            customerRepository.save(new Customer("Ankur Tiwari", "ankur.tiwari@example.com"));
            System.out.println("Initial customers added to the database.");
        } else {
            System.out.println("Customers already exist. Skipping initialization.");
        }
    }

}
