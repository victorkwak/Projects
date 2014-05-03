import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Fibonacci number generator. GUI takes n input and outputs nth Fibonacci number. Uses closed form Binet's formula
 * using a phi approximation.
 */
public class Fibonacci implements ActionListener{

    private static String number;
    private static JTextArea numArea = new JTextArea();
    private static JTextField numInput = new JTextField(10);

    Fibonacci() {
        JFrame frame = new JFrame("Fibonacci");
        frame.setLayout(new FlowLayout());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton calculate = new JButton("Calculate");
        calculate.addActionListener(this);
        JButton clear = new JButton("Clear");
        clear.addActionListener(this);


        JLabel n = new JLabel("n:");

        JPanel results = new JPanel();
        results.setSize(400, 150);
        numArea.setLineWrap(true);
        numArea.setColumns(30);
        numArea.setRows(10);
        numArea.setEditable(false);
	    numArea.setWrapStyleWord(true);
        JScrollPane sPane = new JScrollPane(numArea);
        results.add(sPane);
        frame.add(results);

        JPanel other = new JPanel();

        other.add(n);
        other.add(numInput);
        other.add(calculate);
        other.add(clear);

        frame.add(other);

        JDialog dialog = new JDialog((Frame) null, "Results", false);
        dialog.setSize(200, 200);
        dialog.setResizable(true);
        dialog.setVisible(true);

        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Calculate")) {
            fib(numInput.getText());
            numArea.setText(number);

        }else {
            numArea.setText("");
            numInput.setText("");
        }
    }

    public void dialogAction(ActionEvent event) {
        if (event.getActionCommand().equals("Ok")) {
            //something
        }
        else if (event.getActionCommand().equals("Cancel")) {
            //something
        }
    }

    public static void fib(String in) {
        BigDecimal phi;

        if (in.equals("0") || in.equals("1")) {
            number = in;
        } else { //Binet's Formula
            if (Integer.parseInt(in) <= 200) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576");
            }else if (Integer.parseInt(in) <= 400) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374");
            }else if (Integer.parseInt(in) < 600) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766");
            }else if (Integer.parseInt(in) < 900) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788");
            }else if (Integer.parseInt(in) < 1100) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963");
            }else if (Integer.parseInt(in) < 1500) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963" +
                        "13614438149758701220340805887954454749246185695364" +
                        "86444924104432077134494704956584678850987433944221");

            }else {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963" +
                        "13614438149758701220340805887954454749246185695364" +
                        "86444924104432077134494704956584678850987433944221" +
                        "25448770664780915884607499887124007652170575179788" +
                        "34166256249407589069704000281210427621771117778053" +
                        "15317141011704666599146697987317613560067087480710" +
                        "131795236894275219484353056783002287856997829");
//                    "778347845878228911097625003026961561700250464338243776486102838312683303724292" +
//                    "675263116533924731671112115881863851331620384005222165791286675294654906811317" +
//                    "159934323597349498509040947621322298101726107059611645629909816290555208524790" +
//                    "352406020172799747175342777592778625619432082750513121815628551222480939471234" +
//                    "145170223735805772786160086883829523045926478780178899219902707769038953219681" +
//                    "986151437803149974110692608867429622675756052317277752035361393621076738937645" +
//                    "560606059216589466759551900400555908950229530942312482355212212415444006470340" +
//                    "565734797663972394949946584578873039623090375033993856210242369025138680414577" +
//                    "995698122445747178034173126453220416397232134044449487302315417676893752103068" +
//                    "737880344170093954409627955898678723209512426893557309704509595684401755519881" +
//                    "921802064052905518934947592600734852282101088194644544222318891319294689622002" +
//                    "301443770269923007803085261180754519288770502109684249362713592518760777884665" +
//                    "836150238913493333122310533923213624319263728910670503399282265263556209029798" +
//                    "642472759772565508615487543574826471814145127000602389016207773224499435308899" +
//                    "909501680328112194320481964387675863314798571911397815397807476150772211750826" +
//                    "945863932045652098969855567814106968372884058746103378105444390943683583581381" +
//                    "131168993855576975484149144534150912954070050194775486163075422641729394680367" +
//                    "319805861833918328599130396072014455950449779212076124785645916160837059498786");
            }
            BigDecimal psi = BigDecimal.ONE.subtract(phi);
            BigDecimal one = BigDecimal.ONE.add(BigDecimal.valueOf(Math.sqrt(5)));
            BigDecimal two = BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.sqrt(5)));
            BigDecimal three = BigDecimal.valueOf(2).pow(Integer.parseInt(in)).multiply(BigDecimal.valueOf(Math.sqrt(5)));
            BigDecimal value = (phi.pow(Integer.parseInt(in)).subtract(psi.pow(Integer.parseInt(in)))).divide(phi.subtract(psi));

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setGroupingUsed(false);
            number = decimalFormat.format(value);

        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static void main(String[] args) {

	    try {
		    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			    if ("Nimbus".equals(info.getName())) {
				    UIManager.setLookAndFeel(info.getClassName());
				    break;
			    }
		    }
	    } catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
	    }

        SwingUtilities.invokeLater(Fibonacci::new);
//
    }
}