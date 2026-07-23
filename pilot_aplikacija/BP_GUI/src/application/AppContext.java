package application;

import org.unibl.etf.entity.*;

public class AppContext {
    private static AppContext instance;
    private Domacinstvo domacinstvo;

    private AppContext() {}

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public Domacinstvo getDomacinstvo() {
        return domacinstvo;
    }

    public void setDomacinstvo(Domacinstvo domacinstvo) {
        this.domacinstvo = domacinstvo;
    }
}
