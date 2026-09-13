package com.vikas.testingapplication.repositories;

import com.vikas.testingapplication.TestContainerConfiguration;
import com.vikas.testingapplication.entities.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;


    private Employee employee;

    @BeforeEach
    void setUp(){
        employee=Employee.builder()
                .name("Vikas")
                .email("vikaskunch03@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValidThenReturnEmployee() {
          // arrange
          employeeRepository.save(employee);

           //act
          List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());


          //assert
          assertThat(employeeList).isNotEmpty();
          assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList(){
        // arrange
        employeeRepository.save(employee);
        String email="vivekkunch@gmail.com";

        //act
        List<Employee> employeeList=employeeRepository.findByEmail(email);


        //assert
        assertThat(employeeList).isEmpty();
        assertThat(employeeList).isNotNull();


    }
}
