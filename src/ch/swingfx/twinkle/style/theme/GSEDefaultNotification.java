/*
 * This library is dual-licensed: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation. For the terms of this
 * license, see licenses/gpl_v3.txt or <http://www.gnu.org/licenses/>.
 *
 * You are free to use this library under the terms of the GNU General
 * Public License, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * Alternatively, you can license this library under a commercial
 * license, as set out in licenses/commercial.txt.
 */

package ch.swingfx.twinkle.style.theme;

import ch.swingfx.color.ColorUtil;
import ch.swingfx.twinkle.style.AbstractNotificationStyle;
import ch.swingfx.twinkle.style.background.ColorBackground;
import ch.swingfx.twinkle.style.closebutton.RoundCloseButton;
import ch.swingfx.twinkle.style.overlay.BorderOverlay;
import ch.swingfx.twinkle.style.overlay.GradientOverlay;
import ch.swingfx.twinkle.style.overlay.OverlayPaintMode;
import ch.swingfx.twinkle.window.NotificationWindowTypes;

import java.awt.*;

/**
 * Dark theme for the default window
 * @author Heinrich Spreiter
 * @see NotificationWindowTypes
 */
public class GSEDefaultNotification extends AbstractNotificationStyle {
	public static String titleFont="Î¢ÈíÑÅºÚ";
	public static String titleFontSize="20";
	public static String titleFontColor="ffcc33";
	public static String messageFont="Î¢ÈíÑÅºÚ";
	public static String messageFontSize="26";
	public static String messageFontColor="ffffff";
	public static String backgroundColor="101010";
	public static String backgroundAlpha="0.85";
	public static String width="140";
	public static String cornerRadius="8";
	
	public GSEDefaultNotification() {
		super();
		withNotificationWindowCreator(NotificationWindowTypes.DEFAULT);
		withTitleFont(new Font(titleFont, Font.BOLD, Integer.parseInt(titleFontSize)));
		withTitleFontColor(new Color(Integer.parseInt(titleFontColor,16)));
		withMessageFont(new Font(messageFont, Font.BOLD, Integer.parseInt(messageFontSize)));
		withMessageFontColor(new Color(Integer.parseInt(messageFontColor,16)));
		withAlpha(Float.parseFloat(backgroundAlpha));
		withWidth(Integer.parseInt(width));
		withBackground(new ColorBackground(new Color(Integer.parseInt(backgroundColor,16))));
		withWindowCornerRadius(Integer.parseInt(cornerRadius));
		withOverlay(new BorderOverlay(1, Color.WHITE, OverlayPaintMode.MOUSE_OVER,
					new GradientOverlay(ColorUtil.withAlpha(Color.WHITE, 0f), ColorUtil.withAlpha(Color.WHITE, 0.1f), OverlayPaintMode.MOUSE_OVER)));
		withCloseButton(new RoundCloseButton(ColorUtil.withAlpha(Color.BLACK, 0.6f), Color.WHITE).withPosition(9, 9));
	}
}
