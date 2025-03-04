import java.awt.Graphics;
import java.awt.Graphics2D;
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
        g.drawLine(class1.getX() + 50, class1.getY()  + 25,
                   class2.getX()  + 50, class2.getY() + 25);
    }
}
