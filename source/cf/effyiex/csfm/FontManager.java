package cf.effyiex.csfm;

import cf.effyiex.JDBSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FontManager extends JFrame {

    public static final FontManager INSTANCE = new FontManager();
    public static final File WIN_FONTS = new File("C:\\Windows\\Fonts");

    private static String preGivenDir = System.getProperty("user.dir");
    private static String isBackedUp = "false";

    public static void main(String[] args) {
        JDBSettings.register(FontManager.class, "isBackedUp");
        JDBSettings.register(FontManager.class, "preGivenDir");
        JDBSettings.load();
        INSTANCE.initialize();
    }

    private JPanel panel = new JPanel() {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(33, 33, 44));
            g.drawLine(getWidth() / 2, 1, getWidth() / 2, getHeight() - 3);
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            g.setFont(new Font(g.getFont().getName(), Font.BOLD, 24));
            g.setColor(new Color(125, 25, 90));
            g.drawString("Backup", getWidth() / 4 - g.getFontMetrics().stringWidth("Backup") / 2, g.getFont().getSize() * 2 + 3);
            g.drawString("Injector", getWidth() / 4 * 3 - g.getFontMetrics().stringWidth("Injector") / 2, g.getFont().getSize() * 2 + 3);
            g.setColor(new Color(250, 50, 180));
            g.drawString("Backup", getWidth() / 4 - g.getFontMetrics().stringWidth("Backup") / 2, g.getFont().getSize() * 2);
            g.drawString("Injector", getWidth() / 4 * 3 - g.getFontMetrics().stringWidth("Injector") / 2, g.getFont().getSize() * 2);
            if(!isBackedUp.equals("true")) {
                g.setFont(new Font(g.getFont().getName(), Font.BOLD, 10));
                g.drawString("Don't forget to make a Backup first!", getWidth() / 2
                        - g.getFontMetrics().stringWidth("Don't forget to make a Backup first!") / 2, (int) (g.getFont().getSize() * 7.75F));
            }
        }

    };

    private JButton bMake = new JButton("Make Backup") {

        @Override
        protected void paintComponent(Graphics g) {
            renderButton(this, g);
        }

    };

    private JButton bLoad = new JButton("Load Backup") {

        @Override
        protected void paintComponent(Graphics g) {
            renderButton(this, g);
        }

    };

    private JButton injector = new JButton("Inject Font") {

        @Override
        protected void paintComponent(Graphics g) {
            renderButton(this, g);
        }

    };

    private List<String> fonts = new ArrayList();
    private JComboBox<String> fontBox = new JComboBox();

    private void renderButton(JButton b, Graphics g3d) {
        Graphics2D g = (Graphics2D) g3d;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(22, 22, 33));
        g.fillRect(0, 0, b.getWidth(), b.getHeight());
        g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 14));
        g.setColor(new Color(180, 50, 250));
        g.drawString(b.getText(), b.getWidth() / 2 - g.getFontMetrics().stringWidth(b.getText()) / 2, (b.getHeight() + g.getFont().getSize()) / 2);
    }

    private void popup(String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    private File dir, file, backup;

    private void addFonts(File[] array) {
        for(File fontFile : array) {
            if(!fontFile.getName().toLowerCase().endsWith(".ttf")) continue;
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                String temp = font.getName().toLowerCase();
                if(temp.contains("bold") || temp.contains("italic")) continue;
                fonts.add(fontFile.getAbsolutePath());
                fontBox.addItem(font.getName());
            }
            catch (FontFormatException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void initialize() {
        File appdata = new File(new File(System.getenv("APPDATA")).getParentFile().getAbsolutePath() + "\\Local\\Microsoft\\Windows\\Fonts");
        this.addFonts(appdata.listFiles());
        this.addFonts(WIN_FONTS.listFiles());
        JFileChooser chooser = new JFileChooser(preGivenDir);
        chooser.setDialogTitle("Choose the CSGO-Executable...");
        if(chooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) System.exit(0);
        this.dir = chooser.getSelectedFile();
        if(!dir.getName().endsWith("csgo.exe")) {
            this.popup("You selected a wrong File, idiot!");
            System.exit(0);
        }
        preGivenDir = dir.getParentFile().getAbsolutePath();
        this.dir = new File(dir.getParentFile().getAbsolutePath() + "\\csgo\\panorama\\fonts");
        this.file = new File(dir.getAbsolutePath() + "\\fonts.conf");
        this.backup = new File(dir.getAbsolutePath() + "\\fonts.backup");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("CSFontManager by Effyiex");
        this.setSize(360, 240);
        this.centerize();
        this.setResizable(false);
        //this.popup(dir.getAbsolutePath());
        this.panel.setBackground(new Color(55, 55, 66));
        this.panel.setLayout(null);
        this.bMake.setBounds(16, getHeight() / 3 + 16, getWidth() / 2 - 36, 32);
        this.bLoad.setBounds(16, getHeight() / 3 + 64, getWidth() / 2 - 36, 32);
        this.injector.setBounds(getWidth() / 2 + 5, getHeight() / 3 + 64, getWidth() / 2 - 36, 32);
        this.fontBox.setBounds(getWidth() / 2 + 5, getHeight() / 3 + 16, getWidth() / 2 - 36, 32);
        this.panel.add(fontBox);
        this.panel.add(injector);
        this.panel.add(bLoad);
        this.panel.add(bMake);
        this.fontBox.addActionListener(action -> {
            String ttf = fonts.get(fontBox.getSelectedIndex());
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(ttf)).deriveFont(18.0F);
                injector.setFont(font);
                bLoad.setFont(font);
                bMake.setFont(font);
            }
            catch (FontFormatException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
        });
        this.bMake.addActionListener(action -> {
            try {
                if(!backup.exists()) backup.createNewFile();
                else {
                    popup("There is a backup already!");
                    return;
                }
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(backup);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fos.write(buffer);
                fis.close();
                fos.close();
                isBackedUp = "true";
                JDBSettings.save();
                panel.repaint();
                popup("Successfully backed up!");
            } catch (IOException e) { e.printStackTrace(); }
        });
        this.bLoad.addActionListener(action -> {
            if(!isBackedUp.equals("true")) popup("There is no backup!");
            else try {
                FileInputStream fis = new FileInputStream(backup);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fos.write(buffer);
                fis.close();
                fos.close();
                popup("Successfully loaded the backed!");
            } catch (IOException e) { e.printStackTrace(); }
        });
        this.injector.addActionListener(action -> {
            try {
                File ttfFile = new File(fonts.get(fontBox.getSelectedIndex()));
                String name = fontBox.getSelectedItem().toString(),
                        ttf = ttfFile.getName().replace(".ttf", new StringBuffer().toString());
                File ttfOut = new File(dir.getAbsolutePath() + "\\" + ttf + ".ttf");
                InputStream is = FontManager.class.getResourceAsStream("fonts.conf");
                byte[] in = new byte[is.available()];
                is.read(in);
                String xml = new String(in).replace("{CSFM_FONT_NAME}", name).replace("{CSFM_FILE_NAME}", ttf);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(xml.getBytes());
                fos.close();
                is.close();
                if(!ttfOut.exists()) ttfOut.createNewFile();
                is = new FileInputStream(ttfFile);
                in = new byte[is.available()];
                is.read(in);
                fos = new FileOutputStream(ttfOut);
                fos.write(in);
                fos.close();
                is.close();
                popup("Successfully injected Font!");
            } catch (IOException e) { e.printStackTrace(); }
        });
        this.add(panel);
        try(InputStream stream = FontManager.class.getResourceAsStream("icon.png")) {
            FontManager.this.setIconImage(ImageIO.read(stream));
        } catch (IOException e) { e.printStackTrace(); }
        this.setVisible(true);
        JDBSettings.save();
    }

    public void centerize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

}
