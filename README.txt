Persistens fungerer via en XML-fil, der indeholder payments. Payments directoriet bliver specificeret i WEB-INF/classes/jwig.properties, og skal eksistere ved opstart. Selve XML-filen bliver genereret undervejs.

Vi har valgt at bruge ren Java af IDE-hensyn og fordi vi skulle skrive så relativt lidt XML. I et større system giver det naturligvis ikke mening.

Det største problem vi havde var uønsket caching, men det blev løst med hjælp fra webboardet. Forsøg med at bruge JWIGs indbyggede dependency graf kunne vi kun få til at fungere med /pay.

Vi har generelt end del "should never happen" RuntimExceptions. Det kan selvfølgelig laves pænere, ved at lave et ordentligt exceptions-træ, og håndtere fejlene undervejs, men det opfattede vi som mindre betydende detaljer. 

Ellers relativt nemt at arbejde med (i forhold til JSF2 og Struts2).