import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


class Association {
    private Class class1, class2;
    
    public Association(Class c1, Class c2) {
        class1 = c1;
        class2 = c2;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        if (class1 == class2) {
             int x = class1.getX() + 50;
            int y = class1.getY() + 25;
            
            // Dibujar un rectángulo pequeño en la parte superior derecha
            g.drawRect(x, y - 75, 70, 50);
        } else {
            // Dibujar una asociación normal (línea recta)
            g.drawLine(class1.getX() + 50, class1.getY() + 25,
                    class2.getX() + 50, class2.getY() + 25);
        }
    }

    public Class getClass1() {
        return class1;
    }

    public Class getClass2() {
        return class2;
    }
}
