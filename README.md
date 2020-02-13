# progetto-economia
Progetto Economia-Bilancio

-SISTEMARE GRAFICAMENTE HOME

-sistemare combo box voci di bilancio... non per tutte le voci è sufficentemente ampia!

-fare esercizi per vedere se tutto è corretto ALMENO 2!

-CONTROLLARE GUIDA E LE **TUTTE** LE VOCI DI BILANCIO ATTIVO PASSIVO E CE PER ERRORI ORTOGRAFICI E CHE NON CI SIANO DUE VOCI O SOTTOVOCI CON LO STESSO NOME

- SCEGLIERE NOME E ICONA 

-Release v1.0

-GUIDA

**nome programma** permette di redigere, ed esportare in un pdf, un riepilogo del bilancio dell'azienda della quale si sono inseriti i rispettivi mastrini all'interno dell'applicativo. 
Nella home page che si apre cliccanfo sul file **nome programma**.jar è possibile da subito inserire una nuova azienda, cliccando sul pulsante Aggiungi azienda. Dopo aver cliccato, nella pagina che si aprirà, è possibile inserire il nome dell'azienda e una relativa descrizione. Vengono effettuati alcuni controlli sui dati inseririti (ad esempio viene verificato che sia stato inserito il nome dell'azienda e che sia al massimo di 30 caratteri). Per confermare e inserire l'azienda premere ok.
Successivamente, affinchè sia possibile inserire un bilancio, è necessario selezionare una azienda precedentemente inserita nel database, cliccando la rispettiva voce nel menù a tendina, dove inizialmente sarà indicato il messaggio *Azienda non selezionata!*. E' ora possibile inserire un bilancio relativo all'azienda cliccando il pulsante "Inserisci Bilancio". Si aprirà quindi una finestra nella quale sarà possibile selezionare l'anno del bilancio. Non è possibili inserire bilanci dell'anno corrente (in cui si apre l'applicazione) o futuri: deve essere un anno passato! E' possibile poi confermare l'inserimento cliccando sul pulsante ok, oppure chiudere la finestra cliccando il pulsante chiudi che richiederà una conferma della chiusura. Ora sarà possibile inserire mastrini relativi al bilancio inserito, oppure sarà possibile inserire mastrini relativi a bilanci già inseriti precedentemente (relativi all'azienda selezionata), selezionandoli nel munù a tendina "Bilancio".
Se sono già stati inseriti alcuni mastri nel bilancio selezionato, essi appariranno in una tabella. Se si vuole aggiungere un mastrino sarà possibile selezionare: Attitvo/Passivo, Dare/Avere, il valore del mastrino e la voce del bilancio nella quale si vuole inserire il mastrino. Le voci del bilancio relative all'attivo/passivo/conto economico vengono caricate nel menù a tendina Voce bilancio, dal quale possono essere selezionate. E' possibile inoltre inserire una descrizione del mastrino. Successivamente è possibile inserire il mastrino cliccano sul pulsante "Aggiungi Mastrino" e se tutte le voci sono state selezionate nel modo corretto (e non vengono visualizzati errori) il mastrino viene inserito nel bilancio e mostrato nella tabella. 
Una volta completato l'inserimento di tutti i mastrini è possibile esportare l'intero bilancio, dopo aver indicato la percentuale della tassazione voluta nella casella Imposte(%), in un file pdf, nella quale saranno riportati tutte le voci inseriti e le relative tabelle di Attivo Passivo e Conto Economico. Il file pdf viene salvato nella cartella nella quale è presente il .jar dell'applicazione. Il nome del file sarà nomeazienda_annobilancio.pdf. Se il bilancio non è bilanciato (totale attivo diverso dal totale passivo) viene mostrato un messaggio di conferma dell'esportazione.
E' possibile inoltre eliminare dal database le aziende e i bilanci relativi cliccando sul pulsate Cancella Azienda e Cancella Bilancio e selezionare nel menù a tendina.
E' possibile anche eliminare un mastrino, selezionandolo nella tabella e cliccando Rimuovi Mastrino. 
