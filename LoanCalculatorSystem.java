import java.util.Scanner;

/**
 * ============================================================
 *   LOAN ELIGIBILITY AND EMI CALCULATOR SYSTEM
 *   A complete console-based Java application
 * ============================================================
 */

public class LoanCalculatorSystem {

    static Scanner sc = new Scanner(System.in);

    // ─────────────────────────────────────────────
    //  CONSTANTS
    // ─────────────────────────────────────────────
    static final double MIN_CREDIT_SCORE    = 650;
    static final double MIN_INCOME          = 15000;   // monthly ₹
    static final double MAX_DEBT_RATIO      = 0.50;    // 50 % of income
    static final double MIN_AGE             = 21;
    static final double MAX_AGE             = 60;

    // ─────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> checkEligibility();
                case 2 -> calculateEMI();
                case 3 -> fullLoanAssistant();
                case 4 -> compareLoanPlans();
                case 5 -> User.readFromFile();
                case 6 -> { 
                    System.out.println("\n  Thank you for using Loan Calculator System. Goodbye!\n"); 
                    running = false; 
                }
                default -> System.out.println("  ❌  Invalid choice. Please try again.\n");
            }
        }
        sc.close();
    }

    // ─────────────────────────────────────────────
    //  UI HELPERS
    // ─────────────────────────────────────────────
    static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║      💰  LOAN ELIGIBILITY & EMI CALCULATOR           ║");
        System.out.println("  ║              Smart Banking Assistant                 ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    static void printMenu() {
        System.out.println("  ┌─────────────────────────────────────┐");
        System.out.println("  │            MAIN  MENU               │");
        System.out.println("  ├─────────────────────────────────────┤");
        System.out.println("  │  1. Check Loan Eligibility          │");
        System.out.println("  │  2. Calculate EMI                   │");
        System.out.println("  │  3. Full Loan Assistant (All-in-One)│");
        System.out.println("  │  4. Compare Loan Plans              │");
        System.out.println("  │  5. View Saved Users                │");
        System.out.println("  │  6. Exit                            │");
        System.out.println("  └─────────────────────────────────────┘");
    }

    static void divider() {
        System.out.println("  ─────────────────────────────────────────────────────");
    }

    // ─────────────────────────────────────────────
    //  MODULE 1 – LOAN ELIGIBILITY CHECKER
    // ─────────────────────────────────────────────
    static void checkEligibility() {
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │       LOAN ELIGIBILITY CHECKER      │");
        System.out.println("  └─────────────────────────────────────┘");

        // Collect applicant info
        String name      = readString("  Enter your name              : ");
        int    age       = readInt   ("  Enter your age               : ");
        double income    = readDouble("  Monthly income (₹)           : ");
        double expenses  = readDouble("  Monthly expenses/EMIs (₹)    : ");
        int    credit    = readInt   ("  Credit score (300–900)        : ");
        String empType   = readEmpType();
        int    empYears  = readInt   ("  Years of employment           : ");
        double loanAmt   = readDouble("  Desired loan amount (₹)       : ");
        String loanType  = readLoanType();

        User user = new User(name, income, credit);
        user.display();
        user.saveToFile();

        System.out.println();
        divider();

        // ── Run eligibility rules ──
        boolean eligible = true;
        StringBuilder reasons = new StringBuilder();
        StringBuilder suggestions = new StringBuilder();

        // 1. Age check
        if (age < MIN_AGE || age > MAX_AGE) {
            eligible = false;
            reasons.append("  ✗ Age must be between 21 and 60 years.\n");
        }

        // 2. Minimum income
        if (income < MIN_INCOME) {
            eligible = false;
            reasons.append("  ✗ Monthly income below minimum threshold of ₹15,000.\n");
            suggestions.append("  → Try to increase income or apply with a co-applicant.\n");
        }

        // 3. Credit score
        if (credit < MIN_CREDIT_SCORE) {
            eligible = false;
            reasons.append("  ✗ Credit score " + credit + " is below minimum of 650.\n");
            suggestions.append("  → Pay existing dues on time to improve your credit score.\n");
        }

        // 4. Debt-to-income ratio
        double debtRatio = expenses / income;
        if (debtRatio > MAX_DEBT_RATIO) {
            eligible = false;
            reasons.append(String.format("  ✗ Debt-to-income ratio %.0f%% exceeds limit of 50%%.\n", debtRatio * 100));
            suggestions.append("  → Close existing loans before applying.\n");
        }

        // 5. Employment stability
        if (empType.equals("Unemployed")) {
            eligible = false;
            reasons.append("  ✗ Unemployed applicants are not eligible.\n");
        } else if (empYears < 1) {
            eligible = false;
            reasons.append("  ✗ Minimum 1 year of employment required.\n");
        }

        // 6. Loan amount sanity (max 60× monthly income for home, 20× for others)
        double maxLoan = loanType.equals("Home Loan") ? income * 60 * 12 : income * 20;
        if (loanAmt > maxLoan) {
            eligible = false;
            reasons.append(String.format("  ✗ Requested amount exceeds maximum eligible (₹%.0f).\n", maxLoan));
        }

        // ── Print result ──
        System.out.printf("  Applicant : %s%n", name);
        System.out.printf("  Loan Type : %s%n", loanType);
        System.out.printf("  Amount    : ₹%.2f%n", loanAmt);
        System.out.println();

        if (eligible) {
            double interestRate = getInterestRate(loanType, credit);
            System.out.println("  ✅  ELIGIBLE FOR LOAN!");
            System.out.printf("  Suggested Interest Rate : %.2f%% per annum%n", interestRate);
            System.out.printf("  Maximum Loan Eligible   : ₹%.2f%n", maxLoan);
            System.out.printf("  Debt-to-Income Ratio    : %.0f%% (Healthy ✔)%n", debtRatio * 100);
        } else {
            System.out.println("  ❌  NOT ELIGIBLE – Reasons:");
            System.out.print(reasons);
            System.out.println();
            System.out.println("  💡 Suggestions:");
            System.out.print(suggestions.length() > 0 ? suggestions : "  → Review the eligibility criteria and re-apply.\n");
        }

        divider();
        System.out.println();
    }

    // ─────────────────────────────────────────────
    //  MODULE 2 – EMI CALCULATOR
    // ─────────────────────────────────────────────
    static void calculateEMI() {
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │           EMI  CALCULATOR           │");
        System.out.println("  └─────────────────────────────────────┘");

        double principal = readDouble("  Loan Amount (₹)           : ");
        double annualRate = readDouble("  Annual Interest Rate (%)   : ");
        int    tenureYears = readInt  ("  Loan Tenure (Years)        : ");

        printEMIDetails(principal, annualRate, tenureYears);
    }

    // Core EMI calculation and display
    static void printEMIDetails(double principal, double annualRate, int tenureYears) {
        long start = System.nanoTime();

        double monthlyRate = annualRate / (12 * 100);
        int    months      = tenureYears * 12;

        // EMI formula: P * r * (1+r)^n / ((1+r)^n - 1)
        double emi;
        double totalPayment;
        double totalInterest;

        if (annualRate == 0) {
            emi = principal / months;
        } else {
            double factor = Math.pow(1 + monthlyRate, months);
            emi = principal * monthlyRate * factor / (factor - 1);
        }

        totalPayment  = emi * months;
        totalInterest = totalPayment - principal;

        System.out.println();
        divider();
        System.out.println("  📊  EMI BREAKDOWN");
        divider();
        System.out.printf("  Loan Amount          : ₹%,.2f%n",  principal);
        System.out.printf("  Interest Rate        : %.2f%% p.a.%n", annualRate);
        System.out.printf("  Tenure               : %d Years (%d months)%n", tenureYears, months);
        divider();
        System.out.printf("  Monthly EMI          : ₹%,.2f%n",  emi);
        System.out.printf("  Total Payment        : ₹%,.2f%n",  totalPayment);
        System.out.printf("  Total Interest Paid  : ₹%,.2f%n",  totalInterest);
        System.out.printf("  Interest Percentage  : %.1f%% of Principal%n", (totalInterest / principal) * 100);
        divider();

        // Amortization preview (first 3 + last 3 months)
        printAmortizationPreview(principal, monthlyRate, emi, months);
        System.out.println();

        long end = System.nanoTime();
        System.out.println("⏱ Execution Time: " + (end - start) + " ns\n");
    }

    static void printAmortizationPreview(double principal, double r, double emi, int months) {
        System.out.println("\n  📅  AMORTIZATION SCHEDULE (Preview)");
        System.out.println("  Month │   EMI (₹)  │ Principal (₹)│ Interest (₹) │ Balance (₹)");
        System.out.println("  ──────┼────────────┼──────────────┼──────────────┼──────────────");

        double balance = principal;
        for (int m = 1; m <= months; m++) {
            double interestPart   = balance * r;
            double principalPart  = emi - interestPart;
            balance -= principalPart;
            if (balance < 0) balance = 0;

            if (m <= 3 || m >= months - 2) {
                System.out.printf("  %5d │ %,10.2f │ %,12.2f │ %,12.2f │ %,12.2f%n",
                        m, emi, principalPart, interestPart, balance);
            } else if (m == 4) {
                System.out.println("   ...  │     ...    │     ...      │     ...      │     ...");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  MODULE 3 – FULL LOAN ASSISTANT
    // ─────────────────────────────────────────────
    static void fullLoanAssistant() {
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │       FULL LOAN ASSISTANT           │");
        System.out.println("  └─────────────────────────────────────┘");
        System.out.println("  We'll check eligibility AND compute your EMI.\n");

        String name     = readString("  Your name                  : ");
        int    age      = readInt   ("  Age                        : ");
        double income   = readDouble("  Monthly income (₹)         : ");
        double expenses = readDouble("  Existing monthly EMIs (₹)  : ");
        int    credit   = readInt   ("  Credit score               : ");
        String empType  = readEmpType();
        int    empYears = readInt   ("  Years of employment        : ");
        String loanType = readLoanType();
        double loanAmt  = readDouble("  Loan Amount requested (₹)  : ");
        int    tenure   = readInt   ("  Preferred Tenure (Years)   : ");

        // Check eligibility
        boolean ageOk      = (age >= MIN_AGE && age <= MAX_AGE);
        boolean incomeOk   = (income >= MIN_INCOME);
        boolean creditOk   = (credit >= MIN_CREDIT_SCORE);
        boolean debtOk     = ((expenses / income) <= MAX_DEBT_RATIO);
        boolean empOk      = (!empType.equals("Unemployed") && empYears >= 1);
        double  maxLoan    = loanType.equals("Home Loan") ? income * 60 * 12 : income * 20;
        boolean amountOk   = (loanAmt <= maxLoan);
        boolean eligible   = ageOk && incomeOk && creditOk && debtOk && empOk && amountOk;

        double rate = getInterestRate(loanType, credit);

        System.out.println();
        divider();
        System.out.printf("  👤  Applicant  : %s%n", name);
        System.out.printf("  🏦  Loan Type  : %s%n", loanType);
        System.out.printf("  📋  ELIGIBILITY CHECKLIST%n");
        System.out.printf("      Age (21–60)         : %s%n", ageOk   ? "✅ Pass" : "❌ Fail");
        System.out.printf("      Min Income ₹15K     : %s%n", incomeOk? "✅ Pass" : "❌ Fail");
        System.out.printf("      Credit Score ≥650   : %s%n", creditOk? "✅ Pass" : "❌ Fail");
        System.out.printf("      Debt Ratio ≤50%%     : %s%n", debtOk  ? "✅ Pass" : "❌ Fail");
        System.out.printf("      Employment Valid    : %s%n", empOk   ? "✅ Pass" : "❌ Fail");
        System.out.printf("      Loan within limit   : %s%n", amountOk? "✅ Pass" : "❌ Fail");
        divider();

        if (eligible) {
            System.out.println("  🎉  CONGRATULATIONS! You are ELIGIBLE.\n");
            System.out.printf("  Applied Interest Rate : %.2f%% p.a.%n", rate);
            printEMIDetails(loanAmt, rate, tenure);

            // Affordability check
            double emi = computeEMI(loanAmt, rate, tenure);
            double availableForEMI = income - expenses;
            System.out.printf("  💡  Available for new EMI : ₹%,.2f%n", availableForEMI);
            if (emi > availableForEMI) {
                System.out.println("  ⚠️  Warning: EMI exceeds available monthly surplus.");
                System.out.println("      Consider a longer tenure or lower loan amount.");
            } else {
                System.out.printf("  ✅  EMI is affordable (%.0f%% of surplus).%n",
                        (emi / availableForEMI) * 100);
            }
        } else {
            System.out.println("  ❌  Unfortunately, you are NOT eligible at this time.");
            System.out.println("  💡  Fix the failed criteria above and re-apply.");
        }

        divider();
        System.out.println();
    }

    // ─────────────────────────────────────────────
    //  MODULE 4 – COMPARE LOAN PLANS
    // ─────────────────────────────────────────────
    static void compareLoanPlans() {
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │       COMPARE LOAN PLANS            │");
        System.out.println("  └─────────────────────────────────────┘");

        double principal = readDouble("  Loan Amount (₹)  : ");

        System.out.println("\n  Comparing 3 tenure options for the SAME loan amount:\n");

        int[] tenures = {5, 10, 20};
        double[] rates = {8.5, 9.0, 9.5}; // sample rates

        System.out.println("  Tenure │   Rate  │   Monthly EMI   │  Total Payment  │ Total Interest");
        System.out.println("  ───────┼─────────┼─────────────────┼─────────────────┼────────────────");

        for (int i = 0; i < tenures.length; i++) {
            double emi   = computeEMI(principal, rates[i], tenures[i]);
            double total = emi * tenures[i] * 12;
            double intst = total - principal;
            System.out.printf("  %4d Y │ %5.1f%% │ ₹%,13.2f │ ₹%,13.2f │ ₹%,12.2f%n",
                    tenures[i], rates[i], emi, total, intst);
        }

        divider();
        System.out.println("  💡  Shorter tenure = Higher EMI but Lower Total Interest.");
        System.out.println("      Choose based on your monthly affordability.\n");
    }

    // ─────────────────────────────────────────────
    //  HELPER METHODS
    // ─────────────────────────────────────────────
    static double computeEMI(double principal, double annualRate, int tenureYears) {
        if (annualRate == 0) return principal / (tenureYears * 12);
        double r = annualRate / (12 * 100);
        int    n = tenureYears * 12;
        double f = Math.pow(1 + r, n);
        return principal * r * f / (f - 1);
    }

    static double getInterestRate(String loanType, int credit) {
        double base = switch (loanType) {
            case "Home Loan"     -> 8.5;
            case "Car Loan"      -> 9.5;
            case "Personal Loan" -> 12.0;
            case "Education Loan"-> 8.0;
            default              -> 10.0;
        };
        // Credit score discount
        if (credit >= 800) base -= 0.5;
        else if (credit >= 750) base -= 0.25;
        else if (credit < 680)  base += 1.0;
        return base;
    }

    static String readEmpType() {
        System.out.println("  Employment Type:");
        System.out.println("    1. Salaried   2. Self-Employed   3. Business   4. Unemployed");
        int c = readInt("  Choose (1-4): ");
        return switch (c) {
            case 1 -> "Salaried";
            case 2 -> "Self-Employed";
            case 3 -> "Business";
            default -> "Unemployed";
        };
    }

    static String readLoanType() {
        System.out.println("  Loan Type:");
        System.out.println("    1. Home Loan   2. Car Loan   3. Personal Loan   4. Education Loan");
        int c = readInt("  Choose (1-4): ");
        return switch (c) {
            case 1 -> "Home Loan";
            case 2 -> "Car Loan";
            case 3 -> "Personal Loan";
            default -> "Education Loan";
        };
    }

    // Safe input readers
    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Please enter a valid integer.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Please enter a valid number.");
            }
        }
    }
}
