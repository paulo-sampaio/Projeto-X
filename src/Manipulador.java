package textFileIndexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

public class Manipulador {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/dados.properties");
		props.load(file);
		return props;

	}


	public String getExtensions() throws IOException{
		
		String extensoes; //Variavel que guardará as extensões suportadas
		
		Properties prop = getProp();
		
		extensoes = prop.getProperty("prop.extensoes");
		
		return extensoes; 
		
		
	}
	
	public String getLocalIndice() throws IOException{
		
		Properties prop = getProp();
	
		String localIndice = prop.getProperty("prop.local.indice");
		
		return localIndice;
		
	}
	
	public String getLocalDocs() throws IOException{
		
		Properties prop = getProp();
		
		String localDocs = prop.getProperty("prop.local.docs");
		
		return localDocs;
		
		
	}


}
