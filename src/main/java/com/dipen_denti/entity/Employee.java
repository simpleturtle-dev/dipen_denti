package com.dipen_denti.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults ( level = AccessLevel.PRIVATE)
public class Employee {

    @Column ( length = 18, nullable = false)
    String name;
    @Column ( length = 18, nullable = false )
    String surname;

    @Id
    @Column ( length = 16 )
    String taxCode;

    @Column ( nullable = false )
    Date birth;

    @Column ( length = 10, nullable = false )
    String role;

    @Column ( length = 15, nullable = false )
    float salary;

    @Column ( length = 13, nullable = false )
    String contract;

    @ElementCollection( targetClass = String.class, fetch = FetchType.EAGER )
    @CollectionTable( name = "competences", joinColumns = @JoinColumn(name = "tax_code") )
    @Column(name = "competence")
    List <String> competenceList;
}

/*
    * {
        "name": null,
        "surname": null,
        "taxCode": null,
        "birth": null,
        "role": null,
        "payPerYear": 0.0,
        "contract": null,
        "competenceList": [
            "Competence1", 
            "Competence2", 
            "Competence3"
            ]
    *   }
*/