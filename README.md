#  Tiny Dungeon

Tiny Dungeon è un piccolo gioco di ruolo testuale e a turni, scritto in Java con interfaccia grafica JavaFX. Si scende dentro un dungeon stanza dopo stanza, si combattono mostri leggendo le loro intenzioni prima che attacchino, si potenzia il proprio personaggio nei Santuari(anche checkpoint come sui vari SoulsLike) e si arriva fino allo scontro con il Boss finale. Il progetto è stato pensato per essere ordinato, estendibile e facile da modificare, seguendo i principi SOLID.

---

## 🎮 Cosa fa il gioco

- Combattimento a turni con quattro azioni: Attacco Normale, Attacco Pesante, Schivata e Pozione.
- Sistema di "telegraphing": ogni mostro mostra cosa sta per fare, e tu devi scegliere la mossa giusta per rispondergli.
- Quattro tipi di mostro base (Scheletro, Zombie, Soldato, Troll), ognuno con un comportamento diverso, più un Boss finale con tre fasi.
- Santuari (checkpoint) dove recuperi tutta la vita, salvi la partita, ricevi una pozione e spendi i Frammenti per potenziare Vitalità, Forza o Fortuna.
- NPC narrativi che ti danno indizi e, in alcuni casi, ti offrono equipaggiamento da accettare o rifiutare.
- Sistema di equipaggiamento con armi e armature casuali, ognuna con vantaggi e svantaggi.
- Salvataggio e caricamento della partita su file.

---

##  Come eseguire il progetto

### Prerequisiti
- Java 25
- Gradle (incluso nel progetto tramite il wrapper, non serve installarlo a parte)

### Istruzioni

```bash
git clone <url-del-repository>
cd MpgcRpg
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

##  Uso di strumenti di AI

Per lo sviluppo di questo progetto ho utilizzato un assistente AI (Claude di Anthropic) come supporto durante la realizzazione. Di seguito dichiaro in modo trasparente come è stato usato e quale è stata la mia parte personale.

**Cosa ho fatto io personalmente:**
- Tutta la progettazione dell'architettura del software.
- La scelta e l'applicazione dei design pattern (MVC, Observer, State, Strategy).
- La definizione delle entità, delle classi, delle interfacce e dei metodi, con le relative responsabilità.
- Le decisioni su come strutturare la persistenza, il combattimento e la progressione del gioco.
- La validazione, la comprensione e l'integrazione di tutto il codice, file per file.

**Per cosa ho usato l'AI come supporto:**
- Correzione di vari errori di compilazione e a runtime.
- Generazione del file CSS per il tema grafico.
- Generazione dei testi dei dialoghi degli NPC.
- Creazione del file FXML (la parte grafica), seguendo le indicazioni che fornivo io su come volevo che fosse l'interfaccia.
- Generazione delle classi `Main` di avvio.
- Consultazione per consigli, chiarimenti e dubbi durante lo sviluppo.

Il codice è stato compreso e validato passo dopo passo: l'AI è stata uno strumento di affiancamento e non un sostituto del lavoro di progettazione, che è rimasto interamente una mia responsabilità.

**Nota sui design pattern:**  I design pattern non erano un requisito esplicito dell'esame: ho scelto di applicarli comunque, sfruttando conoscenze acquisite durante il mio percorso (sono uno studente del terzo anno), perché aiutano proprio a rispettare i principi SOLID, in modo più solido e ordinato.

---

