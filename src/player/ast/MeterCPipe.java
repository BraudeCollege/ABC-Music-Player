package player.ast;

public class MeterCPipe implements Meter
{
    private static MeterCPipe instance;

    private MeterCPipe()
    {
    }

    /**
     * @return the sole instance of MeterC
     */
    public static MeterCPipe getInstance()
    {
        if (instance == null)
            instance = new MeterCPipe();

        return instance;
    }

    @Override
    public String toString()
    {
        return "MeterC";
    }
}
