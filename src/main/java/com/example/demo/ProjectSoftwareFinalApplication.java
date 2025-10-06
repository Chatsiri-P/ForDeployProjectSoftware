package com.example.demo;

import java.io.File;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.*;
import com.example.demo.repository.*;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectSoftwareFinalApplication implements CommandLineRunner{

	@Autowired
	private SystemRoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRoleRepository eventRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ProjectSoftwareFinalApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		createUploadDirectories();
        seedRoles();
        seedEventRoles();
        seedCategories();
        seedAdminUser();
	}
	
	public void createUploadDirectories() {
		new File("uploads/profile").mkdirs();
        new File("uploads/events").mkdirs();
	}
	

    private void seedRoles() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new SystemRole("ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new SystemRole("ROLE_ADMIN"));
        }
    }

    private void seedEventRoles() {
        if (eventRoleRepository.findByName("ORGANIZER").isEmpty()) {
            eventRoleRepository.save(new EventRole("ORGANIZER"));
        }
        if (eventRoleRepository.findByName("PARTICIPANT").isEmpty()) {
            eventRoleRepository.save(new EventRole("PARTICIPANT"));
        }
    }

    private void seedCategories() {
    	Object[][] categoriesData = {{"Study & Skill Sharing", "Learn, study, and share skills together", "bi-book"}
        		, {"Sports & Fitness", "Play sports and stay active with friends", "bi-heart-pulse"}
        		, {"Food & Hangouts", "Eat, chill, and explore new spots together", "bi-cup-straw"}
        		, {"Music & Arts", "Create, jam, and enjoy arts together", "bi-palette"}
        		, {"Volunteering & Social Impact", "Make a difference through volunteering and service", "bi-hand-thumbs-up"}
        		, {"Travel & Adventure", "Discover new places, trips, and adventures", "bi-compass"}
        		, {"Gaming & Esports","Play, compete, and connect through games", "bi-controller"}
        		, {"Clubs & Hobbies", "Share passions, join clubs, and grow hobbies", "bi-puzzle"}};
        for (Object[] categoryData : categoriesData) {
            String name = (String) categoryData[0];
            String description = (String) categoryData[1];
            String iconClass = (String) categoryData[2];
            
            if (categoryRepository.findByName(name).isEmpty()) {
                categoryRepository.save(new Category(name, description, iconClass));
            }
        }
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            
            SystemRole adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            admin.setRoles(Set.of(adminRole));
            
            userRepository.save(admin);
        }
    }
	
}
