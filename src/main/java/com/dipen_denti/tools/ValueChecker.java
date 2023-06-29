package com.dipen_denti.tools;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

// * Class that checks genral things in the most universal way possible, so that the
// * application is easy extendable
@NoArgsConstructor
public class ValueChecker {

    // Check if given strings have special characters ( made universal by recieving
    // a list of strings instead of fixed number of strings )
    public boolean checkForSpecialCharacters ( List < String > strings ) {

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        for (String string : strings) 
            if ( special.matcher(string).find() )
                return true;
        
        return false;
    }

    // Check if given date is at least eighteen from now
    public boolean checkIfPersonIsEighteen ( Date date ){

        LocalDate birth = date.toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears() < 18;

    }
    
}
