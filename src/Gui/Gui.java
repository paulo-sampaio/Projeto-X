package Gui;

/* 
 * Imports errados/antigos
 * import javax.swing.JPanel;
 * import javax.swing.JTextField;
 * import javax.swing.JButton;
 * import javax.swing.JList;
 * import javax.swing.JTextPane;
 * import java.awt.*;
*/
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* 
				 * Funcao para realizar a busca e retornar uma rray que vai alimentar
				 * a JList a seguir.
				 */
				 //funcao();
			}
		});
		btnBuscar.setBounds(511, 9, 117, 25);
		add(btnBuscar);

		// Este textpane deve exibir o arquivo selecionado da JList abaixo.
		JTextPane textPane = new JTextPane();
		textPane.setText("texto de exemplo");
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
		/* Funcao para checar quando um item da lista eh selecionado e chamar 
		 * uma funcao pra trocar o conteudo da caixa de texto ao lado.
		 */ 
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					/* Aqui teremos a funcao que ira colocar o conteudo 
					 * do arquivo selecionado na caixa de texto ao lado
					 *
					 * textPane.setText("texto de exemplo");
					 */
				}
			}
		});
		add(list);
		


	}
}
