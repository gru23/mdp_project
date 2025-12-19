package org.unibl.etf.utils;

import javax.swing.text.*;

public class NumericFilter extends DocumentFilter {

    private boolean allowDecimal;

    public NumericFilter(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        if (isValid(string, fb.getDocument().getText(0, fb.getDocument().getLength())))
            super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
            throws BadLocationException {

        String current = fb.getDocument().getText(0, fb.getDocument().getLength());

        if (isValid(string, current))
            super.replace(fb, offset, length, string, attrs);
    }

    private boolean isValid(String text, String currentText) {
        if (text == null) return false;

        // ALLOW EMPTY (CLEAR)
        if (text.isEmpty()) return true;

        if (!allowDecimal) {
            return text.matches("\\d+");
        }

        if (text.equals(".")) {
            return !currentText.contains(".");
        }

        return text.matches("[0-9.]+");
    }

}
