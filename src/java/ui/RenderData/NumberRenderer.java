package ui.RenderData;

import javax.swing.*;
import java.text.NumberFormat;

public class NumberRenderer extends FormatRenderer {

    /*
     *  Use the specified number formatter and right align the text
     */
    public NumberRenderer(NumberFormat formatter)
    {
        super(formatter);
        setHorizontalAlignment( SwingConstants.CENTER );
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



