package gestionhospitalisation.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public interface IPanel {

    // Color
    Color BACKGROUND_COLOR = new Color(240, 248, 255);
    Color HEADER_COLOR = new Color(41, 128, 185);
    Color TEXT_COLOR = Color.WHITE;
    Color SEARCH_BUTTON_COLOR = new Color(135, 206, 250);
    Color BUTTON_PANEL_COLOR = new Color(51, 153, 255);
    Color ADD_BUTTON_COLOR = new Color(46, 204, 113);
    Color EDIT_BUTTON_COLOR = new Color(52, 152, 219);
    Color DELETE_BUTTON_COLOR = new Color(231, 76, 60);
    Color CLEAR_BUTTON_COLOR = new Color(149, 165, 166);
    Color BTN_PANEL_COLOR = new Color(51, 153, 255);
    Color btnPanel_Color = new Color(51, 153, 255);
    Color  UPDATE_BUTTON_COLOR = new Color(52, 152, 219);



    default void styleButton(JButton button, Color buttonColor) {
        button.setBackground(buttonColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBounds().contains(evt.getPoint()) ?
                        buttonColor.brighter() : buttonColor);
            }
        });
    }

}