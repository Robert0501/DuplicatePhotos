package packGraficInterface;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class GraficInterface {
	private JFrame frame = new JFrame("Duplicate Founder");
	private JPanel panel = new JPanel();

	private JLabel initialPathInfo = new JLabel("Insert the directory you want to start searching for duplicates");
	public static JLabel initialPath = new JLabel();
	protected static JLabel fail = new JLabel();
	private JLabel startDuplicateLabel = new JLabel("Just for duplicate photos");
	private JLabel startAllLabel = new JLabel("For all photos");

	private JTextField initialPathField = new JTextField(20);

	private JButton startButton = new JButton("Start");
	private JButton start = new JButton("Start");
	private JButton exitButton = new JButton("Exit");
	private JButton initialBrowse = new JButton("Browse");
	private JButton finalBrowse = new JButton("Browse");
	public static ImageIcon img = new ImageIcon("src\\Images\\icon.png");
	private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

	String photo;
	String secondPhoto;
	static String[] paths = new String[100];
	static int pathsCounter = 0;
	static String[] duplicates = new String[100];
	static String[] firstDuplicates = new String[100];
	static int counter = 0;
	static int comparari = 1;

	static File file;
	static File secondFile;
	static FileWriter fw = null;
	static FileWriter fileWriter = null;

	public GraficInterface() {
		panel();
		frame();
		initialPathInfo();
		start();
		exit();
		initialBrowse();
		startJustDuplicate();
	}

	private void frame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
		frame.setBounds(650, 350, 700, 350);
		frame.setIconImage(img.getImage());
	}

	private void panel() {
		panel.setLayout(null);
		panel.setBackground(Color.white);
		panel.add(initialPathInfo);
		panel.add(initialPathField);
		panel.add(startButton);
		panel.add(exitButton);
		panel.add(initialBrowse);
		panel.add(finalBrowse);
		panel.add(fail);
		panel.add(start);
		panel.add(startDuplicateLabel);
		panel.add(startAllLabel);
	}

	private void initialPathInfo() {
		initialPathInfo.setBounds(30, 50, 550, 20);
		initialPathInfo.setFont(new Font("ARIAL", Font.BOLD, 18));
		initialPathField.setBounds(50, 85, 400, 25);
	}

	private void start() {
		startButton.setBounds(250, 180, 150, 30);
		startAllLabel.setBounds(285, 210, 150, 30);
		fail.setBounds(50, 240, 600, 30);
		fail.setFont(new Font("ARIAL", Font.BOLD, 20));
		startButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (initialPathField.getText().equals("")) {
					fail.setForeground(Color.red);
					fail.setText("You can not start without browsing the initial diractory");
				} else {
					file = new File(initialPath.getText() + "\\Duplicate Photos Found.txt");
					secondFile = new File(initialPath.getText() + "\\Processing.txt");
					try {
						fw = new FileWriter(file);
						fileWriter = new FileWriter(secondFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					fail.setForeground(Color.green);
					fail.setText("Please wait while we are processing your data");
					read(initialPath.getText());
					try {
						checkForAll(paths);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});

	}

	private void startJustDuplicate() {
		start.setBounds(40, 180, 150, 30);
		startDuplicateLabel.setBounds(45, 210, 150, 30);
		fail.setBounds(50, 240, 600, 30);
		fail.setFont(new Font("ARIAL", Font.BOLD, 20));
		start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (initialPathField.getText().equals("")) {
					fail.setForeground(Color.red);
					fail.setText("You can not start without browsing the initial diractory");
				} else {
					file = new File(initialPath.getText() + "\\Duplicates Photo Found.txt");
					secondFile = new File(initialPath.getText() + "\\Processing.txt");
					try {
						fw = new FileWriter(file);
						fileWriter = new FileWriter(secondFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					fail.setForeground(Color.green);
					fail.setText("Please wait while we are processing your data");
					read(initialPath.getText());
					try {
						check(paths);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private void exit() {
		exitButton.setBounds(450, 180, 150, 30);
		exitButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
	}

	private void initialBrowse() {
		initialBrowse.setBounds(520, 80, 100, 30);
		initialBrowse.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				browse(initialPath);
				initialPathField.setText(initialPath.getText());
			}
		});
	}

	private void browse(JLabel label) {
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.showSaveDialog(null);
		label.setText(fileChooser.getSelectedFile().getAbsolutePath());
	}

	private static String name(String path) {
		Scanner scan = new Scanner(path);
		String name = "";
		scan.useDelimiter("\\\\");
		while (scan.hasNext()) {
			name = scan.next();
		}
		scan.close();
		return name;
	}

	public static void checkForAll(String[] pats) throws IOException {
		openFile(secondFile);
		for (int i = 0; i < pathsCounter - 1; i++) {
			for (int j = i + 1; j < pathsCounter; j++) {
				writeProcessingFile(paths[i], paths[j]);
				fileWriter.flush();
				if (isPhoto(paths[i]) && isPhoto(paths[j])) {
					if ((name(paths[j]).equals(name(paths[i])) && resolution(paths[i], paths[j])
							&& creationTime(paths[i], paths[j]))) {
						increaseSpace();
						duplicates[Finish.counter] = paths[j];
						firstDuplicates[Finish.counter] = paths[i];
						Finish.counter++;
					}
				} else {
					if (name(paths[i]).equals(name(paths[j]))) {
						increaseSpace();
						duplicates[Finish.counter] = paths[j];
						firstDuplicates[Finish.counter] = paths[i];
						Finish.counter++;

					}
				}
			}
		}

		for (int i = 0; i < pathsCounter; i++) {
			if (isPhoto(paths[i]) && isDuplicate(paths[i])) {
				duplicates[Finish.counter] = paths[i];
				Finish.counter++;
				counter++;
			}
		}

		for (int i = 0; i < Finish.counter; i++)
			delete(duplicates[i]);

		writeFile();
		new Finish();
	}

	private void check(String[] pats) throws IOException {
		for (int i = 0; i < pathsCounter; i++) {
			if (isPhoto(paths[i]) && isDuplicate(paths[i])) {
				duplicates[Finish.counter] = paths[i];
				Finish.counter++;
				counter++;
			}
		}

		for (int i = 0; i < Finish.counter; i++)
			delete(duplicates[i]);

		writeFile();
		new Finish();
	}

	private static void increaseSpace() {
		if (Finish.counter == duplicates.length) {
			duplicates = Arrays.copyOf(duplicates, duplicates.length + 10);
			firstDuplicates = Arrays.copyOf(firstDuplicates, firstDuplicates.length + 10);
		}
	}

	private static void writeFile() throws IOException {
		openFile(file);

		for (int i = 0; i < Finish.counter - counter; i++) {
			fw.write("Photo found: " + firstDuplicates[i] + "\nPhoto deleted:" + duplicates[i] + "\n\n");
		}

		for (int i = Finish.counter - counter; i < Finish.counter; i++) {
			fw.write("Photo deleted: " + duplicates[i] + "\n");
		}

		fw.close();
	}

	private static void openFile(File filePath) throws IOException {
		Desktop desktop = Desktop.getDesktop();
		desktop.open(filePath);
	}

	private static void writeProcessingFile(String p1, String p2) throws IOException {
		fileWriter.append(comparari + ". Compared: " + p1 + "   with: " + p2 + "\n");
		comparari++;
		fileWriter.flush();
	}

	public static void read(String path) {
		File folder = new File(path);
		File[] contains = folder.listFiles();
		for (int i = 0; i < contains.length; i++) {
			if (contains[i].getAbsoluteFile().isDirectory()) {
				read(contains[i].getAbsolutePath());
			} else if (name(contains[i].getAbsolutePath()).contains(".txt")) {

			} else {
				if (pathsCounter == paths.length) {
					paths = Arrays.copyOf(paths, paths.length + 10);
				}
				paths[pathsCounter] = contains[i].getAbsolutePath();
				pathsCounter++;
			}
		}

	}

	private static void delete(String path) {
		File fileToDelete = new File(path);
		fileToDelete.delete();
	}

	public static boolean resolution(String path1, String path2) throws IOException {
		boolean ok = false;
		File photo1 = new File(path1);
		File photo2 = new File(path2);
		BufferedImage img1 = null;
		BufferedImage img2 = null;
		img1 = ImageIO.read(photo1);
		img2 = ImageIO.read(photo2);
		int w1 = img1.getWidth();
		int w2 = img2.getWidth();
		int h1 = img1.getHeight();
		int h2 = img2.getHeight();
		img1.getData();
		if (w1 == w2 && h1 == h2)
			ok = true;

		return ok;
	}

	public static boolean creationTime(String path1, String path2) throws IOException {
		boolean ok = false;
		Path file = Paths.get(path1);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		String c1 = attr.lastModifiedTime().toString();
		Path file2 = Paths.get(path2);
		BasicFileAttributes attr2 = Files.readAttributes(file2, BasicFileAttributes.class);
		String c2 = attr2.lastModifiedTime().toString();
		if (c1.equals(c2))
			ok = true;
		return ok;
	}

	private static boolean isDuplicate(String path) {
		boolean ok = false;
		for (int i = 0; i < 10; i++) {
			if (path.contains("-" + i + ".") || path.contains("(" + i + ").") || path.contains("Copy")
					|| path.contains("copy")) {
				ok = true;
			}
		}
		return ok;
	}

	private static boolean isPhoto(String path) {
		boolean ok = false;
		if (path.contains(".jpg") || path.contains(".png") || path.contains(".jpeg") || path.contains(".heic")
				|| path.contains(".JPG") || path.contains(".PNG") || path.contains(".JPEG") || path.contains(".HEIC")
				|| path.contains(".cr2") || path.contains(".CR2") || path.contains(".cr3") || path.contains(".CR3")
				|| path.contains(".dng") || path.contains(".DNG")) {
			ok = true;
		}
		return ok;
	}

	// private static boolean isVideo(String path) {
	// boolean ok = false;
	// if (path.contains(".avi") || path.contains(".mp4") || path.contains(".mov")
	// || path.contains(".AVI")
	// || path.contains(".MP4") || path.contains(".MOV") || path.contains(".AAE") ||
	// path.contains(".aae")) {
	// ok = true;
	// }
	// return ok;
	// }

}
