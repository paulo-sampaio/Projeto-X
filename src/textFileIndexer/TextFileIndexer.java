package textFileIndexer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This terminal application creates an Apache Lucene index in a folder and adds files into this index
 * based on the input of the user.
 */
public class TextFileIndexer {
  private static StandardAnalyzer analyzer = new StandardAnalyzer();

  private IndexWriter writer;
  private ArrayList<File> queue = new ArrayList<File>();


  public static void main(String[] args) throws IOException {
	  Manipulador manipulador = new Manipulador();
	  String extensoes = manipulador.getExtensions();
	 
    String indexLocation = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
    String s = manipulador.getLocalIndice();
    		
    TextFileIndexer indexer = null;
    try {
      indexLocation = s;
      indexer = new TextFileIndexer(s);
    } catch (Exception ex) {
      System.out.println("Impossivel criar indice em " + s + ". Erro: " + ex.getMessage());
      System.exit(-1);
    }

    //===================================================
    //read input from user until he enters q for quit
    //===================================================
    while (!s.equalsIgnoreCase("q")) {
      try {
        System.out.println("Entre com um diretorio para ser indexado (q=sair): (Ex.: /home/ron/mydir or c:\\Users\\ron\\mydir)");
        System.out.println("[Arquivos aceitos: " + extensoes + "]");
        s = br.readLine();
        if (s.equalsIgnoreCase("q")) {
          break;
        }

        //try to add file into the index
        indexer.indexFileOrDirectory(s);
      } catch (Exception e) {
        System.out.println("Erro ao indexar " + s + " : " + e.getMessage());
      }
    }

    //===================================================
    //after adding, we always have to call the
    //closeIndex, otherwise the index is not created    
    //===================================================
    indexer.closeIndex();

    //=========================================================
    // Now search
    //=========================================================
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

    s = "";
    while (!s.equalsIgnoreCase("q")) {
      try {
        System.out.println("Enter the search query (q=quit):");
        s = br.readLine();
        if (s.equalsIgnoreCase("q")) {
          break;
        }
        Query q = new QueryParser("contents", analyzer).parse(s);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Encontrado " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
        }

      } catch (Exception e) {
        System.out.println("Erro ao pesquisar " + s + " : " + e.getMessage());
      }
    }

  }

  /**
   * Constructor
   * @param indexDir the name of the folder in which the index should be created
   * @throws java.io.IOException when exception creating index.
   */
  TextFileIndexer(String indexDir) throws IOException {
    // the boolean true parameter means to create a new index everytime, 
    // potentially overwriting any existing files there.
    FSDirectory dir = FSDirectory.open(new File(indexDir));


    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

    writer = new IndexWriter(dir, config);
  }

  /**
   * Indexes a file or directory
   * @param fileName the name of a text file or a folder we wish to add to the index
   * @throws java.io.IOException when exception
   */
  public void indexFileOrDirectory(String fileName) throws IOException {
    //===================================================
    //gets the list of files in a folder (if user has submitted
    //the name of a folder) or gets a single file name (is user
    //has submitted only the file name) 
    //===================================================
    addFiles(new File(fileName));
    
    int originalNumDocs = writer.numDocs();
    for (File f : queue) {
      FileReader fr = null;
      try {
        Document doc = new Document();

        //===================================================
        // add contents of file
        //===================================================
        fr = new FileReader(f);
        doc.add(new TextField("contents", fr));
        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

        writer.addDocument(doc);
        System.out.println("Added: " + f);
      } catch (Exception e) {
        System.out.println("Could not add: " + f);
      } finally {
        fr.close();
      }
    }
    
    int newNumDocs = writer.numDocs();
    System.out.println("");
    System.out.println("************************");
    System.out.println((newNumDocs - originalNumDocs) + " documentos adicionados.");
    System.out.println("************************");

    queue.clear();
  }

  private void addFiles(File file) throws IOException {
	  
	  Manipulador manipulador = new Manipulador();
	  String extensoes = manipulador.getExtensions();
	  String extensao[] = extensoes.split(Pattern.quote(",")); //transforma string com virgula em array

    if (!file.exists()) {
      System.out.println(file + " não existe.");
    }
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        addFiles(f);
      }
    } else {
      String filename = file.getName().toLowerCase();
      //===================================================
      // Only index text files
      //===================================================
      /* if (filename.endsWith(".htm") || filename.endsWith(".html") || 
              filename.endsWith(".xml") || filename.endsWith(".txt")) {
        queue.add(file);
      } else {
        System.out.println("Skipped " + filename);
      } */
      ///LT
      for(String ext :extensao){
          if (filename.endsWith('.' + ext)) {
            queue.add(file);
          } else {
            System.out.println("Arquivo n�o adicionado, extensão invalida. " + filename);
          }  ///LT
    	  
      }
    }
  }

  /**
   * Close the index.
   * @throws java.io.IOException when exception closing
   */
  public void closeIndex() throws IOException {
    writer.close();
  }
}