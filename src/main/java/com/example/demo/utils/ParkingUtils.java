package com.example.demo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15_MINUTES = 1.75;
    private static final double PERCENTAGE_DISCOUNT = 0.30;



    public static String generateParkingReceipt(){
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0, 19);
        return receipt.replace("-","").replace(":","").replace("T","-");
    }

    public static BigDecimal calculatePrice(LocalDateTime entryDate, LocalDateTime departureDate) {
        long minutes = entryDate.until(departureDate, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {

            total = FIRST_15_MINUTES;

        } else if (minutes <= 60) {

            total = FIRST_60_MINUTES;

        } else {
            long addicionalMinutes = minutes - 60;
            Double totalParts = ((double) addicionalMinutes / 15);
            if (totalParts > totalParts.intValue()) {
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else {
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * totalParts.intValue());
            }

        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal price, long times) {
        BigDecimal discount = ((times > 0) && (times % 10 == 0))
                ? price.multiply(new BigDecimal(PERCENTAGE_DISCOUNT))
                : new BigDecimal(0);
        return discount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
