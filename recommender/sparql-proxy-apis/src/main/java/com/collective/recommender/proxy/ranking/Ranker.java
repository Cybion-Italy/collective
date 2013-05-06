package com.collective.recommender.proxy.ranking;


import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Ranker<T> {

    public List<T> rank(List<T> objects)
        throws RankerException;

    public void setLimit(int limit);

    public int getLimit();

}
