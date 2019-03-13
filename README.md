# Code me a sheep University @Breizhcamp 2019

This application was built using Spring Boot.

Using your IDE you can run the class **CodeASheepApplication** and it will start the application.

While your first run, you will get an exception 😔.

Let's have a look at the message:
```
Exception in thread "main" java.lang.RuntimeException: Oooops, something went bad when indexing the documents
	at com.code.a.sheep.codeasheep.lucene.LuceneDocumentIndexer.indexDocuments(LuceneDocumentIndexer.java:43)
	at com.code.a.sheep.codeasheep.reader.LittlePrinceIndexer.indexAndCommit(LittlePrinceIndexer.java:24)
	at com.code.a.sheep.codeasheep.CodeASheepApplication.main(CodeASheepApplication.java:35)
Caused by: java.io.IOException: This feature is not yet implemented ! Go and check the TODOs :p
	at com.code.a.sheep.codeasheep.lucene.LuceneDocumentIndexer.indexDocuments(LuceneDocumentIndexer.java:39)
	... 2 more
```

## "Go and check the TODOs :p"

The lab are filled with gaps, each gap has a **TODO** that explains what to do in each step.

The TODOs are following a simple order:
* TODO-01-a
* TODO-01-b
* TODO-01-b.1
* TODO-01-b.2
* TODO-02 
* ...

Use your IDE to display all the TODOs and then you can easily navigate between them.

For example for IntelliJ: **View > ToolWindows > TODO**

## LAB 1 : Lucene LAB

The purpose of this lab is to use Lucene to index the Little Prince book and execute request on it.

We want to execute two kinds of request:
* *full text*: for example retrieve all lines of the book having the word 'mouton'
* *facets*: classify the result of a request, for example, for all the lines having the word 'mouton', we want to know how many lines were on first chapter, on second chapter etc.

Here is an extract of the book we will index, it's in French:

```
Chapitre 2

J'ai ainsi vécu seul, sans personne avec qui parler 'véritablement, jusqu'à une panne dans le désert du Sahara, il y a six ans. Quelque chose s'était cassée dans mon moteur. Et comme je n'avais avec moi ni mécanicien, ni passagers, je me préparai à essayer de réussir, tout seul, une réparation difficile. C'était pour moi une question de vie ou de mort. J'avais à peine de l'eau à boire pour huit jours,

Le premier soir je me suis donc endormi sur le sable à mille milles de toute terre habitée. J'étais bien plus isolé qu'un naufragé sur un radeau au milieu de l'Océan. Alors vous imaginez ma surprise, au lever du jour, quand une drôle de petite voix m'a réveillé. Elle disait :

- S'il vous plaît... dessine-moi un mouton !

- Dessine-moi un mouton...

J'ai sauté sur mes pieds comme si j'avais été frappé par la foudre. J'ai bien frotté mes yeux. J'ai bien regardé. Et j'ai vu un petit bonhomme tout à fait extraordinaire qui me considérait gravement. Voilà le meilleur portrait que, plus tard, j'ai réussi à faire de lui. Mais mon dessin, bien sûr, est beaucoup moins ravissant que le modèle. Ce n'est pas ma faute. J'avais été découragé dans ma carrière de peintre par les grandes personnes, à l'âge de six ans, et je n'avais rien appris à dessiner, sauf les boas fermés et les boas ouverts.

je regardai donc cette apparition avec des yeux tout ronds d'étonnement. N'oubliez pas que je me trouvais à mille milles de toute région habitée. Or mon petit bonhomme ne me semblait ni égaré, ni mort de fatigue, ni mort de faim, ni mort de soif, ni mort de peur. Il n'avait en rien l'apparence d'un enfant perdu au milieu du désert, à mille milles de toute région habitée. Quand je réussis enfin à parler, je lui dis :

- Mais... qu'est-ce que tu fais là ?
```

You have different kind of lines:
* Start of a chapter
* Normal line
* Questions
* Dialog

In our application, every line of the book are represented as a document. Here an example of a document in JSON format:
````json
{
  "chapter": "Chapitre 7",
  "text": "- Un mouton mange tout ce qu'il rencontre.",
  "isDialog": true,
  "isQuestion": false
}
````

**Okay, we can start !**

If you feel confident, go to your IDE and follow the TODOs.

##### TODO-01 Read the file
In every search application, the first step is to retrieve the data. In our case, our data is the content of the Little Prince book.
The first step is to read the book and to map it as domain objects.
###### TODO-01-a Run this test, it should fail
The test fails because the lines are read but not correctly mapped to a document like this one :
````json
{
  "chapter": "Chapitre 7",
  "text": "- Un mouton mange tout ce qu'il rencontre.",
  "isDialog": true,
  "isQuestion": false
}
````

###### TODO-01-b Review the document we are expecting
The method *expectedDocuments* defines 6 documents, each document is the line representation of the book.
For this test we use the file *src/test/resources/le-petit-prince-extract.txt*

```
Chapitre 1

Un mouton sauvage apparait !

- Voulez vous lancer une MoutonBall de type classique ?

- Oui !

Chapitre 2

Vous avez attrapé un Moucool !
```

Each document defined in the method *expectedDocuments* has the information about those lines

##### TODO-02 Map a String into a Document
###### TODO-02-a Build a document from the read line and return it
For now, the application reads each line of the file but retrieves them as String objects. We need to parse those lines and extract information to create a document.
###### TODO-02-a.1 manage text field
Fill the property *text* with the read line.
###### TODO-02-a.2 Dialog mark
If the line starts with the dialog mark "-", set its property *isDialog* to true
###### TODO-02-a.3 manage isQuestion field
If the line ends with a question mark "?", set its property *isQuestion* to true
###### TODO-02-b Run this test, it should pass
Run the test again, it should be a success ! We correctly created our documents from the book file.

##### TODO-03 Review and call the method mapToLuceneDocuments
At this moment, we have read the book and produce from each line a **Document**, the next step is to map those Document to a format known by Lucene.
Once the documents have a format known by Lucene, we can index them !

##### TODO-04 Index documents in Lucene
###### TODO-04-a Execute this test it should pass
This test fails because we did not implement yet the call to index the documents in Lucene
###### TODO-04-b Remove the dummy if bloc and replace it by a call to indexWriter to add the documents in Lucene
In this step, you need to clean a dummy bloc that was present only to make the app compiling :D

Replace this block by a call to the lucene index writer.
```java
if (true) {
    throw new IOException("This feature is not yet implemented ! Go and check the TODOs :p");
}
```

*The first exception we had while running the app was thrown here !*

###### TODO-04-c Execute this test, it should fail. When the test is green, remove it
Remove this test when it's green

At this stage, we are able to write documents in the Lucene index ! \o/

##### TODO-05 Verify we properly indexed our document
Obviously, we want to test that the documents we indexed are correct.

###### TODO-05-a Run this test it should fail
We can easily see while running this test that we did some mistake. Let's have a look at the test.

We are expecting the first document matching the word 'mouton' is :
**Warning**, this is a representation of a Lucene document.
```
{
  "chapter": "Chapitre 26",
  "text": "- J'ai ton mouton. Et j'ai la caisse pour le mouton. Et j'ai la muselière...",
  "isDialog": "1",
  "isQuestion": null
}
```

The error message of the test tells us that the *isDialog* field is not properly managed.

###### TODO-05-b Review the schema and be sure you understand every configuration

The schema is the heart of the solution, it will be define for each field it's behavior in the index.

In our implementation we offer several options:
* every field is searchable.
* fields having raw field can be use to perform facets/aggregations
* fields stored can be retrieved

While doing this:
```java
.addField(LuceneTextField.builder()
    .name(TEXT.getName())
    .isStored(true)
    .build())
```

we are telling to our system that the field TEXT is searchable and can be retrieved by a search.

###### TODO-05-c Update the Lucene schema by modifiying the isStored property for IS_DIALOG field
In our test, the message shows an error for the field *IS_DIALOG*.

```java
.addField(LuceneBooleanField.builder()
    .name(IS_DIALOG.getName())
    .isStored(false)
    .withRawField(true)
    .build())
```
In this configuration, the field is not stored that means its value cannot be retrieved. Change this :)

###### TODO-05-d Run this test again, it should pass
The test is now ok, as said in javadoc: 

```
You can see Lucene retrieves boolean field as "O/1", that's why a mapping phase is needed : {@link com.code.a.sheep.codeasheep.lucene.schema.LuceneBooleanField#getValue(IndexableField)}
```

Our documents are correctly indexed in Lucene ! Well done \o/
That's definitely cool, but now we may want to search on those lines.


##### TODO-06 Let's search some mouton !
###### TODO-06-a Have a look at this parser, why do we use our customAnalyzer here ?
In this step we are in the component responsible of the search.
The *customAnalyzer* object is a Spring component defined in the class *LuceneConfiguration*. Is role is to provide text analysis.

In the line:
```
return new StandardQueryParser(customAnalyzer).parse(query, TEXT.getName());
```
we create a query using this analyzer, that means the query will be parse and may be modified before searching on it ?

Definitely yes ! When i'm searching for "MOUton" i want to search the lines having the token "mouton". The case doesn't have a strong meaning.

Mmmh, okay, but... in my inverted index, i have the token **MOUton** so it won't work.

We lied :D

###### TODO-06-b Look at this bean, it's used to write on the index, we use the customAnalyzer

In the configuration (*LuceneConfiguration*), we defined a bean responsible to write on Lucene. This bean is aware of our *CustomAnalyzer* and then applies text analysis also while indexing.
In the inverted index, we won't have token 'MOUton' but the token 'mouton' : this is lowercase token filter.

**The most useful phase of the text analysis is the tokenizer**: how do we split the tokens.

The line "Hey there, how is the lab ?" will be splitted and produce several tokens > "hey", "there" etc..

Check the bean creation:
````java
/**
 * Creates the bean that can write documents in Lucene index.
 * @return
 */
@Bean
public IndexWriter indexWriter() {
    return new IndexWriter(directory(), new IndexWriterConfig(customAnalyzer(resourceLoader())));
}
````

###### TODO-07-a Use indexSearcher to perform a search and retrieve the 10 most matching documents
Perform the search in Lucene.
The query is parsed:
```
Query luceneQuery = parseQuery(query, customAnalyzer);
```
and then the results are parsed and map to a *SearchResult* object:
```
SearchResult.builder()
    .nbHits(topDocs.totalHits)
    .hits(Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList()))
    .facets(aggregateResultOnField(facetFields, luceneQuery))
    .build();
```

###### TODO-07-b Remove @Ignore and execute this test it should not pass
The test fails :(
Let's see what token we are looking for:
```
luceneDocumentSearcher.searchDocuments("moUtôn", List.of());
```
We are looking for the token **moUtôn**.

It might be a text analysis issue, that "ô" seems suspect.

##### TODO-08 Custom analyzer
Let's have a look at our analyzer:
```
@Bean
public CustomAnalyzer customAnalyzer(ResourceLoader resourceLoader) {
    return CustomAnalyzer.builder(resourceLoader)
            .withTokenizer(StandardTokenizerFactory.class)
            .addTokenFilter(LowerCaseFilterFactory.class)
            .build();
}
```

It's a combination of:
* a standard tokenizer: split the lines into token
* a lowercase token filter: modify all the produced tokens using lowercase

Mmmmh, we might need something to manage accents and special characters.

###### TODO-08-a Add a token filter that removes the accent/special characters etc. (ascii folding behavior)

In fact, we are laking of a token filter that removes those painful characters.
This token filter is *ASCIIFoldingFilterFactory*

###### TODO-08-b Execute this test it should pass
The test is now green !

Something is important in this test, let's have a look to those lines:
``` 
assertThat(searchResult.getNbHits()).isEqualTo(29);
assertThat(searchResult.getHits().size()).isEqualTo(10);
```

There are 29 lines having the word mouton, but only 10 are retrieved.
Only the 10 more relevant are retrieved. It's a common use case in the search world, do you usually go to Google's results second page ? :p

###### Faceting

Faceting is a nice functionality to have on a search engine: classify the result of a request, for example, for all the lines having the word 'mouton', we want to know how many lines were on first chapter, on second chapter etc.
They are mainly use to produce some statistics on the documents, on search results 

####### TODO-09-a Review this test and the method expectedFacetValues, what's the meaning of the FacetValue("Chapitre 2", 11) ?

This object means that there are 11 eleven lines having the word mouton in the chapter "Chapitre 2"

###### TODO-09-b Execute this test, it should not pass
The test is not ok. Why ?

First, what's the request ?
```java
luceneDocumentSearcher.searchDocuments("mouton", List.of("chapter"));
```
We are searching on token "mouton" and we want to perforl faceting on field "chapter".

Before looking at the test, let's think about what we are doing. We want to know for every document matching the request on which chapter he is, and then collect all information, and then produce the aggregated result.

**Here we have two problems**
* In our inverted index, "Chapitre 2" line was not indexed as "Chapitre 2" but with tokens ("chapitre, ...")
* Is the inverted index a good structure to know for every document, all it's tokens ? **Not at all**. that's why it's called an inverted index, it's not an index ! 

> We might need another structure that looks like an index with original value of the field: "Chapitre 2"

###### TODO-09-c Remove this dummyCollector and pass facetCollector to search method

First of all, let's update our search. We add a dummy collector that you can remove:
```
var dummyCollector = new SimpleCollector() {
    @Override
    public void collect(int doc) throws IOException {

    }

    @Override
    public boolean needsScores() {
        return false;
    }
};
```
Replace it by the **facetCollector** defined above:
```
FacetCollector facetCollector = new FacetCollector(facetFields, luceneSchema);
```

This collector will produces the facets.

###### TODO-09-d Add a raw field for chapter

Add a raw field to the chapter field.
Just by adding this configuration, everything will be taken in account for you.

To understand what's going on, let's go to the the class *LuceneTextField*

```
if (this.isWithRawField()) {
    luceneFields.add(new SortedDocValuesField(getRawFieldName(field.getKey()), new BytesRef(field.getValue())));
}
```

Those lines are performing the **magic** !

If a field has the property *withRawField* set to true, it has another field indexed in a structured named **DocV

###### TODO-09-e Execute this test, it should pass

The test is now OK :) 

##### TODO-10 Have a look at this class, aggregate it not easy, even when using Lucene

You can review this class, just to see that's it's not trivial.

Faceting implementation is not a feature provided by Lucene out of the box. You have to create it yourself !

##### TODO-11 You can run the application and perform some request
For example: http://localhost:8080/search?text=mouton&facet.field=isDialog&facet.field=isQuestion

This is the end of the first lab, you can run the application and perform some searches using the web API :)
