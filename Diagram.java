	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
	import java.util.*;

	public class Diagram 
			extends JPanel 
			implements MouseListener, 
				MouseMotionListener, 
				KeyListener {
		
		//atributos
		private Window window; // Ventana en la que está el diagrama
		private Vector<Class> classes = new Vector<>(); // Clases creadas por el usuario
		private Vector<Association> associations = new Vector<>(); // Asociaciones creadas por el usuario
		private Class selectedClass = null; // Clase seleccionada
		private Point lastMousePosition = null; // Última posición del ratón
	

		//metodos
		public Diagram(Window theWindow) {
			window = theWindow;
			
			addMouseListener(this);
			addMouseMotionListener(this);
			addKeyListener(this);
			
			setBorder(BorderFactory.createLineBorder(Color.black));
			setFocusable(true);
		}
		
		//Añade una clase al diagrama
		public void addClass(Point position){
			System.out.println("Añadiendo clase en " + position);
			Class newClass = new Class(position, getNClasses());
			classes.add(newClass);
			window.updateNClasses(this);
			repaint();
		}

/*
		//Añade una asociación al diagrama
		public void addAssociation(Point position){
			System.out.println("Añadiendo asociacion en " + position);
			Class newClass = new Association();
			classes.add(newClass);
			updateNAssociations();
			repaint();
		}
*/

		//Devuelve el número de clases
		public int getNClasses(){
			return classes.size();
		}
		
		//Devuelve el numero de asociaciones
		public int getNAssociations(){
			return associations.size();
		}

		//Dibuja el diagrama de clases
		public void paint(Graphics g) {
			super.paint(g);
			for (Class c : classes) {
				c.draw(g);
			}
			for (Association a : associations) {
				a.draw(g);
			}
		}
		
		/********************************************/
		/** MÈtodos de MouseListener               **/
		/********************************************/

		public void mousePressed(MouseEvent e) {
			for (Class c : classes) {
				if (c.contains(e.getPoint())) {
					selectedClass = c;
					lastMousePosition = e.getPoint();
					return;
				}
			}
			selectedClass = null;
		}
		
		public void mouseReleased(MouseEvent e) {
			selectedClass = null;
		}
		
		public void mouseEntered(MouseEvent e) {
			// Si se hace clic derecho, se añade una nueva clase en la posición del cursor
			if (SwingUtilities.isRightMouseButton(e)) {
				addClass(e.getPoint());
			}
		}
		
		public void mouseExited(MouseEvent e) {
			// Si se está arrastrando una clase seleccionada
			if (selectedClass != null) {
				int dx = e.getX() - lastMousePosition.x;
				int dy = e.getY() - lastMousePosition.y;
				selectedClass.setPosition(selectedClass.getX() + dx, selectedClass.getY() + dy);
				lastMousePosition = e.getPoint();
				repaint();
			}
		}
		
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				addClass(e.getPoint());
			}
		}

		/********************************************/
		/** MÈtodos de MouseMotionListener         **/
		/********************************************/    
		public void mouseMoved(MouseEvent e) {
			//…
		}
		
		public void mouseDragged(MouseEvent e) {
			if (selectedClass != null && lastMousePosition != null) {
				int dx = e.getX() - lastMousePosition.x;
				int dy = e.getY() - lastMousePosition.y;
				selectedClass.setPosition(dx, dy);
				lastMousePosition = e.getPoint();
				repaint();
			}
		}
		
		/********************************************/
		/** MÈtodos de KeyListener                 **/
		/********************************************/

		public void keyTyped(KeyEvent e) {
		// Si se presiona una tecla mientras hay una clase seleccionada
			if (selectedClass != null && Character.isLetterOrDigit(e.getKeyChar())) {
				String newName = selectedClass.getName() + e.getKeyChar();
				selectedClass.setName(newName);
				repaint();
			}
		}
		
		public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_DELETE && selectedClass != null) {
        	classes.remove(selectedClass);
        	selectedClass = null;
        	repaint();
    	} else if (e.getKeyCode() == KeyEvent.VK_S && selectedClass != null) {
        	selectedClass.setHighlighted(true); // Método que cambia el color a cian
        	repaint();
    	}
	}
		
		public void keyReleased(KeyEvent e) {
			//No es necesaria implementación
		}
	}
