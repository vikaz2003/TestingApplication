package com.vikas.testingapplication.services.impl;

import com.vikas.testingapplication.dto.EmployeeDto;
import com.vikas.testingapplication.entities.Employee;
import com.vikas.testingapplication.exceptions.ResourceNotFoundException;
import com.vikas.testingapplication.repositories.EmployeeRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {


    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp(){
        mockEmployee=Employee.builder()
                .id(1L)
                .email("vikaskunch03@gmail.com")
                .name("vikas")
                .salary(100L)
                .build();
        mockEmployeeDto=modelMapper.map(mockEmployee, EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_whenEmailIdISPresentThenReturnEmployeeDto(){
        //assign
        Employee mockedEmployee=Employee.builder()
                         .id(1L)
                        .email("vikaskunch03@gmail.com")
                        .name("vikas")
                        .salary(100L)
                .build();
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(mockedEmployee));

        //act
        EmployeeDto employee=employeeService.getEmployeeById(1L);

        //assert
        assertThat(employee.getName()).isEqualTo(mockedEmployee.getName());
        assertThat(employee.getId()).isEqualTo(mockedEmployee.getId());
        verify(employeeRepository,atLeastOnce()).findById(1l);

    }

    @Test
    void testGetEmployeeById_whenEmailIdIsNotPresent(){
        when(employeeRepository.findById(2L))
                .thenReturn(Optional.empty());


        assertThatThrownBy(()-> employeeService.getEmployeeById(2L))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void testCreateNewEmployee_WhenValidEmployeeIsToBeCreated(){

        // assign
        when(employeeRepository.findByEmail("vikaskunch03@gmail.com")).thenReturn(new ArrayList<>());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        // act
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);


        // assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository,times(1)).save(employeeArgumentCaptor.capture());
        verify(employeeRepository,times(1)).findByEmail(employeeDto.getEmail());


    }


    @Test
    void testCreateNewEmployee_WhenEmployeeAlreadyExists(){
        //assign
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail()))
                .thenReturn(List.of(mockEmployee));
        //act and assert

        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployeeDto.getEmail());


        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());


    }


    @Test
    void testUpdateEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException(){
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(employeeRepository).findById(1L);
    }


    @Test
    void testUpdateEmployeeWhenAttemptingToUpdateEmail_ThenThrowException(){
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");
        assertThatThrownBy(()-> employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");
        verify(employeeRepository,never()).save(mockEmployee);

    }


    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){
        // arrange
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("random");
        mockEmployeeDto.setSalary(1000L);
        Employee newEmployee=modelMapper.map(mockEmployeeDto,Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        //act

        EmployeeDto updatedEmployeeDto=employeeService.updateEmployee(1L,mockEmployeeDto);


        //assert

        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).save(any());
        verify(employeeRepository).findById(1L);

    }




    @Test
    void testDeleteEmployee1(){
        when(employeeRepository.existsById(1L))
                .thenReturn(false);
        //act
        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1L);

        verify(employeeRepository,never()).deleteById(anyLong());
    }


    @Test
    void testDeleteEmployee2(){
        when(employeeRepository.existsById(1L))
                .thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);

    }







}