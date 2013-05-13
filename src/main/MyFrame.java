package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class MyFrame extends JFrame {
	private JPanel panel;
	private GridBagLayout gbl;
	private String sid;
	private String uid;
	private boolean next;
	private Matcher match;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4874481754647933051L;

	private String[][] a = { { "幻兽接口", "pet" }, { "背包接口", "bag" },
			{ "竞技接口", "athletics" }, { "主角", "status" }, { "任务", "mission" },
			{ "副本", "instance" }, { "阵容", "formation" }, { "英雄", "general" },
			{ "技能", "skill" }, { "强化", "reinforce" },

	};

	private String[][] m = { { "" }, { "" }, { "" }, { "" }, { "" }, { "" },
			{ "" }, { "" }, { "" }, { "" }, };

	public MyFrame() {
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		init();
	}

	public void init() {
		panel = new JPanel();
		panel.setBackground(Color.red);
		add(panel);
		
		gbl = new GridBagLayout();
		panel.setLayout(gbl);
		JLabel id = new JLabel("open_id");
		id.setFont(new Font("迷你简粗圆", Font.PLAIN, 20));
		setGbc(id, 1, 1, 1, 1, 1, 1);
		panel.add(id);

		final JTextField jf = new JTextField();
		setGbc(jf, 2, 1, 1, 1, 5, 1);
		panel.add(jf);

		JComboBox box = new JComboBox();
		box.setFont(new Font("迷你简粗圆", Font.PLAIN, 20));
		for (int i = 0; i < a.length; i++) {
			box.addItem(a[i][0]);
		}
		setGbc(box, 1, 2, 1, 1, 5, 1);
		panel.add(box);
		box.setEnabled(false);

		JComboBox box1 = new JComboBox();
		box1.addItem("get");
		setGbc(box1, 2, 2, 1, 1, 1, 1);
		panel.add(box1);
		box1.setEnabled(false);

		final JTextArea text = new JTextArea();
		text.setFont(new Font("黑体", Font.BOLD, 20));
		text.setEditable(false);

		final JScrollPane scroll = new JScrollPane(text);
		setGbc(scroll, 1, 5, 4, 5, 1, 20);
		panel.add(scroll);

		JButton btn = new JButton("确定");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("");

			}
		});
		setGbc(btn, 1, 3, 1, 1, 1, 1);
		panel.add(btn);
		btn.setEnabled(false);

		JButton clear = new JButton("清除");
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				text.setText("");
			}

		});
		setGbc(clear, 2, 3, 1, 1, 1, 1);
		panel.add(clear);
		clear.setEnabled(false);

		JButton logout = new JButton("退出登陆");
		setGbc(logout, 3, 3, 1, 1, 1, 1);
		panel.add(logout);
		logout.setEnabled(false);

		JButton login = new JButton("登陆");
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!jf.getText().trim().equals("")) {
					text.setText("");
					sendHttp("server_id=1&a=develop&m=login&debug=1&open_id="
							+ jf.getText().trim(), text);
				} else {
					text.setText("请先输入帐号");
				}
			}
		});
		setGbc(login, 3, 1, 1, 1, 2, 1);
		panel.add(login);

		final JTextField search = new JTextField();
		search.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				match = null;
			}
		});
		search.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String str = search.getText().trim();
					if (!str.equals("")) {
						if (match != null) {
							if(match.find()){
								text.getCaret().setDot(match.start());
								text.getCaret().moveDot(match.end());
								text.getCaret().setSelectionVisible(true);
							}else{
								match.reset();
							}

						} else {
							Pattern p = Pattern.compile(str);
							match = p.matcher(text.getText());
							if(match.find()){
								text.getCaret().setDot(match.start());
								text.getCaret().moveDot(match.end());
								text.getCaret().setSelectionVisible(true);
							}

						}
					}
				}
			}
		});
		setGbc(search, 1, 4, 1, 1, 1, 1);
		panel.add(search);

	}

	public void setGbc(Component com, int x, int y, int width, int height,
			int weightx, int weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weighty = weighty;
		gbc.weightx = weightx;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(com, gbc);
	}

	private void sendHttp(String send, JTextArea text) {
		HttpURLConnection con = null;
		try {
			URL http = new URL("http://android-shuihu.huanleya.com/app.php");
			con = (HttpURLConnection) http.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			out.write(send);
			out.flush();
			out.close();

			InputStream in = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				text.append(str + "\n");
			}
			br.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
}
