package player.ast;

public class MeterC implements Meter
{
    private static MeterC instance;

    private MeterC()
    {
    }

    /**
     * @return the sole instance of MeterC
     */
    public static MeterC getInstance()
    {
        if (instance == null)
            instance = new MeterC();

        return instance;
    }

    @Override
    public String toString()
    {
        return "MeterC";
    }
}
