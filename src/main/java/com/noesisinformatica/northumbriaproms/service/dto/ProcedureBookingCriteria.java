package com.noesisinformatica.northumbriaproms.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the ProcedureBooking entity. This class is used in ProcedureBookingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /procedure-bookings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProcedureBookingCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter consultantName;

    private StringFilter hospitalSite;

    private ZonedDateTimeFilter scheduledDate;

    private ZonedDateTimeFilter performedDate;

    private StringFilter primaryProcedure;

    private StringFilter otherProcedures;

    private LongFilter patientId;

    public ProcedureBookingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(StringFilter consultantName) {
        this.consultantName = consultantName;
    }

    public StringFilter getHospitalSite() {
        return hospitalSite;
    }

    public void setHospitalSite(StringFilter hospitalSite) {
        this.hospitalSite = hospitalSite;
    }

    public ZonedDateTimeFilter getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(ZonedDateTimeFilter scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public ZonedDateTimeFilter getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(ZonedDateTimeFilter performedDate) {
        this.performedDate = performedDate;
    }

    public StringFilter getPrimaryProcedure() {
        return primaryProcedure;
    }

    public void setPrimaryProcedure(StringFilter primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
    }

    public StringFilter getOtherProcedures() {
        return otherProcedures;
    }

    public void setOtherProcedures(StringFilter otherProcedures) {
        this.otherProcedures = otherProcedures;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "ProcedureBookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (consultantName != null ? "consultantName=" + consultantName + ", " : "") +
                (hospitalSite != null ? "hospitalSite=" + hospitalSite + ", " : "") +
                (scheduledDate != null ? "scheduledDate=" + scheduledDate + ", " : "") +
                (performedDate != null ? "performedDate=" + performedDate + ", " : "") +
                (primaryProcedure != null ? "primaryProcedure=" + primaryProcedure + ", " : "") +
                (otherProcedures != null ? "otherProcedures=" + otherProcedures + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }

}
