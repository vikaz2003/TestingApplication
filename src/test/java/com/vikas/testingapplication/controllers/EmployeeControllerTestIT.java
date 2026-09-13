package com.vikas.testingapplication.controllers;

import com.vikas.testingapplication.TestContainerConfiguration;
import com.vikas.testingapplication.dto.EmployeeDto;
import com.vikas.testingapplication.entities.Employee;
import com.vikas.testingapplication.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;



class EmployeeControllerTestIT extends AbstractIntegrationTest{



    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;

    @BeforeEach
    void setUp(){
        testEmployee =Employee.builder()

                .email("vikaskunch03@gmail.com")
                .name("vikas")
                .salary(100L)
                .build();

        testEmployeeDto= EmployeeDto.builder()
                .email("vikaskunch03@gmail.com")
                .name("vikas")
                .salary(100L)
                .id(1L)
                .build();
        employeeRepository.deleteAll();
    }


    @Test
    void testGetEmployeeById() {
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(EmployeeDto.class)
//                .isEqualTo(testEmployeeDto)

                .value(employeeDto -> {
                    assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
                });



    }


    @Test
    void testGetEmployeeById_Failure(){
        webTestClient.get()
                .uri("employees/{id}",1L)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExistsThenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDto.class).equals(testEmployeeDto);
    }


    @Test
    void testUpdateEmployeeWhenEmployeeDoesNotExists(){
        webTestClient.put()
                .uri("/employees/{id}",929)
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployeeWhenAttemptingToUpdateTheEmail_thenReturnThrowException(){
           Employee savedEmployee=employeeRepository.save(testEmployee);
           testEmployeeDto.setEmail("random@gmail.com");
           webTestClient.put()
                   .uri("/employees/{id}",savedEmployee.getId())
                   .bodyValue(testEmployeeDto)
                   .exchange()
                   .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_Success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setSalary(250L);
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class).equals(testEmployeeDto);
    }


    @Test
    void testDeleteEmployeeWhenEmployeeDoesNotExists(){
        webTestClient.delete()
                .uri("employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testDelete_SUCESS(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.delete()
                .uri("employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }



}
