package ui.RenderData;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class NumberRenderer extends FormatRenderer {

    /**
     * Used for Number rendering in JTable
     * @author Taken in its entirety from http://www.camick.com/java/source/NumberRenderer.java
     */
    public NumberRenderer(NumberFormat formatter)
    {
        super(formatter);
        setHorizontalAlignment( SwingConstants.CENTER );
        setForeground(new Color(110, 173, 76));
    }

    /*
     *  Use the default currency formatter for the default locale
     */
    public static NumberRenderer getCurrencyRenderer()
    {
        return new NumberRenderer( NumberFormat.getCurrencyInstance() );
    }

    /*
     *  Use the default integer formatter for the default locale
     */
    public static NumberRenderer getIntegerRenderer()
    {
        return new NumberRenderer( NumberFormat.getIntegerInstance() );
    }

    /*
     *  Use the default percent formatter for the default locale
     */
    public static NumberRenderer getPercentRenderer()
    {
        return new NumberRenderer( NumberFormat.getPercentInstance() );
    }
}



