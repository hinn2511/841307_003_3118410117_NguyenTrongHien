package Client.GUI;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.Client;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.Color;

public class WaitingRoom extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private String nickname = "";

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public WaitingRoom(String nickname) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 287);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("PHÒNG CHỜ");
		lblNewLabel.setForeground(new Color(70, 130, 180));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 24));
		lblNewLabel.setBounds(126, 38, 169, 37);
		panel.add(lblNewLabel);

		JLabel lblNicknameCaBn = new JLabel("Nickname của bạn");
		lblNicknameCaBn.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNicknameCaBn.setBounds(59, 85, 129, 37);
		panel.add(lblNicknameCaBn);

		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setForeground(new Color(0, 139, 139));
		lblNickname.setFont(new Font("Arial", Font.BOLD, 14));
		lblNickname.setBounds(188, 85, 168, 37);
		panel.add(lblNickname);

		lblNickname.setText(nickname);
		
		JButton btnNewButton = new JButton("Tìm kiếm người chat");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Client.sendRequest("searching");
				} catch (IOException e1) {
					Client.alert("Đã có lỗi xảy ra");
				}
			}
		});
		
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 14));
		btnNewButton.setBounds(59, 178, 297, 37);
		panel.add(btnNewButton);
		
		JLabel lblBmVoNt = new JLabel("Bấm vào nút tìm kiếm để bắt đầu kết nối");
		lblBmVoNt.setFont(new Font("Arial", Font.PLAIN, 14));
		lblBmVoNt.setBounds(59, 122, 297, 57);
		panel.add(lblBmVoNt);
	}
	 
	static void alert(String str) {
		JOptionPane.showMessageDialog(null, str);
	}
}
