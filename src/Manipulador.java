import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Manipulador {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"./properties/dados.properties");
		props.load(file);
		return props;

	}

	public static void  main(String args[]) throws IOException {
		String extensoes; //Variavel que guardará as extensões suportadas

		System.out.println("************Teste de leitura do arquivo de propriedades************");
		
		Properties prop = getProp();
		
		extensoes = prop.getProperty("prop.extensoes");

		
		System.out.println("Login = " + extensoes);

	}
}
