package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class TimeUnit extends OBDUnit<Integer> {
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getFormattedValue() {
        if (value != null) {
            final String hh = String.format("%02d", value / 3600);
            final String mm = String.format("%02d", (value % 3600) / 60);
            final String ss = String.format("%02d", value % 60);
            return String.format("%s:%s:%s", hh, mm, ss);
        }else
            return "00:00:00";
    }
}
