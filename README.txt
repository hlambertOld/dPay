Persistens fungerer via en XML-fil, der indeholder payments. Payments directoriet bliver specificeret i WEB-INF/classes/jwig.properties, og skal eksistere ved opstart. Selve XML-filen bliver genereret undervejs.

Vi har valgt at bruge ren Java af IDE-hensyn og fordi vi skulle skrive så relativt lidt XML. I et større system giver det naturligvis ikke mening.

Det største problem vi havde var uønsket caching, men det blev løst med hjælp fra webboardet. Forsøg med at bruge JWIGs indbyggede dependency graf kunne vi kun få til at fungere med /pay.

Der er et par enkelte TODOs tilbage i PaymentsApp, hvor der bliver kastet exceptions i stedet for en mere "pæn" håndtering. Vi valgte at se lidt stort på det, da det kun sker i situationer, hvor der på en dBay instans bliver aktivt ændret i et item på en måde, så den ikke længere er konsistent, eller i situationen, hvor en dBay instans går ned. 

Ellers relativt nemt at arbejde med (i forhold til JSF2 og Struts2).