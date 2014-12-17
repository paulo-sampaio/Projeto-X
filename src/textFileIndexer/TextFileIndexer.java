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

import Gui.Estudos;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/*
 * Esta alpicacao cria um indice em um caminho definido pelas configuracoes do
 * sistema e organiza funcoes uteis ao programa todo.
 * Usamos uma classe como base e alteramos ela, mantivemos alguns
 * comentarios por estarem num padrao legal.
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
    	JOptionPane.showMessageDialog(null, "Impossivel criar indice em " + s + ". Erro: " + ex.getMessage());
      System.exit(-1);
    }
    JOptionPane.showMessageDialog(null, "Indice criado com sucesso! "); 
   
    // Leitura de comandos, 'q' fecha
    while (!s.equalsIgnoreCase("q")) {
      try {
        System.out.println("Entre com um diretorio para ser indexado (q=sair): (Ex.: /home/ron/mydir or c:\\Users\\ron\\mydir)");
        System.out.println("[Arquivos aceitos: " + extensoes + "]");
        s = br.readLine();
        if (s.equalsIgnoreCase("q")) {
          break;
        }

        // Adiciona arquivos ao indice
        indexer.indexFileOrDirectory(s);
      } catch (Exception e) {
    	  JOptionPane.showMessageDialog(null, "Erro ao indexar " + s + " : " + e.getMessage());
      }
    }

    // Fechar o indice vai cria-lo
    indexer.closeIndex();

    // Busca
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

    s = "";
    while (!s.equalsIgnoreCase("q")) {
      try {
        System.out.println("Escreve o que deseja pesquisar. (q=quit):");
        s = br.readLine();
        if (s.equalsIgnoreCase("q")) {
          break;
        }
        Query q = new QueryParser("contents", analyzer).parse(s);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // Imprime o resultado
        System.out.println("Encontrado " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
        }

      } catch (Exception e) {
    	  JOptionPane.showMessageDialog(null, "Erro ao pesquisar " + s + " : " + e.getMessage());
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
    addFiles(new File(fileName));

    int originalNumDocs = writer.numDocs();
    for (File f : queue) {
      FileReader fr = null;
      try {
        Document doc = new Document();

        fr = new FileReader(f);
        doc.add(new TextField("contents", fr));
        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

        writer.addDocument(doc);
        System.out.println("Adicionado: " + f);
      } catch (Exception e) {
        System.out.println("Não foi possivel adicionar: " + f);
      } finally {
        fr.close();
      }
    }

    int newNumDocs = writer.numDocs();
    System.out.println("");
    //System.out.println("************************");
    JOptionPane.showMessageDialog(null, (newNumDocs - originalNumDocs) + " documentos adicionados.");
    //System.out.println("************************");

    queue.clear();
  }

  private void addFiles(File file) throws IOException {
	  
	  boolean adicionado = false;

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
      // Opcao para indexar apenas arquivos
//	Verifica se a extensão está cadastrada do properties.
      for(String ext :extensao){
          if (filename.endsWith('.' + ext)) {
            queue.add(file);
            adicionado = true;
          } 
      }
      if (adicionado == false) {
    	 System.out.println("Arquivo não adicionado, extensão invalida. " + filename);
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
