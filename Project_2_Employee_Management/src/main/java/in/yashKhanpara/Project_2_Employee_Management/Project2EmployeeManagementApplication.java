package in.yashKhanpara.Project_2_Employee_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Employee Management REST API.
 *
 * <p>This class bootstraps the Spring Boot application and starts the embedded server.</p>
 */
@SpringBootApplication
public class Project2EmployeeManagementApplication {

	/**
	 * Launches the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(Project2EmployeeManagementApplication.class, args);
	}

}
