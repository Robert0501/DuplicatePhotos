package packGraficInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Finish {
	protected static JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JLabel finish = new JLabel("We found " + counter + " duplicate photos");

	private JButton ok = new JButton("Ok");

	public static int counter = 0;

	public Finish() {
		panel();
		frame();
		finish();
		ok();
	}

	private void panel() {
		panel.setLayout(null);
		panel.setBackground(Color.white);
		panel.add(finish);
		panel.add(ok);
	}

	private void frame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
		frame.setBounds(800, 480, 450, 150);
		frame.setIconImage(GraficInterface.img.getImage());
	}

	private void finish() {
		finish.setBounds(70, 35, 400, 20);
		finish.setFont(new Font("ARIAL", Font.BOLD, 20));
	}

	private void ok() {
		ok.setBounds(180, 80, 50, 25);
		ok.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				GraficInterface.fail.setText("");
			}
		});
	}
}
