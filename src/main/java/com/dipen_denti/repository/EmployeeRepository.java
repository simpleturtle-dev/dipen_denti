package com.dipen_denti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipen_denti.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository < Employee, String > {
    

    // Metodo per leggere i dipendenti con il ral maggiore di ( float salary )
    public List< Employee > findBySalaryGreaterThan( float salary );
}
