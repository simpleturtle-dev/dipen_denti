package com.dipen_denti.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipen_denti.entity.Employee;
import com.dipen_denti.service.EmployeeService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@CrossOrigin
@RequestMapping ( "/employee-controller" )
public class EmployeeController {

    private EmployeeService employeeService;
    public EmployeeController ( EmployeeService employeeService ){
        this.employeeService = employeeService;
    }

    // CREATE ::::::::::::::::::::::::::::::::::::::
    @PostMapping ( "/hire-employee" )
    public ResponseEntity < String > hireemployee ( @RequestBody Employee employee) {

        String result = employeeService.createEmployee(employee);

        if ( !result.equals( "Dipendente salvato" ))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body( result );

        return ResponseEntity.status(HttpStatus.OK).body( result );
    } 
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


    
    // READ ::::::::::::::::::::::::::::::::::::::::
    @GetMapping ( "/all-employees" )
    public ResponseEntity < List < Employee > > allemployees () {
        
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.findAllEmployees());
    }
    
    @GetMapping ( "/employee/{taxCode}" )
    public ResponseEntity < Employee > specificemployee ( @PathVariable String taxCode ) {
        
        Employee employee = employeeService.findEmployee(taxCode);

        if ( employee == null )
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body( new Employee() );
        
        return ResponseEntity.status(HttpStatus.OK).body( employee );
    }
    
    @GetMapping ( "/employees/{salary}k" )
    public ResponseEntity < List < Employee > > 
     allEmployeesWithBigRal ( @PathVariable float salary ) {
        
        return ResponseEntity.status(HttpStatus.OK).body(
                        employeeService.findAllEmployeesWithBigRal( salary ));
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    // UPDATE ::::::::::::::::::::::::::::::::::::::
    @PutMapping ( "/update/{taxCode}" )
    public ResponseEntity < String > modifyemployee ( @PathVariable String taxCode,
        @RequestBody Employee employee ) {

        String result = employeeService.updateEmployee(employee , taxCode);

        if ( !result.equals("Dipendente aggiornato") )
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body( result );

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    // DELETE ::::::::::::::::::::::::::::::::::::::
    @DeleteMapping ( "/delete-employee/{taxCode}" )
    public ResponseEntity < String > deleteemployee ( @PathVariable String taxCode ) {

        String result = employeeService.deleteEmployee(taxCode);

        if ( !result.equals("Cancellato") )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    // ? DELEVOPER APIs
    // ? EASIER TIME WITH NON-SWAGGER API PLATFORMS ::::::::::::::::::::::
    @Hidden
    @GetMapping ( "/developer/employee-json" )
    public String employeeJson () {
                
                return "{\n" + 
                "\t\"name\": \"name\",\n" +
                "\t\"surname\": \"surname\",\n" + 
                "\t\"taxCode\": \"NNNSSS12X12X\",\n" +
                "\t\"birth\": \"YYYY-dd-MM\",\n" +
                "\t\"role\": \"STAGISTA|DIPENDENTE|MANAGER|DIRETTORE\",\n" +
                "\t\"salary\": 0.00,\n" +
                "\t\"contract\": \"INDETERMINATO|DETERMINATO|STAGE|PARTTIME\",\n" +
                "\t\"competenceList\": [\n" +
                "\t\t\"Competence1\",\n" +
                "\t\t\"Competence2\",\n" +
                "\t\t\"Competence3\"\n" +
                "\t]\n" +
                "}";
    }
    // ? ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
