public class Main {
    public static void main(String[] args){
        System.out.println(fact(3));
    }

    static long fact(int n){
        if(n==0 || n==1)
            return 1;
        else
            return n * fact(n-1);
    }
}
