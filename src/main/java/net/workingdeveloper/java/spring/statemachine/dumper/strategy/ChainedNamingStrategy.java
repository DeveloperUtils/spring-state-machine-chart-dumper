package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Christoph Graupner on 2016-09-07.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
public class ChainedNamingStrategy implements INamingStrategy {
    List<INamingStrategy> fNamingStrategies = new ArrayList<>();

    public ChainedNamingStrategy(List<INamingStrategy> aNamingStrategies) {
        fNamingStrategies = new ArrayList<>(aNamingStrategies);
    }

    public ChainedNamingStrategy(INamingStrategy[] aNamingStrategies) {
        fNamingStrategies = Arrays.asList(aNamingStrategies);
    }

    @Override
    public String getName(Object aO) throws Exception {
        for (INamingStrategy lNamingStrategy : fNamingStrategies) {
            try {
                String lName = lNamingStrategy.getName(aO);
                if (lName != null)
                    return lName;
            } catch (Exception aEx) {
                //nothing to do, just skip
            }
        }
        return null;
    }
}
