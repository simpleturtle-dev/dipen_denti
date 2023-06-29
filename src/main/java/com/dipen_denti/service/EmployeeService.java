package com.dipen_denti.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dipen_denti.entity.Employee;

@Service
public interface EmployeeService {
    
    //CREATE
    public String createEmployee ( Employee employee );

    //READ
    public List < Employee > findAllEmployees ();
    public Employee findEmployee ( String taxCode );
    public List < Employee > findAllEmployeesWithBigRal ( float pay );
    
    //UPDATE
    public String updateEmployee ( Employee employee, String taxCode );

    //DELETE
    public String deleteEmployee ( String taxCode );

}
