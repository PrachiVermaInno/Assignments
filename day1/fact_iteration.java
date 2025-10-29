import java.util.Scanner;
public class fact_iteration{
    static int fact_iteration(int n ){
        int result=1;
        for (int i=1;i<=n;i++){
            result=result*i;

        }return result;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter number to find factorial : ");
        int num=sc.nextInt();
        System.out.println("Factorial of "+num+" : "+ fact_iteration(num));
    }
}