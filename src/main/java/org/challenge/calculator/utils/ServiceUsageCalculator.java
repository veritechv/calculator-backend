package org.challenge.calculator.utils;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.entity.User;

public class ServiceUsageCalculator {
    public static boolean isBalanceEnough(User user, Service service){
        boolean isEnough = false;

        if(user!=null && service!=null){
            isEnough = user.getBalance() > service.getCost();
        }

        return isEnough;
    }

    public static long calculateNewBalance(User user, Service service){
        long newBalance = 0L;

        if(user!=null && service!=null){
            newBalance = user.getBalance() - service.getCost();
        }

        return newBalance;
    }
}
