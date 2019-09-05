package root.Symbol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Key log parser i.e. logs generated through key logger ( i.e. Symbol.java)
 */
public class Parser {

	static final Map<Long, String[]> VIRTUAL_KEY_CODE_TO_TEXT_MAP = new HashMap<>();
	static
	{

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(0), new String[] { "VK_UNDEFINED" }); // VK_UNDEFINED

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(1), new String[] { "VK_LBUTTON" }); // VK_LBUTTON
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(2), new String[] { "VK_RBUTTON" }); // VK_RBUTTON
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(3), new String[] { "VK_CANCEL" }); // VK_CANCEL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(4), new String[] { "VK_MBUTTON" }); // VK_MBUTTON
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(5), new String[] { "VK_XBUTTON1" }); // VK_XBUTTON1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(6), new String[] { "VK_XBUTTON2" }); // VK_XBUTTON2

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(8), new String[] { "VK_BACK" }); // VK_BACK
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(9), new String[] { "VK_TAB" }); // VK_TAB

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(12), new String[] { "VK_CLEAR" }); // VK_CLEAR

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(13), new String[] { "VK_RETURN" }); // VK_RETURN

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(16), new String[] { "VK_SHIFT" }); // VK_SHIFT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(17), new String[] { "VK_CONTROL" }); // VK_CONTROL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(18), new String[] { "VK_MENU" }); // VK_MENU
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(19), new String[] { "VK_PAUSE" }); // VK_PAUSE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(20), new String[] { "VK_CAPSLOCK" }); // VK_CAPITAL CapsLock key

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(21), new String[] { "VK_KANA" }); // VK_KANA
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(21), new String[] { "VK_HANGUEL" }); // VK_HANGUEL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(21), new String[] { "VK_HANGUL" }); // VK_HANGUL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(23), new String[] { "VK_JUNJA" }); // VK_JUNJA
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(24), new String[] { "VK_FINAL" }); // VK_FINAL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(25), new String[] { "VK_HANJA" }); // VK_HANJA
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(25), new String[] { "VK_KANJI" }); // VK_KANJI

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(27), new String[] { "VK_ESCAPE" }); // VK_ESCAPE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(28), new String[] { "VK_CONVERT" }); // VK_CONVERT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(29), new String[] { "VK_NONCONVERT" }); // VK_NONCONVERT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(30), new String[] { "VK_ACCEPT" }); // VK_ACCEPT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(31), new String[] { "VK_MODECHANGE" }); // VK_MODECHANGE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(32), new String[] { "VK_SPACE" }); // VK_SPACE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(33), new String[] { "VK_PRIOR" }); // VK_PRIOR VK_PAGEUP
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(34), new String[] { "VK_NEXT" }); // VK_NEXT VK_PAGEDOWN
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(35), new String[] { "VK_END" }); // VK_END
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(36), new String[] { "VK_HOME" }); // VK_HOME

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(37), new String[] { "VK_LEFT" }); // VK_LEFT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(38), new String[] { "VK_UP" }); // VK_UP
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(39), new String[] { "VK_RIGHT" }); // VK_RIGHT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(40), new String[] { "VK_DOWN" }); // VK_DOWN

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(41), new String[] { "VK_SELECT" }); // VK_SELECT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(42), new String[] { "VK_PRINT" }); // VK_PRINT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(43), new String[] { "VK_EXECUTE" }); // VK_EXECUTE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(44), new String[] { "VK_SNAPSHOT" }); // VK_SNAPSHOT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(45), new String[] { "VK_INSERT" }); // VK_INSERT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(46), new String[] { "VK_DELETE" }); // VK_DELETE

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(47), new String[] { "VK_HELP" }); // VK_HELP

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(48), new String[] { "VK_0", "0", ")" }); // VK_0
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(49), new String[] { "VK_1", "1", "!" }); // VK_1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(50), new String[] { "VK_2", "2", "@" }); // VK_2
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(51), new String[] { "VK_3", "3", "#" }); // VK_3
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(52), new String[] { "VK_4", "4", "$" }); // VK_4
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(53), new String[] { "VK_5", "5", "%" }); // VK_5
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(54), new String[] { "VK_6", "6", "^" }); // VK_6
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(55), new String[] { "VK_7", "7", "&" }); // VK_7
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(56), new String[] { "VK_8", "8", "*" }); // VK_8
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(57), new String[] { "VK_9", "9", "(" }); // VK_9

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(65), new String[] { "VK_A", "A", "a" }); // VK_A
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(66), new String[] { "VK_B", "B", "b" }); // VK_B
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(67), new String[] { "VK_C", "C", "c" }); // VK_C
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(68), new String[] { "VK_D", "D", "d" }); // VK_D
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(69), new String[] { "VK_E", "E", "e" }); // VK_E
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(70), new String[] { "VK_F", "F", "f" }); // VK_F
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(71), new String[] { "VK_G", "G", "g" }); // VK_G
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(72), new String[] { "VK_H", "H", "h" }); // VK_H
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(73), new String[] { "VK_I", "I", "i" }); // VK_I
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(74), new String[] { "VK_J", "J", "j" }); // VK_J
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(75), new String[] { "VK_K", "K", "k" }); // VK_K
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(76), new String[] { "VK_L", "L", "l" }); // VK_L
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(77), new String[] { "VK_M", "M", "m" }); // VK_M
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(78), new String[] { "VK_N", "N", "n" }); // VK_N
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(79), new String[] { "VK_O", "O", "o" }); // VK_O
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(80), new String[] { "VK_P", "P", "p" }); // VK_P
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(81), new String[] { "VK_Q", "Q", "q" }); // VK_Q
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(82), new String[] { "VK_R", "R", "r" }); // VK_R
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(83), new String[] { "VK_S", "S", "s" }); // VK_S
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(84), new String[] { "VK_T", "T", "t" }); // VK_T
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(85), new String[] { "VK_U", "U", "u" }); // VK_U
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(86), new String[] { "VK_V", "V", "v" }); // VK_V
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(87), new String[] { "VK_W", "W", "w" }); // VK_W
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(88), new String[] { "VK_X", "X", "x" }); // VK_X
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(89), new String[] { "VK_Y", "Y", "y" }); // VK_Y
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(90), new String[] { "VK_Z", "Z", "z" }); // VK_Z

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(91), new String[] { "VK_LWIN" }); // VK_LWIN
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(92), new String[] { "VK_RWIN" }); // VK_RWIN

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(93), new String[] { "VK_APPS" }); // VK_APPS
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(95), new String[] { "VK_SLEEP" }); // VK_SLEEP

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(96), new String[] { "VK_NUMPAD0", "0" }); // VK_NUMPAD0
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(97), new String[] { "VK_NUMPAD1", "1" }); // VK_NUMPAD1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(98), new String[] { "VK_NUMPAD2", "2" }); // VK_NUMPAD2
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(99), new String[] { "VK_NUMPAD3", "3" }); // VK_NUMPAD3
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(100), new String[] { "VK_NUMPAD4", "4" }); // VK_NUMPAD4
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(101), new String[] { "VK_NUMPAD5", "5" }); // VK_NUMPAD5
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(102), new String[] { "VK_NUMPAD6", "6" }); // VK_NUMPAD6
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(103), new String[] { "VK_NUMPAD7", "7" }); // VK_NUMPAD7
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(104), new String[] { "VK_NUMPAD8", "8" }); // VK_NUMPAD8
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(105), new String[] { "VK_NUMPAD9", "9" }); // VK_NUMPAD9

		// @formatter:off 
		// Special symbol keys
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(106), new String[] { "VK_MULTIPLY", 	"*" }); // VK_MULTIPLY
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(107), new String[] { "VK_ADD", 		"+" }); // VK_ADD
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(108), new String[] { "VK_SEPARATOR", 	"|" }); // VK_SEPARATOR
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(109), new String[] { "VK_SUBTRACT", 	"-" }); // VK_SUBTRACT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(110), new String[] { "VK_DECIMAL", 	"." }); // VK_DECIMAL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(111), new String[] { "VK_DIVIDE", 	"/" }); // VK_DIVIDE
		// @formatter:on

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(112), new String[] { "VK_F1" }); // VK_F1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(113), new String[] { "VK_F2" }); // VK_F2
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(114), new String[] { "VK_F3" }); // VK_F3
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(115), new String[] { "VK_F4" }); // VK_F4
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(116), new String[] { "VK_F5" }); // VK_F5
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(117), new String[] { "VK_F6" }); // VK_F6
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(118), new String[] { "VK_F7" }); // VK_F7
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(119), new String[] { "VK_F8" }); // VK_F8
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(120), new String[] { "VK_F9" }); // VK_F9
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(121), new String[] { "VK_F10" }); // VK_F10
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(122), new String[] { "VK_F11" }); // VK_F11
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(123), new String[] { "VK_F12" }); // VK_F12
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(124), new String[] { "VK_F13" }); // VK_F13
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(125), new String[] { "VK_F14" }); // VK_F14
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(126), new String[] { "VK_F15" }); // VK_F15
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(127), new String[] { "VK_F16" }); // VK_F16
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(128), new String[] { "VK_F17" }); // VK_F17
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(129), new String[] { "VK_F18" }); // VK_F18
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(130), new String[] { "VK_F19" }); // VK_F19
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(131), new String[] { "VK_F20" }); // VK_F20
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(132), new String[] { "VK_F21" }); // VK_F21
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(133), new String[] { "VK_F22" }); // VK_F22
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(134), new String[] { "VK_F23" }); // VK_F23
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(135), new String[] { "VK_F24" }); // VK_F24

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(144), new String[] { "VK_NUMLOCK" }); // VK_NUMLOCK
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(145), new String[] { "VK_SCROLL" }); // VK_SCROLL

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(160), new String[] { "VK_LSHIFT" }); // VK_LSHIFT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(161), new String[] { "VK_RSHIFT" }); // VK_RSHIFT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(162), new String[] { "VK_LCONTROL" }); // VK_LCONTROL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(163), new String[] { "VK_RCONTROL" }); // VK_RCONTROL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(164), new String[] { "VK_LMENU" }); // VK_LMENU Alt key
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(165), new String[] { "VK_RMENU" }); // VK_RMENU Alt key

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(166), new String[] { "VK_BROWSER_BACK" }); // VK_BROWSER_BACK
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(167), new String[] { "VK_BROWSER_FORWARD" }); // VK_BROWSER_FORWARD
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(168), new String[] { "VK_BROWSER_REFRESH" }); // VK_BROWSER_REFRESH
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(169), new String[] { "VK_BROWSER_STOP" }); // VK_BROWSER_STOP
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(170), new String[] { "VK_BROWSER_SEARCH" }); // VK_BROWSER_SEARCH
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(171), new String[] { "VK_BROWSER_FAVORITES" }); // VK_BROWSER_FAVORITES
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(172), new String[] { "VK_BROWSER_HOME" }); // VK_BROWSER_HOME
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(173), new String[] { "VK_VOLUME_MUTE" }); // VK_VOLUME_MUTE
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(174), new String[] { "VK_VOLUME_DOWN" }); // VK_VOLUME_DOWN
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(175), new String[] { "VK_VOLUME_UP" }); // VK_VOLUME_UP
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(176), new String[] { "VK_MEDIA_NEXT_TRACK" }); // VK_MEDIA_NEXT_TRACK
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(177), new String[] { "VK_MEDIA_PREV_TRACK" }); // VK_MEDIA_PREV_TRACK
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(178), new String[] { "VK_MEDIA_STOP" }); // VK_MEDIA_STOP
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(179), new String[] { "VK_MEDIA_PLAY_PAUSE" }); // VK_MEDIA_PLAY_PAUSE
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(180), new String[] { "VK_LAUNCH_MAIL" }); // VK_LAUNCH_MAIL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(181), new String[] { "VK_LAUNCH_MEDIA_SELECT" }); // VK_LAUNCH_MEDIA_SELECT
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(182), new String[] { "VK_LAUNCH_APP1" }); // VK_LAUNCH_APP1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(183), new String[] { "VK_LAUNCH_APP2" }); // VK_LAUNCH_APP2

		// Special symbol keys
		// @formatter:off 
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(186), new String[] { "VK_OEM_1", 		";", ":" }); // VK_OEM_1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(187), new String[] { "VK_OEM_PLUS", 	"=", "+" }); // VK_OEM_PLUS
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(188), new String[] { "VK_OEM_COMMA", 	",", "<" }); // VK_OEM_COMMA
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(189), new String[] { "VK_OEM_MINUS", 	"-", "_" }); // VK_OEM_MINUS
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(190), new String[] { "VK_OEM_PERIOD", ".", ">" }); // VK_OEM_PERIOD
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(191), new String[] { "VK_OEM_2", 		"/", "?" }); // VK_OEM_2
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(192), new String[] { "VK_OEM_3", 		"`", "~" }); // VK_OEM_3
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(219), new String[] { "VK_OEM_4", 		"[", "{" }); // VK_OEM_4
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(220), new String[] { "VK_OEM_5", 		"\\", "|" }); // VK_OEM_5
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(221), new String[] { "VK_OEM_6", 		"]", "}" }); // VK_OEM_6
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(222), new String[] { "VK_OEM_7", 		"'", "\"" }); // VK_OEM_7
		// @formatter:on

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(223), new String[] { "VK_OEM_8" }); // VK_OEM_8
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(226), new String[] { "VK_OEM_102" }); // VK_OEM_102
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(229), new String[] { "VK_PROCESSKEY" }); // VK_PROCESSKEY
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(231), new String[] { "VK_PACKET" }); // VK_PACKET
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(246), new String[] { "VK_ATTN" }); // VK_ATTN
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(247), new String[] { "VK_CRSEL" }); // VK_CRSEL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(248), new String[] { "VK_EXSEL" }); // VK_EXSEL
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(249), new String[] { "VK_EREOF" }); // VK_EREOF
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(250), new String[] { "VK_PLAY" }); // VK_PLAY
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(251), new String[] { "VK_ZOOM" }); // VK_ZOOM
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(252), new String[] { "VK_NONAME" }); // VK_NONAME
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(253), new String[] { "VK_PA1" }); // VK_PA1
		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(254), new String[] { "VK_OEM_CLEAR" }); // VK_OEM_CLEAR

		VIRTUAL_KEY_CODE_TO_TEXT_MAP.put(new Long(255), new String[] { "VK_FUNCTION" }); // VK_FUNCTION
	}

	/**
	 * Input here output of Symbol.java so as to decode key logs.
	 */
	public static void main(String[] args) throws IOException {
		boolean caps = false;
		boolean num = false;
		boolean shiftDown = false;

		try (Scanner in = new Scanner(System.in))
		{
			String line;
			while (in.hasNextLine())
			{
				line = in.nextLine();
				if (line.contains("CAPS") && line.contains("true"))
					caps = true;
				else
					caps = false;
				if (line.contains("NUM") && line.contains("true"))
					num = true;
				else
					num = false;
				if (!line.contains("CAP") && !line.contains("NUM") && !line.contains("Sym"))
					System.out.println(parseLine(line, caps, num, shiftDown));
			}
		}
	}

	static String parseLine(String line, boolean capsLockStatus, boolean numLockStatus, boolean isShiftDown)
			throws IOException {

		String current = "";
		String result = "";
		String charsToBeAdded = "";
		boolean holdKeyPressed = false;
		for (int i = 0; i < line.length(); i++)
		{
			if ('<' == line.charAt(i))
			{
				i++;
				if (line.charAt(i) == '/')
				{
					i++;
					current = line.substring(i, line.indexOf('>', i));
					if ("160".equals(current) || "161".equals(current))
						isShiftDown = false;

					i = line.indexOf('>', i);
					continue;
				}

				current = line.substring(i, line.indexOf('>', i));

				if (line.charAt(i) == 'H')
				{
					int temp = line.indexOf("\"", i);
					i = line.indexOf('>', i);
					String valueStr = line.substring(temp + 1, i - 2);
					result += valueStr + "'>";

					if (!charsToBeAdded.isEmpty())
					{
						result += charsToBeAdded;
						charsToBeAdded = "";
					}
					holdKeyPressed = false;
					continue;
				}

				if (current.contains("20") && current.contains("value"))
				{
					capsLockStatus = !capsLockStatus;
				} else if ("160".equals(current) || "161".equals(current))
				{
					isShiftDown = true;
				} else if ("144".equals(current))
				{
					numLockStatus = !numLockStatus;
				} else
				{
					long key = 0;// Long.parseLong(current);

					try
					{
						key = Long.parseLong(current);
					} catch (NumberFormatException e)
					{
						System.out.println(">>current>>" + current);
						throw e;
					}

					if (holdKeyPressed)
					{
						if (isAlphabetKey(key))
							charsToBeAdded += getAlphabet(
									(capsLockStatus && !isShiftDown) || (!capsLockStatus && isShiftDown), key);
						else if (isNumericKey(key, isShiftDown))
							charsToBeAdded += getDualKeyVal(isShiftDown, key);
						else if (isNumPadKey(key, numLockStatus))
							charsToBeAdded += getSingleKeyVal(key);
						else if (isMathSymbol(key))
							charsToBeAdded += getSingleKeyVal(key);
						else if (isSpecialSymbolKey(key, isShiftDown))
							charsToBeAdded += getDualKeyVal(isShiftDown, key);
					} else
					{
						if (isAlphabetKey(key))
							result += getAlphabet((capsLockStatus && !isShiftDown) || (!capsLockStatus && isShiftDown),
									key);
						else if (isNumericKey(key, isShiftDown))
							result += getDualKeyVal(isShiftDown, key);
						else if (isNumPadKey(key, numLockStatus))
							result += getSingleKeyVal(key);
						else if (isMathSymbol(key))
							result += getSingleKeyVal(key);
						else if (isSpecialSymbolKey(key, isShiftDown))
							result += getDualKeyVal(isShiftDown, key);
					}

					int temp;
					String str;
					String valueStr;

					switch (current)
					{
					case "32":
						result += " ";
						break;// Space
					case "9":
						result += "\t";
						break;// Tab
					case "35":
						result += "<END>";
						break;
					case "36":
						result += "<HOME>";
						break;
					case "162":
						result += "<CTRL>";
						break;
					case "163":
						result += "<R CTRL>";
						break;
					case "8":
						holdKeyPressed = true;
						result += "<BACKSPACE Value='";
						break;
					case "33":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<PAGE UP Value='" + valueStr + "'>";
						break;
					case "34":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<PAGE DOWN Value='" + valueStr + "'>";
						break;
					case "37":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<LEFT Value='" + valueStr + "'>";
						break;
					case "38":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<UP Value='" + valueStr + "'>";
						break;
					case "39":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<RIGHT Value='" + valueStr + "'>";
						break;
					case "40":
						temp = i;
						i = line.indexOf('/', i) + 1;
						str = line.substring(temp, i);
						if (str.indexOf("\"") == -1)
							valueStr = "";
						else
							valueStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf('"'));
						result += "<DOWN Value='" + valueStr + "'>";
						break;
					case "46":
						result += "<DELETE Value='";
						holdKeyPressed = true;
						break;
					}

					if ("13".equals(current))
					{
						result += System.lineSeparator();
					}
				}
			}
		}

		return result;
	}

	static boolean isAlphabetKey(final long VK_CODE) {
		return (VK_CODE >= 65 && VK_CODE <= 90);
	}

	static boolean isNumericKey(final long VK_CODE, boolean isShiftDown) {
		return (!isShiftDown && VK_CODE >= 48 && VK_CODE <= 57);
	}

	static boolean isNumPadKey(final long VK_CODE, boolean NUM_LOCK_STATE) {
		return NUM_LOCK_STATE && VK_CODE >= 96 && VK_CODE <= 105;
	}

	static boolean isSpecialSymbolKey(final long VK_CODE, boolean isShiftDown) {
		return (VK_CODE >= 186 && VK_CODE <= 222) || (isShiftDown && VK_CODE >= 48 && VK_CODE <= 57);
	}

	static boolean isMathSymbol(final long VK_CODE) {
		return (VK_CODE >= 106 && VK_CODE <= 111);
	}

	static String getAlphabet(boolean upper, long key) {
		return upper ? VIRTUAL_KEY_CODE_TO_TEXT_MAP.get(key)[1] : VIRTUAL_KEY_CODE_TO_TEXT_MAP.get(key)[2];
	}

	static String getDualKeyVal(boolean isShiftDown, long key) {
		return isShiftDown ? VIRTUAL_KEY_CODE_TO_TEXT_MAP.get(key)[2] : VIRTUAL_KEY_CODE_TO_TEXT_MAP.get(key)[1];
	}

	static String getSingleKeyVal(long key) {
		return VIRTUAL_KEY_CODE_TO_TEXT_MAP.get(key)[1];
	}

}
