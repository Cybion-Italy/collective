package com.collective.usertests.persistence.mappers;

import com.collective.usertests.model.Reason;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface ReasonMapper {

    public void insertReason(Reason reason);
    public Reason getReason(int id);
    public void updateReason(Reason reason);
    public void deleteReason(int id);
    public List<Reason> getAllReasons();
}
