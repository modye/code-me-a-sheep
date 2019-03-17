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
* *full text*: for example retrieve all lines of the book having the word "mouton"
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
* Question
* Dialog

In our application, each line of the book is represented as a document. Here an example of a document in JSON format:
```json
{
  "chapter": "Chapitre 7",
  "text": "- Un mouton mange tout ce qu'il rencontre.",
  "isDialog": true,
  "isQuestion": false
}
```

**Okay, we can start !**

If you feel confident, go to your IDE and follow the TODOs.

##### TODO-01 Read the file
In every search application, the first step is to retrieve the data. In our case, our data is the content of the Little Prince book.
The first step is to read the book and to map it as domain objects.
###### TODO-01-a Run this test, it should fail
The test fails because the lines are read but not correctly mapped to a document like this one :
```json
{
  "chapter": "Chapitre 7",
  "text": "- Un mouton mange tout ce qu'il rencontre.",
  "isDialog": true,
  "isQuestion": false
}
```

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
At this moment, we have read the book and produce from each line a **Document**, the next step is to map those documents into a format known by Lucene.
Once the documents have a format known by Lucene, we can index them !

##### TODO-04 Index documents in Lucene
###### TODO-04-a Execute this test it should pass
This test fails because we did not implement the call to index the documents in Lucene yet.
###### TODO-04-b Remove the dummy if bloc and replace it by a call to indexWriter to add the documents in Lucene
In this step, you need to clean a dummy bloc that was present only to make the app compiling :D

Replace this block by a call to the lucene index writer.
```java
if (true) {
    throw new IOException("This feature is not yet implemented ! Go and check the TODOs :p");
}
```

*The first exception we had while running the app was thrown here !*

###### TODO-04-c Execute this test, it should fail. When the test is red, remove it
Remove this test when it's red

At this stage, we are able to write documents in the Lucene index ! \o/

##### TODO-05 Verify we properly indexed our document
Obviously, we want to test that the documents we indexed are correct.

###### TODO-05-a Run this test it should fail
We can easily see while running this test that we did some mistakes. Let's have a look at the test.

We are expecting the first document matching the word "mouton" is :
**Warning**, this is a representation of a Lucene document.
```json
{
  "chapter": "Chapitre 26",
  "text": "- J'ai ton mouton. Et j'ai la caisse pour le mouton. Et j'ai la muselière...",
  "isDialog": "1",
  "isQuestion": null
}
```

The error message of the test tells us that the *isDialog* field is not properly managed.

###### TODO-05-b Review the schema and be sure you understand every configuration

The schema is the heart of the solution. For each field, it will define its behavior in the index.

In our implementation we offer several options:
* each field is searchable.
* fields having raw field can be used to perform facets/aggregations
* fields stored can be retrieved

While doing this:
```java
.addField(LuceneTextField.builder()
    .name(TEXT.getName())
    .isStored(true)
    .build())
```

we are telling to our system that the field TEXT is searchable and can be retrieved by a search.

###### TODO-05-c Update the Lucene schema by modifying the isStored property for IS_DIALOG field
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
You can see Lucene retrieves boolean field as "O/1", that's why a mapping phase is needed: {@link com.code.a.sheep.codeasheep.lucene.schema.LuceneBooleanField#getValue(IndexableField)}
```

Our documents are correctly indexed in Lucene ! Well done \o/
That's definitely cool, but now we may want to search on those lines.


##### TODO-06 Let's search some _moutons_ !
###### TODO-06-a Have a look at this parser, why do we use our customAnalyzer here ?
In this step we are in the component responsible of the search.
The *customAnalyzer* object is a Spring component defined in the class *LuceneConfiguration*. Its role is to provide text analysis.

In the line:
```java
return new StandardQueryParser(customAnalyzer).parse(query, TEXT.getName());
```
we create a query using this analyzer, does that mean the query will be parsed and may be modified before searching on it ?

Definitely yes ! When i'm searching for "MOUton" i want to search the lines having the token "mouton". The case doesn't have a strong meaning.

Mmmh, okay, but... in my inverted index, i have the token **MOUton** so it won't work.

We lied :D

###### TODO-06-b Look at this bean, it's used to write on the index, we use the customAnalyzer

In the configuration (*LuceneConfiguration*), we defined a bean responsible for writing on Lucene. This bean is aware of our *CustomAnalyzer* and then applies text analysis also while indexing.
In the inverted index, we won't have token "MOUton" but the token "mouton": this is due to the lowercase token filter.

**The most useful phase of the text analysis is the tokenizer**: how do we split the tokens.

The line "Hey there, how is the lab ?" will be split and produce several tokens > "hey", "there" etc..

Check the bean creation:
```java
/**
 * Creates the bean that can write documents in Lucene index.
 * @return
 */
@Bean
public IndexWriter indexWriter() {
    return new IndexWriter(directory(), new IndexWriterConfig(customAnalyzer(resourceLoader())));
}
```

###### TODO-07-a Use indexSearcher to perform a search and retrieve the 10 most matching documents
Perform the search in Lucene.
The query is parsed:
```java
Query luceneQuery = parseQuery(query, customAnalyzer);
```
and then the results are parsed and map to a *SearchResult* object:
```java
SearchResult.builder()
    .nbHits(topDocs.totalHits)
    .hits(Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList()))
    .facets(aggregateResultOnField(facetFields, luceneQuery))
    .build();
```

###### TODO-07-b Remove @Ignore and execute this test it should not pass
The test fails :(
Let's see what token we are looking for:
```java
luceneDocumentSearcher.searchDocuments("moUtôn", List.of());
```
We are looking for the token **moUtôn**.

It might be a text analysis issue, that "ô" seems suspect.

##### TODO-08 Custom analyzer
Let's have a look at our analyzer:
```java
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
```java 
assertThat(searchResult.getNbHits()).isEqualTo(29);
assertThat(searchResult.getHits().size()).isEqualTo(10);
```

There are 29 lines having the word "mouton", but only 10 are retrieved.
Only the 10 more relevant are retrieved. It's a common use case in the search world, do you usually check the Google's results second page ? :p

###### Faceting

Faceting is a nice functionality to have on a search engine: classify the result of a request, for example, for all the lines having the word "mouton", we want to know how many lines were on first chapter, on second chapter etc.
They are mainly used to produce some statistics on documents, on search results. 

####### TODO-09-a Review this test and the method expectedFacetValues, what's the meaning of the FacetValue("Chapitre 2", 11) ?

This object means that there are 11 lines having the word "mouton" in the chapter "Chapitre 2"

###### TODO-09-b Execute this test, it should not pass
The test is not ok. Why ?

First, what's the request ?
```java
luceneDocumentSearcher.searchDocuments("mouton", List.of("chapter"));
```
We are searching on token "mouton" and we want to perform faceting on field "chapter".

Before looking at the test, let's think about what we are doing. We want to know for each document matching the request on which chapter he is, and then collect all information, and, finaly produce the aggregated result.

**Here we have two problems**
* In our inverted index, the "Chapitre 2" line was not indexed as "Chapitre 2" but with tokens ("chapitre, ...")
* Is the inverted index a good structure to know for each document, all its tokens ? **Not at all**. that's why it's called an inverted index, it's not an index ! 

> We might need another structure that looks like an index with original value of the field: "Chapitre 2"

###### TODO-09-c Remove this dummyCollector and pass facetCollector to search method

First of all, let's update our search. We add a dummy collector that you can remove:
```java
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
```java
FacetCollector facetCollector = new FacetCollector(facetFields, luceneSchema);
```

This collector will produce the facets.

###### TODO-09-d Add a raw field for chapter

Add a raw field to the chapter field.
Just by adding this configuration, everything will be taken in account for you.

To understand what's going on, let's go to the the class *LuceneTextField*

```java
if (this.isWithRawField()) {
    luceneFields.add(new SortedDocValuesField(getRawFieldName(field.getKey()), new BytesRef(field.getValue())));
}
```

Those lines are performing the **magic** !

If a field has the property *withRawField* set to true, it has another field indexed in a structured named **DocValues**.

###### TODO-09-e Execute this test, it should pass

The test is now OK :) 

##### TODO-10 Have a look at this class, aggregate it not easy, even when using Lucene

You can review this class, just to see that's it's not trivial.

Faceting implementation is not a feature provided by Lucene out of the box. You have to create it yourself !

##### TODO-11 You can run the application and perform some request
For example: http://localhost:8080/search?text=mouton&facet.field=isDialog&facet.field=isQuestion

This is the end of the first lab, you can run the application and perform some searches using the web API :)

## LAB 2 : Plain Java LAB

The purpose of this lab is to use plain java code to index the Little Prince book and execute request on it.

We want to execute two kinds of request:
* *full text*: for example retrieve all lines of the book having the word "mouton"
* *facets*: classify the result of a request, for example, for all the lines having the word "mouton", we want to know how many lines were on first chapter, on second chapter etc.

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
* Question
* Dialog

In our application, each line of the book is represented as a document. Here an example of a document in JSON format:
```json
{
  "chapter": "Chapitre 7",
  "text": "- Un mouton mange tout ce qu'il rencontre.",
  "isDialog": true,
  "isQuestion": false
}
```

In every search application, the first step is to retrieve the data. In our case, our data is the content of the Little Prince book.
The first step is to read the book and to map it as domain objects.

Everything was done during the first lab so reading the book and generating documents should already work.

**Okay, we can start !**

If you feel confident, go to your IDE and follow the TODOs.

##### TODO-01 Add document to the index

###### TODO-01-a Look at this @Profile annotation, we will use Spring to switch between Lucene and Plain Java implementation

As said before this application is a Spring Boot application.
We will use its _profile_ ability in order to switch from Lucene indexing/searching to plain Java code indexing/searching.

###### TODO-01-b Run this test, it should fail

Currently documents are not added to the index at all.
We need to complete the code in order to fix the problem.

###### TODO-01-c Add documents into index

We just need to add documents to the plain java index implementation.

###### TODO-01-d Run this test, it should pass

With documents added to the index, the test should pass now.

##### TODO-02 Commit indexed documents

###### TODO-02-a Review this test, run it, it should fail. Why ?

Documents are buffered into the index until a commit operation is called.

###### TODO-02-b Look at this instruction, why do we need to call this method ?

This commit operation will create and optimize searching structures.

###### TODO-02-c Call the index to commit

We just need to call the commit function of the index and it should work !
Well, no. If you try to run the test again you will see that's not enough.
It would have been to easy :p
There's much work to be done. Let' continue.

##### TODO-03 build searching structures
###### TODO-03-a Call a method to create searching structures

Figure out how to call the function which create searching structures.

###### TODO-03-b Run this test, it should fail, why ?

Even if the function which creates searching structures is called, currently the process is not complete.

###### TODO-03-c Add the tokens in the field index for this content

You need to tokenize the content of documents and add result tokens to the field index.
A first simple tokenization function can be found in the class **TextAnalysis**:

```java
public static List<String> defaultAnalysis(String content) {
        return Arrays.asList(StringUtils.stripAccents(content)
                .split("[^a-zA-Z0-9]"));
    }
```

###### TODO-03-d For each token, add it to posting list

Now that tokens are created, we need to add them into posting lists.

###### TODO-03-e Run this test, it should pass now :)

After all this hard work, we should now be able to retrieve documents ! \o/ 
Well done !

##### TODO-04 Improve text analysis
###### TODO-04-a Run this test it should fail, why is it failing ?

The last test checked that these tokens where present in the index:

```java
assertThat(textInvertedIndex.keySet()).contains("Chapitre", "mouton", "SAUVAGE", "vraiment", "apparait");
```

But what if a use looks for "un mouton sauvage est plus dangereux qu'un moucool apprivoisé" ?
The word "sauvage" is lowercased in the user query.

The new test check that the lowercased token are present in the index and that's why it fails:

```java
assertThat(textInvertedIndex.keySet()).contains("chapitre", "mouton", "sauvage", "vraiment", "apparait");
```

###### TODO-04-b Create a new analysis method that applies lowercase filtering logic to produced tokens

Add a lowercase function to the new analysis process.

###### TODO-04-c Replace previous analysis with the one you just created

Use your new incredible analysis function and check if it works.

###### TODO-04-d Run this test it should pass

Excellent ! The test now pass. Tokens are stored in their normalized form in the index.

##### TODO-05 Create columnar storage for faceting purpose
###### TODO-05-a First be sure to understand what's under testing here, run this test, it should fail

If you want to use faceting or sorting functions, inverted indices are not good structures.
You will need to create additional storage with complete field values in order to be able to use faceting and sorting.

For instance, if you need faceting on the chapter field, you will want to aggregate documents on values like "Chapitre 1".
Currently, inverted index stored the two tokens "chapitre" and "1" for such a field.

Let's try to improve this.

###### TODO-05-b Update columnar storage for this document and this value

Add field values for each document in columnar storage.

###### TODO-05-c Run this test, it should pass

You should be able to use faceting now ! Well done.
Let's start try search for documents now.

##### TODO-06 Call searching function
###### TODO-06-a Run this test, you should receive a Not yet implemented exception

Let's start to use our new fresh created index.

###### TODO-06-b Remove the exception, call the index to perform a search operations, returns only the 10 most relevant documents

Searching functions are not used yet. Add needed functions call in the code.

###### TODO-06-c Replace this empty List.of() with: Analyze the incoming query and search documents

Analysis functions are used during indexing **AND** during searching processes.
Add analysis function call.

###### TODO-06-d Have a look at this, here we call the invertedIndex (You shall add a breakpoint here during Debug session)

You should check the inner process while debugging here.

###### TODO-06-e Run this test, it should pass

Great ! You retrieved your first documents ! \o/
But how are documents sorted ?

##### TODO-07 Scoring
###### TODO-07-a Run this test, it should fail

Currently documents are just retrieved, they are not well sorted in order to get the best matches.

###### TODO-07-b Replace this 1 by a call to our tfIdf method

Let's compute the score for each document.
A tf-idf function is implemented, make good use of it. 

###### TODO-07-c Run this test, it should pass

Your results are nearly perfect now.

###### TODO-07-d Add an assertion on the score value

Just make sure that's the case.

##### TODO-08 Faceting
###### TODO-08-a Run this test, it will fail

Faceting functions are not used yet. Go on and finish the lab !

###### TODO-08-b Collect the facets and add them to the builder

Use the result collector in order to collect facets.

###### TODO-08-c Review this facet collect method

You can see that collecting facets is not an easy task.
Document field values are used in order to aggregate and count documents.

###### TODO-08-d Run this test, it should pass

Facets are retrieved. Let's check how it works.

###### TODO-08-e Add a breakpoint here and see what's happening here

You can observe here the document aggregation.
You can observe the facet reverse sorting as well.

```java
    List<FacetValue> facetValues = valueMap.entrySet().stream()
            .map(e -> FacetValue.builder()
                    .count(e.getValue())
                    .key(e.getKey())
                    .build())
            // Sort facets by count
            .sorted(Comparator.comparingInt(FacetValue::getCount).reversed())
            .collect(Collectors.toList()); 
```

###### TODO-08-f Pass it to false

You need to specify a that you want faceting on fields.
Remember, it uses a specific columnar storage. 
That mean additional disk and memory space.
Let's try to disable the columnar storage.

###### TODO-08-g Run this test, it will fail   

Without columnar storage this exception is thrown:

```
java.lang.IllegalArgumentException: Field 'chapter' does not contain columnar storage
```

###### TODO-08-h Set it back to true :)

The test works again.
Now you are ready to use your service.

##### TODO-09 Use the search engine
###### TODO-09 You can run the app and play for the API as you did in first lab
For example: http://localhost:8080/search?text=mouton&facet.field=isDialog&facet.field=isQuestion

This is the end of the second lab, you can run the application and perform some searches using the web API :)
Make sure to activate the "plain-java" profile !
