Java Agent Middleware 
---

[ ![Download](https://api.bintray.com/packages/scunsaiocu/it.francescosantagati/jam/images/download.svg) ](https://bintray.com/scunsaiocu/it.francescosantagati/jam/_latestVersion)

**Laboratorio di Programmazione III per l’anno accademico 2014/2015.**  
*Università degli studi di Torino*  
**Professore: Matteo Baldoni**  

Il presente laboratorio ha come obiettivo la realizzazione di una semplice infrastruttura di rete basata su RMI per lo svilluppo di applicazioni che utilizzino a tecnologia ad agenti.
L’infrastruttura è denominata Java Agent Middleware (JAM) e ispirata al Java Agent DEvelopment Framework (JADE) di TILab.

Download
---

#### GRADLE
```
compile 'it.francescosantagati:jam:1.0'
```

#### MAVEN
```
<dependency>
  <groupId>it.francescosantagati</groupId>
  <artifactId>jam</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```

Agenti intelligenti
---
Un agente è un sistema computazionale situato in un ambiente, e capace di azioni autonome nell’ambiente per raggiungere i suoi obiettivi di progetto.
Nella maggior parte dei domini un agente non ha completa conoscenza dell’ambiente e completo controllo su di esso. L’ambiente può evolvere dinamicamente independentemente dall’agente, le azioni dell agente possono fallire. In generale, si assume che l’ambiente sia non-deterministico. Il problema principale per un agente è quello di decidere quale delle sue azioni dovrà eseguire per soddisfare meglio i suoi obiettivi di progetto. Un agente intelligente deve avere un comportamento flessibile per riuscire a soddisfare i propri obiettivi di progetto.
Con flessibile si intende:

* **Reattivo**: Gli agenti intelligenti sono capaci di percepire il loro ambiente e di rispondere in modo tempestivo ai cambiamenti che si verificano in esso.
* **Pro-attivo**: Gli agenti intelligenti sono capaci di esibire un comportamento goal-directed pren- dendo l’iniziativa.
* **Sociale**: Gli agenti intelligenti sono capaci di interagire con altri agenti (cooperare, negoziare, ...).

Spesso agli agenti vengono attribuite anche altre proprietà come ad esempio:

- la mobilità: la capacità di spostarsi fra i nodi di una rete;
- l’apprendimento: la capacità di imparare per migliorare le proprie prestazioni.

Spesso gli agenti sono modellati per mezzo di concetti cognitivi basati su stati mentali come credenze ( *beliefs* ), conoscenze ( *knowledge* ), desideri, intenzioni. I modelli più noti sono quelli basati su **belief-desire-intention** (BDI, agenti basati su modelli intenzionali):

- **Beliefs**: corrispondono all informazione che l’agente ha sul mondo. Può essere incompleta o non corretta. 
- **Desires**: rappresentano la situazione che l’agente vorrebbe realizzare.
- **Intentions**: rappresentano i desideri che l’agente si è impegnato (committed) a realizzare.

Il comportamento di un agente intelligente è descritto sinteticamente così:

```java
while (true) {
    acquisisce una nuova percezione;
    aggiorna le credenze (belief);
    genera un insieme di opzioni (desideri);
    sceglie fra queste una che intende realizzare (intenzione)
        e per cui si impegna (commitment);
    trova un piano per soddisfare l'intenzione scelta;
    esegue il piano;
}
```

Per poter interagire, gli agenti in un sistema multiagente devono comunicare fra loro. Il meccanismo di comunicazione normalmente usato è quello dello scambio di messaggi. L’invio o la ricezione di un messaggio può essere visto come una azione eseguita dall'agente. Sono stati definiti dei linguaggi di comunicazione fra agenti in cui le azioni comunicative sono modellate basandosi sulla teoria filosofica delle azioni: inform, request, query-if, ask, tell, ecc.
Ci sono ovvie somiglianze fra agenti e oggetti: entrambi sono entità computationali che incapsulano uno stato, sanno eseguire delle azioni (metodi), e comunicano via scambio di messaggi. Ci sono però anche differenze significative: gli oggetti non hanno autonomia. Se un oggetto o ha un metodo pubblico, ogni altro oggetto puo invocare questo metodo, e o, quando gli viene richiesto, deve eseguirlo. Viceversa, i chiede an un agente j di eseguire un’azione, non è garantito che l’agente j la eseguirà (l’agente j è autonomo: Objects do it for free. Agents do it because they want to).
Un’altra differenza riguarda la nozione di comportamento flessibile (reattivo, proattivo, sociale). Il modello standard ad oggetti non considera la costruzione di sistemi che presentino questi tipi di comportamento. Inoltre i sistemi multiagente sono inerentemente distribuiti: ogni agente ha un flusso di controllo indipendente (processo o thread).
Le architetture software sono sempre più costituite da componenti che interagiscono dinamicamente attraverso protocolli complessi. L’interazione è probabilmente la caratteristica più importante del software complesso. La programmazione ad agenti viene vista da molti come un nuovo paradigma di programmazione, naturale evoluzione di altri paradigmi di programmazione, in particolare della programmazione ad oggetti. Sono gi`e state sviluppate metodologie per analisi e progetto agent-oriented, metodi formali di specifica e verifica di sistemi ad agenti e tecniche di implementazione.

Introduzione agli agenti in JAM
---
L’infrastruttura è basata sulla tecnologia RMI di Java. 
![RMI Architecture][rmi_architecture]

Questa sarà costituita da un ambiente a runtime, denominato Runtime Agent Middleware (RAM, nel seguito). Il RAM sarà realizzato direttamente sull’infrastruttura RMI offerta da Java mediante una serie di classi che costituiranno lo Agent Directory Service Layer (ADSL, nel seguito). Il RAM permetterà di ospitare, cercare, trovare, eseguire e soprattutto scambiare messaggi tra gli agenti presenti in un dato momento. Agenti che saranno definiti mediante un insieme di opportune classi, che costituiscono un altro degli obiettivi del laboratorio, denominate Java Agent Middleware (JAM, nel seguito).
In JAM un agente è un oggetto, istanza di una particolare classe che estende la JAMAgent e che descrive il proprio stato interno. Il comportamento di un agente è definito da un insieme di oggetti di tipo JAMBehaviour che implementano il metodo action e che hanno accesso, mediante opportuno puntatore, allo stato dell’agente proprietario di tali comportamenti. In un certo senso, tale metodo rappresenta le “azioni” che definiscono il comportamento dell’agente stesso. Il codice (Java) di ogni metodo action è eseguito su un thread dedicato (vedi Figura 3). Il metodo action è eseguito una volta soltanto se la classe che implementa il metodo estende JAMSimpleBehaviour, è invece eseguito ciclicamente fino a che il metodo done non è invocato, se la classe estende JAMWhileBehaviour.

![JAM Agent schema][jam_state]

In generale un agente in JAM è definito mediante la classe che descrive il proprio stato interno (la classe che estende JAMAgent e fornisce le funzionalità comunicative) e le classi che definiscono il suo comportamento. Ad esempio, le seguenti sono tre classi che definiscono tre comportamenti diversi:

```java
class ComportamentoMioAgente extends JAMWhileBehaviour {
    [ . . . ]
    public ComportamentoMioAgente (JAMAgent myAgent ) {
        super (myAgente) ;
        [ . . . ]
    }
    [ . . . ]
    public void setup () {
        [ . . . ]
    }
    public void dispose () {
        [ . . . ]
    }
    public void action () {
        [ . . . ]
    }
 }
 
class AltroComportamentoMioAgente extends JAMSimpleBehaviour {
    [ . . . ]
    public AltroComportamentoMioAgente (JAMAgent myAgent) {
        super (myAgente);
        [ . . . ]
    }
    [ . . . ]
    public void setup () {
        [ . . . ]
    }
    public void dispose () {
        [ . . . ]
    }
    public void action () {
        [ . . . ]
    }
}

class AltroAncoraComportamentoMioAgente extends JAMWhileBehaviour {
    [ . . . ]
    public AltroAncoraComportamentoMioAgente (JAMAgent myAgent) {
        super (myAgente ) ;
        [ . . . ]
    }
    [ . . . ]
    public void setup () {
        [ . . . ]
    }
    public void dispose () {
        [ . . . ]
    }
    public void action () {
        [ . . . ]
    }
}
 
```

Tali comportamenti possono essere associati alla definizione di un agente nel seguente modo:
```java
class MioAgente extends JAMAgent {
    [ . . . ]
    public MioAgente( . . . ) {
        [ . . . ]
        addBehaviour(new ComportamentoMioAgente(this));
        addBehaviour(new AltroComportamentoMioAgente(this));
        addBehaviour(new AltroAncoraComportamentoMioAgente(this));
    }
    [ . . . ]
}
```

L'attivazione di un agente avviene mediante invocazione del metodo start (dopo init che ha lo scopo di collegarlo alla RAM), il quale attiva i propri comportamenti, ognuno su un thread diverso.
```java
[ . . . ]
JAMAgent agent = new MioAgente ( . . . );
agent.init();
agent.start();
[ . . . ]
```

Un agente può comunicare con altri agenti (conoscendone il nome (identicatore) o inviando messaggi a tutti gli agenti presenti in un dato momento) attraverso i metodi send. Inoltre può leggere i messaggi ricevuti dagli altri agenti mediante i metodi receive. Tipicamente tali medodi sono utilizzti all'interno del metodo action:
```java
[ . . . ]
JAMAgent agent = new MioAgente ( . . . );
agent.init();
agent.start();
[ . . . ]
```

Un agente può comunicare con altri agenti (conoscendone il nome (identicatore) o inviando messaggi a tutti gli agenti presenti in un dato momento) attraverso i metodi send. Inoltre può leggere i messaggi ricevuti dagli altri agenti mediante i metodi receive. Tipicamente tali metodi sono utilizzti all'interno del metodo action:
```java
public void action () {
    [ . . . ]
    Message request = new Message (Performative .REQUEST);
    request.setSender(myagent.getMyID());
    request.setReceiver( . . . );
    request.setContent("Che ora è?");
    myAgent.send(request);
    [ . . . ]
    Message aswerRequest = myAgent.receive(Performative.INFORM, . . . );
    [ . . . ]
}
```

Dell'effetivo trasferimento dei messaggi via rete si occuperà la sottostante classe JAMAgent (la cui realizzazione è uno degli obiettivi del progetto).

[rmi_architecture]: https://raw.githubusercontent.com/FrancescoSantagati/java-agent-middleware/master/images/rmi_architecture.png "RMI"
[jam_state]: https://raw.githubusercontent.com/FrancescoSantagati/java-agent-middleware/master/images/jam_state.png "JAM Agent"

### Riferimenti bibliografici

Per alcuni sono richieste le credenziali i-learn su UniTO.

1. M. Baldoni. Diagrammi uml del laboratorio di programmazione III - [esempio iniziale](http://informatica.i-learn.unito.it/mod/resource/view.php?id=57101)
2. M. Baldoni. Diagrammi uml dell laboratorio di programmazione III - [link](http://informatica.i-learn.unito.it/mod/resource/view.php?id=56776)
3. M. Baldoni. Package e Documentazione in Java. Lucidi disponibili presso il supporto on-line al corso presso http://informatica.i-learn.unito.it/ per l'a.a. 2008/2009.
4. C. S. Horstmann and G. Cornell. Java 2: Fondamenti. Pearson, Prentice Hall, 2005
5. C. S. Horstmann and G. Cornell. Java 2: Tecniche Avanzate. Pearson, Prentice Hall, 2005
6. [A. Martelli. Agenti e sistemi multi-agente](http://www.di.unito.it/~baldoni/didattica/a304/ProgInRete/Agenti.pdf)
7. [Sum Microsystem. The java tutorials](http://java.sun.com/docs/books/tutorial/java/javaOO/enum.html)
8. [TILab S.p.A. Java Agent DEvelopment framework](http://jade.cselt.it)
9. M.Wooldridge. An Introduction to MultiAgent Systems. Wiley, 2002
