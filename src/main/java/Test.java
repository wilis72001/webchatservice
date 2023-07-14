public class Test {
    public static void main(String[] args) throws InterruptedException {
        Long a = System.currentTimeMillis();
        Thread.sleep(1000);
        Long b = System.currentTimeMillis();
        System.out.println("a value："+a+"b value："+b);
        System.out.println(b-a);
        System.out.println(a-b);

    }
}
