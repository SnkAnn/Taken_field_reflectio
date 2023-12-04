import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.io.FileInputStream;
import java.io.IOException;

public class Experiment{
    public TimeZone timeZone;
    public Currency fromCurrency;
    public Currency toCurrency;
    public double amount;

    public Experiment(String timeZoneId, String fromCurrencyCode, String toCurrencyCode, double amount) {
        this.timeZone = TimeZone.getTimeZone(timeZoneId);
        this.fromCurrency = Currency.getInstance(fromCurrencyCode);
        this.toCurrency = Currency.getInstance(toCurrencyCode);
        this.amount = amount;
    }

    public String showTimeInBelarus(String timeZoneDifference) {
        TimeZone currentTimeZone = TimeZone.getDefault();
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        localDateFormat.setTimeZone(currentTimeZone);
        return "Текущее время в Беларуси (Минск) : " + localDateFormat.format(new Date(currentTime));
    }

    public String showTimeInLondon(String timeZoneDifference) {
        TimeZone targetTimeZone = TimeZone.getTimeZone("GMT");
        long currentTime = System.currentTimeMillis();
        long targetTime = currentTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(targetTimeZone);

        return   "Time in London: " + dateFormat.format(new Date(targetTime)) ;
    }


    public String showBYNtoUSD(double usdRate, double amount) {
        double convertedAmount = amount / usdRate;
        return amount + " BYN эквивалентно " + convertedAmount + " USD";
    }

    public String showUSDtoBYN(double usdRate, double amount) {
        double convertedAmount = amount * usdRate;
        return amount + " USD is equivalent to " + convertedAmount + " BYN";
    }
}
