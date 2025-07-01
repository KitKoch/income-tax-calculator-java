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

public final class TaxCalculator {
    private TaxCalculator(){}

    private static final double[] TAX_RATES = { 0.1, 0.12, 0.22, 0.24, 0.32, 0.35, 0.37 };

    private static final int[][] TAXABLE_INCOME = {
            { 11_600, 47_150, 100_525, 191_950, 243_725, 609_350 }, //SINGLE FILER
            { 23_200, 94_300, 201_050, 383_900, 487_450, 731_200 }, //MARRIED JOINTLY
            { 11_600, 47_150, 100_525, 191_950, 243_725, 365_600 }, //MARRIED SEPARATELY
            { 16_550, 63_100, 100_500, 191_950, 243_700, 609_350 }, //HEAD OF HOUSEHOLD
    };

    private enum FilingStatus {
        SINGLE, JOINT_MARRIED, SEPARATE_MARRIED, HEAD_OF_HOUSEHOLD;
    }


    private static double computeTax(FilingStatus status, double income) {
        double tax = 0;
        double prevCap = 0;
        int[] bracket = TAXABLE_INCOME[status.ordinal()];

        for(int i = 0; i < bracket.length; i++) {
            double cap = bracket[i];
            if(income <= cap) {
                tax += (income - prevCap) * TAX_RATES[i];
                return tax;
            } else {
                //Over 10% but under 37%
                tax += (cap - prevCap) * TAX_RATES[i];
                prevCap = cap;
            }
        }
        //Over 37%
        tax += (income - prevCap) * TAX_RATES[TAX_RATES.length - 1];
        return tax;
    }

    private static double userDoublePostive(Scanner input, String msg) {
        while(true) {
            System.out.print(msg);
            if(input.hasNextDouble()) {
                double income = input.nextDouble();
                if(income >= 0) {
                    return income;
                }
                System.out.println("Amount must be positive");
            } else {
                System.out.println("Please enter a number for income");
                input.next();
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {

            System.out.println("(0-single filer, 1-married jointly or qualifying widow(er),");
            System.out.println("2-married separately, 3-head of household)");
            System.out.print("Enter the filing status: ");
            int maxStatus = FilingStatus.values().length - 1;
            if(!input.hasNextInt()) {
                System.out.println("Please enter a number (0 to " + maxStatus + ") for your filing status");
                return;
            }
            int statusNum = input.nextInt();
            if(statusNum < 0 || statusNum >= FilingStatus.values().length) {
                System.out.println("Please enter a number (0 to " + maxStatus + ") for your filing status");
                return;
            }

            double income = userDoublePostive(input, "Enter the taxable income: ");
            double tax = computeTax(FilingStatus.values()[statusNum], income);

            System.out.printf("Tax is: $%.2f%n", tax);
        }
    }
}
