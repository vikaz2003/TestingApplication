package com.vikas.testingapplication;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestingApplicationTests {

    @Test
    @DisplayName("test number two")
    @Order(1)
    void testNumberTwo() {
           log.info("In test number two");
           int a=5;
           int b=0;
           assertThatThrownBy(()-> divide(a,b))
                   .isInstanceOf(ArithmeticException.class)
                   .hasMessage("fucked");
    }




    @Test
    @Order(2)
    void testNumberOne(){
        log.info("In test number one");
        int a=5;
        int b=3;
        int res=add(a,b);


        assertThat(res).isEqualTo(8);
        assertThat("Apple").contains("App");
    }

    int add(int a,int b){
        return a+b;
    }

    @BeforeAll
    static void setUpOnce(){
        log.info("Nefore all testing");
    }


    @BeforeEach
    void beforeEach(){
         log.info("Before Each");
    }

    int divide(int a,int b){
        try{
            return  a/b;
        }catch (ArithmeticException e){
            log.error("arithmetic exception occured");
            throw new ArithmeticException("fucked");
        }
    }


}
