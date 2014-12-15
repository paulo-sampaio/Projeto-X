// Este exemplo mostra como selecionar diretório usando
// JFileChooser a partir de uma JFrame
package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Estudos extends JFrame{
    public Estudos(){
        super("Escolher um diretório usando JFileChooser");
    
        Container c = getContentPane();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        c.setLayout(layout);
        
        JButton btn = new JButton("Escolher Diretório");
        btn.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JFileChooser fc = new JFileChooser();
                    
                    // restringe a amostra a diretorios apenas
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    
                    int res = fc.showOpenDialog(null);
                    
                    if(res == JFileChooser.APPROVE_OPTION){
                        File diretorio = fc.getSelectedFile();
                        JOptionPane.showMessageDialog(null, "Voce escolheu o diretório: " + diretorio.getName());
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum diretorio."); 
                }
            }   
        );
    
        c.add(btn);
        
        setSize(400, 200);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Estudos app = new Estudos();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
