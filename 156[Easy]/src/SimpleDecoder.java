/**
 * Name:        Kwak, Victor
 * Due:         April 02, 2014
 * <p/>
 * Description: To honor our mistake this week's easy challenge is to decode a message.
 *              I have encoded a message by adding a "4" to each character's ASCII value.
 *              It will be your job to decode this message by reversing the process and making a decoder.
 */

public class SimpleDecoder {
	public static void main(String[] args) {

		String toBeDecoded = "Etvmp$Jsspw%%%%\n" +
				"[e}$xs$ks%$]sy$lezi$wspzih$xli$lmhhir$qiwweki2$Rs{$mx$mw$}syv$xyvr$xs$nsmr\n" +
				"mr$sr$xlmw$tvero2$Hs$rsx$tswx$er}xlmrk$xlex${mpp$kmzi$e{e}$xlmw$qiwweki2$Pix\n" +
				"tistpi$higshi$xli$qiwweki$sr$xlimv$s{r$erh$vieh$xlmw$qiwweki2$]sy$ger$tpe}$epsrk\n" +
				"f}$RSX$tswxmrk$ls{$}sy$higshih$xlmw$qiwweki2$Mrwxieh$tswx$}syv$wspyxmsr$xs$fi$}syv\n" +
				"jezsvmxi$Lipps${svph$tvskveq$mr$sri$perkyeki$sj$}syv$glsmgi2$\n" +
				"Qeoi$wyvi$}syv$tvskveq$we}w$&Lipps$[svph%%%&${mxl$7$%$ex$xli$irh2$Xlmw${e}\n" +
				"tistpi$fvs{wmrk$xli$gleppirki${mpp$xlmro${i$lezi$epp$pswx$syv$qmrhw2$Xlswi${ls$tswx$lipps\n" +
				"{svph$wspyxmsrw${mxlsyx$xli$xlvii$%%%${mpp$lezi$rsx$higshih$xli$qiwweki$erh$ws$}sy$ger$\n" +
				"tspmxip}$tsmrx$syx$xlimv$wspyxmsr$mw$mr$ivvsv$,xli}$evi$nywx$jspps{mrk$epsrk${mxlsyx$ors{mrk-\n" +
				"Irns}$xlmw$jyr2$Xli$xvyxl${mpp$fi$liph$f}$xlswi${ls$ger$higshi$xli$qiwweki2$>-";

		String[] split = toBeDecoded.split("\n");

		char[][] splitChar = new char[split.length][];

		for (int i = 0; i < split.length; i++) {
			splitChar[i] = split[i].toCharArray();
			for (int j = 0; j < splitChar[i].length; j++) {
				splitChar[i][j] = (char) (splitChar[i][j] - 4);
			}
			split[i] = new String(splitChar[i]);
			System.out.println(split[i]);
		}
	}
}