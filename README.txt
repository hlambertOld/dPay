Vi ville egentlig gerne checke, at vi modtager og henter validt XML via et relaxeret RELAX_NG skema (tøhø), men vi fandt ikke ud af hvordan (eller om) XACT har faciliteter til dette. Spørgsmål på webboard samt kig i manual, slides og sourcekoden gav ingen brugbare resultater.

Der er ikke brugt tid på persistens, da vi ikke mener, at det er en del af opgaven. At gemme data i hukommelsen er naturligvis ikke acceptabelt i et rigtigt system. Hibernate virkede som en meget stor hammer at smide efter det problem, når der i bund og grund er én tabel.

Vi har valgt at bruge ren java af IDE-hensyn og fordi vi skulle skrive så relativt lidt XML. I et større system giver det naturligvis ikke mening.

Det største problem vi havde var uønsket caching, men det blev løst med hjælp fra webboardet. Forsøg med at bruge JWIGs indbyggede dependency graf kunne vi kun få til at fungere med /pay.

Ellers relativt nemt at arbejde med (i forhold til JSF2 og Struts2).