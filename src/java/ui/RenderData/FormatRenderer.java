package ui.RenderData;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;
import java.text.Format;

/**
 * Used for format rendering in JTable
 * Taken in its entirety from http://www.camick.com/java/source/FormatRenderer.java
 */
public class FormatRenderer extends DefaultTableCellRenderer
{
    private Format formatter;

    /*
     *   Use the specified formatter to format the Object
     */
    public FormatRenderer(Format formatter)
    {
        this.formatter = formatter;
    }

    public void setValue(Object value)
    {
        //  Format the Object before setting its value in the renderer

        try
        {
            if (value != null)
                value = formatter.format(value);
        }
        catch(IllegalArgumentException e) {}

        super.setValue(value);
    }

    /*
     *  Use the default date/time formatter for the default locale
     */
    public static FormatRenderer getDateTimeRenderer()
    {
        return new FormatRenderer( DateFormat.getDateTimeInstance() );
    }

    /*
     *  Use the default time formatter for the default locale
     */
    public static FormatRenderer getTimeRenderer()
    {
        return new FormatRenderer( DateFormat.getTimeInstance() );
    }
}