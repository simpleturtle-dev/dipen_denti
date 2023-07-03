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
    // ? Metodo per creare nuovi dipendenti nel databas
    @Override
    public String createEmployee(Employee employee) {
        
        // Controllo se il programma ha ricevuto un dipendente
        if ( employee == null )
            return "Devi inserire un dipendente";
        
        // Controllo se il dipendente ha tutti i campi obbligatori riempiti 
        if ( isNotFilled(employee) )
            return "Devi riempire tutti i campi obbligatori";

        // Capitalizzo il ruolo e contratto per rendere piú facili controlli
        // e piú ordinato database
        employee.setRole(employee.getRole().toUpperCase());
        employee.setContract(employee.getContract().toUpperCase());
        
        // Capitalizzo il codice fiscale per rendere piú ordinato database
        employee.setTaxCode(employee.getTaxCode().toUpperCase());

        // Controllo se i campi obbligatori siano correttamente compilati
        String result = isNotCorrectlyFilled(employee);
        if ( !result.equals("clear") )
            return result;
        
        // Controllo se il dipendente che sto per provare a salvare giá
        // esiste nel database attraverso il codice fiscale
        if ( employeeRepository.existsById(employee.getTaxCode()) )
            return "Dipendente giá salvato";

        employeeRepository.save(employee);
        
        // Controllo se il dipendente appena salvato sia presente nel db
        if ( !employeeRepository.existsById(employee.getTaxCode()) )
            return "Impossibile salvare dipendente";

        return "Dipendente salvato";
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    
    // READ ::::::::::::::::::::::::::::::::::
    // ? Metodo per leggere tutti i dipendenti nel database
    @Override
    public List<Employee> findAllEmployees() {
        
        return employeeRepository.findAll();
    }
    
    // ? Metodo per leggere un dipendente in specifico nel database
    @Override
    public Employee findEmployee(String taxCode) {

        //In caso non trovo il dipendente chiesto mando null per eveitare eccezioni
        Optional < Employee > employee = employeeRepository.findById(taxCode);
        if ( employee.isEmpty() )
            return null;

        return employee.get();
    }

    // ? Metodo per leggere tutti i dipendenti che hanno un ral superiore a
    // ? {salary}k
    @Override
    public List<Employee> findAllEmployeesWithBigRal( float salary ) {
        
        return employeeRepository.findBySalaryGreaterThan( salary );
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    


    
    // UPDATE ::::::::::::::::::::::::::::::::
    // ? Metodo per aggiornare un {taxCode} dipendente
    @Override
    public String updateEmployee(Employee employee, String taxCode) {

        // Controllo se il programma ha ricevuto un dipendente
        if ( employee == null )
            return "Devi inserire un dipendente";
        
        // Controllo se il dipendente ha tutti i campi obbligatori riempiti 
        if ( isNotFilled(employee) )
            return "Devi riempire tutti i campi obbligatori";

        // Capitalizzo il ruolo, contratto e taxCode per rendere piú facili controlli
        // e piú ordinato database
        employee.setRole(employee.getRole().toUpperCase());
        employee.setContract(employee.getContract().toUpperCase());
        employee.setTaxCode(employee.getTaxCode().toUpperCase());

        // Controllo se il codice fiscale dato sia uguale al codice fiscale dentro il
        // body del dipendente che si vuole aggiornare
        if ( !employee.getTaxCode().equals(taxCode) )
            return "Tax code del url e tax code del body devono combaciare!";

        // Controllo se i campi obbligatori siano correttamente compilati
        String result = isNotCorrectlyFilled(employee);
        if ( !result.equals("clear") )
            return result;

        // Controllo se il dipendente che sto per provare ad aggiornare giá
        // esiste nel database attraverso il codice fiscale
        if ( !employeeRepository.existsById(employee.getTaxCode()) )
            return "Dipendente non presente, impossibile aggiornare";

        employeeRepository.save(employee);

        return "Dipendente aggiornato";
        
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




    // DELETE ::::::::::::::::::::::::::::::::
    // ? Metodo per cancellare un {taxCode} dipendente
    @Override
    public String deleteEmployee(String taxCode) {

        // Controllo se il dipendente che sto per provare a cancellare
        // esiste nel database attraverso il codice fiscale
        if ( !employeeRepository.existsById(taxCode) )
            return "Dipendente non esiste, impossibile cancellare";
        
        employeeRepository.deleteById(taxCode);

        // Controllo se il dipendente che ho provarto a cancellare
        // esiste nel database attraverso il codice fiscale;
        // se esiste ancora la cancellazione é fallita
        if ( employeeRepository.existsById(taxCode) )
            return "Impossibile cancellare";

        return "Cancellato";
    }
    // ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    

    // ? METODI DI CONTROLLO ::::::::::::::::::::
    // ? Metodo per controllare se il dipendente ha tutti i campi obbligatori
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


    
    
    // ? Metodo per controllare se il dipendente ha tutti i campi obbligatori
    // ? correttamente compilati
    private String isNotCorrectlyFilled ( Employee employee ) {
        
        // Controllo se ci sono caratteri speciali nel codice fiscale
        if ( new ValueChecker().checkForSpecialCharacters(
                Arrays.asList(employee.getTaxCode())))
                    return "Non mettere caratteri speciali!";

        // Controllo se ci sono caratteri speciali e numeri
        // nel cnome e cognome
        if ( new ValueChecker().checkForSpecialCharactersAndNumbers(
                Arrays.asList(employee.getName(),employee.getSurname())))
                    return "Non mettere caratteri speciali!";
            
            
        // Controllo se il tipo di contratto ricevuto é fra quelli gestiti dal programma
        int i = 4;
        for (ContractTypes type : ContractTypes.values()) {
            if ( !employee.getContract().equals( type.toString() ) )
                i--;
        }
        if ( i == 0 )
            return "Scegli un tipo di contratto esistente";
            
            
            
        // Controllo se il tipo di ruolo ricevuto é fra quelli gestiti dal programma
        i = 4;
        for (RoleTypes type : RoleTypes.values()) {
            if ( !employee.getRole().equals( type.toString() ) )
                i--;
        }
        if ( i == 0 )
            return "Scegli un tipo di ruolo esistente";
            
            
            
        // Controllo se, in caso sia stato scelto stage come contratto o stagista come
        // ruolo, sia, rispettivo al caso, stato scelto stagista come ruolo o stage
        // come contratto
        if (    employee.getContract().equals( "STAGE" ) 
            && !employee.getRole().equals("STAGISTA"))
                return 
                    "Un contratto da stage non puó essere assegnato ad un non stagista";
            
        if (    employee.getRole().equals( "STAGISTA" ) 
            && !employee.getContract().equals("STAGE"))
                return "Un ruolo da stagista non puó essere assegnato ad un non stage";


        // Ho messo un RAL minimo di 6000
        if ( employee.getSalary() < 6000 )
            return "La paga minima 6000";
        
        
        
        // Controllo se il dipendente é maggiorenne
        if ( new ValueChecker().checkIfPersonIsEighteen( employee.getBirth()))
            return "Il dipendente deve essere almeno diciottenne";
        
        return "clear";
        
    }
    // ? ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
