import java.util.Scanner;

/*
    Rate        Single                  Married Filling Jointly         Married Filling         Head of Household
                                        or Qualifying Widow(er)         Separately
    10%         $0-$8350                $0-$16700                       $0-$8350                $0-$11950
    15%         $8351-$33950            $16701-$67900                   $8351–$33950            $11951–$45500
    25%         $33951–$82250           $67901–$137050                  $33951–$68525           $45501–$117450
    28%         $82251–$171550          $137051–$208850                 $68526–$104425          $117451–$190200
    33%         $171551–$372950         $208851–$372950                 $104426–$186475         $190201–$372950
    35%         $372951+                $372951+                        $186476+                $372951+
*/

public class TaxCalculator {
    private static final double[] STATUSES = { 0.1, 0.15, 0.25, 0.28, 0.33, 0.35 };
    private static final int[][] VALUES = {{ 8350, 33950, 82250, 171550, 372950 }, //SINGLE FILER
            { 16700, 67900, 137050, 208850, 372950 }, //MARRIED JOINTLY
            { 8350, 33950, 68525, 104425, 186475 }, //MARRIED SEPARATELY
            { 11950, 45500, 117450, 190200, 372950 }}; //HEAD OF HOUSEHOLD

    private static double computeTax(int status, double income) {
        double tax = 0;
        double prevCap = 0;
        int[] bracket = VALUES[status];

        for(int i = 0; i < bracket.length; i++) {
            double cap = bracket[i];
            if(income <= cap) {
                tax += (income - prevCap) * STATUSES[i];
                return tax;
            } else {
                //Over 10% but under 35%
                tax += (cap - prevCap) * STATUSES[i];
                prevCap = cap;
            }
        }
        //Over 35%
        tax += (income - prevCap) * STATUSES[STATUSES.length - 1];
        return tax;
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("(0-single filer, 1-married jointly or qualifying widow(er),");
            System.out.println("2-married separately, 3-head of household)");
            System.out.print("Enter the filing status: ");
            if(!input.hasNextInt()) {
                System.out.println("Please enter a number (0 to 3) for your filing status");
                return;
            }
            int status = input.nextInt();
            System.out.print("Enter the taxable income: ");
            if(!input.hasNextDouble()) {
                System.out.println("Please enter a number for your income");
                return;
            }
            double income = input.nextDouble();
            double tax;
            switch (status) {
                case 0:
                    tax = computeTax(0, income);
                    break;
                case 1:
                    tax = computeTax(1, income);
                    break;
                case 2:
                    tax = computeTax(2, income);
                    break;
                case 3:
                    tax = computeTax(3, income);
                    break;
                default:
                    System.out.println("Please enter a number from 0 to 3");
                    return;
            }
            System.out.println("Tax is: " + tax);
        }
    }
}
