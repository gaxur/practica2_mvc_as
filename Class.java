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
	private List<String> metodos;
	private List<String> atributos;
    private boolean highlighted = false;

    public Class(Point location, int N) {
		this.name = "Class" + N;
        bounds = new Rectangle(location.x, location.y, 100, 50);
		metodos = new ArrayList<>();
		atributos = new ArrayList<>();
	}

	public void setHighlighted(boolean highlighted) {
    	this.highlighted = highlighted;
	}
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar el rectángulo de la clase
        g2.setColor(Color.WHITE);
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g2.setColor(Color.BLACK);
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Obtener las métricas para centrar el texto
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(name);
        int textX = this.getX() + (this.getWidth() - textWidth) / 2; // Centrar el texto horizontalmente
        int textY = this.getY() + metrics.getAscent() + 2; // Pequeño margen superior

        // Dibujar el nombre de la clase centrado en la franja superior
        g.drawString(name, textX, textY);

        // Dibujar línea separadora debajo del nombre
        g.drawLine(this.getX(), this.getY() + 20, this.getX() + bounds.width, this.getY() + 20);

        // Dibujar atributos
        int yOffset = 35;
        for (String atributo : atributos) {
            g.drawString(atributo, this.getX() + 5, this.getY() + yOffset);
            yOffset += 15;
        }

        // Dibujar línea separadora entre atributos y métodos
        g.drawLine(this.getX(), this.getY() + yOffset, this.getX() + this.getWidth(), this.getY() + yOffset);
        yOffset += 15;

        // Dibujar métodos
        for (String metodo : metodos) {
            g.drawString(metodo, this.getX() + 5, this.getY() + yOffset);
            yOffset += 15;
        }
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
