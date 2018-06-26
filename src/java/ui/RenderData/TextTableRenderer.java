package ui.RenderData;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.*;
import java.awt.*;

/**
 * This entire class was pieced together from several places on the internet
 * http://java-sl.com/tip_center_vertically.html
 * https://stackoverflow.com/questions/13933402/how-to-center-text-and-a-jcomponent-in-a-jtextpane-vertically
 */
public class TextTableRenderer extends JTextPane implements TableCellRenderer {

    public TextTableRenderer() {
        setEditorKit(new MyEditorKit());
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = (StyledDocument)getDocument();
        doc.setParagraphAttributes(0, doc.getLength() - 1, attributes, false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        setText((value == null) ? "" : value.toString());
        return this;
    }

    class MyEditorKit extends StyledEditorKit {

        public ViewFactory getViewFactory() {
            return new StyledViewFactory();
        }

        class StyledViewFactory implements ViewFactory {

            public View create(Element elem) {
                String kind = elem.getName();
                if (kind != null) {
                    if (kind.equals(AbstractDocument.ContentElementName)) {

                        return new LabelView(elem);
                    } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                        return new ParagraphView(elem);
                    } else if (kind.equals(AbstractDocument.SectionElementName)) {

                        return new CenteredBoxView(elem, View.Y_AXIS);
                    } else if (kind.equals(StyleConstants.ComponentElementName)) {
                        return new ComponentView(elem);
                    } else if (kind.equals(StyleConstants.IconElementName)) {

                        return new IconView(elem);
                    }
                }

                return new LabelView(elem);
            }

        }
    }

    class CenteredBoxView extends BoxView {
        public CenteredBoxView(Element elem, int axis) {

            super(elem,axis);
        }
        protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {

            super.layoutMajorAxis(targetSpan,axis,offsets,spans);
            int textBlockHeight = 0;
            int offset = 0;

            for (int i = 0; i < spans.length; i++) {

                textBlockHeight = spans[i];
            }
            offset = (targetSpan - textBlockHeight) / 2;
            for (int i = 0; i < offsets.length; i++) {
                offsets[i] += offset;
            }

        }
    }
}
