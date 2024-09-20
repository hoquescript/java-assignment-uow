import java.awt.Button;

public class UI {
	public void addButton(Panel panel) {
		Button b = new Button();
		b.setVisible(true);
		b.setAttrib(0);
		panel.init();
		panel.addElement(b);
		b.addMouseListener(panel);
		panel.setAttrib(b.getAttrib(panel));
		panel.init();
	}
}
