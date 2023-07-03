package com.dipen_denti.tools;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

// * Ho creato questa classe per rendere questi tipi di controlli quanto piú generici
// * possibili, cosí che il programma é piú facile da estendere
@NoArgsConstructor
public class ValueChecker {

    // Controlla se delle stringhe date in paramentro hanno caratteri speciali 
    // ( fatto in maniera universale, dato che riceve in input una lista e non un 
    // numero fisso di stringhe) 
    public boolean checkForSpecialCharacters ( List < String > strings ) {

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        for (String string : strings) 
            if ( special.matcher(string).find() )
                return true;
        
        return false;
    }


    // Controlla se delle stringhe date in paramentro hanno caratteri speciali 
    // o numeri ( fatto in maniera universale, dato che riceve in input una lista 
    // e non un numero fisso di stringhe) 
    public boolean checkForSpecialCharactersAndNumbers ( List < String > strings ) {

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        for (String string : strings) 
            if ( special.matcher(string).find() )
                return true;

        special = Pattern.compile ("\\d");

        for (String string : strings) 
            if ( special.matcher(string).find() )
                return true;
        
        return false;
    }

    // Controlla se la data in input é minore di 18 anni fa
    public boolean checkIfPersonIsEighteen ( Date date ){

        LocalDate birth = date.toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears() < 18;

    }
    
}
