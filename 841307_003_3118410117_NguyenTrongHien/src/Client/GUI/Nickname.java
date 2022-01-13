package Client.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.Client;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.Color;

public class Nickname extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNickname;

	public Nickname() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 306);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		txtNickname = new JTextField();
		txtNickname.setBounds(58, 130, 313, 37);
		panel.add(txtNickname);
		txtNickname.setColumns(10);
		
		JButton btnTao = new JButton("Chọn");
		btnTao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtNickname.getText().equals("")) 
				{
					Client.alert("Vui lòng nhập nickname");
					return;
				}
				else {
					try {
						Client.sendRequest("nickname-choosen:" + txtNickname.getText());
					} catch (IOException e1) {
						Client.alert("Đã có lỗi xảy ra");
					}
				}
			}
		});
		
		btnTao.setFont(new Font("Arial", Font.BOLD, 14));
		btnTao.setBounds(58, 175, 313, 37);
		panel.add(btnTao);
		
		JLabel lblNewLabel = new JLabel("Nhập nickname của bạn");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel.setBounds(58, 83, 313, 37);
		panel.add(lblNewLabel);
		
		JLabel lblChnNickname = new JLabel("Chat với người lạ");
		lblChnNickname.setForeground(new Color(70, 130, 180));
		lblChnNickname.setFont(new Font("Arial", Font.BOLD, 20));
		lblChnNickname.setBounds(127, 36, 241, 37);
		panel.add(lblChnNickname);
	}
	
	
}
