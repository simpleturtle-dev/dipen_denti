package com.dipen_denti.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dipen_denti.entity.Employee;
import com.dipen_denti.model.ContractTypes;
import com.dipen_denti.model.RoleTypes;
import com.dipen_denti.repository.EmployeeRepository;
import com.dipen_denti.tools.ValueChecker;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    public EmployeeServiceImpl ( EmployeeRepository employeeRepository ){
        this.employeeRepository = employeeRepository;
    }


    // CREATE ::::::::::::::::::::::::::::::::
    @Override
    public String createEmployee(Employee employee) {
        
        // Check if program recieved a employee
        if ( employee == null )
            return "Devi inserire un dipendente";
        
        // Check if employee object has all the required fields 
        if ( isNotFilled(employee) )
            return "Devi riempire tutti i campi obbligatori";

        // Capitalizing role and contract for easier controls
        employee.setRole(employee.getRole().toUpperCase());
        employee.setContract(employee.getContract().toUpperCase());

        // Check if employee object has all the required fields correctly compiled
        String result = isNotCorrectlyFilled(employee);
        if ( !result.equals("clear") )
            return result;
        
        // Check if same employee is already been saved before
        if ( employeeRepository.existsById(employee.getTaxCode()) )
            return "Dipendente giá salvato";

        employeeRepository.save(employee);
        
        // Check if employee inserting in database was successfull
        if ( !employeeRepository.existsById(employee.getTaxCode()) )
            return "Impossibile salvare dipendente";

        return "Dipendente salvato";
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    
    // READ ::::::::::::::::::::::::::::::::::
    @Override
    public List<Employee> findAllEmployees() {
        
        return employeeRepository.findAll();
    }
    
    @Override
    public Employee findEmployee(String taxCode) {

        Optional < Employee > employee = employeeRepository.findById(taxCode);
        if ( employee.isEmpty() )
            return null;

        return employee.get();
    }
    
    @Override
    public List<Employee> findAllEmployeesWithBigRal( float salary ) {
        
        return employeeRepository.findBySalaryGreaterThan( salary );
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    


    
    // UPDATE ::::::::::::::::::::::::::::::::
    @Override
    public String updateEmployee(Employee employee, String taxCode) {

        // Check if program recieved a employee
        if ( employee == null )
            return "Devi inserire un dipendente";
        
        // Check if employee object has all the required fields 
        if ( isNotFilled(employee) )
            return "Devi riempire tutti i campi obbligatori";

        // Check if emplyee tax code is the same as the given taxcode
        if ( !employee.getTaxCode().equals(taxCode) )
            return "Tax code del url e tax code del body devono combaciare!";

        // Check if employee object has all the required fields correctly compiled
        String result = isNotCorrectlyFilled(employee);
        if ( !result.equals("clear") )
            return result;

        // Check if same employee is already been saved before
        if ( !employeeRepository.existsById(employee.getTaxCode()) )
            return "Dipendente non presente, impossibile aggiornare";

        employeeRepository.save(employee);

        // Check if employee taxCode is the same as the one given
        return "Dipendente aggiornato";
        
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




    // DELETE ::::::::::::::::::::::::::::::::
    @Override
    public String deleteEmployee(String taxCode) {

        // Check if employee is in database
        if ( !employeeRepository.existsById(taxCode) )
            return "Dipendente non esiste, impossibile cancellare";
        
        employeeRepository.deleteById(taxCode);

        // Check if employee is still in database
        if ( employeeRepository.existsById(taxCode) )
            return "Impossibile cancellare";

        return "Cancellato";
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    

    // ? VERFYING METHODS ::::::::::::::::::::
    // Method to check if employee object has all the required fields
    private boolean isNotFilled ( Employee employee ) {
        
        if ( employee.getName() == null 
        || employee.getSurname() == null
        || employee.getTaxCode() == null 
        || employee.getSalary() <= 0
        || employee.getContract() == null 
        || employee.getRole() == null
        || employee.getBirth() == null )
            return true;

        return (   employee.getName().isBlank() 
        || employee.getSurname().isBlank()
        || employee.getTaxCode().isBlank() 
        || employee.getSalary() <= 0
        || employee.getContract().isBlank() 
        || employee.getRole().isBlank()
        || employee.getBirth() == null 
        );
    }


    
    
    // Method to check if employee object has all the required fields correctly compiled
    private String isNotCorrectlyFilled ( Employee employee ) {
        
        // Check if there are special charachters in name, surname and taxcode
        if ( new ValueChecker().checkForSpecialCharacters(
                Arrays.asList(employee.getName(),employee.getSurname(), employee.getTaxCode())))
                    return "Non mettere caratteri speciali!";
            
            
        // Check if selected contract type is one of the accepted one
        int i = 4;
        for (ContractTypes type : ContractTypes.values()) {
            if ( !employee.getContract().equals( type.toString() ) )
                i--;
        }

        if ( i == 0 )
            return "Scegli un tipo di contratto esistente";
            
            
            
        // Check if selected role type is one of the accepted one
        i = 4;
        for (RoleTypes type : RoleTypes.values()) {
            if ( !employee.getRole().equals( type.toString() ) )
                i--;
        }

        if ( i == 0 )
            return "Scegli un tipo di ruolo esistente";
            
            
            
        // Check if, in case Contract Type is STAGE, the contract is STAGITSTA
        // and viceversa
        if (    employee.getContract().equals( "STAGE" ) 
            && !employee.getRole().equals("STAGISTA"))
                return 
                    "Un contratto da stage non puó essere assegnato ad un non stagista";
            
        if (    employee.getRole().equals( "STAGISTA" ) 
            && !employee.getContract().equals("STAGE"))
                return "Un ruolo da stagista non puó essere assegnato ad un non stage";


            // Check if PayPerYear is valid
        if ( employee.getSalary() < 6000 )
            return "La paga minima 6000";
        
        
        
        // Check if employee is at least 18
        if ( new ValueChecker().checkIfPersonIsEighteen( employee.getBirth()))
            return "Il dipendente deve essere almeno diciottenne";
        
        return "clear";
        
    }
    // ? ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
