# progetto-economia
Il tuo bilancio

-Release v1.0

-GUIDA

Manuale d’uso di “Il tuo bilancio”
                                                   Univesità degli studi di Bergamo 
                                 Progetto di Economia e organizzazione aziendale (Ing. Informatica) 19/20 

A cura di Matteo Verzeroli 1057926, Mohammed Amine Bikhtancer 1057874 e Davide Salvetti 1057596

Introduzione
“Il tuo bilancio” è un software che permette di organizzare aziende e i rispettivi bilanci, e di redigere ed esportare in formato pdf un riepilogo del bilancio di ogni azienda. L’obbiettivo è quello di fornire all’utente uno strumento per il calcolo automatico di attivo, passivo e conto economico.
Come usare “Il tuo bilancio”
Nella home page che si apre all’avvio del programma è possibile inserire una nuova azienda, cliccando sul pulsante “Aggiungi Azienda” in alto a destra. Si aprirà una schermata come la seguente:
 
Qui verrà richiesto di inserire il nome e una descrizione dell’azienda che si vuole inserire.
ATTENZIONE: il nome dell’azienda non può superare i 30 caratteri e non si possono inserire due aziende con lo stesso nome!
Dal momento in cui si preme il tasto “Ok” l’azienda diventa disponibile nel menu a tendina contenente tutte le aziende inserite e che al momento dell’avvio conterrà la voce “*Azienda non selezionata!*” proprio per indicare all’utente che prima di creare un bilancio deve selezionarne una.
 
Tramite il pulsante “Inserisci Bilancio” si aprirà una finestra che chiederà di quale anno di bilancio stiamo parlando. 

ATTENZIONE: non è possibile inserire bilanci né dell’anno corrente (anno in cui si apre l’applicazione) perché ancora non è possibile fare un bilancio completo, né di anni futuri.
Da questo momento è possibile inserire i mastrini all’interno del bilancio che è selezionato nel menu a tendina “Bilancio”, oppure è possibile modificare bilanci inseriti precedentemente, selezionandoli dal menù a tendina ed eliminando o inserendo mastrini.
I mastrini inseriti nel bilancio compariranno nella tabella nella parte sinistra dell’applicazione.
Se si vuole aggiungere un mastrino sarà possibile selezionare: Attivo/Passivo/Conto Economico, Dare/Avere, il valore del mastrino e la voce del bilancio nella quale si vuole inserire il mastrino. Le voci del bilancio relative all' Attivo/Passivo/Conto Economico vengono caricate automaticamente nel menù a tendina “Voce bilancio”, dal quale possono essere selezionate. E' possibile inoltre inserire una descrizione del mastrino.

Una volta completato l'inserimento di tutti i mastrini è possibile esportare l'intero bilancio, dopo aver indicato la percentuale della tassazione voluta nella casella “Imposte(%)”, in un file pdf, all’interno del quale saranno riportate tutte le voci inserite e le relative tabelle di Attivo, Passivo e Conto Economico.
Il file pdf viene salvato nella cartella nella quale è presente il file.jar dell'applicazione. Il nome del file sarà nomeazienda_annobilancio.pdf. Se il bilancio non è bilanciato (totale attivo diverso da totale passivo) viene mostrato un messaggio di conferma dell'esportazione. 
E' anche possibile eliminare dal database le aziende e i bilanci relativi ad esse cliccando sul pulsate “Cancella Azienda” e “Cancella Bilancio” e selezionando dal menù a tendina quale azienda/bilancio si vuole eliminare.
 
Si possono anche eliminare i mastrini, selezionandone uno direttamente dalla tabella e cliccando il pulsante “Rimuovi Mastrino”.

