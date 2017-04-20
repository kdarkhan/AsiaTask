package kz.darkhan;

import java.util.List;

public abstract class AbstractNumbersPersister {
    public abstract boolean persist(List<NumWrapper> numbers);
}
