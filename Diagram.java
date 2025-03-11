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
		private Class lastselectedClass = null; // Clase seleccionada azul (S)
		private Class highlightedClass = null; // Clase seleccionada verde (S + move)

		private Point lastMousePosition = null; // Última posición del ratón
		private boolean mousePressedBefore = false; // Si el ratón ha sido presionado antes
		private Point currentMousePosition = null; // Guarda la posición actual del ratón mientras se arrastra
		private Stack<Class> deletedClasses = new Stack<>();

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
		public void addClass(Point position) {
			System.out.println("Añadiendo clase en " + position);
		
			Class newClass;
			
			if (!deletedClasses.isEmpty()) {
				// Recuperar la última clase eliminada
				newClass = deletedClasses.pop();
				System.out.println("Recuperando clase eliminada: " + newClass.getName());
			} else {
				// Crear una nueva clase con el número secuencial normal
				newClass = new Class(position, getNClasses());
			}
		
			// Agregar la clase al diagrama
			classes.add(newClass);
			window.updateNClasses(this);
			repaint();
		}
		

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
			// Dibujar línea temporal mientras se arrastra el ratón (si hay una clase seleccionada)
			if (lastselectedClass != null && currentMousePosition != null && mousePressedBefore) {
				g.setColor(Color.BLACK); // Color de la línea
				g.drawLine(lastselectedClass.getX() + lastselectedClass.getWidth() / 2,
						lastselectedClass.getY() + lastselectedClass.getHeight() / 2,
						currentMousePosition.x, 
						currentMousePosition.y);
			}
		}
		
		/********************************************/
		/** MÈtodos de MouseListener               **/
		/********************************************/

		// Se ejecuta cuando el usuario presiona cualquier botón del ratón sobre el componente.
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				for (Class c : classes) {
					if (c.contains(e.getPoint())) {	
						selectedClass = c;
						lastMousePosition = e.getPoint();
						return;	
					}
				}
				selectedClass = null;
			}
		}
		
		// Se ejecuta cuando el usuario suelta el botón del ratón después de haberlo presionado.
		public void mouseReleased(MouseEvent e) {
			if (mousePressedBefore){
				for (Class c : classes) {
					if (c.contains(e.getPoint()) && lastselectedClass != null && hayAsociacion(lastselectedClass, c) == false) {
						Association a = new Association(lastselectedClass, c);
						associations.add(a);
						window.updateNAssociations(this);
						c.setHighlighted(false);
						lastselectedClass.setSelected(false);
						repaint();
						break;
					}
				}
				selectedClass = null;
				lastMousePosition = null;
				mousePressedBefore = false;
			}	
			// Borrar la línea temporal al soltar el ratón
    		currentMousePosition = null;
    		repaint(); // Redibujar sin la línea
		}

		public boolean hayAsociacion(Class c1, Class c2){
			for (Association a : associations) {
				if ((a.getClass1() == c1 && a.getClass2() == c2) || (a.getClass1() == c2 && a.getClass2() == c1)) {
					return true;
				}
			}
			return false;
		}
		
		// Se ejecuta cuando el cursor entra en el área del componente JPanel.
		public void mouseEntered(MouseEvent e) {
			
		}
		
		// Se ejecuta cuando el cursor sale del área del componente JPanel.
		public void mouseExited(MouseEvent e) {
		
		}
		
		// Se ejecuta cuando el usuario hace clic y suelta el botón del ratón en el mismo lugar.
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) { // Clic derecho para eliminar una clase
				Iterator<Class> classIterator = classes.iterator();
				while (classIterator.hasNext()) {
					Class c = classIterator.next();
					if (c.contains(e.getPoint())) {
						System.out.println("Eliminando clase y sus asociaciones: " + c.getName());
						// Se pushea la clase eliminada para poder recuperarla
						deletedClasses.push(c);
						// Usar Iterator para eliminar asociaciones sin errores
						Iterator<Association> associationIterator = associations.iterator();
						while (associationIterator.hasNext()) {
							Association a = associationIterator.next();
							if (a.getClass1() == c || a.getClass2() == c) {
								associationIterator.remove();
								window.updateNAssociations(this);
							}
						}
						classIterator.remove();
						window.updateNClasses(this);
						repaint();
						return;
					}
				}
			}
		}
		

		/********************************************/
		/** MÈtodos de MouseMotionListener         **/
		/********************************************/    
		
		// Se ejecuta cuando el cursor se mueve sobre el componente sin presionar botones.
		public void mouseMoved(MouseEvent e) {
			for (int i = 0; i < classes.size(); i++) {
            	Class c = classes.get(i);
            	if (c.contains(e.getPoint())) {
                	classes.remove(i);
                	classes.add(c);
                	repaint();
                	return;
            	}
        	}
		}
		
		// Se ejecuta cuando el usuario mueve el cursor mientras mantiene presionado un botón del ratón.
		public void mouseDragged(MouseEvent e) {
			currentMousePosition = e.getPoint();
			if (SwingUtilities.isLeftMouseButton(e)) {
				for (Class c : classes) {
					System.out.println("Clase seleccionada: " + c.getName());
					if (lastselectedClass == c && c.isSelected()) {
						mousePressedBefore = true;
						for (Class c2 : classes) {
							if (c2.contains(e.getPoint())) {
								if (!hayAsociacion(selectedClass, c2)){
									if(highlightedClass != null && highlightedClass != c2){
										highlightedClass.setHighlighted(false);
									}
									highlightedClass = c2;
									highlightedClass.setHighlighted(true);
									repaint();
								}
								else{
									if (highlightedClass != null) {
										highlightedClass.setHighlighted(false);
										highlightedClass = null;
										repaint();
									}
								}
							}
						}

					} 
					else if (c.isSelected() == false && selectedClass == c) { // Condición modificada y poner aqui despues la logica
						int dx = e.getX() - lastMousePosition.x;
						int dy = e.getY() - lastMousePosition.y;
						selectedClass.setPosition(dx, dy);
						lastMousePosition = e.getPoint();
						repaint();
					} else if(highlightedClass != null){
						highlightedClass.setHighlighted(false);
						highlightedClass = null;
						repaint();
					}
				}
				repaint();
			}
		}
		
		/********************************************/
		/** MÈtodos de KeyListener                 **/
		/********************************************/

		public void keyTyped(KeyEvent e) {
		// Si se presiona una tecla mientras hay una clase seleccionada
		/*
			if (selectedClass != null && Character.isLetterOrDigit(e.getKeyChar())) {
				String newName = selectedClass.getName() + e.getKeyChar();
				selectedClass.setName(newName);
				repaint();
			}
			*/
		}
		
		
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_S) {
				Point mousePosition = getMousePosition();
				if (mousePosition != null) {
					for (Class c : classes) {
						if (c.contains(mousePosition)) {
							if (c.isSelected()) {
								c.setSelected(false);
								lastselectedClass = null;
							} else {
								if (lastselectedClass != null) {
									lastselectedClass.setSelected(false);
								}
								c.setSelected(true);
								lastselectedClass = c;
							}
							repaint();
							return;
						}
					}
				}
				if (lastselectedClass != null) {
					lastselectedClass.setSelected(false);
					lastselectedClass = null;
					repaint();
				}
			}
		}
	
		
		public void keyReleased(KeyEvent e) {
			//No es necesaria implementación
		}
	}
