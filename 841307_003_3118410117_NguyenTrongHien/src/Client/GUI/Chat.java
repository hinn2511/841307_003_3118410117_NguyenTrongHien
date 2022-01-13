package Client.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Client.Client;

import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Chat extends JFrame {

	private static final long serialVersionUID = 1L;
	public static JTextPane txtKhungChat;
	public static StyledDocument doc;
	private static SimpleAttributeSet right = new SimpleAttributeSet();
	private static SimpleAttributeSet left = new SimpleAttributeSet();
	
	private JPanel contentPane;
	private JTextField txtTinNhan;

	public Chat(String nickname, String otherNickname) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 602);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		final JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		txtKhungChat = new JTextPane();
		txtKhungChat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtKhungChat.setBackground(Color.WHITE);
		txtKhungChat.setEditable(false);
		txtKhungChat.setBounds(10, 116, 738, 378);
		panel.add(txtKhungChat);

		txtTinNhan = new JTextField();
		txtTinNhan.setBounds(10, 515, 615, 31);
		panel.add(txtTinNhan);
		txtTinNhan.setColumns(10);
		
		txtTinNhan.addKeyListener(new KeyAdapter() {
		        public void keyPressed(KeyEvent e) {
		            if(e.getKeyCode() == KeyEvent.VK_ENTER){
		            	if(txtTinNhan.getText().equals(""))
							return;
						try {
							Client.sendRequest("send-message:" + txtTinNhan.getText());
							showMyMessage(txtTinNhan.getText());
							txtTinNhan.setText("");
						} catch (IOException | BadLocationException e2) {
							Client.alert("Đã có lỗi xảy ra.");
						}
		            }
		        }

		    });

		doc = txtKhungChat.getStyledDocument();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);

		JButton btnGui = new JButton("Gửi");
		btnGui.setForeground(new Color(0, 0, 139));
		btnGui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtTinNhan.getText().equals(""))
					return;
				try {
					Client.sendRequest("send-message:" + txtTinNhan.getText());
					showMyMessage(txtTinNhan.getText());
					txtTinNhan.setText("");
				} catch (IOException | BadLocationException e2) {
					Client.alert("Đã có lỗi xảy ra.");
				}
				
			}

		});
		btnGui.setFont(new Font("Arial", Font.BOLD, 14));
		btnGui.setBounds(635, 514, 113, 31);
		panel.add(btnGui);

		JLabel lblNewLabel = new JLabel("Đang chat với ");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 83, 127, 23);
		panel.add(lblNewLabel);

		JLabel lblNguoiNhan = new JLabel("User");
		lblNguoiNhan.setForeground(new Color(220, 20, 60));
		lblNguoiNhan.setFont(new Font("Arial", Font.BOLD, 14));
		lblNguoiNhan.setBounds(140, 83, 160, 23);
		panel.add(lblNguoiNhan);

		lblNguoiNhan.setText(otherNickname);
		
		JLabel lblPhngChat = new JLabel("PHÒNG CHAT");
		lblPhngChat.setForeground(new Color(70, 130, 180));
		lblPhngChat.setFont(new Font("Ebrima", Font.BOLD, 24));
		lblPhngChat.setBounds(10, 10, 210, 42);
		panel.add(lblPhngChat);
		
		JLabel lblToi = new JLabel("Nickname của tôi");
		lblToi.setFont(new Font("Arial", Font.PLAIN, 14));
		lblToi.setBounds(10, 51, 120, 23);
		panel.add(lblToi);
		
		JLabel lblNguoiGui = new JLabel("<dynamic>");
		lblNguoiGui.setForeground(new Color(0, 139, 139));
		lblNguoiGui.setFont(new Font("Arial", Font.BOLD, 14));
		lblNguoiGui.setBounds(140, 50, 160, 23);
		panel.add(lblNguoiGui);
		
		lblNguoiGui.setText(nickname);
		
	}

	
	public static void showMyMessage(String message)
			throws BadLocationException {
		int length = doc.getLength();
		doc.insertString(doc.getLength(), "\n" + Client.clientNickname + ": " + message, null);
		doc.setParagraphAttributes(length + 1, 1, right, false);
	}
	
	public static void showOtherMessage(String message)
			throws BadLocationException {
		int length = doc.getLength();
		doc.insertString(doc.getLength(), "\n" + Client.otherClientNickname + ": " + message, null);
		doc.setParagraphAttributes(length + 1, 1, left, false);
	}
}
