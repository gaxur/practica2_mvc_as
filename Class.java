import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

class Class {
    private Rectangle bounds;
	private String name;
    private boolean selected = false;
    private boolean highlighted = false;


    public Class(Point location, int N) {
		this.name = "Class" + N;
        bounds = new Rectangle(location.x, location.y, 150, 100);
	}

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    
    public void draw(Graphics g) {
        // Dibujar el rectángulo de la clase
        if (highlighted) {
            g.setColor(Color.GREEN);
        } else if (selected) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(Color.WHITE);
        }

        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        // Obtener las métricas para centrar el texto
        FontMetrics metrics = g.getFontMetrics();

        // Calcular las divisiones del cuadrado
        int nombreAltura = 20; // Altura para el nombre
        int atributosAltura = 30; // Altura para los atributos
        int metodosAltura = 30; // Altura para los métodos

        // Calcular las posiciones Y de cada sección
        int nombreY = this.getY() + metrics.getAscent() + 2;
        int atributosY = this.getY() + nombreAltura + (atributosAltura / 2) + (metrics.getAscent() / 2);
        int metodosY = this.getY() + nombreAltura + atributosAltura + (metodosAltura / 2) + (metrics.getAscent() / 2);

        // Centrar el nombre de la clase
        int nombreWidth = metrics.stringWidth(name);
        int nombreX = this.getX() + (this.getWidth() - nombreWidth) / 2;
        g.drawString(name, nombreX, nombreY);

        // Dibujar línea separadora debajo del nombre
        g.drawLine(this.getX(), this.getY() + nombreAltura, this.getX() + this.getWidth(), this.getY() + nombreAltura);

        // Centrar "atributos"
        int atributosWidth = metrics.stringWidth("atributos");
        int atributosX = this.getX() + (this.getWidth() - atributosWidth) / 2;
        g.drawString("atributos", atributosX, atributosY);

        // Dibujar línea separadora entre atributos y métodos
        g.drawLine(this.getX(), this.getY() + nombreAltura + atributosAltura, this.getX() + this.getWidth(), this.getY() + nombreAltura + atributosAltura);

        // Centrar "metodos"
        int metodosWidth = metrics.stringWidth("metodos");
        int metodosX = this.getX() + (this.getWidth() - metodosWidth) / 2;
        g.drawString("metodos", metodosX, metodosY);

   }
    
    public boolean contains(Point p) {
        return bounds.contains(p);
    }
    
    public void setPosition(int dx, int dy) {
        bounds.translate(dx, dy);
    }

    public int getX() {
        return bounds.x;
    }
    
    public int getY() {
        return bounds.y;
    }
    
    public int getWidth() {
        return bounds.width;
    }
    
    public int getHeight() {
        return bounds.height;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
