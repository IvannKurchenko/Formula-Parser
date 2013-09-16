package formula.parser.constants;

public enum Constants implements Constant{

    PI(Math.PI, "pi", "π"),

    E(Math.E, "e");

    private double value;
    private String[] signs;

    Constants(double value, String... signs){
        this.value = value;
        this.signs = signs;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String[] getSigns() {
        return signs;
    }
}
