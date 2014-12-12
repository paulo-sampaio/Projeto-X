package Gui;

/*import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextPane;
*/
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*; 

public class Gui extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public Gui() {
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 12, 487, 22);
		add(textField);
		textField.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(511, 9, 117, 25);
		add(btnBuscar);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(204, 50, 424, 360);
		add(textPane);
		
		// Exemplo de lista
		String	listData[] = {
			"Arquivo 1",
			"Arquivo 2",
			"Arquivo 3",
			"Arquivo 4"
		};
		JList list = new JList(listData);
		list.setBounds(12, 50, 180, 360);
		// Funcao para checar quando um item da lista eh selecionado e chamar 
		// uma funcao pra trocar o conteudo da caixa de texto ao lado. 
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					// Aqui teremos a funcao que ira colocar o conteudo 
					// do arquivo selecionado na caixa de texto ao lado
					System.out.println("teste");
				}
			}
		});
		add(list);

	}
}
